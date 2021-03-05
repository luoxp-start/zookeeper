package com.example.zookeeper.curator;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.ArrayList;

/**
 * @author: luoxp
 * @date: 2021-03-05
 **/

public class CuratorTest {

    public static void main(String[] args) throws Exception{
        //create();
        create1();
    }


    public static void create()throws Exception{
        CuratorFramework connection = CuratorConnection.getConnection();
        connection.create()
                //如果父节点不存在就创建父节点。（同时创建多级节点的时候）//递归创建节点树
                .creatingParentsIfNeeded()
                //节点类型：持久化
                .withMode(CreateMode.PERSISTENT)
                //节点权限列表 world:anyone:cdraw
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/create/child","创建节点".getBytes());

        System.out.println("节点创建完成");
        connection.close();
    }

    public static void create1()throws Exception{
        //自定义权限列表
        ArrayList<ACL> list = new ArrayList<>();
        //授权模式和授权对象
        Id id = new Id("ip","192.168.2.99");
        list.add(new ACL(ZooDefs.Perms.ALL,id));
        CuratorFramework connection = CuratorConnection.getConnection();
        connection.create().withMode(CreateMode.PERSISTENT).withACL(list).forPath("/create1","IP授权模式".getBytes());
        System.out.println("创建完成");
    }
}
