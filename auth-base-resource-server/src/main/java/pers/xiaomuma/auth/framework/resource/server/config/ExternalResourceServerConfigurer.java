package pers.xiaomuma.auth.framework.resource.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.web.client.RestTemplate;
import pers.xiaomuma.auth.framework.converter.DefaultAdditionalUserConverter;
import pers.xiaomuma.auth.framework.converter.DefaultUserAuthenticationConverter;
import pers.xiaomuma.auth.framework.resource.server.authorize.AuthorizeConfigManager;
import pers.xiaomuma.auth.framework.resource.server.exception.CustomAccessDeniedHandler;
import pers.xiaomuma.auth.framework.resource.server.exception.ResourceAuthExceptionEntryPoint;
import pers.xiaomuma.auth.framework.resource.server.token.CustomRemoteTokenServices;


public class ExternalResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ResourceServerProperties resourceProperties;
    private final AuthorizeConfigManager authorizeConfigManager;

    public ExternalResourceServerConfigurer(RestTemplate restTemplate, ObjectMapper objectMapper,
                                            ResourceServerProperties resourceProperties,
                                            AuthorizeConfigManager authorizeConfigManager) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.resourceProperties = resourceProperties;
        this.authorizeConfigManager = authorizeConfigManager;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        authorizeConfigManager.config(http.authorizeRequests());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(new ResourceAuthExceptionEntryPoint(objectMapper));
        resources.accessDeniedHandler(new CustomAccessDeniedHandler());
        resources.tokenServices(this.customRemoteTokenServices());
    }

    private CustomRemoteTokenServices customRemoteTokenServices() {
        this.checkResourceProperties();
        CustomRemoteTokenServices remoteTokenServices = new CustomRemoteTokenServices();
        String tokenEndpointUri = resourceProperties.getTokenInfoUri();
        remoteTokenServices.setCheckTokenEndpointUrl(tokenEndpointUri);
        remoteTokenServices.setRestTemplate(restTemplate);
        remoteTokenServices.setAccessTokenConverter(this.customAccessTokenConverter());
        return remoteTokenServices;
    }

    private AccessTokenConverter customAccessTokenConverter() {
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        UserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter(new DefaultAdditionalUserConverter());
        accessTokenConverter.setUserTokenConverter(userTokenConverter);
        return accessTokenConverter;
    }

    private void checkResourceProperties() {
        if (resourceProperties == null) {
            throw new IllegalArgumentException("资源配置信息为空");
        }
        if (StringUtils.isBlank(resourceProperties.getTokenInfoUri())) {
            throw new IllegalArgumentException("资源配置tokenInfoUri为空");
        }
    }
}
