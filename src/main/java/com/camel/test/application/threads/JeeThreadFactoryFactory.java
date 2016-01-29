package com.camel.test.application.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.concurrent.ManagedThreadFactory;

public class JeeThreadFactoryFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JeeThreadFactoryFactory.class);

    private ManagedThreadFactory managedThreadFactory;

    public ManagedThreadFactory getManagedThreadFactory() {
        return managedThreadFactory;
    }

    public void setManagedThreadFactory(ManagedThreadFactory managedThreadFactory) {
        this.managedThreadFactory = managedThreadFactory;
    }

    public JeeThreadFactory createJeeThreadFactory(String pattern, String name, boolean isDaemon) {
        JeeThreadFactory jeeThreadFactory = new JeeThreadFactory();
        jeeThreadFactory.setManagedThreadFactory(managedThreadFactory);
        jeeThreadFactory.setPattern(pattern);
        jeeThreadFactory.setName(name);
        jeeThreadFactory.setDaemon(isDaemon);
        return jeeThreadFactory;
    }

}
