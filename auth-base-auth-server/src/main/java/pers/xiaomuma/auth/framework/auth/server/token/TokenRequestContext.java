package pers.xiaomuma.auth.framework.auth.server.token;

import org.springframework.security.oauth2.provider.TokenRequest;

public interface TokenRequestContext {

	TokenRequest getTokenRequest();

	void setTokenRequest(TokenRequest tokenRequest);

}
