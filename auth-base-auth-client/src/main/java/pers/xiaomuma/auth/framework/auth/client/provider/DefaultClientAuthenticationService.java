package pers.xiaomuma.auth.framework.auth.client.provider;

import com.google.common.collect.Maps;
import pers.xiaomuma.auth.framework.auth.client.entity.AuthCredential;
import pers.xiaomuma.auth.framework.auth.client.param.*;
import pers.xiaomuma.framework.exception.InternalBizException;
import java.util.Map;

public class DefaultClientAuthenticationService implements ClientAuthenticationService {

	private OAuth2AuthenticationClient authenticationClient;
	private AuthCredentialConverter authCredentialConverter = new DefaultAuthCredentialConverter();

	public DefaultClientAuthenticationService(OAuth2AuthenticationClient authenticationClient) {
		this.authenticationClient = authenticationClient;
	}

	public AuthCredential authentication(OAuth2AuthenticationParam authenticationParam) {
		Map<String, Object> parameters = oAuthAuthorizeParameter(authenticationParam);
		switch (authenticationParam.getGrantType()) {
			case PASSWORD:
				PasswordAuthenticationParam passwordParam = (PasswordAuthenticationParam) authenticationParam;
				parameters.put("username", passwordParam.getUsername());
				parameters.put("password", passwordParam.getPassword());
				break;
			case SMS:
				SmsAuthenticationParam smsParam = (SmsAuthenticationParam) authenticationParam;
				parameters.put("mobile", smsParam.getMobile());
				break;
			case WX:
				WxAuthenticationParam wxAuthenticationParam = (WxAuthenticationParam) authenticationParam;
				parameters.put("unionId", wxAuthenticationParam.getUnionId());
				break;
			default:
				throw new InternalBizException("不支持的登陆方式");
		}
		ClientAuthenticationParam param = new ClientAuthenticationParam();
		param.setParameters(parameters);
		Map<String, ?> response = authenticationClient.getAccessToken(param);
		return authCredentialConverter.convert2Credential(response);
	}

	private Map<String, Object> oAuthAuthorizeParameter(OAuth2AuthenticationParam authenticationParam){
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("grant_type", authenticationParam.getGrantType().getCode());
		return parameters;
	}

	public OAuth2AuthenticationClient getAuthenticationClient() {
		return authenticationClient;
	}

	public void setAuthenticationClient(OAuth2AuthenticationClient authenticationClient) {
		this.authenticationClient = authenticationClient;
	}

	public AuthCredentialConverter getAuthCredentialConverter() {
		return authCredentialConverter;
	}

	public void setAuthCredentialConverter(AuthCredentialConverter authCredentialConverter) {
		this.authCredentialConverter = authCredentialConverter;
	}

}
