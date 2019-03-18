package com.HazelcastTest;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import info.jerrinot.subzero.SubZero;

public class HazelcastInstanceHelper {

	private static HazelcastInstanceHelper instance = null;

	private HazelcastInstance hz;

	private HazelcastInstanceHelper() {
		Config config = new Config();
		SubZero.useAsGlobalSerializer(config);
		hz = Hazelcast.newHazelcastInstance(config);
	}

	public static HazelcastInstance getHazelcastInstance() {
		if (instance == null) {
			instance = new HazelcastInstanceHelper();
		}
		return instance.hz;
	}
	
}
