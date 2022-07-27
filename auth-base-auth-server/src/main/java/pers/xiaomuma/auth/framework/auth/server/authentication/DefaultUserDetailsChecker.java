package pers.xiaomuma.auth.framework.auth.server.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;


public class DefaultUserDetailsChecker implements UserDetailsChecker {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    public DefaultUserDetailsChecker() {
    }

    @Override
    public void check(UserDetails user) {
        if (!user.isEnabled()) {
            this.logger.debug("User account is disabled");
            throw new DisabledException(this.messages.getMessage("AuthenticationProvider.disabled", "账户已禁用"));
        } else if (!user.isAccountNonLocked()) {
            this.logger.debug("User account is locked");
            throw new LockedException(this.messages.getMessage("AuthenticationProvider.locked", "账户已锁定"));
        } else if (!user.isAccountNonExpired()) {
            this.logger.debug("User account is expired");
            throw new AccountExpiredException(this.messages.getMessage("AuthenticationProvider.expired", "账号已过期"));
        } else if (!user.isCredentialsNonExpired()) {
            this.logger.debug("User account credentials have expired");
            throw new CredentialsExpiredException(this.messages.getMessage("AuthenticationProvider.credentialsExpired", "账户凭证过期"));
        }
    }
}
