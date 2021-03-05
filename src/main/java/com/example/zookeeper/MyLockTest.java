package com.example.zookeeper;

import java.util.concurrent.TimeUnit;

/**
 * @author: luoxp
 * @date: 2021-02-26
 **/
public class MyLockTest {
    public static void main(String[] args) {

    }


    private void sell(){
        System.out.println("卖票开始");
        try {
            TimeUnit.SECONDS.sleep(5L);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("收票结束 ");
    }
}
