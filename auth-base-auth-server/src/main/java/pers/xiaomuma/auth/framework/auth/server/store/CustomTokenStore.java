package pers.xiaomuma.auth.framework.auth.server.store;

import org.springframework.security.oauth2.provider.token.TokenStore;

public interface CustomTokenStore extends TokenStore {

	FindKeysResult findKeysForPage(Integer pageNum, Integer pageSize);

}
