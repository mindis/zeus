package com.zeus.event;

import java.util.Date;

import org.junit.Test;

import com.zeus.event.KafkaEventProducer;

public class KafkaEventProducerTest {

	@Test
	public void test() throws InterruptedException {
		KafkaEventProducer ep = KafkaEventProducer.getConnection("127.0.0.1", "2181");
		
		while(true){
			Date now = new Date();
			ep.send("event-counts", "{ \"name\" : \"Event Name\", \"timeInMs\" : \""+ now.getTime() +"\" }");
			Thread.sleep(20);
		}
	}

}