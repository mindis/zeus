package com.zeus.runner;

import java.util.Date;

import com.zeus.event.KafkaEventProducer;

public class RunKafkaDummyProducer {
	public static void main(String[] args) throws InterruptedException{
		KafkaEventProducer ep = KafkaEventProducer.getConnection("127.0.0.1", "2181");
		
		while(true){
			Date now = new Date();
			ep.send("event-counts", "{ \"name\" : \"Event Name\", \"timeInMs\" : \""+ now.getTime() +"\" }");
			Thread.sleep(20);
		}
	}
}
