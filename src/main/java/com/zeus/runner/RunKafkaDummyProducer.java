package com.zeus.runner;

import com.zeus.event.EventProducer;
import com.zeus.event.KafkaEventProducer;

public class RunKafkaDummyProducer {
	public static void main(String[] args) throws InterruptedException{
		EventProducer ep = KafkaEventProducer.getConnection("127.0.0.1", "2181");
		
		while(true){
			long unixTimestamp = System.currentTimeMillis() / 1000L;
			ep.send("event-counts", "{ \"name\" : \"Event Name\", \"timeInMs\" : \""+ unixTimestamp +"\" }");
			Thread.sleep(20);
		}
	}
}
