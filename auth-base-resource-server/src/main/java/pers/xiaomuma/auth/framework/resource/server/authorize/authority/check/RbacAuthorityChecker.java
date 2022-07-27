package pers.xiaomuma.auth.framework.resource.server.authorize.authority.check;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface RbacAuthorityChecker {

	boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
