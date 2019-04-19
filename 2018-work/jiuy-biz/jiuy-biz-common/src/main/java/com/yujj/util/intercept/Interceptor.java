package com.yujj.util.intercept;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.instantiate.CachingInstanciator;
import com.jiuyuan.util.instantiate.DefaultCollectionFactory;
import com.jiuyuan.util.instantiate.TypeDescriptor;
import com.jiuyuan.util.intercept.AnnotatedAnnotation;
import com.jiuyuan.util.intercept.AnnotationUtil;
import com.jiuyuan.util.intercept.IndexedAnnotation;
import com.jiuyuan.util.intercept.ReturnType;
import com.jiuyuan.util.intercept.merge.annotation.MergeCapable;
import com.jiuyuan.util.intercept.merge.handler.MergeCapableHandler;
import com.jiuyuan.util.intercept.prevalid.ExecutionFlow;
import com.jiuyuan.util.intercept.prevalid.PreValidateComposite;
import com.jiuyuan.util.intercept.prevalid.PreValidateUtil;
import com.jiuyuan.util.intercept.split.annotation.SplitParam;
//import com.yujj.util.CollectionUtil;

/**
 * <pre>
 * 目前已知在两种情况下这个拦截器会生效：
 * 1. JDK动态代理：在接口的方法上使用Interceptable注解（或者被Interceptable修饰的注解），此时要求接口必须继承InterceptableComponent接口。
 * 2. CGLib动态子类：在被代理的class的方法上使用Interceptable注解（或者被Interceptable修饰的注解）。此时无须实现InterceptableComponent接口。
 * </pre>
 * 
 */
@Aspect
public class Interceptor {

    private static final Logger log = LoggerFactory.getLogger(Interceptor.class);

    private static final Object NOT_SPLITTED = new Object();

    @Around("execution(* com.yujj.util.intercept.InterceptableComponent+.*(..)) || "
        + "execution(@(@(com.yujj.util.intercept.Interceptable) *) * *(..))")
    public Object invoke(final ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();

        // If the target class implements some interfaces and JDK dynamic proxy is used, this method object belongs to
        // one of its interfaces, not to the target class.
        Method method = sig.getMethod();

        if (AnnotationUtil.getAnnotationAnnotatedBy(method, Interceptable.class) == null) {
            return pjp.proceed();
        }

        if (log.isDebugEnabled()) {
            log.debug("Intercepting method: {}", method.toGenericString());
        }

        TypeDescriptor returnTypeDescriptor = getReturnTypeDescriptor(method);
        Object[] args = pjp.getArgs();

        ExecutionFlow executionFlow = preValidate(method, args);
        if (executionFlow == ExecutionFlow.RETURN) {
            return returnTypeDescriptor.createDefaultInstance();
        }

        Object result = splitAndProceed(pjp.getTarget(), method, args, returnTypeDescriptor);
        if (result != NOT_SPLITTED) {
            return result;
        }

        return pjp.proceed();
    }

    private ExecutionFlow preValidate(Method method, Object[] args) throws Exception {
        List<List<PreValidateComposite>> composites = PreValidateUtil.searchPreValidateComposites(method);

        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            Object arg = args[argIndex];

            for (PreValidateComposite composite : composites.get(argIndex)) {
                String annotationName = composite.getAnnotation().annotationType().getSimpleName();
                if (log.isDebugEnabled()) {
                    log.debug("Pre-validating argument. arg index: {}, annotation: {}", argIndex, annotationName);
                }

                ExecutionFlow executionFlow = composite.validate(arg);
                if (executionFlow == ExecutionFlow.RETURN) {
                    if (log.isDebugEnabled()) {
                        log.debug("Pre-validation failed. method: {}, arg index: {}, annotation: {}", method, argIndex,
                            annotationName);
                    }
                    return ExecutionFlow.RETURN;
                } else if (executionFlow == ExecutionFlow.THROW) {
                    throw new IllegalStateException("Pre-validation failed. method: " + method + ", arg index: " +
                        argIndex + ", annotation: " + annotationName);
                }
            }
        }

        return ExecutionFlow.CONTINUE;
    }

    /**
     * Return NOT_SPLITTED if the split-merge actions are not performed.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object splitAndProceed(Object target, Method method, Object[] args, TypeDescriptor returnTypeDescriptor)
        throws Exception {
        IndexedAnnotation<SplitParam> indexedSplitParam =
            AnnotationUtil.findFirstParameterAnnotation(method, SplitParam.class);

        if (indexedSplitParam != null) {
            SplitParam splitParam = indexedSplitParam.getAnnotation();
            int argIndex = indexedSplitParam.getIndex();
            Class<?> argType = method.getParameterTypes()[argIndex];

            if (log.isDebugEnabled()) {
                log.debug("SplitParam found, arg index: {}, segment size: {}", argIndex, splitParam.size());
            }

            boolean isVoidReturnType = returnTypeDescriptor.isVoidType();
            AnnotatedAnnotation<MergeCapable> mergeCapableAA =
                AnnotationUtil.getAnnotationAnnotatedBy(method, MergeCapable.class);

            if (!isVoidReturnType && mergeCapableAA == null) {
                throw new IllegalStateException(
                    "Contract violation: A MergeCapable-annotated annotation must be supplied when the method has "
                        + "a return value and an @SplitParam is present.");
            }

            Object arg = args[argIndex];
            if (arg instanceof Collection) {
                Collection collection = (Collection) arg;
                if (collection.size() <= splitParam.size()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Collection size ({}) is smaller than {}, no split-merge actions will be performed.",
                            collection.size(), splitParam.size());
                    }
                    return NOT_SPLITTED;
                }

                if (log.isDebugEnabled()) {
                    log.debug("Splitting param, arg index: {}, arg size: {}, segment size: {}", argIndex,
                        collection.size(), splitParam.size());
                }

                List<Object> partialResults = new ArrayList<Object>();

                // split
                List<List<?>> lists = CollectionUtil.splitCollection(collection, splitParam.size());
                for (List<?> list : lists) {
                    Collection part = DefaultCollectionFactory.createCollection(argType, arg.getClass(), list.size());
                    part.addAll(list);

                    Object[] newArgs = new Object[args.length];
                    System.arraycopy(args, 0, newArgs, 0, args.length);
                    newArgs[argIndex] = part;

                    Object partialResult = method.invoke(target, newArgs);
                    partialResults.add(partialResult);
                }

                // merge
                if (!isVoidReturnType) {
                    if (log.isDebugEnabled()) {
                        log.debug("Merging results, segment count: {}", partialResults.size());
                    }

                    MergeCapable mergeCapable = mergeCapableAA.getMetaAnnotation();
                    MergeCapableHandler handler = CachingInstanciator.INSTANCE.instantiate(mergeCapable.handler());
                    return handler.merge(mergeCapableAA.getAnnotatedAnnotation(), returnTypeDescriptor, partialResults);
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Method has void return type. No merging actions will be performed.");
                    }
                }

                return null;
            } else {
                throw new IllegalArgumentException("A SplitParam-annotated object must be a collection.");
            }
        }

        return NOT_SPLITTED;
    }

    private TypeDescriptor getReturnTypeDescriptor(Method method) {
        ReturnType returnType = method.getAnnotation(ReturnType.class);
        if (returnType != null) {
            return new TypeDescriptor(returnType.type(), returnType.factory(), returnType.hint());
        } else {
            return new TypeDescriptor(method.getReturnType());
        }
    }
}