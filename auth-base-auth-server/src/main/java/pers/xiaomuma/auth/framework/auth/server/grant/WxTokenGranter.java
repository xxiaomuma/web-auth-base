package pers.xiaomuma.auth.framework.auth.server.grant;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import pers.xiaomuma.auth.framework.auth.server.authentication.social.wx.WxAuthenticationToken;
import java.util.LinkedHashMap;
import java.util.Map;


public class WxTokenGranter extends CustomizedTokenGranter {

    private static final String GRANT_TYPE = "wx";
    private final AuthenticationManager authenticationManager;

    protected WxTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                             ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    public WxTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                          ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String unionId = parameters.get("unionId");

        Authentication authentication = new WxAuthenticationToken(unionId);
        ((AbstractAuthenticationToken) authentication).setDetails(parameters);
        try {
            authentication = authenticationManager.authenticate(authentication);
        } catch (AccountStatusException | BadCredentialsException e) {
            throw new InvalidGrantException(e.getMessage());
        }
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate unionId: " + unionId);
        }
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, authentication);
    }
}
