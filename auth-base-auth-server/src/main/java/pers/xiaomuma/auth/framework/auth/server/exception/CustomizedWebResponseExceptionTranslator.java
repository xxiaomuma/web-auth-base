package pers.xiaomuma.auth.framework.auth.server.exception;

import com.google.common.collect.Maps;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;

import java.io.IOException;
import java.util.Map;


public class CustomizedWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

	private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

	private static OAuth2ExceptionConverter oAuth2ExceptionConverter = new DefaultOAuth2ExceptionConverter();

	@Override
	public ResponseEntity<Map<String, Object>> translate(Exception e) throws Exception {
		// Try to extract a SpringSecurityException from the stacktrace
		Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
		Exception ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);
		if (ase != null) {
			return handleAuthException(ase);
		}
		return handleAuthException(e);
	}

	private ResponseEntity<Map<String, Object>> handleAuthException(Exception e) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Cache-Control", "no-store");
		headers.set("Pragma", "no-cache");
		Map<String, Object> response = Maps.newHashMap();
		response.putAll(oAuth2ExceptionConverter.convert2ExceptionDetails(e));
		return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}

	public void setThrowableAnalyzer(ThrowableAnalyzer throwableAnalyzer) {
		this.throwableAnalyzer = throwableAnalyzer;
	}


}
