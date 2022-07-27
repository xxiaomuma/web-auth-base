package pers.xiaomuma.auth.framework.auth.server.store.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class OAuth2AccessTokenSerializer extends StdSerializer<OAuth2AccessToken> {

	protected OAuth2AccessTokenSerializer(Class<OAuth2AccessToken> t) {
		super(t);
	}

	public OAuth2AccessTokenSerializer() {
		this(null);
	}


	@Override
	public void serializeWithType(OAuth2AccessToken value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
		doSerialize(value, gen, serializers, typeSer);
	}

	@SneakyThrows
	@Override
	public void serialize(OAuth2AccessToken token, JsonGenerator jgen, SerializerProvider provider) {
		doSerialize(token, jgen, provider, null);
	}

	private void doSerialize(OAuth2AccessToken token, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
		jgen.writeStartObject();
		if (typeSer != null) {
			jgen.writeStringField(typeSer.getPropertyName(), token.getClass().getName());
		}
		jgen.writeStringField(OAuth2AccessToken.ACCESS_TOKEN, token.getValue());
		jgen.writeStringField(OAuth2AccessToken.TOKEN_TYPE, token.getTokenType());
		OAuth2RefreshToken refreshToken = token.getRefreshToken();
		if (refreshToken != null) {
			jgen.writeStringField(OAuth2AccessToken.REFRESH_TOKEN, refreshToken.getValue());
		}
		Date expiration = token.getExpiration();
		if (expiration != null) {
			long now = System.currentTimeMillis();
			jgen.writeNumberField(OAuth2AccessToken.EXPIRES_IN, (expiration.getTime() - now) / 1000);
		}
		Set<String> scope = token.getScope();
		if (scope != null && !scope.isEmpty()) {
			StringBuilder scopes = new StringBuilder();
			for (String s : scope) {
				Assert.hasLength(s, "Scopes cannot be null or empty. Got " + scope + "");
				scopes.append(s);
				scopes.append(" ");
			}
			jgen.writeStringField(OAuth2AccessToken.SCOPE, scopes.substring(0, scopes.length() - 1));
		}
		Map<String, Object> additionalInformation = token.getAdditionalInformation();
		for (String key : additionalInformation.keySet()) {
			jgen.writeObjectField(key, additionalInformation.get(key));
		}
		jgen.writeEndObject();
	}

}
