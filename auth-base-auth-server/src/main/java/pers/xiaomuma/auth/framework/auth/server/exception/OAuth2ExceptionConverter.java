package pers.xiaomuma.auth.framework.auth.server.exception;

import java.util.Map;

public interface OAuth2ExceptionConverter {

	Map<String, String> convert2ExceptionDetails(Throwable exception);

}
