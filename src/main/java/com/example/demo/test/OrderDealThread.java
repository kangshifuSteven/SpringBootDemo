package com.example.demo.test;

import com.example.demo.order.domain.OrderRequest;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  订单处理线程
 *  按照队列请求一条条处理
 * @author ranlongkang
 * @date 2021/10/18 15:19
 */
public class OrderDealThread implements Runnable {

    ConcurrentLinkedQueue<OrderRequest> orderQueue;

    private static AtomicInteger totalprodocut;

    public static ReentrantLock dealLock = new ReentrantLock(true);

    static {
        totalprodocut = new AtomicInteger(15);
    }

    public OrderDealThread() {
    }

    public OrderDealThread(ConcurrentLinkedQueue queque) {
        this.orderQueue = queque;
    }

    @Override
    public void run() {
        while (!orderQueue.isEmpty()) {
            dealLock.lock();
            try {
                Iterator<OrderRequest> it = orderQueue.iterator();
                while (it.hasNext()) {
                    dealQueque(it.next());
                }
            } catch (Exception e) {
                // 如果业务执行失败,则补充队列请求执行

                e.printStackTrace();
            } finally {
                dealLock.unlock();
            }
        }
    }

    void dealQueque(OrderRequest orderRequest) {
        // 待处理状态
        if (orderRequest.getStatus() == 0) {
            int status = 2;
            // 再次判断商品库存
            if (totalprodocut.get() > 0) {
                //TODO 下单处理其他逻辑

                totalprodocut.decrementAndGet();// 减库存
                status = 1;
            }
            if (status == 2) {
                orderRequest.setStatus(2);
            } else {
                System.out.println(orderRequest.getUserId() + " deal ok:" + Thread.currentThread().getName());
                orderRequest.setStatus(1);
            }
        }
    }
}