package com.example.demo.clouddo.service;

import com.example.demo.clouddo.domain.UserDO;

/**
 * Created by Administrator on 2019/7/5.
 */
public interface UserService {

    public UserDO getUserDo(Long id);

    public void deleteUser(Long id);
}
