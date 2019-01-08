package com.vens.zookeeper.client;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author LuZhiqing
 * @Description:
 * @date 2019/1/8
 */
@Service
public class ZookRegister {
    @Autowired
    private Environment environment;

    public static final String fixedPath = "/luzhiqing";

    private String servername="l_zk_test_1";
    @Autowired
    private Zook zook;


    //@PostConstruct
    public void doRegister() throws KeeperException, InterruptedException, UnknownHostException {
        String name = environment.getProperty("spring.application.name");
        String port = environment.getProperty("server.port");
        String ip = environment.getProperty("server.address");
        Zook.zooKeeper = zook.create();
        Stat existFixedPath = Zook.zooKeeper.exists(fixedPath, false);
        if (null == existFixedPath) {
            Zook.zooKeeper.create(fixedPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        String severNode = fixedPath + "/" + servername;
        Stat existServerNode = Zook.zooKeeper.exists(severNode, false);
        if (null == existServerNode) {
            Zook.zooKeeper.create(severNode, "".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        if (ip == null || ip.equals("")) {
            ip = InetAddress.getLocalHost().getHostAddress();
        }
        if (port == null || port.equals("")) {
            port = "8080";
        }
        String address = ip + ":" + port;
        String svipmlNode = fixedPath + "/" + servername + "/" + name;
        Stat existSvipmlNode = Zook.zooKeeper.exists(svipmlNode, false);
        if (null == existSvipmlNode) {
            Zook.zooKeeper.create(svipmlNode, address.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        }
    }

}
