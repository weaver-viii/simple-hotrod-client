package org.everythingjboss.jdg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

public class SimpleHotRodClient {

	public static void main(String[] args) throws InterruptedException {

		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder
			.addServers(System.getProperty("serverEndpoint"))
			.connectionPool().maxTotal(1);
		RemoteCacheManager rcm = new RemoteCacheManager(builder.build());
		RemoteCache<String,DummyClass> cache = rcm.getCache(System.getProperty("cacheName"));
		SimpleHotRodClient shrc = new SimpleHotRodClient();
		shrc.writeEntries(cache);
		shrc.readEntries(cache);
		rcm.stop();
	}

	public void writeEntries(RemoteCache<String,DummyClass> rc) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(99);
		for(int i=0;i<100;i++) {
			executor.execute(new JDGPutThread(rc, Integer.toString(i), new DummyClass()));
		}
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);
	}
	
	public void readEntries(RemoteCache<String,DummyClass> rc) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(99);
		for(int i=0;i<100;i++) {
			executor.execute(new JDGGetThread(rc, Integer.toString(i)));
		}
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);
		
	}
}