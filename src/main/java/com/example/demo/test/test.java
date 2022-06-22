package com.example.demo.test;

import com.example.demo.clouddo.domain.Order;
import com.example.demo.clouddo.domain.OrderDetail;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class test {

    // 定义线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(100);

    public static void main(String[] args) {
//        for (int i = 0; i < 50; i++) {
//            Thread thread = new BusiThread();
//            thread.start();
//        }

        // 目标list
        List<String> list = new ArrayList<>();
        list.add("11111");
        list.add("222222");
        System.out.println(list);

        // 通过异步的方式获取所有信息
        System.out.println("开始获取信息...");
        List<Future> tasks = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            // 创建 100 个任务，将这 100 个任务添加到 tasks 中
            tasks.add(getData(executorService, i));
        }
        // 遍历 tasks，处理每一个 task
        for (Future task : tasks) {
            try {
                // task.get(): 获取任务处理得到的信息
                Optional.ofNullable(task.get()).ifPresent(str -> list.add((String) str));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        tasks.clear();
        System.out.println("结束获取信息...");

        // 关闭线程池
        executorService.shutdown();
        System.out.println("list: " + list);
    }

    @Test
    public void test1() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date costStartDate = dateFormat.parse("2021-01-13");
        Date costEndDate = dateFormat.parse("2022-01-13");

        while (costStartDate.getTime() <= costEndDate.getTime()) {

            Calendar ca = Calendar.getInstance();
            ca.setTime(costStartDate);
            int addMonth = 3;
            //起始日期号数
            int day = ca.get(Calendar.DAY_OF_MONTH);
            Date endDate = null;
            //结算日为0，表示到自然月最后一天为结算结算日期
            if (0 == 0) {
                //如果结算日 >= 当前日期号数，则增加月份数需-1
                //月份数-1
                addMonth = addMonth - 1;
                //计算几个月周期（周期月份-1)
                ca.add(Calendar.MONTH, addMonth);
                //最大号数
                int maxDay = ca.getActualMaximum(Calendar.DATE);
                //设置号数
                ca.set(Calendar.DATE, maxDay);
                endDate = ca.getTime();
            }
            System.out.println("每个周期的结束日期:"+dateFormat.format(endDate));
            costStartDate = addOrReduceDays(endDate,1);
            System.out.println("每个周期的开始日期:"+dateFormat.format(costStartDate));
        }

    }


    @Test
    public void test2(){
        Order a = new Order();
        List<OrderDetail> b = a.getOrderdetails();
        //未初始化
        if(b == null){
            b = new ArrayList<>();
        }
        OrderDetail c = new OrderDetail();
        c.setId(1L);
        b.add(c);
        a.setOrderdetails(b);
        System.out.println(b == null);
        System.out.println(a.getOrderdetails() == null);
    }

    @Test
    public void test3(){
        List<OrderDetail> a = null;
        List<OrderDetail> b = new ArrayList<>();
        if(a != null || b != null){
            System.out.println("测试");
        }
        if(!Objects.isNull(a) || !Objects.isNull(b)){
            System.out.println("测试2");
        }
    }

    @Test
    public void test4(){
        try {
            List<String> a = new ArrayList<>();
            a.add("1");
            a.add("2");
            a.add("");
            a.add("5");
            String b = "2";
            getTestIsArray(a,true);
            getTestIsArray(b,false);

            System.out.println(a.stream().reduce("0", BinaryOperator.maxBy(Comparator.comparing(Integer::parseInt))));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test5(){
        int[] a = {1,2,3,4,5};
        BitSet bitSet = new BitSet(6);
        for (int i = 0; i < a.length; i++) {
            bitSet.set(a[i],true);
        }
        System.out.println(bitSet.size());
        System.out.println(bitSet.get(6));
        System.out.println(bitSet.get(5));
    }

    @Test
    public void test6(){
        List<Long> a = new ArrayList<>();
        a.add(1L);
        a.add(2L);
        List<String> b = a.stream().map(String::valueOf).collect(Collectors.toList());
        System.out.println(a);
    }

    @Test
    public void test7() throws ParseException {
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse("2021-11-8"+" 00:00:00");
        System.out.println(date.getTime());
        System.out.println(date.getTime()<time);
    }


    public boolean getTestIsArray(Object o, Boolean isArray) throws Exception {
        try {
            if(o instanceof ArrayList){
                List<String> arr = (List<String>) o;
                for (String s:arr){
                    isValidExamineFlag(isArray,s);
                }
            }else{
                String b = (String) o;
                //isArray = true;
                isValidExamineFlag(isArray,b);
            }
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    private void isValidExamineFlag(boolean flag,String proceedCode) throws Exception {
        try {
            //空并且是集合
            if(StringUtils.isEmpty(proceedCode)){
                //默认集合下中断执行
                if(flag){
                    flag = false;
                }else{
                    //默认单个数据抛出异常中断执行
                    throw new Exception("不能为空");
                }
            }else{
                flag = true;
            }
            if(flag){
                System.out.println(proceedCode+"循环执行下一步");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }
    public static Date addOrReduceDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(date);
        //日期+-天数
        c.add(Calendar.DATE, days);
        return c.getTime();
    }


    //-------------------------------------测试信号量线程数控制-----------------------------------

    private static DBPoolSemaphore dbPool = new DBPoolSemaphore();

    private static class BusiThread extends Thread {
        @Override
        public void run() {
            Random r = new Random();//让每个线程持有连接的时间不一样
            long start = System.currentTimeMillis();
            try {
                Connection connect = dbPool.takeConnect();
                System.out.println("Thread_" + Thread.currentThread().getId()
                        + "_获取数据库连接共耗时【" + (System.currentTimeMillis() - start) + "】ms.");
                //模拟业务操作，线程持有连接查询数据
                Thread.sleep(100 + r.nextInt(100));
                System.out.println("查询数据完成，归还连接！");
                dbPool.returnConnect(connect);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void test8(){
        for (int i = 0; i < 50; i++) {
            Thread thread = new BusiThread();
            thread.start();
        }

    }

    @Test
    public void test9(){
        BigDecimal val = new BigDecimal("11.457111");
        val = val.setScale(2,BigDecimal.ROUND_HALF_UP);
        System.out.println(val);
    }
    public static Future<String> getData(ExecutorService executorService, int param) {
        return executorService.submit(()->{
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                try {
                    // 睡眠0.1s，为了体验多线程的速度
                    // 如果不使用多线程，速度很慢；
                    // 如果使用多线程，速度很快，基本为睡眠时长。
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sb.append(param * 10 + i).append(",");
            }

            sb = sb.length() > 0 ? sb.deleteCharAt(sb.length() - 1) : sb;

            System.out.println(Thread.currentThread().getName() + "...");
            return sb.toString();
        });
    }
}
