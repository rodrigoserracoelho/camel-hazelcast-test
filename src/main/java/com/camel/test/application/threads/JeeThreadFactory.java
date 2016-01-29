package com.camel.test.application.threads;

import org.apache.camel.util.concurrent.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.concurrent.ManagedThreadFactory;
import java.util.concurrent.ThreadFactory;

public class JeeThreadFactory implements ThreadFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JeeThreadFactory.class);

    private ManagedThreadFactory managedThreadFactory;
    private String pattern;
    private String name;
    private boolean daemon;

    
    public Thread newThread(Runnable runnable) {
        String threadName = ThreadHelper.resolveThreadName(pattern, name);
        Thread result = managedThreadFactory.newThread(runnable);
        result.setName(threadName);
        result.setDaemon(daemon);

        LOG.trace("Created thread[{}] -> {}", threadName, result);
        return result;
    }

    public ManagedThreadFactory getManagedThreadFactory() {
        return managedThreadFactory;
    }

    public void setManagedThreadFactory(ManagedThreadFactory managedThreadFactory) {
        this.managedThreadFactory = managedThreadFactory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String toString() {
        return "JeeThreadFactory[" + name + "]";
    }

}
