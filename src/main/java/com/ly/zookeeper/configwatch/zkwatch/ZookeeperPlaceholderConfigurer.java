package com.ly.zookeeper.configwatch.zkwatch;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Repository;

import java.util.Properties;

@Repository("zkPlaceholderConfigurer")
public class ZookeeperPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    @Autowired
    private ZookeeperCentralConfigurer zkCentralConfigurer;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
    }

    public void ignoreUnresolvablePlaceholders(){
        super.ignoreUnresolvablePlaceholders = true;
    }

    public void order(){
        super.setOrder(1);
    }

}
