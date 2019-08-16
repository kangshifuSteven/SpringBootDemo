package com.example.demo.clouddo.dao;

import com.example.demo.clouddo.domain.Order;

import java.util.List;

/**
 * Created by Administrator on 2019/7/6.
 */
public interface OrderDao {

    List<Order> findOrderAndOrderDetails();
}
