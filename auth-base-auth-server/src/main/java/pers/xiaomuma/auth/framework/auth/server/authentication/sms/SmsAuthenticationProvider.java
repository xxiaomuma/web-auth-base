package pers.xiaomuma.auth.framework.auth.server.authentication.sms;

import cn.hutool.core.lang.Validator;
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


public class SmsAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsChecker userDetailsChecker;
    private UserConnectionRepository userConnectionRepository;

    public SmsAuthenticationProvider(UserConnectionRepository userConnectionRepository) {
        this.userDetailsChecker = new DefaultUserDetailsChecker();
        this.userConnectionRepository = userConnectionRepository;
    }

    public SmsAuthenticationProvider(UserDetailsChecker userDetailsChecker,
                                     UserConnectionRepository userConnectionRepository) {
        this.userDetailsChecker = userDetailsChecker;
        this.userConnectionRepository = userConnectionRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();

        if (!Validator.isMobile(mobile)) {
            throw new BadCredentialsException("leak of authentication parameters");
        }

        UserConnection userConnection = userConnectionRepository.getConnection(mobile);
        if (Objects.isNull(userConnection)) {
            throw new UsernameNotFoundException("手机号不存在");
        }
        userDetailsChecker.check(userConnection.getUserDetails());
        return this.createSuccessAuthentication(userConnection.getUserDetails(), authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (SmsAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private Authentication createSuccessAuthentication(Object principal, Authentication authentication) {
        SmsAuthenticationToken result = new SmsAuthenticationToken(
                principal, ((UserDetails) principal).getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }
}
