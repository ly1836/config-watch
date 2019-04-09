package com.ly.zookeeper.configwatch.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.ly.zookeeper.configwatch.ConfigWatchApplication;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by leiyang on 2018/12/03
 */
@Configuration
public class config {
    private static Logger logger = LoggerFactory.getLogger(config.class);

    @Bean
    public PropertiesConfiguration propertiesConfiguration() throws ConfigurationException {

        return new PropertiesConfiguration("application.properties");
    }

    @Bean(name = "dataSource")
    public DruidDataSource dataSource(@Autowired PropertiesConfiguration properties) {
        DruidDataSource dataSource = new DruidDataSource();
        //PropertiesConfiguration properties = new PropertiesConfiguration("application.properties");
        String driverClassName = properties.getString("my.datasource.db.driver-class-name");
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(properties.getString("my.datasource.db.jdbc-url"));
        dataSource.setUsername(properties.getString("my.datasource.db.username"));
        dataSource.setPassword(properties.getString("my.datasource.db.password"));

        return dataSource;
    }


    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Autowired ProxyFactoryBean proxyFactoryBean) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource 作为数据源则不能实现切换
        sqlSessionFactoryBean.setDataSource((DataSource) proxyFactoryBean.getObject());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/*.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSessionFactoryBean;
    }

    @Bean("hotSwappableTargetSource")
    public HotSwappableTargetSource hotSwappableTargetSource(@Autowired DataSource dataSource){
        return new HotSwappableTargetSource(dataSource);
    }

    @Bean("proxyFactoryBean")
    public ProxyFactoryBean proxyFactoryBean(@Autowired HotSwappableTargetSource hotSwappableTargetSource){
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTargetSource(hotSwappableTargetSource);
        return proxyFactoryBean;
    }
}
