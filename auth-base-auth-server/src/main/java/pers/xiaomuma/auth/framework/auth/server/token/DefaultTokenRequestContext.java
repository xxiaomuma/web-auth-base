package pers.xiaomuma.auth.framework.auth.server.token;

import org.springframework.security.oauth2.provider.TokenRequest;

public class DefaultTokenRequestContext implements TokenRequestContext {

	private TokenRequest tokenRequest;

	public DefaultTokenRequestContext() {

	}

	public DefaultTokenRequestContext(TokenRequest tokenRequest) {
		this.tokenRequest  =  tokenRequest;
	}

	@Override
	public TokenRequest getTokenRequest() {
		return tokenRequest;
	}

	@Override
	public void setTokenRequest(TokenRequest tokenRequest) {
		this.tokenRequest = tokenRequest;
	}

}
