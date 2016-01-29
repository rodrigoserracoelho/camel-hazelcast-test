package com.camel.test.hazelcast;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.hazelcast.core.Hazelcast;

public class HazelcastLoader implements ServletContextListener {

	
	public void contextInitialized(ServletContextEvent sce) {
	}

	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Shutting down all Hazelcast!");
		Hazelcast.shutdownAll();
	}

}
