package com.yujj.util.performance;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.jiuyuan.util.performance.PerformanceRecord;

@Aspect
public class PerformanceWatcher {

    public static ThreadLocal<PerformanceRecord> PERFORMANCE_RECORD_HOLDER = new ThreadLocal<PerformanceRecord>();

    @Around("execution(* com.yujj.business..*.*(..)) || "
        + "execution(* com.yujj.dao..*.*(..)) || "
        + "@annotation(com.yujj.util.performance.WatchPerformance) || "
        + "@within(com.yujj.util.performance.WatchPerformance)")
    public Object invoke(final ProceedingJoinPoint pjp) throws Throwable {
        PerformanceRecord record = PERFORMANCE_RECORD_HOLDER.get();
        if (record != null) {
            String typeName = pjp.getSignature().getDeclaringTypeName();
            int lastDot = typeName.lastIndexOf('.');
            if (lastDot != -1) {
                typeName = typeName.substring(lastDot + 1);
            }

            String name = pjp.getSignature().getName();
            String marker = typeName + "#" + name;

            record.start(marker);
            try {
                return pjp.proceed();
            } finally {
                record.end(marker);
            }
        } else {
            return pjp.proceed();
        }
    }
}
