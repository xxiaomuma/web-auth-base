package pers.xiaomuma.auth.framework.resource.server.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Map;


public class CustomRemoteTokenServices implements ResourceServerTokenServices {

    private final Logger logger = LoggerFactory.getLogger(CustomRemoteTokenServices.class);
    private RestOperations restTemplate = new RestTemplate();
    private String checkTokenEndpointUrl;
    private String tokenName = "token";
    private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

    public CustomRemoteTokenServices() {
        ((RestTemplate)this.restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add(this.tokenName, accessToken);

        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> map;
        try {
            map = postForMap(checkTokenEndpointUrl, formData, headers);
        } catch (Exception e) {
            logger.error(getErrorInfo("请求校验token异常", accessToken), e);
            throw new UserDeniedAuthorizationException(accessToken);
        }
        if (map.containsKey("error")) {
            logger.debug(getErrorInfo("token校验失败:" + map.get("error"), accessToken));
            throw new InvalidTokenException(accessToken);
        }
        // gh-838
        if (!Boolean.TRUE.equals(map.get("active"))) {
            logger.debug(getErrorInfo("非active token", accessToken));
            throw new InvalidTokenException(accessToken);
        }
        return tokenConverter.extractAuthentication(map);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    public void setRestTemplate(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setCheckTokenEndpointUrl(String checkTokenEndpointUrl) {
        this.checkTokenEndpointUrl = checkTokenEndpointUrl;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
        this.tokenConverter = accessTokenConverter;
    }

    private Map<String, Object> postForMap(String path, MultiValueMap<String, Object> formData, HttpHeaders headers) {
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        ResponseEntity<?> responseEntity = restTemplate.exchange(
                path, HttpMethod.POST,
                new HttpEntity<>(formData, headers), Map.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) responseEntity.getBody();
        return result;
    }

    private String getErrorInfo(String prefix, String token) {
        String template = "%s, checkTokenEndpointUrl: %s, token:%s ";
        return String.format(template, prefix, checkTokenEndpointUrl, token);
    }

}
