package com.zeus.persistence;

import java.net.UnknownHostException;
import java.util.Date;

import org.junit.Test;

public class MongoDataStoreTest {
	String mongoHost = "localhost";
	int mongoPort = 27017;
	
	@Test
	public void testIncrementHourlyCounter() {
		DataStore store = null;
		try {
			store = MongoDataStore.getInstance(mongoHost, mongoPort);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		store.increment("Test event", 1422179845);
	}
	
	@Test
	public void testGetData(){
		DataStore store = null;
		try {
			store = MongoDataStore.getInstance(mongoHost, mongoPort);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		Date now = new Date();
		store.increment("Test event", now.getTime());
		int count = store.getHourlyCount("Test event", now, 9);
		System.out.println(count);
	}
}
