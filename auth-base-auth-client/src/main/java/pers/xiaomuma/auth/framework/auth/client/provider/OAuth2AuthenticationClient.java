package pers.xiaomuma.auth.framework.auth.client.provider;


import pers.xiaomuma.auth.framework.auth.client.param.ClientAuthenticationParam;
import java.util.Map;

public interface OAuth2AuthenticationClient {

	Map<String, ?> getAccessToken(ClientAuthenticationParam param);

}
