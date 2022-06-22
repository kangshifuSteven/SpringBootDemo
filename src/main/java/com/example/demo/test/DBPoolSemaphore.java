package com.example.demo.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
/**
 * 信号量测试类
 * @author ranlongkang
 * @date 2021/12/24 16:05
 */
public class DBPoolSemaphore {

    private final static int POOL_SIZE = 10;
    //两个指示器，分别表示池子还有可用连接和已用连接
    private final Semaphore useful, useless;
    //存放数据库连接的容器
    private static LinkedList<Connection> pool = new LinkedList<Connection>();

    //初始化池
    static {
        for (int i = 0; i < POOL_SIZE; i++) {
            Connection connection = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false","root","123456");
                pool.addLast(connection);
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }finally {
                if(connection != null){
                    try {
                        connection.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        }
    }

    public DBPoolSemaphore() {
        this.useful = new Semaphore(10);
        this.useless = new Semaphore(0);
    }

    /*归还连接*/
    public void returnConnect(Connection connection) throws InterruptedException {
        if (connection != null) {
            System.out.println("当前有" + useful.getQueueLength() + "个线程等待数据库连接!!"
                    + "可用连接数：" + useful.availablePermits());
            useless.acquire();
            synchronized (pool) {
                pool.addLast(connection);
            }
            useful.release();
        }
    }

    /*从池子拿连接*/
    public Connection takeConnect() throws InterruptedException {
        useful.acquire();
        Connection connection;
        synchronized (pool) {
            connection = pool.removeFirst();
        }
        useless.release();
        return connection;
    }
}