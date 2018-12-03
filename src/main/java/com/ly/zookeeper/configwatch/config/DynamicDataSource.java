package com.ly.zookeeper.configwatch.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * Created by leiyang on 2018/12/03
 */
public class DynamicDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        System.out.println("数据源为"+DataSourceContextHolder.getDB());
        return DataSourceContextHolder.getDB();
    }


}
