package pers.xiaomuma.auth.framework.auth.client.param;

import lombok.Builder;
import lombok.Data;
import pers.xiaomuma.auth.framework.constant.TokenGrantType;

@Data
@Builder
public class SmsAuthenticationParam implements OAuth2AuthenticationParam {

	private String clientId;

	private String clientSecret;

	private String mobile;

	private String code;

	@Override
	public TokenGrantType getGrantType() {
		return TokenGrantType.SMS;
	}
}
