package com.example.zookeeper.utils;



import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author: luoxp
 * @date: 2021-02-25
 **/
public class Mylock {
    String IP = "192.168.2.204:2181";
    CountDownLatch latch = new CountDownLatch(1);
    ZooKeeper zk;
    private static final String LOCK_ROOT_PATH = "/LOCKS";
    private static final String LOCK_NODE_NAME = "/LOCK_";
    private String lockPath;

    public void acquireLock()throws Exception{
        //创建锁节点
        createLock();
        //尝试获取锁
        attemptLock();
    }
    //创建锁
    public void createLock()throws Exception{
        Stat exists = zk.exists(LOCK_ROOT_PATH, false);
        if(exists == null){
            String s = zk.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        lockPath = zk.create(LOCK_ROOT_PATH + LOCK_NODE_NAME, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("节点创建成功："+lockPath);
    }
    //尝试获取锁
    public void attemptLock()throws Exception{
        //获取locks下的所有子节点
        List<String> children = zk.getChildren(LOCK_ROOT_PATH, false);
        Collections.sort(children);//对节点进行排序
        int i = children.indexOf(lockPath.substring(LOCK_ROOT_PATH.length() + 1));
        if(i == 0){
            System.out.println("获取锁成功");
            return;
        }else {
            String path = children.get(i - 1);
            Stat exists = zk.exists(LOCK_ROOT_PATH + "/" + path, watcher);
            if(exists == null){
                attemptLock();
            }else {
                synchronized (watcher){
                    watcher.wait();
                }
                attemptLock();
            }
        }
    }

    //监视上一个节点是否被删除
    Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            if(watchedEvent.getType() == Event.EventType.NodeDeleted){
                synchronized (this){
                    this.notifyAll();
                }
            }
        }
    };
    //释放锁
    public void releaseLock()throws Exception{
        //删除临时有序节点
        zk.delete(this.lockPath,-1);
        zk.close();
        System.out.println("锁释放");

    }

    public Mylock(){
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            zk = new ZooKeeper(IP, 5000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getType() == Event.EventType.None){
                        System.out.println("连接成功");
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
        }catch (Exception E){
            E.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Mylock mylock = new Mylock();
        mylock.createLock();
    }

}
