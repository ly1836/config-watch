package com.ly.zookeeper.configwatch.zkwatch;

import com.alibaba.druid.pool.DruidDataSource;
import com.ly.zookeeper.configwatch.config.TabDataSource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

/**
 * <p>
 * zookeeper客户端配置
 * </p>
 */
/*@Component("zkCentralConfigurer")
@Configuration
@EnableTransactionManagement*/
public class ZookeeperCentralConfigurer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @Autowired
    private TabDataSource tabDataSource;

    //@Autowired
    //PropertyPlaceholderConfigurer propertyPlaceholderConfigurer;

    private CuratorFramework zkClient;

    private TreeCache treeCache;

    public String zkServers = "192.168.1.106:2181";

    //@Value("${zk.path}")
    private String zkPath = "/jdbc";

    //@Value("${zk.sessionTimeout}")
    private int sessionTimeout = 1000;


    //@Value("${zk.server.zkServers}")
    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    public void setZkPath(String zkPath) {
        this.zkPath = zkPath;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    @PostConstruct
    public void init() {
        initZkclient();
        //getConfigData();
        addZkListener();
    }


    private void initZkclient() {
        zkClient = CuratorFrameworkFactory.builder().connectString(zkServers).sessionTimeoutMs(sessionTimeout)
                .retryPolicy(new ExponentialBackoffRetry(100, 3)).build();
        zkClient.start();
    }

    private void addZkListener() {
        TreeCacheListener listener = (curatorFramework, treeCacheEvent) -> {
            if (treeCacheEvent.getType() == TreeCacheEvent.Type.NODE_UPDATED) {
                getConfigData();
            }
        };

        treeCache = new TreeCache(zkClient, zkPath);
        try {
            treeCache.start();
            treeCache.getListenable().addListener(listener);
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }

    private void getConfigData() {
        try {
            /*List<String> list = zkClient.getChildren().forPath(zkPath);
            for(String key : list){

            }*/

            String value = new String(zkClient.getData().forPath(zkPath + "/url"));
            logger.info("==========================================");
            logger.info(value);
            logger.info("==========================================");

            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl(value);
            dataSource.setUsername("root");
            dataSource.setPassword("admin");

            tabDataSource.changeDataSource(dataSource);
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }
}
