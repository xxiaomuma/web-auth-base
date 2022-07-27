package pers.xiaomuma.auth.framework.auth.client.provider;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import pers.xiaomuma.auth.framework.constant.ResponseAuthCode;
import pers.xiaomuma.auth.framework.auth.client.entity.AuthCredential;
import java.util.Map;

public class DefaultAuthCredentialConverter implements AuthCredentialConverter {

	@Override
	public AuthCredential convert2Credential(Map<String, ?> response) {
		if (MapUtils.isEmpty(response)) {
			return AuthCredential.error(ResponseAuthCode.AUTH_REQ_FAILED);
		}
		if (determineAuthSuccess(response)) {
			return AuthCredential.success(response);
		} else {
			String authErrorType = (String) response.get("auth_error_type");
			String errorMsg = (String) response.get("auth_error_message");
			return AuthCredential.error(ResponseAuthCode.codeValueOf(authErrorType), errorMsg);
		}
	}

	private boolean determineAuthSuccess(Map<String, ?> response) {
		for (String key : response.keySet()) {
			if (StringUtils.startsWith(key, "auth_error")) {
				return false;
			}
		}
		return true;
	}

}
