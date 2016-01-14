package com.cup;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

/**
 * Created by leiping on 2016/1/14.
 */
public class CuratorWatcherTest {

    public static String ZK_ADDRESS = "172.18.63.75:2181";
    private static String ZK_PATH = "/zktest";

    public static void main(String[] args) throws Exception {
        // connect to zk.
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new RetryNTimes(10,5000));

        client.start();
        System.out.println("zk client start successfully.");

        //register watcher
        PathChildrenCache watcher = new PathChildrenCache(
                client,
                ZK_PATH,
                true // if cache data
        );

        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                ChildData data = pathChildrenCacheEvent.getData();
                if(data == null){
                    System.out.println("no data in event[" + pathChildrenCacheEvent + "]");
                } else {
                    System.out.println("Receive event:"
                        + "type=[" + pathChildrenCacheEvent.getType() + "]"
                            + ", path=[" + data.getPath() + "]"
                            + ", data=[" + new String(data.getData()) + "]"
                            + ", stat=[" + data.getStat() + "]"
                    );
                }
            }
        };

        watcher.getListenable().addListener(listener);
        watcher.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        System.out.println("Register zk watcher successfully!");

        Thread.sleep(Integer.MAX_VALUE);
    }
}
