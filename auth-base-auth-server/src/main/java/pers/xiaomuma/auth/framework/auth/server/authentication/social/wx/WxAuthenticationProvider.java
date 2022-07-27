package pers.xiaomuma.auth.framework.auth.server.authentication.social.wx;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pers.xiaomuma.auth.framework.auth.server.authentication.DefaultUserDetailsChecker;
import pers.xiaomuma.auth.framework.auth.server.authentication.UserConnection;
import pers.xiaomuma.auth.framework.auth.server.authentication.UserConnectionRepository;
import java.util.Objects;


public class WxAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsChecker userDetailsChecker;
    private UserConnectionRepository userConnectionRepository;

    public WxAuthenticationProvider(UserConnectionRepository userConnectionRepository) {
        this.userDetailsChecker = new DefaultUserDetailsChecker();
        this.userConnectionRepository = userConnectionRepository;
    }

    public WxAuthenticationProvider(UserDetailsChecker userDetailsChecker,
                                    UserConnectionRepository userConnectionRepository) {
        this.userDetailsChecker = userDetailsChecker;
        this.userConnectionRepository = userConnectionRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WxAuthenticationToken authenticationToken = (WxAuthenticationToken) authentication;
        String unionId = (String) authenticationToken.getPrincipal();

        if (StringUtils.isAnyBlank(unionId)) {
            throw new BadCredentialsException("leak of authentication parameters");
        }

        UserConnection userConnection = userConnectionRepository.getConnection(unionId);
        if (Objects.isNull(userConnection)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        userDetailsChecker.check(userConnection.getUserDetails());
        return this.createSuccessAuthentication(userConnection.getUserDetails(), authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (WxAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private Authentication createSuccessAuthentication(Object principal, Authentication authentication) {
        WxAuthenticationToken result = new WxAuthenticationToken(
                principal,
                ((UserDetails) principal).getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }
}
