package pers.xiaomuma.auth.framework.constant;

public enum TokenGrantType {

	PASSWORD("general_password"), //用户名密码
	SMS("sms"), //短信验证码
	WX("wx"); //微信小程序

	private String code;

	TokenGrantType(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
