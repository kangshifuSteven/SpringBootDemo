package com.example.demo.clouddo.service.impl;

import com.example.demo.clouddo.dao.UserDao;
import com.example.demo.clouddo.domain.UserDO;
import com.example.demo.clouddo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2019/7/5.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Cacheable(cacheNames = "user_cache",keyGenerator = "keyGenerator")
    public UserDO getUserDo(Long id){
        return userDao.selectById(id);
    }

    @Override
    @CacheEvict(cacheNames = "user_cache",keyGenerator = "keyGenerator")
    public void deleteUser(Long id) {
       userDao.deleteById(id);
    }
}
