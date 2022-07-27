package pers.xiaomuma.auth.framework.auth.server.store.serialize;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;
import org.springframework.security.web.jackson2.WebJackson2Module;

import java.util.Map;

public class JsonSerializationStrategy extends StandardStringSerializationStrategy {

	private static final ObjectMapper OBJECT_MAPPER;
	private static final Map<String, Jackson2JsonRedisSerializer<?>> SERIALIZER_CACHE;
	static {
		OBJECT_MAPPER = initialObjectMapper();
		SERIALIZER_CACHE = Maps.newConcurrentMap();
	}

	private static ObjectMapper initialObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
		objectMapper.disable(MapperFeature.AUTO_DETECT_SETTERS);
		objectMapper.registerModule(new CoreJackson2Module());
		objectMapper.registerModule(new WebJackson2Module());
		objectMapper.addMixIn(OAuth2Authentication.class, OAuth2AuthenticationMixIn.class);
		objectMapper.addMixIn(OAuth2Request.class, OAuth2RequestMixIn.class);
		objectMapper.addMixIn(OAuth2AccessToken.class, OAuth2AccessTokenMinIn.class);
		return objectMapper;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
		RedisSerializer<?> serializer = getSerializerByClazz(clazz);
		return (T) serializer.deserialize(bytes);
	}

	private Jackson2JsonRedisSerializer<?> getSerializerByClazz(Class<?> clazz) {
		String clazzName = clazz.getName();
		Jackson2JsonRedisSerializer<?> serializer = SERIALIZER_CACHE.get(clazzName);
		if (serializer == null) {
			serializer = SERIALIZER_CACHE.get(clazzName);
			synchronized (JsonSerializationStrategy.class) {
				if (serializer == null) {
					serializer = new Jackson2JsonRedisSerializer<>(clazz);
					serializer.setObjectMapper(OBJECT_MAPPER);
					SERIALIZER_CACHE.put(clazzName, serializer);
				}
			}
		}
		return serializer;
	}

	@Override
	protected byte[] serializeInternal(Object object) {
		Jackson2JsonRedisSerializer<?> serializer = getSerializerByClazz(object.getClass());
		return serializer.serialize(object);
	}

	public static void main(String[] args) {
//		JsonSerializationStrategy serializationStrategy = new JsonSerializationStrategy();
//		OAuth2Request storedRequest = new OAuth2Request(Maps.newHashMap(), "client_id", AuthorityUtils.NO_AUTHORITIES, true,
//				Sets.newHashSet(), Sets.newHashSet(), "", Sets.newHashSet(), Maps.newHashMap());
//		storedRequest.refresh(new TokenRequest(Maps.newHashMap(), "a", Collections.EMPTY_LIST, "a"));
//		Authentication userAuthentication = new WxAuthenticationToken("a", AuthorityUtils.NO_AUTHORITIES);
//		OAuth2Authentication authentication = new OAuth2Authentication(storedRequest, userAuthentication);
//		authentication.setAuthenticated(true);
//		authentication.setDetails(null);
//		authentication.getOAuth2Request();
//		byte[] bytes = serializationStrategy.serialize(authentication);
//		System.out.println(new String(bytes));
//		OAuth2Authentication oAuth2Authentication = serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
//		System.out.println(oAuth2Authentication);

//		OAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken("aa");
//		byte[] bytes = serializationStrategy.serialize(oAuth2AccessToken);
//		System.out.println(bytes);
//		OAuth2AccessToken auth2AccessToken = serializationStrategy.deserialize(bytes, OAuth2AccessToken.class);
//		System.out.println(auth2AccessToken);
	}

}
