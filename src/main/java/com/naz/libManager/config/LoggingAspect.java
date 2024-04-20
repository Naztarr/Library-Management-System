package com.naz.libManager.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging method executions and performance monitoring*/
@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.naz.libManager.service.serviceImplementation.BookServiceImplementation.addBook(..)) || " +
            "execution(* com.naz.libManager.service.serviceImplementation.BookServiceImplementation.updateBookDetail(..)) || " +
            "execution(* com.naz.libManager.service.serviceImplementation.BookServiceImplementation.removeBook(..)) || " +
            "execution(* com.naz.libManager.service.serviceImplementation.BorrowingServiceImplementation.borrowBook(..)) || " +
            "execution(* com.naz.libManager.service.serviceImplementation.BorrowingServiceImplementation.returnBook(..))"
    )
    private void publicMethodsFromLoggingPackage(){};

    @Before(value = "publicMethodsFromLoggingPackage()")
    public void logBefore(JoinPoint joinPoint){
        logger.info("Method execution started: {}", joinPoint.getSignature());
    }

    @After(value = "publicMethodsFromLoggingPackage()")
    public void logAfter(JoinPoint joinPoint){
        logger.info("Method execution completed: {}", joinPoint.getSignature());
    }
    @AfterThrowing(value = "publicMethodsFromLoggingPackage()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex){
        logger.error("Exception in method {}: {}", joinPoint.getSignature(), ex.getMessage());
    }

    @Around(value = "publicMethodsFromLoggingPackage()")
    public Object logPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            logger.info("Method {} executed in {} ms", joinPoint.getSignature(), executionTime);
            return result;

        } catch(Throwable throwable){
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            logger.error("Method {} threw an exception after {} ms: {}", joinPoint.getSignature(), executionTime, throwable.getMessage());
            throw throwable;
        }
    }
}