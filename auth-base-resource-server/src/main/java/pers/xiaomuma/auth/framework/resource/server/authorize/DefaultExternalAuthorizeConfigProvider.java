package pers.xiaomuma.auth.framework.resource.server.authorize;

import lombok.SneakyThrows;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;


@Order
public class DefaultExternalAuthorizeConfigProvider implements AuthorizeConfigProvider {

    private static final String EXTERNAL_SERVER_SCOPE_SPEL = "#oauth2.hasScope('external')";

    @Override
    @SneakyThrows
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config
                .anyRequest()
                .access(EXTERNAL_SERVER_SCOPE_SPEL)
                .and()
                .csrf().disable();
        return true;
    }
}
