package com.vens.zookeeper;

import com.vens.zookeeper.client.Zook;
import com.vens.zookeeper.client.ZookDiscover;
import com.vens.zookeeper.client.ZookRegister;
import org.apache.zookeeper.KeeperException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.UnknownHostException;

/**
 * @author LuZhiqing
 * @Description:
 * @date 2019/1/8
 */
@Configuration
@PropertySource("classpath:zk.properties")
public class Application {
    public static void main(String[] args) {
        ApplicationContext context= new ClassPathXmlApplicationContext(new String[]{"app.xml"});
        Zook zook=(Zook)context.getBean("zook");
        ZookDiscover zookDiscover=(ZookDiscover)context.getBean("zookDiscover");
        ZookRegister zookRegister=(ZookRegister)context.getBean("zookRegister");
        try {
            zookRegister.doRegister();
            System.out.println("×¢²á³É¹¦");
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
