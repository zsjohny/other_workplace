package com.finace.miscroservice.task_scheduling.test;

import javassist.*;
import javassist.bytecode.AnnotationDefaultAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 动态注解赋值 --考虑到项目使用shiro,@Requirexxxx 动态赋值权限问题 解决方面很多
 * 这是其中一种方法
 * <p>
 * Created by Ness on 2017/2/26.
 */
//@Order(1)
//@Aspect
public class DynamicAnnotationEval {

    @Autowired
    private HttpServletRequest request;

    private final String INTERCEPT_CLASS_NAME = "org.apache.shiro.authz.annotation.RequiresPermissions";

    private void getAnnotationCLass() throws NotFoundException, ClassNotFoundException, CannotCompileException, IOException {
        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(this.getClass()));
        CtClass ctClass = classPool.getCtClass(INTERCEPT_CLASS_NAME);


        CtMethod method = ctClass.getDeclaredMethod("value");

        if (method == null) {
            throw new RuntimeException(System.err.append(INTERCEPT_CLASS_NAME).append(" not exist value \n\n").toString());
        }


        MethodInfo methodInfo = method.getMethodInfo();
        System.err.println(methodInfo);

        AnnotationDefaultAttribute defaultAttribute = new AnnotationDefaultAttribute(methodInfo.getConstPool(), AnnotationDefaultAttribute.tag.getBytes());
        ArrayMemberValue arrayMemberValue = new ArrayMemberValue(methodInfo.getConstPool());
        StringMemberValue[] stringMemberValues = new StringMemberValue[]{
                new StringMemberValue("user.*", methodInfo.getConstPool())
        };
        arrayMemberValue.setValue(stringMemberValues);

        defaultAttribute.setDefaultValue(arrayMemberValue);
        methodInfo.addAttribute(defaultAttribute);
        System.out.println(new String(method.getAttribute(AnnotationDefaultAttribute.tag)));
//        AttributeInfo info = new AttributeInfo(methodInfo.getConstPool(), "default ", "xxx".getBytes());
//        methodInfo.addAttribute(info);

//        method.setBody("return  new String[]{\"hello\"} ");
        ctClass.writeFile(Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1));
        ctClass.detach();

    }

    public static void main(String[] args) throws Exception {

        Tests test = new Tests();
        System.out.println(test.get());
        //回收
        test = null;
        System.gc();


        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(DynamicAnnotationEval.class.getClass()));
        CtClass ctClass = classPool.getCtClass("com.finace.miscroservice.task_scheduling.test.Test");

        CtMethod method = ctClass.getDeclaredMethod("get");

        MethodInfo methodInfo = method.getMethodInfo();
        System.err.println(methodInfo);

//        AnnotationDefaultAttribute defaultAttribute = new AnnotationDefaultAttribute(methodInfo.getConstPool(), AnnotationDefaultAttribute.tag.getBytes());
//        ArrayMemberValue arrayMemberValue = new ArrayMemberValue(methodInfo.getConstPool());
//        StringMemberValue[] stringMemberValues = new StringMemberValue[]{
//                new StringMemberValue("user.*", methodInfo.getConstPool())
//        };
//        arrayMemberValue.setValue(stringMemberValues);
//
//        defaultAttribute.setDefaultValue(arrayMemberValue);
//        methodInfo.addAttribute(defaultAttribute);
//        System.out.println(new String(method.getAttribute(AnnotationDefaultAttribute.tag)));
//        AttributeInfo info = new AttributeInfo(methodInfo.getConstPool(), "default ", "xxx".getBytes());
//        methodInfo.addAttribute(info);

        method.setBody("return  \"hello\"; ");
        ctClass.stopPruning(true);
        ctClass.writeFile(Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1));
//        ctClass.detach();
//        ctClass.toClass();
        ctClass.detach();

        URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        addURL.invoke(loader, new File(Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1), "Test.class").toURI().toURL());
//

////        Test test = (Test) Class.forName("com.finace.miscroservice.task_scheduling.test.Test").newInstance();
        Class<?> aClass = loader.loadClass("com.finace.miscroservice.task_scheduling.test.Test");
        test = (Tests) aClass.newInstance();
//        ctClass.toClass();
        System.out.println(test.get());

    }

    // 前置通知，*包含任意个位置的..任意参数
    @Before(value = "execution(@" + INTERCEPT_CLASS_NAME + " * *(..))")
    public void beforeValidation(JoinPoint joinPoint) throws ClassNotFoundException, CannotCompileException, NotFoundException, IOException {
        System.out.println("into..22222.");
        getAnnotationCLass();
    }
}
