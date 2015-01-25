package com.zeus.persistence;

import java.net.UnknownHostException;
import java.util.Date;

import org.junit.Test;

import com.zeus.persistence.MongoDataStore;

public class MongoDataStoreTest {

	@Test
	public void testIncrementHourlyCounter() {
		MongoDataStore store = null;
		try {
			store = MongoDataStore.getInstance();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		store.increment("Test event", 1422179845);
	}
	
	@Test
	public void testGetData(){
		MongoDataStore store = null;
		try {
			store = MongoDataStore.getInstance();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		Date now = new Date();
		store.increment("Test event", now.getTime());
		int count = store.getHourlyCount("Test event", now, 9);
		System.out.println(count);
	}
}
