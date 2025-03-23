package com.ortim.aspect;

import com.ortim.component.MessageService;
import com.ortim.core.results.Result;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ManagerAspect {

    private final Logger logger = LoggerFactory.getLogger(ManagerAspect.class);
    private final MessageService messageService;

    @Autowired
    public ManagerAspect(MessageService messageService) {
        this.messageService = messageService;
    }

    @Around("execution(* com.ortim.service.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        logger.info(messageService.getMessage("log.work.method", new Object[]{joinPoint.getSignature().getName()}));
        Object data = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long workerTime = endTime - startTime;
        logger.info(messageService.getMessage("log.over.method", new Object[]{joinPoint.getSignature().getName(), workerTime}));
        return data;
    }

    @Around("execution(* com.ortim.security.auth.SecurityManager.getCurrentUser(..))")
    public Object securityManagerLog(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info(messageService.getMessage("log.securityManager.access.true", null));
        return joinPoint.proceed();
    }

    @AfterReturning(pointcut = "execution(* com.ortim.*.*.*(..))",
            returning = "result")
    public void logMessage(JoinPoint joinPoint, Object result){
        if (result instanceof Result dataResult) {
            String message = dataResult.getMessage();
            if(dataResult.isSuccess()){
                logger.info(messageService.getMessage("log.afterReturning",
                        new Object[]{joinPoint.getSignature().getName(), message}));
            }else{
                logger.error(messageService.getMessage("log.afterReturning",
                        new Object[]{joinPoint.getSignature().getName(), message}));
            }
        }
    }

    @AfterThrowing(pointcut = "execution(* com.ortim.*.*.*(..))",
            throwing = "exResult")
    public void logThrowingMessage(JoinPoint joinPoint, Exception exResult){
        String message = exResult.getMessage();
        logger.error(messageService.getMessage("log.afterThrowing",
                new Object[]{joinPoint.getSignature().getName(), message}));
    }

}
