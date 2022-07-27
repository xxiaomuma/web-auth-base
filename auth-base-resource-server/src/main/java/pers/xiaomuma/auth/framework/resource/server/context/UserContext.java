package pers.xiaomuma.auth.framework.resource.server.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import pers.xiaomuma.auth.framework.user.DefaultUserDetails;

import java.util.Objects;


public class UserContext {

	public static Integer getAccountId() {
		return getUserDetails().getAdditionalUser().getUserId();
	}

	public static String getUsername() {
		return getUserDetails().getUsername();
	}

	private static DefaultUserDetails getUserDetails() {
		OAuth2Authentication authentication =  getAuthentication();
		if (Objects.isNull(authentication)) {
			throw new UsernameNotFoundException("OAuth2信息不存在");
		}
		Authentication userAuthentication = authentication.getUserAuthentication();
		DefaultUserDetails user =  (DefaultUserDetails) userAuthentication.getPrincipal();
		if (Objects.isNull(user)) {
			throw new UsernameNotFoundException("用户为空");
		}
		return user;
	}

	public static boolean isAuthenticated() {
		OAuth2Authentication oAuthAuthentication =  getAuthentication();
		if (Objects.isNull(oAuthAuthentication)) {
			return false;
		}
		Authentication userAuthentication = oAuthAuthentication.getUserAuthentication();
		return Objects.nonNull(userAuthentication);
	}

	private static OAuth2Authentication getAuthentication() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext == null) {
			return null;
		}
		Authentication authentication = securityContext.getAuthentication();
		if (authentication instanceof OAuth2Authentication) {
			return (OAuth2Authentication) authentication;
		}
		return null;
	}

}
