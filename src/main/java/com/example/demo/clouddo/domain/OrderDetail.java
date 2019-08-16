package com.example.demo.clouddo.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/7/6.
 */
public class OrderDetail implements Serializable{

    private Long id;

    private Long itemNum;

    private Long itemId;

    public OrderDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemNum() {
        return itemNum;
    }

    public void setItemNum(Long itemNum) {
        this.itemNum = itemNum;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
