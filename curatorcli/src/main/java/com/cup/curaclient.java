package com.cup;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import java.util.List;

/**
 * Created by leiping on 2016/1/14.
 */
public class curaclient {

    public static String ZK_ADDRESS = "172.18.63.75:2181";
    private static String ZK_PATH = "/zktest";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new RetryNTimes(10,5000));

        client.start();
        System.out.println("zk client start successfully.");

        //create node
        String data1 = "helo";
        print("create", ZK_PATH, data1);
        client.create().creatingParentsIfNeeded().forPath(ZK_PATH, data1.getBytes());

        //get node data
        print("ls", "/");
        print(client.getChildren().forPath("/"));
        print("get",ZK_PATH);
        print(client.getData().forPath(ZK_PATH));

        //modify data
        String data2 = "wod";
        print("set",ZK_PATH,data2);
        client.setData().forPath(ZK_PATH, data2.getBytes());
        print("get",ZK_PATH);
        print(client.getData().forPath(ZK_PATH));

        //remove node
        print("delete", ZK_PATH);
        client.delete().forPath(ZK_PATH);
        print("ls", "/");
        print(client.getChildren().forPath("/"));

    }

    private static void print(Object result) {
        System.out.println(
                result instanceof byte[] ? new String((byte[]) result) : result);
    }

    private static void print(String... cmds) {
        StringBuffer text = new StringBuffer("$ ");
        for(String cmd: cmds){
            text.append(cmd).append(" ");
        }
        System.out.println(text.toString());
    }


}
