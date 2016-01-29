package com.camel.test.application.threads;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultExecutorServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeExecutorServiceManager extends DefaultExecutorServiceManager {

    private static final Logger LOG = LoggerFactory.getLogger(JeeExecutorServiceManager.class);

    protected JeeThreadFactoryFactory jeeThreadFactoryFactory;

    public JeeThreadFactoryFactory getJeeThreadFactoryFactory() {
        return jeeThreadFactoryFactory;
    }

    public void setJeeThreadFactoryFactory(JeeThreadFactoryFactory jeeThreadFactoryFactory) {
        this.jeeThreadFactoryFactory = jeeThreadFactoryFactory;
    }

    public JeeExecutorServiceManager(CamelContext camelContext) {
        super(camelContext);
    }

    @Override
    public Thread newThread(String name, Runnable runnable) {
        JeeThreadFactory jeeThreadFactory = jeeThreadFactoryFactory.createJeeThreadFactory(getThreadNamePattern(), name, true);
        return jeeThreadFactory.newThread(runnable);
    }

}
