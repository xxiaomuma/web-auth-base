package pers.xiaomuma.auth.framework.auth.server.endpoint;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.endpoint.AbstractEndpoint;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import pers.xiaomuma.auth.framework.auth.server.store.CustomTokenStore;
import pers.xiaomuma.auth.framework.auth.server.token.DefaultTokenRequestContext;
import pers.xiaomuma.auth.framework.auth.server.token.TokenRequestContextHolder;
import pers.xiaomuma.framework.response.BaseResponse;

import java.security.Principal;
import java.util.*;


@FrameworkEndpoint
public class CustomizedTokenEndpoint extends AbstractEndpoint {

    private final Logger logger = LoggerFactory.getLogger(CustomizedTokenEndpoint.class);
    private OAuth2RequestValidator oAuth2RequestValidator = new DefaultOAuth2RequestValidator();
    private Set<HttpMethod> allowedRequestMethods = new HashSet<HttpMethod>(Collections.singletonList(HttpMethod.POST));
    private CustomTokenStore tokenStore;

    @RequestMapping(value = "/oauth/token", method = RequestMethod.GET)
    public ResponseEntity<OAuth2AccessToken> getAccessToken(Principal principal, @RequestParam
            Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        if (!allowedRequestMethods.contains(HttpMethod.GET)) {
            throw new HttpRequestMethodNotSupportedException("GET");
        }
        return this.postAccessToken(principal, parameters);
    }

    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal, @RequestBody
            Map<String, String> parameters) {
        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException(
                    "There is no client authentication. Try adding an appropriate authentication filter.");
        }
        String clientId = this.getClientId(principal);
        ClientDetails authenticatedClient = getClientDetailsService().loadClientByClientId(clientId);

        TokenRequest tokenRequest = getOAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);
        if (StrUtil.isNotEmpty(clientId)) {
            if (!clientId.equals(tokenRequest.getClientId())) {
                throw new InvalidClientException("Given client ID does not match authenticated client");
            }
        }
        if (Objects.nonNull(authenticatedClient)) {
            oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
        }
        if (!StringUtils.hasText(tokenRequest.getGrantType())) {
            throw new InvalidRequestException("Missing grant type");
        }
        if ("implicit".equals(tokenRequest.getGrantType())) {
            throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
        }
        if (this.isAuthCodeRequest(parameters)) {
            if (!tokenRequest.getScope().isEmpty()) {
                logger.debug("Clearing scope of incoming token request");
                tokenRequest.setScope(Collections.<String>emptySet());
            }
        }
        if (this.isRefreshTokenRequest(parameters)) {
            tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
        }

        TokenRequestContextHolder.setContext(new DefaultTokenRequestContext(tokenRequest));
        OAuth2AccessToken token;
        try {
            token = getTokenGranter().grant(tokenRequest.getGrantType(), tokenRequest);
            if (token == null) {
                throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
            }
        } finally {
            TokenRequestContextHolder.clearContext();
        }
        return this.getResponse(token);
    }

    @DeleteMapping("/logout")
    public BaseResponse<Void> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StrUtil.isBlank(authHeader)) {
            return BaseResponse.failed("退出失败，token 为空");
        }
        String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StrUtil.EMPTY).trim();
        return removeTokenFromTokenStore(tokenValue);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<OAuth2Exception> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        }
        return getExceptionTranslator().translate(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
        logger.error("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage(), e);
        return getExceptionTranslator().translate(e);
    }

    @ExceptionHandler(ClientRegistrationException.class)
    public ResponseEntity<OAuth2Exception> handleClientRegistrationException(Exception e) throws Exception {
        if (logger.isWarnEnabled()) {
            logger.warn("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        }
        return getExceptionTranslator().translate(new BadClientCredentialsException());
    }

    @ExceptionHandler(OAuth2Exception.class)
    public ResponseEntity<OAuth2Exception> handleException(OAuth2Exception e) throws Exception {
        if (logger.isWarnEnabled()) {
            logger.warn("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        }
        return getExceptionTranslator().translate(e);
    }

    protected String getClientId(Principal principal) {
        Authentication client = (Authentication) principal;
        if (!client.isAuthenticated()) {
            throw new InsufficientAuthenticationException("The client is not authenticated.");
        }
        String clientId = client.getName();
        if (client instanceof OAuth2Authentication) {
            clientId = ((OAuth2Authentication) client).getOAuth2Request().getClientId();
        }
        return clientId;
    }

    private BaseResponse<Void> removeTokenFromTokenStore(String tokenValue) {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
            return BaseResponse.success();
        }
        tokenStore.removeAccessToken(accessToken);

        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        tokenStore.removeRefreshToken(refreshToken);
        return  BaseResponse.success();
    }

    private ResponseEntity<OAuth2AccessToken> getResponse(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(accessToken, headers, HttpStatus.OK);
    }

    private boolean isAuthCodeRequest(Map<String, String> parameters) {
        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
    }

    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }

    public void setOAuth2RequestValidator(OAuth2RequestValidator oAuth2RequestValidator) {
        this.oAuth2RequestValidator = oAuth2RequestValidator;
    }

    public void setAllowedRequestMethods(Set<HttpMethod> allowedRequestMethods) {
        this.allowedRequestMethods = allowedRequestMethods;
    }

    public CustomTokenStore getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(CustomTokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }
}
