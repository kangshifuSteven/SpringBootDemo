package com.example.demo.init;
import com.example.demo.redis.RedisService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageSearchWebApplicationTests {
     @Autowired
     private RedisService redisService;

     ExecutorService executorService = null;

    @Before
    public void init() {
        List<String> list = new ArrayList<>(100);
        for (int i = 0; i < 10; i++) {
            list.add("1");
        }
       //模拟商品Id和库存
       redisService.listRPushByJson("5", list, 10000);
       executorService = Executors.newFixedThreadPool(800);

    }

    @Test
    public void contextLoads() {
        AtomicInteger successNum = new AtomicInteger(0);
        AtomicInteger failNum = new AtomicInteger(0);
        CountDownLatch countDownLatch = new CountDownLatch(100);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(100);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                String value = redisService.listLPopByJson("5", String.class);
                System.out.println("商品库存:"+value);
                if ( value != null) {
                    System.out.println("成功的线程name: " + Thread.currentThread().getName());
                    successNum.getAndIncrement();
                } else {
                    failNum.getAndIncrement();
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("下单成功：" + successNum.get());
        System.out.println("下单失败：" + failNum.get());
    }
}