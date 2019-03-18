package com.HazelcastTest;

import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.config.MaxSizeConfig.MaxSizePolicy;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.listener.EntryEvictedListener;

public class LRUCache {
	
	public LRUCache () {
		
	}

    private IMap<Object, Items> map;
	
	public void getCache(int size, String name) {
		MapConfig mapcfg = new MapConfig(name);
		//mapcfg.setAsyncBackupCount(1);
		mapcfg.setBackupCount(1);
		mapcfg.setReadBackupData(true);
		EvictionPolicy ep = EvictionPolicy.LRU;
		mapcfg.setEvictionPolicy(ep).getMaxSizeConfig().setMaxSizePolicy(MaxSizePolicy.PER_NODE).setSize(size);
		mapcfg.setTimeToLiveSeconds(0);
		HazelcastInstance hz = HazelcastInstanceHelper.getHazelcastInstance();
		int nearcachesize = (int) (0.05 * size); 
		EvictionConfig evictionConfig = new EvictionConfig()
				  .setEvictionPolicy(EvictionPolicy.LRU)
				  .setSize(nearcachesize);
		NearCacheConfig nearCacheConfig = new NearCacheConfig()
				  .setName(name)
				  .setInMemoryFormat(InMemoryFormat.OBJECT)
				  .setInvalidateOnChange(false)
				  .setTimeToLiveSeconds(60)
				  .setEvictionConfig(evictionConfig);
		nearCacheConfig.setCacheLocalEntries(false);
		mapcfg.setNearCacheConfig(nearCacheConfig);
		
		hz.getConfig().setProperty("hazelcast.heartbeat.failuredetector.type", "phi-accrual");
		hz.getConfig().setProperty("hazelcast.heartbeat.interval.seconds", "5");
		hz.getConfig().setProperty("hazelcast.max.no.heartbeat.seconds", "1200");
		hz.getConfig().setProperty("hazelcast.heartbeat.phiaccrual.failuredetector.threshold", "10");
		hz.getConfig().setProperty("hazelcast.heartbeat.phiaccrual.failuredetector.sample.size", "200");
		hz.getConfig().setProperty("hazelcast.heartbeat.phiaccrual.failuredetector.min.std.dev.millis", "100");
		hz.getConfig().addMapConfig(mapcfg);
		map = hz.getMap(name);
		map.addEntryListener(new EntryEvictedListener<Integer, String>() {
			public void entryEvicted(EntryEvent<Integer, String> event) {
				Object target = event.getOldValue();
			}
		}, true);
	}

	public void insertitems() {
		for(int i=0; i<100000; i++) {
			String name = "item"+Integer.toString(i+1);
			Object key = (Object)name;
			Items item = new Items();
			item.setId(i);
			item.setCategory("Mobile");
			item.setCompany("Apple");
			item.setCount(1000);
			item.setDescription("It is a newer version");
			item.setModel("X");
			item.setName(key.toString());
			item.setPrice(59999);
			item.setShipping_time(4);
			map.put(key, item);
			System.out.println("Value inserted with key "+name);
		}
	}

	public void getitems() {
		long start = System.currentTimeMillis();
		for(int i=0; i<100000; i++) {
			Object key = "item"+Integer.toString(i+1);
			Items value = map.get(key);
		}
		long end = System.currentTimeMillis();
		
		System.out.println(end-start);
	}
	
}
