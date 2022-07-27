package pers.xiaomuma.auth.framework.auth.server.authentication.social.wx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@JsonIgnoreProperties({"name", "credentials"})
public class WxAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    public WxAuthenticationToken() { this(null); }

    public WxAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        this.setAuthenticated(false);
    }

    public WxAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return null;
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
