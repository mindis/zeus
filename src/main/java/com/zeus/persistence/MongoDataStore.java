package com.zeus.persistence;

import java.net.UnknownHostException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

public class MongoDataStore {
	private static final String MONGO_HOST = "localhost";
	private static final int MONGO_PORT = 27017;
	private static MongoDataStore mongoDataStore;
	private static DBCollection dailyMetricsColl;
	private static DBCollection rawEventsColl;

	private MongoDataStore() {
	};

	public static MongoDataStore getInstance() throws UnknownHostException {
		synchronized (MongoDataStore.class) {
			if(mongoDataStore == null){
				MongoClient mongoClient = new MongoClient(MONGO_HOST, MONGO_PORT);
				DB db = mongoClient.getDB("zeus");
				dailyMetricsColl = db.getCollection("daily_metrics");
				rawEventsColl = db.getCollection("raw_events");
				mongoDataStore = new MongoDataStore();
			}
		}

		return mongoDataStore;
	}

	public int getDailyCount(String eventName, Date date){		
		DateTime utcDate = new DateTime(date, DateTimeZone.UTC);
		DateTime dateBucket = utcDate.withTimeAtStartOfDay();
		
		BasicDBObject allQuery = new BasicDBObject();
		allQuery.put("event_name", eventName);
		allQuery.put("date", dateBucket.toDate());
		
		DBObject data = dailyMetricsColl.findOne(allQuery);
		int count = 0;
		if(data != null){
			count = (int) data.get("daily");
		}
		
		return count;
	}
	
	public WriteResult storeRawEvent(String jsonData){
		DBObject rawEvent = (DBObject) JSON.parse(jsonData);
		return rawEventsColl.insert(rawEvent);
	}
	
	public int getHourlyCount(String eventName, Date date, Integer hour){
		if(hour < 0 && hour > 23){
			throw new IllegalArgumentException();
		}
		
		DateTime utcDate = new DateTime(date, DateTimeZone.UTC);
		DateTime dateBucket = utcDate.withTimeAtStartOfDay();
		
		BasicDBObject allQuery = new BasicDBObject();
		allQuery.put("event_name", eventName);
		allQuery.put("date", dateBucket.toDate());
		
		DBObject data = dailyMetricsColl.findOne(allQuery);
		int count = 0;
		if(data != null){
			DBObject hourlyData = (BasicDBObject) data.get("hourly");
			if(hourlyData != null && hourlyData.get(String.valueOf(hour)) != null){
				count = (int)hourlyData.get(String.valueOf(hour));
			}
		}
		
		return count;
	}
	
	public void increment(String eventName, long epochTimeStamp) {
		DateTime date = new DateTime(new Long(epochTimeStamp * 1000), DateTimeZone.UTC);
		int hr = date.getHourOfDay();
		DateTime dateBucket = date.withTimeAtStartOfDay();
		
		BasicDBObject allQuery = new BasicDBObject();
		allQuery.put("date", dateBucket.toDate());
		allQuery.put("event_name", eventName);
		
		DBObject data = dailyMetricsColl.findOne(allQuery);
		
		if (data != null) {
			DBObject hourlyQuery = new BasicDBObject("$inc", new BasicDBObject("hourly." + hr, 1));
			dailyMetricsColl.update(data, hourlyQuery);
			
			data = dailyMetricsColl.findOne(allQuery);
			DBObject dailyQuery = new BasicDBObject("$inc", new BasicDBObject("daily", 1));
			dailyMetricsColl.update(data, dailyQuery);
			
		} else {
			BasicDBObjectBuilder daily = BasicDBObjectBuilder.start().add("date", dateBucket.toDate());
			
			BasicDBObjectBuilder hourSubArray = BasicDBObjectBuilder.start();
			for(int i=0; i <=23; i++){
				if(i == hr){
					hourSubArray.add(String.valueOf(i), 1);
				} else {
					hourSubArray.add(String.valueOf(i), 0);
				}
			}

			daily.add("hourly", hourSubArray.get());
			daily.add("daily", 1);
			daily.add("event_name", eventName);
			
			dailyMetricsColl.insert(daily.get());
		}
	}
}
