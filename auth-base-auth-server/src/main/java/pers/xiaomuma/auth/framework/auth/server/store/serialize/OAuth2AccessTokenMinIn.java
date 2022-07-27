package pers.xiaomuma.auth.framework.auth.server.store.serialize;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OAuth2AccessTokenSerializer.class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OAuth2AccessTokenDeserializer.class)
public class OAuth2AccessTokenMinIn {

}
