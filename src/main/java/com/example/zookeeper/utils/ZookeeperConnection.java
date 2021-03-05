package com.example.zookeeper.utils;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.sql.SQLOutput;
import java.util.concurrent.CountDownLatch;

/**
 * @author: luoxp
 * @date: 2021-02-25
 **/
public class ZookeeperConnection {

    public static ZooKeeper getConnection(){
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            ZooKeeper zooKeeper = new ZooKeeper("192.168.2.204:2181", 5000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
                        System.out.println("连接成功");
                        countDownLatch.countDown();
                    }else if(watchedEvent.getState() == Event.KeeperState.Disconnected){
                        System.out.println("断开连接，通知运维人员");
                    }else if(watchedEvent.getState() == Event.KeeperState.Expired){
                        System.out.println("超时，重新创建会话");
                    }else if(watchedEvent.getState() == Event.KeeperState.AuthFailed){
                        System.out.println("认证失败");
                    }
                }
            });
            countDownLatch.await();
            System.out.println("seesionId:"+zooKeeper.getSessionId());
            return zooKeeper;
        }catch (Exception E){
            E.printStackTrace();
        }
        return null;
    }
}
