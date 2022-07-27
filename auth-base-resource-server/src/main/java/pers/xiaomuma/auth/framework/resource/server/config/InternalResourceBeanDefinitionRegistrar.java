package pers.xiaomuma.auth.framework.resource.server.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import pers.xiaomuma.auth.framework.resource.server.constant.SecurityConstant;


public class InternalResourceBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        if (beanDefinitionRegistry.isBeanNameInUse(SecurityConstant.RESOURCE_SERVER_CONFIGURER)) {
            return;
        }

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(InternalResourceBeanDefinitionRegistrar.class);
        beanDefinitionRegistry.registerBeanDefinition(SecurityConstant.RESOURCE_SERVER_CONFIGURER, beanDefinition);
    }
}
