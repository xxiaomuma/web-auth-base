package pers.xiaomuma.auth.framework.auth.server.token;

import lombok.SneakyThrows;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import pers.xiaomuma.auth.framework.constant.AuthConstant;
import pers.xiaomuma.auth.framework.converter.AdditionalUserConverter;
import pers.xiaomuma.auth.framework.converter.DefaultAdditionalUserConverter;
import pers.xiaomuma.auth.framework.user.DefaultUserDetails;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class CustomizedTokenEnhancer implements TokenEnhancer {

    private ClientDetailsService clientDetailsService;
    private AdditionalUserConverter additionalUserConverter;

    public CustomizedTokenEnhancer(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
        this.additionalUserConverter = new DefaultAdditionalUserConverter();
    }

    public CustomizedTokenEnhancer(ClientDetailsService clientDetailsService, AdditionalUserConverter additionalUserConverter) {
        this.clientDetailsService = clientDetailsService;
        this.additionalUserConverter = additionalUserConverter;
    }

    @Override
    @SneakyThrows
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (AuthConstant.CLIENT_CREDENTIALS.equals(authentication.getOAuth2Request().getGrantType())) {
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId((String) authentication.getPrincipal());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(clientDetails.getAdditionalInformation());
            return accessToken;
        }
        Map<String, Object> additionalInfo = getTokenAdditionalInfo(accessToken.getScope(), authentication);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

    public Map<String, Object> getTokenAdditionalInfo(Set<String> scopes, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>(8);
        DefaultUserDetails customUserDetailsUser = (DefaultUserDetails) authentication.getUserAuthentication().getPrincipal();
        if (scopes.contains(AuthConstant.EXTERNAL_SCOPE)) {
            // 定制
        } else if (scopes.contains(AuthConstant.INTERNAL_SCOPE)) {
            // 定制
        }
        additionalInfo.putAll(additionalUserConverter.convert2Map(customUserDetailsUser.getAdditionalUser()));
        additionalInfo.put("code",200);
        return additionalInfo;
    }
}
