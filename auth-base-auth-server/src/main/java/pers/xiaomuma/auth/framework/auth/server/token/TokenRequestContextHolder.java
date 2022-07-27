package pers.xiaomuma.auth.framework.auth.server.token;

public class TokenRequestContextHolder {

	private final static ThreadLocal<TokenRequestContext> contextHolder = ThreadLocal.withInitial(
			DefaultTokenRequestContext::new
	) ;


	public static void clearContext() {
		contextHolder.remove();
	}

	public static TokenRequestContext getContext() {
		return contextHolder.get();
	}


	public static void setContext(TokenRequestContext context) {
		contextHolder.set(context);
	}


}
