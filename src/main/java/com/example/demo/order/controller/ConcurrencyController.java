package com.example.demo.order.controller;

import com.example.demo.InitService;
import com.example.demo.order.domain.OrderRequest;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.StatusCode;
import com.example.demo.test.OrderDealThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/8/25.
 */
@RestController
public class ConcurrencyController {

    private static final Logger log= LoggerFactory.getLogger(ConcurrencyController.class);

    private static ConcurrentLinkedQueue<OrderRequest> orderQueue = new ConcurrentLinkedQueue();

    private static final String Prefix="concurrency";

    @Autowired
    private InitService initService;

    @Autowired
    private TaskExecutor taskExecutor;

    private static AtomicInteger totalprodocut;

    static {
        totalprodocut = new AtomicInteger(15);
    }

    @RequestMapping(value = Prefix+"/robbing/thread",method = RequestMethod.POST)
    public BaseResponse robbingThread(OrderRequest orderRequest){
        try {
            // redis查询库存量
            if(totalprodocut.get() < 1){
                System.out.println("失败了,超出产品库存");
                return new BaseResponse(StatusCode.Fail);
            }else{
                if(orderQueue.size() < 15){
                    // 所有请求放入队列ConcurrentLinkedQueue
                    orderQueue.add(orderRequest);
                }else{
                    System.out.println("失败了,超出队列长度");
                    // 超出了最大请求数,直接返回提示
                    return new BaseResponse(StatusCode.Fail);
                }
            }
            // 线程池执行队列请求
            if (!OrderDealThread.dealLock.isLocked()) {
                OrderDealThread dealQueue = new OrderDealThread(orderQueue);
                taskExecutor.execute(dealQueue);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse(StatusCode.Fail);
        }
        return new BaseResponse(StatusCode.Success);
//        BaseResponse response=new BaseResponse(StatusCode.Success);
//        initService.generateMultiThread();
//        return response;
    }
}







































