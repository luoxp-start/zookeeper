package com.example.zookeeper;

import com.example.zookeeper.utils.ZookeeperConnection;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;

/**
 * @author: luoxp
 * @date: 2021-02-25
 **/
public class ZookeeperTest {

    public static ZooKeeper zk = ZookeeperConnection.getConnection();

    public static void main(String[] args) throws Exception{
        get();
        zk.close();
    }


    public static void get () throws Exception{
        byte[] data = zk.getData("/hadoop", false, new Stat());
        String s = new String(data);
        System.out.println(s);
    }

    public static void create()throws Exception{
        /**
         *  args1:节点名
         *  args2：节点数据
         *  args3:权限列表
         *  args4:节点类型  持久化
         */

        String s = zk.create("/create", "数据".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(s);
    }

    public static  void create2()throws Exception{
        ArrayList<ACL> acls = new ArrayList<>();
        //授权模式  和 授权对象
        Id id = new Id("world","anyone");
        acls.add(new ACL(ZooDefs.Perms.READ,id));
        acls.add(new ACL(ZooDefs.Perms.WRITE,id));
        zk.create("/create","data".getBytes(),acls,CreateMode.PERSISTENT);
    }
    public static  void create3()throws Exception{
        zk.addAuthInfo("digest","username:password".getBytes());
        zk.create("/create","data".getBytes(),ZooDefs.Ids.CREATOR_ALL_ACL,CreateMode.PERSISTENT);
    }

    public static void create4()throws Exception{
        String s = zk.create("/create", "data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println(s);
    }
    public static void create5()throws Exception{
        /*zk.create("/create", "data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL, new AsyncCallback.Create2Callback() {
            @Override
            public void processResult(int i, String path, Object o, String name, Stat stat) {
                System.out.println(i);
                System.out.println(path);
                System.out.println(o);
                System.out.println(name);
                System.out.println(stat);
            }
        },"123");
        System.out.println("主线程结束");*/
    }


}
