package com.vens.zookeeper.client;


import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author LuZhiqing
 * @Description:
 * @date 2019/1/8
 */
@Service
public class Zook {
    private static Logger logger=LoggerFactory.getLogger(Zook.class);
    public static ZooKeeper zooKeeper;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    @Autowired
    private Environment environment;

    public ZooKeeper create(){
        String conStr=environment.getProperty("zookeeper.address");
        String timeout=environment.getProperty("zookeeper.session_timeout");
        Watcher watcher=new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                logger.info("successfully create zk session");
                countDownLatch.countDown();
            }
        };
        if(timeout.isEmpty()){
            timeout = "6000";
        }
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper=new ZooKeeper(conStr,Integer.parseInt(timeout),watcher);
            zooKeeper.addAuthInfo("digest","vens:7416kobe".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zooKeeper;
    }
}
