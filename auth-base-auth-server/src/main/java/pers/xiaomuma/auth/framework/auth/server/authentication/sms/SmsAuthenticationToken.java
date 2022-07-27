package pers.xiaomuma.auth.framework.auth.server.authentication.sms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@JsonIgnoreProperties({"name", "credentials"})
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    public SmsAuthenticationToken() {
        this(null);
    }

    public SmsAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        this.setAuthenticated(false);
    }

    public SmsAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        super.setAuthenticated(isAuthenticated);
    }
}
