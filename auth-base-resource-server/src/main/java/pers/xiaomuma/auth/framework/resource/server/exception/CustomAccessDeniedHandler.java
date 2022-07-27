package pers.xiaomuma.auth.framework.resource.server.exception;

import cn.hutool.json.JSONUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import pers.xiaomuma.framework.response.ResponseCode;
import pers.xiaomuma.framework.response.ViewResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        ViewResponse<?> viewResponse = convert2ViewResponse(accessDeniedException);
        response.getWriter().write(JSONUtil.toJsonStr(viewResponse));
    }

    private ViewResponse<?> convert2ViewResponse(AccessDeniedException ase) {
        return ViewResponse.builder()
                .code(ResponseCode.UN_AUTHORIZED)
                .message(ase.getMessage())
                .build();
    }
}
