package pers.xiaomuma.auth.framework.auth.client.provider;


import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pers.xiaomuma.auth.framework.auth.client.param.ClientAuthenticationParam;
import pers.xiaomuma.framework.serialize.JsonUtils;
import java.io.IOException;
import java.util.Map;

public class DefaultOAuth2AuthenticationClient implements OAuth2AuthenticationClient {

	private String clientId;
	private String clientSecret;
	private String tokenEndpointUrl;
	private RestOperations restTemplate;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public DefaultOAuth2AuthenticationClient() {
		restTemplate = new RestTemplate();
		((RestTemplate) restTemplate).setErrorHandler(
				new DefaultResponseErrorHandler() {
					@Override
					// Ignore 400
					public void handleError(ClientHttpResponse response) throws IOException {
						if (response.getRawStatusCode() != 400) {
							super.handleError(response);
						}
					}
				});
	}

	public void setRestTemplate(RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setTokenEndpointUrl(String tokenEndpointUrl) {
		this.tokenEndpointUrl = tokenEndpointUrl;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	@Override
	public Map<String, ?> getAccessToken(ClientAuthenticationParam param) {
		if (MapUtils.isEmpty(param.getParameters())) {
			throw new InvalidRequestException("参数为空");
		}
		Map<String, Object> parameters = param.getParameters();
		Map<String, ?> map = null;
		try {
			String requestUri = requestUri(tokenEndpointUrl, clientId, clientSecret);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(requestUri, parameters, String.class);
			String responseStr = responseEntity.getBody();
			if (responseStr != null) {
				map = JsonUtils.json2Map(responseStr, String.class, Object.class);
			}
		} catch (Exception e) {
			logger.error("请求获取token失败, tokenEndpointUrl: {}, param: {}", tokenEndpointUrl, JsonUtils.object2Json(param), e);
			throw new InvalidRequestException("认证异常", e);
		}
		return map;
	}

	private String requestUri(String tokenEndpointUrl, String clientId, String clientSecret) {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(tokenEndpointUrl)
				.queryParam("client_id", clientId)
				.queryParam("client_secret", clientSecret);
		return builder.build().toUriString();
	}

}
