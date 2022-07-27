package pers.xiaomuma.auth.framework.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class DefaultUserDetails extends User {

    private AdditionalUser additionalUser;

    public DefaultUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, AdditionalUser additionalUser) {
        super(username, correctPassword(password), authorities);
        this.additionalUser = additionalUser;
    }

    public DefaultUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled, AdditionalUser additionalUser) {
        super(username, correctPassword(password), enabled, true, true, true, authorities);
        this.additionalUser = additionalUser;
    }

    public AdditionalUser getAdditionalUser() {
        return this.additionalUser;
    }

    private static String correctPassword(String password) {
        return "{i-pbkdf2}" + password;
    }

}
