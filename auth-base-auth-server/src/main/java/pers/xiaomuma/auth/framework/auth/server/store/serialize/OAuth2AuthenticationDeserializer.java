package pers.xiaomuma.auth.framework.auth.server.store.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.io.IOException;

public class OAuth2AuthenticationDeserializer extends JsonDeserializer<OAuth2Authentication> {

	@Override
	public OAuth2Authentication deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode jsonNode = oc.readTree(jsonParser);
		JsonNode storedRequestNode = jsonNode.get("storedRequest");
		JsonNode userAuthenticationNode = jsonNode.get("userAuthentication");
		OAuth2Request request = oc.readValue(storedRequestNode.traverse(oc), OAuth2Request.class);
		Authentication userAuthentication = null;
		if (userAuthenticationNode != null && !userAuthenticationNode.isMissingNode()) {
			userAuthentication = oc.readValue(userAuthenticationNode.traverse(oc), Authentication.class);
		}
		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(request, userAuthentication);
		JsonNode detailsNode = jsonNode.get("details");
		if (detailsNode != null && !detailsNode.isMissingNode()) {
			OAuth2AuthenticationDetails details = oc.readValue(detailsNode.traverse(oc), OAuth2AuthenticationDetails.class);
			oAuth2Authentication.setDetails(details);
		}
		return oAuth2Authentication;
	}

}
