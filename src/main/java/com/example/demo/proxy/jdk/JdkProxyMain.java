package com.example.demo.proxy.jdk;
import com.example.demo.proxy.jdk.service.TestService;
import com.example.demo.proxy.jdk.service.TestServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JdkProxyMain {
    public static void main(String[] args) {
        InvocationHandler invocationHandler = new JdkProxySubject(new TestServiceImpl());
        TestService testService = (TestService) Proxy.newProxyInstance(JdkProxyMain.class.getClassLoader(), new Class[]{TestService.class},invocationHandler);
        testService.getTestProxy();
    }
}
