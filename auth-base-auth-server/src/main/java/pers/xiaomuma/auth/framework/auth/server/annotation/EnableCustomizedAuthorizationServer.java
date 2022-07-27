package pers.xiaomuma.auth.framework.auth.server.annotation;


import org.springframework.context.annotation.Import;
import pers.xiaomuma.auth.framework.auth.server.config.CustomizedAuthorizationServerEndpointsConfiguration;
import pers.xiaomuma.auth.framework.auth.server.config.CustomizedAuthorizationServerSecurityConfiguration;
import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        CustomizedAuthorizationServerEndpointsConfiguration.class,
        CustomizedAuthorizationServerSecurityConfiguration.class
})
public @interface EnableCustomizedAuthorizationServer {
}
