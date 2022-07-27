package pers.xiaomuma.auth.framework.auth.server.authentication;

import org.springframework.security.core.userdetails.UserDetails;


public interface UserConnection {

    String getUsername();

    UserDetails getUserDetails();
}
