package com.camel.test.application.threads;

import org.apache.camel.impl.DefaultThreadPoolFactory;
import org.apache.camel.spi.ThreadPoolProfile;
import org.apache.camel.util.concurrent.CamelThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.getField;

public class JeeThreadPoolFactory extends DefaultThreadPoolFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JeeThreadPoolFactory.class);

    static Field patternField;
    static Field nameField;
    static Field daemonField;

    static {
        patternField = findField(CamelThreadFactory.class, "pattern");
        patternField.setAccessible(true);
        nameField = findField(CamelThreadFactory.class, "name");
        nameField.setAccessible(true);
        daemonField = findField(CamelThreadFactory.class, "daemon");
        daemonField.setAccessible(true);
    }

    private JeeThreadFactoryFactory jeeThreadFactoryFactory;

    public JeeThreadFactoryFactory getJeeThreadFactoryFactory() {
        return jeeThreadFactoryFactory;
    }

    public void setJeeThreadFactoryFactory(JeeThreadFactoryFactory jeeThreadFactoryFactory) {
        this.jeeThreadFactoryFactory = jeeThreadFactoryFactory;
    }

    @Override
    public ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        threadFactory = convertThreadFactory(threadFactory);
        return super.newCachedThreadPool(threadFactory);
    }

    @Override
    public ExecutorService newThreadPool(ThreadPoolProfile profile, ThreadFactory threadFactory) {
        threadFactory = convertThreadFactory(threadFactory);
        return super.newThreadPool(profile, threadFactory);
    }

    @Override
    public ScheduledExecutorService newScheduledThreadPool(ThreadPoolProfile profile, ThreadFactory threadFactory) {
        threadFactory = convertThreadFactory(threadFactory);
        return super.newScheduledThreadPool(profile, threadFactory);
    }

    protected ThreadFactory convertThreadFactory(ThreadFactory original) {
        if (original == null) {
            return null;
        }

        if (original instanceof CamelThreadFactory) {
            String pattern = (String) getField(patternField, original);
            String name = (String) getField(nameField, original);
            boolean daemon = (Boolean) getField(daemonField, original);
            return jeeThreadFactoryFactory.createJeeThreadFactory(pattern, name, daemon);
        } else {
            if (!(original instanceof JeeThreadFactory)) {
                LOG.warn("Failed to convert to JeeThreadFactory: " + original.getClass().getName());
            }
            return original;
        }
    }

}
