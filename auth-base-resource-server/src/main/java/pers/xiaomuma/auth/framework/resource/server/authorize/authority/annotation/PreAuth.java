package pers.xiaomuma.auth.framework.resource.server.authorize.authority.annotation;

import java.lang.annotation.*;

/**
 * Created by keets on 2017/12/7.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreAuth {
    String value();
}
