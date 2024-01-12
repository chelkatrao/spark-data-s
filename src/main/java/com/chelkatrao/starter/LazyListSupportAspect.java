package com.chelkatrao.starter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@Aspect
public class LazyListSupportAspect {

    @Autowired
    private FirstLevelCacheService cacheService;
    @Autowired
    private ConfigurableApplicationContext context;

    @Before("execution(* com.chelkatrao.starter.LazySparkList.*()) & execution(* java.util.*.*(..))")
    public void beforeEachMethodInvocationCheckAndFillContent(JoinPoint jp) {
        LazySparkList lazySparkList = (LazySparkList) jp.getTarget();
        if (!lazySparkList.initialized()) {
            List<?> content = cacheService.getDataFor(lazySparkList.getOwnerId(),
                                                      lazySparkList.getModelClass(),
                                                      lazySparkList.getPathToSource(),
                                                      lazySparkList.getForeignKeyName(),
                                                      context);
            lazySparkList.setContent(content);
        }

    }
}
