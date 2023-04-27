package com.imooc.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author 昴星
 * @date 2023-03-18 17:11
 * @explain
 */

@Aspect
@Component
public class ServiceLogAspect {
    final static Logger logger= LoggerFactory.getLogger(ServiceLogAspect.class);

    @Around("execution(* com.imooc.*.service.impl..*.*(..))")
    public Object recordTimeOfService(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("====当前执行的Service{}=====执行的方法{}",joinPoint.getTarget().getClass()
                                                         ,joinPoint.getSignature().getName());
        long startTime=System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime=System.currentTimeMillis();
        long takeTime=endTime-startTime;
        if(takeTime>3000) logger.error("------当前执行耗时:{}",takeTime);
        else if(takeTime>2000) logger.warn("------当前执行耗时:{}",takeTime);
        else logger.info("------当前执行耗时:{}",takeTime);
        return result;
    }
}
