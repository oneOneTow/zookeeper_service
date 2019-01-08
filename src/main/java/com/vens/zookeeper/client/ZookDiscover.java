package com.vens.zookeeper.client;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author LuZhiqing
 * @Description:
 * @date 2019/1/8
 */
@Service
public class ZookDiscover {
    private static final Logger logger = LoggerFactory.getLogger(ZookDiscover.class);
    private static Map<String,List<String>> serverMap;
    public Watcher watcher=new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            Event.KeeperState state=event.getState();
            String path=event.getPath();
            event.getWrapper();
            String eventStr=event.toString();
            if(event.getType().equals(Event.EventType.NodeChildrenChanged)){
                serverMap=null;
            }
        }
    };
    private List<String> getNodes(String serverName) throws KeeperException, InterruptedException {
        if(null == serverMap){
            serverMap = new HashMap<>();
        }
        ZooKeeper zooKeeper = Zook.zooKeeper;
        Stat exists = null;
        Watcher existsWatcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                logger.info("zook discover SSS");
            }
        };
        String s="/vens/"+serverName;
        exists = zooKeeper.exists(s,existsWatcher);
        if(exists==null){
            return null;
        }
        List<String> serverList = serverMap.get("serverName");
        if(serverList != null && serverList.size()>0){
            return serverList;
        }
        List<String> children = zooKeeper.getChildren("/vens/"+serverName,watcher);
        List<String> list = new ArrayList<>();
        for(String c:children){
            byte[]data = zooKeeper.getData("/vens/"+serverName+"/"+s,watcher,null);
            list.add(new String(data));
        }
        serverMap.put(serverName,list);
        return list;
    }
    public String getServer(String serverName) throws KeeperException, InterruptedException {
        List<String> nodeList = getNodes(serverName);
        if(nodeList == null
                || nodeList.size()<1){
            return null;
        }
        //使用随机函数实现负载均衡
        String server=nodeList.get((int)(Math.random()*nodeList.size()));
        return server;
    }
}
