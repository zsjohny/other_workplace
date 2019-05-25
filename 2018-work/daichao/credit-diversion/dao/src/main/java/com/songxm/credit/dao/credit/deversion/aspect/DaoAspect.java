package com.songxm.credit.dao.credit.deversion.aspect;

import com.moxie.commons.BaseAspectUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class DaoAspect {

    @Around("execution(public * com.songxm.credit.dao.credit.deversion.persistence..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return BaseAspectUtils.logAround(joinPoint, 300l);
    }
}
