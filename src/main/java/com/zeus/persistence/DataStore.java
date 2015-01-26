package com.zeus.persistence;

import java.util.Date;

public interface DataStore {
	public int getDailyCount(String eventName, Date date);
	public int getHourlyCount(String eventName, Date date, Integer hour);
	public boolean storeRawEvent(String jsonData);
	public void increment(String eventName, long epochTimeStamp);
}
