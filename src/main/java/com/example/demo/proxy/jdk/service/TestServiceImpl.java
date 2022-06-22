package com.example.demo.proxy.jdk.service;

import org.junit.Test;

public class TestServiceImpl implements TestService {

    @Override
    public String getTestProxy() {
        System.out.println("执行目标对象的proxy方法");
        return "执行目标对象的proxy方法";
    }
}
