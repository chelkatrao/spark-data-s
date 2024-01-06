package com.chelkatrao.starter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LazyListSupportAspect {

    @Before()
    public void beforeEachMethodInvocationCheckAndFillContent(JoinPoint jp) {

    }
}
