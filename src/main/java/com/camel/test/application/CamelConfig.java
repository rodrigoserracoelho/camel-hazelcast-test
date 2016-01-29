package com.camel.test.application;


import org.apache.camel.CamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.camel.test.application.threads.JeeExecutorServiceManager;
import com.camel.test.application.threads.JeeThreadFactoryFactory;
import com.camel.test.application.threads.JeeThreadPoolFactory;

import javax.enterprise.concurrent.ManagedThreadFactory;

@Configuration
public class CamelConfig {

    @Bean(name = "jeeExecutorServiceManager")
    public JeeExecutorServiceManager jeeExecutorServiceManager(CamelContext camelContext, ManagedThreadFactory managedThreadFactory) {
    	JeeExecutorServiceManager jeeExecutorServiceManager = new JeeExecutorServiceManager(camelContext);
        jeeExecutorServiceManager.setThreadPoolFactory(jeeThreadPoolFactory(jeeThreadFactoryFactory(managedThreadFactory)));
        jeeExecutorServiceManager.setJeeThreadFactoryFactory(jeeThreadFactoryFactory(managedThreadFactory));
        return jeeExecutorServiceManager;
    }

    @Bean(name = "jeeThreadPoolFactory")
    public JeeThreadPoolFactory jeeThreadPoolFactory(JeeThreadFactoryFactory jeeThreadFactoryFactory) {
    	JeeThreadPoolFactory jeeThreadPoolFactory = new JeeThreadPoolFactory();
        jeeThreadPoolFactory.setJeeThreadFactoryFactory(jeeThreadFactoryFactory);
        return jeeThreadPoolFactory;
    }

    @Bean(name = "jeeThreadFactoryFactory")
    public JeeThreadFactoryFactory jeeThreadFactoryFactory(ManagedThreadFactory managedThreadFactory) {
    	JeeThreadFactoryFactory jeeThreadFactoryFactory = new JeeThreadFactoryFactory();
        jeeThreadFactoryFactory.setManagedThreadFactory(managedThreadFactory);
        return jeeThreadFactoryFactory;
    }

}
