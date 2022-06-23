package com.example.demo.aop;

import com.example.demo.annotation.MonitorLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class MonitorLogAop {

    @Around("@annotation(com.example.demo.annotation.MonitorLog)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // 获得当前访问的class
            Class<?> className = joinPoint.getTarget().getClass();
            // 获得访问的方法名
            String methodName = joinPoint.getSignature().getName();
            // 得到方法的参数的类型
            Class<?>[] argClass = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);
            method.setAccessible(true);
            // 判断是否存在@MonitorLog注解
            if (method.isAnnotationPresent(MonitorLog.class)) {
                MonitorLog annotation = method.getAnnotation(MonitorLog.class);
                System.out.println(annotation.name()+","+annotation.value());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return joinPoint.proceed();
    }
}
