package com.example.demo.proxy.jdk;

import com.example.demo.clouddo.service.UserService;
import com.example.demo.proxy.jdk.service.TestService;
import org.apache.shiro.subject.Subject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Administrator
 */
public class JdkProxySubject implements InvocationHandler {

    private TestService testService;

    public JdkProxySubject(TestService testService) {
        this.testService = testService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("前置通知。。。。。。。。。。");
        Object result = null;
        try {
            result = method.invoke(testService,args);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            System.out.println("后置通知...................");
        }
        return result;
    }


}
