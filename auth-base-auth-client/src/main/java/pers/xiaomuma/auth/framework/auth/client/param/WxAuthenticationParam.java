package pers.xiaomuma.auth.framework.auth.client.param;

import lombok.Builder;
import lombok.Data;
import pers.xiaomuma.auth.framework.constant.TokenGrantType;

@Data
@Builder
public class WxAuthenticationParam implements OAuth2AuthenticationParam {

	private String clientId;

	private String clientSecret;

	private String unionId;

	@Override
	public TokenGrantType getGrantType() {
		return TokenGrantType.WX;
	}
}
