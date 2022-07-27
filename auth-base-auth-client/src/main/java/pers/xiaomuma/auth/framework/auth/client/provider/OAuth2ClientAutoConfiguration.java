package pers.xiaomuma.auth.framework.auth.client.provider;

import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.web.client.RestOperations;

public class OAuth2ClientAutoConfiguration {

	private String tokenEndpointUri;
	private RestOperations restTemplate;
	private OAuth2ClientProperties oAuth2ClientProperties;

	public DefaultClientAuthenticationService defaultAuthenticationClientService() {
		return new DefaultClientAuthenticationService(oAuth2AuthenticationClient());
	}

	private OAuth2AuthenticationClient oAuth2AuthenticationClient() {
		DefaultOAuth2AuthenticationClient client = new DefaultOAuth2AuthenticationClient();
		client.setClientId(oAuth2ClientProperties.getClientId());
		client.setClientSecret(oAuth2ClientProperties.getClientSecret());
		client.setTokenEndpointUrl(tokenEndpointUri);
		return client;
	}

}
