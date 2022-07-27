package pers.xiaomuma.auth.framework.resource.server.annotation;


import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import pers.xiaomuma.auth.framework.resource.server.authorize.DefaultAuthorizeConfigManager;
import pers.xiaomuma.auth.framework.resource.server.authorize.DefaultExternalAuthorizeConfigProvider;
import pers.xiaomuma.auth.framework.resource.server.config.ExternalResourceBeanDefinitionRegistrar;
import pers.xiaomuma.auth.framework.resource.server.config.ExternalResourceServerConfigurer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({
        ExternalResourceBeanDefinitionRegistrar.class,
        ExternalResourceServerConfigurer.class,
        DefaultAuthorizeConfigManager.class,
        DefaultExternalAuthorizeConfigProvider.class
})
public @interface EnableExternalResourceServer {
}
