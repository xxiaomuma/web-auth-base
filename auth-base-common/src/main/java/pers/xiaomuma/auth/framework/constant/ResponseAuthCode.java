package pers.xiaomuma.auth.framework.constant;

import org.apache.commons.lang3.StringUtils;

public enum ResponseAuthCode {

    SUCCESS("success", "认证成功"),
    // 认证异常
    UNAUTHORIZED("unauthorized", "认证失败"),
    USERNAME_NOT_FOUND("username_not_found","用户不存在"),
    ABNORMAL_ACCOUNT_STATUS("abnormal_account_status", "账户状态异常"),
    // oauth2异常
    AUTH_REQ_FAILED("auth_req_failed", "请求认证失败"),
    INVALID_CLIENT("invalid_client", "无效client"),
    BAD_CLIENT_CREDENTIAL("bad_client_credential", "client密码错误"),
    INVALID_SCOPE("invalid_scope", "异常scope权限"),
    INSUFFICIENT_SCOPE("insufficient_scope", "无scope权限"),
    INVALID_GRANT("invalid_grant", "异常授权"),
    UNSUPPORTED_GRANT_TYPE("unsupported_grant_typ", "不支持的授权模式"),
    REDIRECT_MISMATCH("redirect_mismatch", "跳转链接不匹配"),
    INVALID_REQUEST("invalid_request", "请求认证失败"),
    INVALID_TOKEN("invalid_token", "错误的token"),
    OAUTH2_ACCESS_DENIED("oauth2_access_token", "oauth2访问失败"),
    OAUTH2_ERROR("oauth2_error", "oauth2认证异常"),
    UNSUPPORTED_RESPONSE_TYPE("unsupported_response_type", "不支持的响应类型"),
    // 权限不足
    ACCESS_DENIED("access_denied", "权限不足"),
    // http请求异常
    METHOD_NOT_ALLOWED("method_not_allowed", "不支持的请求方法"),
    BAD_CREDENTIALS("bad_credentials", "账号或密码错误");

    private String code;
    private String desc;

    ResponseAuthCode(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public static ResponseAuthCode codeValueOf(String code) {
        for (ResponseAuthCode authResult: values()) {
            if (StringUtils.equals(authResult.getCode(), code)) {
                return authResult;
            }
        }
        return ResponseAuthCode.UNAUTHORIZED;
    }
}
