package com.example.demo.order.domain;


import java.util.Random;

public class OrderRequest {

    Integer userId = new Random().nextInt(1000);

    Integer productId = new Random().nextInt(1000);

    Integer status = 0;//状态 失败,成功

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
