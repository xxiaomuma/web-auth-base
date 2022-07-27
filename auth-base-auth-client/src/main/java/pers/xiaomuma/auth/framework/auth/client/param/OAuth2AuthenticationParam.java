package pers.xiaomuma.auth.framework.auth.client.param;

import pers.xiaomuma.auth.framework.constant.TokenGrantType;

public interface OAuth2AuthenticationParam {

	TokenGrantType getGrantType();
}
