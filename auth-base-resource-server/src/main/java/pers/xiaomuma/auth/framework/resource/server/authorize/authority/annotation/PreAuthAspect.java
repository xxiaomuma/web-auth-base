package pers.xiaomuma.auth.framework.resource.server.authorize.authority.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import pers.xiaomuma.auth.framework.resource.server.authorize.authority.CustomerSecurityExpressionRoot;


@Aspect
public class PreAuthAspect {


    @Pointcut("@annotation(pers.xiaomuma.auth.framework.resource.server.authorize.authority.annotation.PreAuth)")
    private void cut() {
    }

    @Around("cut() && @annotation(preAuth)")
    public Object record(ProceedingJoinPoint joinPoint, PreAuth preAuth) throws Throwable {
        String value = preAuth.value();
        SecurityContextHolder.getContext();
        //Spring EL 对value进行解析
        SecurityExpressionOperations operations = new CustomerSecurityExpressionRoot(SecurityContextHolder.getContext().getAuthentication());
        StandardEvaluationContext operationContext = new StandardEvaluationContext(operations);
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(value);
        Boolean result = expression.getValue(operationContext, boolean.class);
        if (result != null && result) {
            return joinPoint.proceed();
        }
        return "Forbidden";
    }

}

