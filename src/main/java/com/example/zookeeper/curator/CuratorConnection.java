package com.example.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.retry.RetryUntilElapsed;

/**
 * @author: luoxp
 * @date: 2021-03-05
 **/
public class CuratorConnection {

    public static void main(String[] args) {

        //重连策略 1 ，只重连一次 (超时后3秒开始重连)
        RetryOneTime retryOneTime = new RetryOneTime(3000);
        //每3秒重连一次，重连3次
        RetryNTimes retryNTimes = new RetryNTimes(3, 3000);
        //每3秒重连一次，最多重连10秒
        RetryUntilElapsed retryUntilElapsed = new RetryUntilElapsed(10000, 3000);
        //随着重连重次数的增加，重连间隔会边长 公式：baseSleepTimeMs * Math.max(1,random.nextInt(1<<( retryCount + 1 )))
        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);

        //创建连接对象
        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                //IP端口和地址，多个用逗号隔开
                .connectString("192.168.2.204:2181")
                //会话超时时间
                .sessionTimeoutMs(5000)
                .retryPolicy(new RetryOneTime(3000))
                .namespace("test")
                .build();

        //打开连接
        client.start();

        System.out.println("连接对象：            "+client.isStarted());
        //关闭连接
        client.close();
    }

    public static CuratorFramework  getConnection(){
        //重连策略 1 ，只重连一次 (超时后3秒开始重连)
        RetryOneTime retryOneTime = new RetryOneTime(3000);
        //每3秒重连一次，重连3次
        RetryNTimes retryNTimes = new RetryNTimes(3, 3000);
        //每3秒重连一次，最多重连10秒
        RetryUntilElapsed retryUntilElapsed = new RetryUntilElapsed(10000, 3000);
        //随着重连重次数的增加，重连间隔会边长 公式：baseSleepTimeMs * Math.max(1,random.nextInt(1<<( retryCount + 1 )))
        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);

        //创建连接对象
        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                //IP端口和地址，多个用逗号隔开
                .connectString("192.168.2.204:2181")
                //会话超时时间
                .sessionTimeoutMs(5000)
                .retryPolicy(new RetryOneTime(3000))
                .namespace("test")
                .build();

        //打开连接
        client.start();
        return client;
    }
}
