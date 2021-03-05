package com.example.zookeeper;

import com.example.zookeeper.utils.ZookeeperConnection;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author: luoxp
 * @date: 2021-02-25
 **/
public class ZKWatcher implements Watcher {
    public static ZooKeeper zk = ZookeeperConnection.getConnection();


    @Override
    public void process(WatchedEvent watchedEvent) {

    }

    public static void main(String[] args) {

    }

    public static void watcherExist()throws Exception{
        //使用创建连接的watcher对象
        zk.exists("/hadoop",true);

        //使用自定义的监听对象
        //一次性的解决
        Stat exists = zk.exists("/hadoop", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    zk.exists("/hadoop",this);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        System.out.println(exists);
    }

    public static void watcherGetData()throws Exception{
        byte[] data1 = zk.getData("/hadoop", true, null);//使用创建连接时的监听对象

        //使用自定义的监听对象
        byte[] data = zk.getData("/hadoop", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    if(watchedEvent.getType() == Event.EventType.NodeDataChanged){
                        zk.getData("/hadoop",this,null);
                    }
                }catch (Exception E){

                }

            }
        }, null);
    }

}
