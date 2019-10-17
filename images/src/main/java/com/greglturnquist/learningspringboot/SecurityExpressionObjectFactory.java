package com.greglturnquist.learningspringboot;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.spring5.context.webflux.ISpringWebFluxContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class SecurityExpressionObjectFactory implements IExpressionObjectFactory {

    private final SecurityExpressionHandler<MethodInvocation> handler;

    public SecurityExpressionObjectFactory(SecurityExpressionHandler<MethodInvocation> handler) {
        this.handler = handler;
    }

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList("authorization")));
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        if (expressionObjectName.equals("authorization")) {
            if (context instanceof ISpringWebFluxContext) {
                return new Authorization((ISpringWebFluxContext) context, handler);
            }
        }

        return null;
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return true;
    }
}
