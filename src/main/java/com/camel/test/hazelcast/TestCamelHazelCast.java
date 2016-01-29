package com.camel.test.hazelcast;

import org.apache.camel.spi.IdempotentRepository;
import org.apache.camel.support.ServiceSupport;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class TestCamelHazelCast extends ServiceSupport implements IdempotentRepository<String> {

    private String repositoryName;
    private IMap<String, Boolean> repo;
    private HazelcastInstance hazelcastInstance;
    private static TestCamelHazelCast fileLockerInstance;
    
    public TestCamelHazelCast(String repositoryName) {
        this.repositoryName = repositoryName;
        Config config = new Config();
        config.setInstanceName("fileLocker");
        this.hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        fileLockerInstance = this;
    }

    @Override
    protected void doStart() throws Exception {
        repo = hazelcastInstance.getMap(repositoryName);
    }

    @Override
    protected void doStop() throws Exception {
    }

    public boolean add(String key) {
        repo.lock(key);
        if(repo.containsKey(key)) {
        	repo.unlock(key);
        	return true;
        } else {
        	try {
            	boolean result = repo.putIfAbsent(key, false) == null;
            	return result;
            } finally {
                repo.unlock(key);
            }
        }
    }

    public boolean confirm(String key) {
        repo.lock(key);
        try {
            return repo.replace(key, false, true);
        } finally {
            repo.unlock(key);
        }
    }

    public boolean contains(String key) {
        repo.lock(key);
        try {
        	boolean contains = this.repo.containsKey(key);
        	return contains;
        } finally {
            repo.unlock(key);
        }
    }

    public boolean remove(String key) {
    	if(repo.isLocked(key)) {
    		repo.unlock(key);
    	}
        repo.lock(key);
        try {
        	return repo.remove(key) != null;
        } finally {
            repo.unlock(key);
        }
    }
    
    public void clear() {
        repo.clear();        
    }

    public String getRepositoryName() {
        return repositoryName;
    }
    
    public static TestCamelHazelCast getFileLocker() {
    	return fileLockerInstance;
    }
}
