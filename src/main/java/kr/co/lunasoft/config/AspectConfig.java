package kr.co.lunasoft.config;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class AspectConfig {
	
	@Around("execution(* kr.co.lunasoft.controller.*Controller.*(..))")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String uri = request.getRequestURI();
        log.info("[BEG] [" + uri + "]");
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("[END] [" + uri + "] [" + executionTime + "]");
        return result;
    }

}
