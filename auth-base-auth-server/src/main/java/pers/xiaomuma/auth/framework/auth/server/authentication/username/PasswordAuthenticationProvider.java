package pers.xiaomuma.auth.framework.auth.server.authentication.username;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pers.xiaomuma.auth.framework.auth.server.authentication.DefaultUserDetailsChecker;
import pers.xiaomuma.auth.framework.auth.server.authentication.UserConnection;
import pers.xiaomuma.auth.framework.auth.server.authentication.UserConnectionRepository;
import java.util.Objects;


public class PasswordAuthenticationProvider implements AuthenticationProvider {

    private PasswordEncoder passwordEncoder;
    private UserDetailsChecker userDetailsChecker;
    private UserConnectionRepository userConnectionRepository;

    public PasswordAuthenticationProvider(PasswordEncoder passwordEncoder,
                                          UserConnectionRepository userConnectionRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsChecker = new DefaultUserDetailsChecker();
        this.userConnectionRepository = userConnectionRepository;
    }

    public PasswordAuthenticationProvider(PasswordEncoder passwordEncoder,
                                          UserDetailsChecker userDetailsChecker,
                                          UserConnectionRepository userConnectionRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsChecker = userDetailsChecker;
        this.userConnectionRepository = userConnectionRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PasswordAuthenticationToken authenticationToken = (PasswordAuthenticationToken) authentication;
        String username = (String) authenticationToken.getPrincipal();
        String password = (String) authentication.getCredentials();

        if (StringUtils.isAnyBlank(username) || StringUtils.isAnyBlank(password)) {
            throw new BadCredentialsException("leak of authentication parameters");
        }

        UserConnection userConnection = userConnectionRepository.getConnection(username);
        if (Objects.isNull(userConnection)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        this.passwordAuthenticationCheck(userConnection, authenticationToken);
        userDetailsChecker.check(userConnection.getUserDetails());
        return this.createSuccessAuthentication(userConnection.getUserDetails(), password, authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (PasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private void passwordAuthenticationCheck(UserConnection userConnection, PasswordAuthenticationToken authentication) {
        String presentedPassword = (String) authentication.getCredentials();
        String password = userConnection.getUserDetails().getPassword();
        if (!this.passwordEncoder.matches(presentedPassword, password)) {
            throw new BadCredentialsException("密码不匹配");
        }
    }

    private Authentication createSuccessAuthentication(Object principal, Object credentials, Authentication authentication) {
        PasswordAuthenticationToken result = new PasswordAuthenticationToken(
                principal,
                credentials,
                ((UserDetails) principal).getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }
}
