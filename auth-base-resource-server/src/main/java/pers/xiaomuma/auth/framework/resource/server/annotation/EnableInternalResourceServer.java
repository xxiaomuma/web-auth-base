package pers.xiaomuma.auth.framework.resource.server.annotation;


import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import pers.xiaomuma.auth.framework.resource.server.authorize.DefaultAuthorizeConfigManager;
import pers.xiaomuma.auth.framework.resource.server.authorize.DefaultInternalAuthorizeConfigProvider;
import pers.xiaomuma.auth.framework.resource.server.config.InternalResourceBeanDefinitionRegistrar;
import pers.xiaomuma.auth.framework.resource.server.config.InternalResourceServerConfigurer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({
        InternalResourceBeanDefinitionRegistrar.class,
        InternalResourceServerConfigurer.class,
        DefaultAuthorizeConfigManager.class,
        DefaultInternalAuthorizeConfigProvider.class
})
public @interface EnableInternalResourceServer {
}
