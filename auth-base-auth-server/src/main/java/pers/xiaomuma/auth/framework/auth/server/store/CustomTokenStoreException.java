package pers.xiaomuma.auth.framework.auth.server.store;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class CustomTokenStoreException extends OAuth2Exception {

	private static final String SERVER_ERROR_ERROR_CODE = "server_error";
	private String errorCode = SERVER_ERROR_ERROR_CODE;
	private int httpStatus = 500;

	public CustomTokenStoreException(String message) {
		super(message);
	}

	public CustomTokenStoreException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Returns the <code>error</code> used in the <i>OAuth2 Error Response</i>
	 * sent back to the caller. The default is &quot;server_error&quot;.
	 *
	 * @return the <code>error</code> used in the <i>OAuth2 Error Response</i>
	 */
	@Override
	public String getOAuth2ErrorCode() {
		return this.errorCode;
	}

	/**
	 * Returns the Http Status used in the <i>OAuth2 Error Response</i>
	 * sent back to the caller. The default is 500.
	 *
	 * @return the <code>Http Status</code> set on the <i>OAuth2 Error Response</i>
	 */
	@Override
	public int getHttpErrorCode() {
		return this.httpStatus;
	}

}
