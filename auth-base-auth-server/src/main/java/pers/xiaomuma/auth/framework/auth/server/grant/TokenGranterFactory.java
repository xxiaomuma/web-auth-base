package pers.xiaomuma.auth.framework.auth.server.grant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import pers.xiaomuma.auth.framework.constant.TokenGrantType;


public class TokenGranterFactory {

    private OAuth2RequestFactory requestFactory;
    private ClientDetailsService clientDetailsService;
    private AuthenticationManager authenticationManager;
    private AuthorizationServerTokenServices tokenServices;

    public TokenGranterFactory(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService) {
        this.tokenServices = tokenServices;
        this.clientDetailsService = clientDetailsService;
        this.authenticationManager = authenticationManager;
        this.requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
    }

    public TokenGranter buildTokenGranter(TokenGrantType grantType) {
        switch (grantType) {
            case PASSWORD:
                return new PasswordTokenGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory);
            case SMS:
                return new SmsTokenGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory);
            case WX:
                return new WxTokenGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory);
            default:
                throw new UnsupportedGrantTypeException("不支持该授权模式: " + grantType.getCode());
        }
    }
}
