package pers.xiaomuma.auth.framework.auth.client.param;

import lombok.Builder;
import lombok.Data;
import pers.xiaomuma.auth.framework.constant.TokenGrantType;

@Data
@Builder
public class PasswordAuthenticationParam implements OAuth2AuthenticationParam {

	private String clientId;

	private String clientSecret;

	private String username;

	private String password;

	@Override
	public TokenGrantType getGrantType() {
		return TokenGrantType.PASSWORD;
	}

}
