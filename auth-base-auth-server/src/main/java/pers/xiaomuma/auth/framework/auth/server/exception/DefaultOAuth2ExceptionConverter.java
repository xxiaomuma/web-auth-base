package pers.xiaomuma.auth.framework.auth.server.exception;

import com.google.common.collect.Maps;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import pers.xiaomuma.auth.framework.constant.AuthConstant;
import pers.xiaomuma.auth.framework.constant.ResponseAuthCode;
import java.util.Map;
import java.util.Optional;

public class DefaultOAuth2ExceptionConverter implements OAuth2ExceptionConverter {

	@Override
	public Map<String, String> convert2ExceptionDetails(Throwable exception) {
		ResponseAuthCode authCode;
		if (exception instanceof AuthenticationException) {
			authCode = this.handleAuthenticationException((AuthenticationException) exception);
		} else if (exception instanceof OAuth2Exception) {
			authCode = this.handleOAuth2Exception((OAuth2Exception) exception);
		} else  if (exception instanceof AccessDeniedException) {
			authCode = ResponseAuthCode.ACCESS_DENIED;
		} else if (exception instanceof HttpRequestMethodNotSupportedException){
			authCode = ResponseAuthCode.METHOD_NOT_ALLOWED;
		} else {
			authCode = ResponseAuthCode.UNAUTHORIZED;
		}
		Map<String, String> details = Maps.newHashMap();
		details.put(AuthConstant.AUTH_ERROR_TYPE, authCode.getCode());
		details.put(AuthConstant.AUTH_ERROR_DESC, authCode.getDesc());
		details.put(AuthConstant.AUTH_ERROR_MESSAGE, exception.getMessage());
		return details;
	}

	private ResponseAuthCode handleAuthenticationException(AuthenticationException exception) {
		ResponseAuthCode authCode = null;
		if (exception instanceof UsernameNotFoundException) {
			authCode = ResponseAuthCode.USERNAME_NOT_FOUND;
		} else if (exception instanceof BadCredentialsException) {
			authCode = ResponseAuthCode.BAD_CREDENTIALS;
		} else if (exception instanceof AccountStatusException) {
			authCode = ResponseAuthCode.ABNORMAL_ACCOUNT_STATUS;
		}
		return Optional.ofNullable(authCode).orElse(ResponseAuthCode.UNAUTHORIZED);
	}

	private ResponseAuthCode handleOAuth2Exception(OAuth2Exception exception) {
		ResponseAuthCode authCode = ResponseAuthCode.OAUTH2_ERROR;
		if (exception instanceof InvalidClientException) {
			authCode = ResponseAuthCode.INVALID_CLIENT;
		} else if (exception instanceof BadClientCredentialsException) {
			authCode = ResponseAuthCode.BAD_CLIENT_CREDENTIAL;
		} else if (exception instanceof InsufficientScopeException) {
			authCode = ResponseAuthCode.INSUFFICIENT_SCOPE;
		} else if (exception instanceof InvalidGrantException) {
			authCode = ResponseAuthCode.INVALID_GRANT;
		} else if (exception instanceof InvalidScopeException) {
			authCode = ResponseAuthCode.INVALID_SCOPE;
		} else if (exception instanceof InvalidRequestException) {
			authCode = ResponseAuthCode.INVALID_REQUEST;
		} else if (exception instanceof InvalidTokenException) {
			authCode = ResponseAuthCode.INVALID_TOKEN;
		} else if (exception instanceof OAuth2AccessDeniedException) {
			authCode = ResponseAuthCode.OAUTH2_ACCESS_DENIED;
		} else if (exception instanceof UnsupportedGrantTypeException) {
			authCode = ResponseAuthCode.UNSUPPORTED_GRANT_TYPE;
		} else if (exception instanceof RedirectMismatchException) {
			authCode = ResponseAuthCode.REDIRECT_MISMATCH;
		}
		return authCode;
	}

}
