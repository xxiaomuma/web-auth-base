package pers.xiaomuma.auth.framework.auth.client.entity;

import com.google.common.collect.Maps;
import pers.xiaomuma.auth.framework.constant.ResponseAuthCode;
import java.util.Map;


public class AuthCredential {

    private ResponseAuthCode authCode;
    private String errorMsg;
    private Map<String, ?> credential;

    public AuthCredential(ResponseAuthCode authCode, String errorMsg, Map<String, ?> credential) {
        this.authCode = authCode;
        this.errorMsg = errorMsg;
        this.credential = credential;
    }

    public ResponseAuthCode getAuthCode() {
        return authCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Map<String, ?> getCredential() {
        return credential;
    }

    public boolean isAuthSuccess() {
        return authCode == ResponseAuthCode.SUCCESS;
    }

    public static AuthCredential success(Map<String, ?> credential) {
        return new AuthCredential(ResponseAuthCode.SUCCESS, "Auth Success",credential);
    }

    public static AuthCredential error(ResponseAuthCode authCode) {
        return new AuthCredential(authCode, "Auth Failed", Maps.newHashMap());
    }

    public static AuthCredential error(ResponseAuthCode authCode, String errorMsg) {
        return new AuthCredential(authCode, errorMsg, Maps.newHashMap());
    }

}
