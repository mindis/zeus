package com.zeus.event;

import java.util.Date;

import org.junit.Test;

public class KafkaEventProducerTest {

	@Test
	public void test() throws InterruptedException {
		EventProducer ep = KafkaEventProducer.getConnection("127.0.0.1", "2181");
		
		while(true){
			Date now = new Date();
			ep.send("event-counts", "{ \"name\" : \"Event Name\", \"timeInMs\" : \""+ now.getTime() +"\" }");
			Thread.sleep(20);
		}
	}

}