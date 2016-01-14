package com.cup;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.TimeUnit;

/**
 * Created by leiping on 2016/1/14.
 */
public class CuratorDistrLockTest {

    public static String ZK_ADDRESS = "172.18.63.75:2181";
    public static String ZK_LOCK_PATH = "/zktest";

    private static void doWithLock(CuratorFramework client){
        InterProcessMutex lock = new InterProcessMutex(client, ZK_LOCK_PATH);
        try{
            if (lock.acquire(10 * 1000, TimeUnit.SECONDS)) {
                System.out.println(Thread.currentThread().getName() + " hold lock.");
                Thread.sleep(5000L);
                System.out.println(Thread.currentThread().getName() + " release lock");
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.release();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // connect to zk.
        final CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new RetryNTimes(10,5000));

        client.start();
        System.out.println("zk client start successfully.");

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                doWithLock(client);
            }
        }, "t1");

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                doWithLock(client);
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
