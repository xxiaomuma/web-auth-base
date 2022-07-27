package pers.xiaomuma.auth.framework.resource.server.exception;

import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import pers.xiaomuma.framework.response.ResponseCode;
import pers.xiaomuma.framework.response.ViewResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 客户端异常处理
 */
@Slf4j
public class ResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {
	private ObjectMapper objectMapper;

	public ResourceAuthExceptionEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	@SneakyThrows
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		ViewResponse<String> result = new ViewResponse<>();
		if (authException != null) {
			result.setCode(ResponseCode.UN_AUTHORIZED);
			result.setView(authException.getMessage());
			result.setMessage("未授权访问");
			response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
		} else {
			result.setCode(ResponseCode.INTERNAL_SERVER_ERROR);
			result.setView("服务器错误");
			response.setStatus(HttpStatus.HTTP_INTERNAL_ERROR);
		}
		PrintWriter printWriter = response.getWriter();
		printWriter.append(objectMapper.writeValueAsString(result));
	}
}
