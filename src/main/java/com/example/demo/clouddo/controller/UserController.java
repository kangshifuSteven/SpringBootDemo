package com.example.demo.clouddo.controller;

import com.example.demo.annotation.MonitorLog;
import com.example.demo.clouddo.domain.UserDO;
import com.example.demo.clouddo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2019/7/6.
 */
@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("user/{id}")
    @ResponseBody
    @MonitorLog(value = "测试啊",name = "不知道大撒撒")
    public UserDO getUser(@PathVariable Long id){
        return userService.getUserDo(id);
    }
}
