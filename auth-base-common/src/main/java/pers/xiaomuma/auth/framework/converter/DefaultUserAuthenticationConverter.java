package pers.xiaomuma.auth.framework.converter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;
import pers.xiaomuma.auth.framework.user.AdditionalUser;
import pers.xiaomuma.auth.framework.user.DefaultUserDetails;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 根据check_token 的结果转化用户信息
 */
public class DefaultUserAuthenticationConverter implements UserAuthenticationConverter {

	private static final String N_A = "N/A";
	private static final String SERVER_AUTHORITY = "server";
	private AdditionalUserConverter additionalUserConverter;

	public DefaultUserAuthenticationConverter(AdditionalUserConverter additionalUserConverter) {
		this.additionalUserConverter = additionalUserConverter;
	}

	/**
	 * Extract information about the user to be used in an access token (i.e. for resource servers).
	 *
	 * @param authentication an authentication representing a user
	 * @return a map of key values representing the unique information about the user
	 */
	@Override
	public Map<String, ?> convertUserAuthentication(Authentication authentication) {
		DefaultUserDetails user = (DefaultUserDetails) authentication.getPrincipal();
		Map<String, Object> response = new LinkedHashMap<>();
		response.put(UserAuthenticationConverter.USERNAME, authentication.getName());
		response.putAll(additionalUserConverter.convert2Map(user.getAdditionalUser()));
		if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
			response.put(UserAuthenticationConverter.AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
		}
		return response;
	}

	/**
	 * Inverse of {@link #convertUserAuthentication(Authentication)}. Extracts an Authentication from a map.
	 *
	 * @param map a map of user information
	 * @return an Authentication representing the user or null if there is none
	 */
	@Override
	public Authentication extractAuthentication(Map<String, ?> map) {
		if (map.containsKey(UserAuthenticationConverter.USERNAME)) {
			Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
			String username = (String) map.get(UserAuthenticationConverter.USERNAME);
			AdditionalUser additionalUser = additionalUserConverter.convert2Entity(map);
			DefaultUserDetails user = new DefaultUserDetails(username, N_A, authorities, true, additionalUser);
			return new UsernamePasswordAuthenticationToken(user, N_A, authorities);
		}
		return null;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
		Object authorities = map.get(UserAuthenticationConverter.AUTHORITIES);
		if(authorities == null ){
			authorities = SERVER_AUTHORITY;
		}
		if (authorities instanceof String) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
		}
		if (authorities instanceof Collection) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
		}
		throw new IllegalArgumentException("Authorities must be either a String or a Collection");
	}
}
