package pers.xiaomuma.auth.framework.auth.client.provider;


import pers.xiaomuma.auth.framework.auth.client.entity.AuthCredential;
import pers.xiaomuma.auth.framework.auth.client.param.OAuth2AuthenticationParam;

public interface ClientAuthenticationService {

	AuthCredential authentication(OAuth2AuthenticationParam authenticationParam);

}
