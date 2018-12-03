package com.ly.zookeeper.configwatch.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leiyang on 2018/12/03
 */
@Configuration
public class config {

    @Bean(name = "datasource")
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDefaultTargetDataSource(dataSource());

        Map<Object, Object> dsMap = new HashMap();
        dsMap.put("datasource", dataSource());

        dynamicDataSource.setTargetDataSources(dsMap);

        return dynamicDataSource;
    }
}
