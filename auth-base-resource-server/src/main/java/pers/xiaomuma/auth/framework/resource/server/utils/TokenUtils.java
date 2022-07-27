package pers.xiaomuma.auth.framework.resource.server.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class TokenUtils {

	public static boolean isValidBearerToken(String authHeader) {
		return StringUtils.isNoneBlank(authHeader) && (authHeader.startsWith("Bearer") || authHeader.startsWith("bearer"));
	}

	public static String extractBearerToken(String authHeader) {
		if (StringUtils.isNoneBlank(authHeader) && authHeader.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase())) {
			String authHeaderValue = authHeader.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
			// Add this here for the details later. Would be better to change the signature of this method.
			int commaIndex = authHeaderValue.indexOf(',');
			if (commaIndex > 0) {
				authHeaderValue = authHeaderValue.substring(0, commaIndex);
			}
			return authHeaderValue;
		}
		return null;
	}

}
