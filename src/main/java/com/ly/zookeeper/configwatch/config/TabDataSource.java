package com.ly.zookeeper.configwatch.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



@Repository
public class TabDataSource {
    Logger logger = LoggerFactory.getLogger(TabDataSource.class);

    @Autowired
    public HotSwappableTargetSource hotSwappableTargetSource;


    public void  changeDataSource(DruidDataSource dataSource){
        try {
            hotSwappableTargetSource.swap(dataSource);
            logger.info("==========================================");
            logger.info("数据源切换成功!!![" + dataSource.getUrl() + "]");
            logger.info("==========================================");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
