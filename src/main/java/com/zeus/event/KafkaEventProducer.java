package com.zeus.event;

import java.util.Properties;

import org.slf4j.LoggerFactory;

import com.zeus.controller.BaseController;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;


public class KafkaEventProducer implements EventProducer{
	private static KafkaEventProducer kafkaProducer = null;
	private static Producer<String, String> internalProducer; 
	private KafkaEventProducer(){}
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	public static EventProducer getConnection(String host, String port) {
		synchronized (KafkaEventProducer.class) {
			if(kafkaProducer == null){
				Properties props = new Properties();
				props.put("zk.connect", host + ":" + port);
				
				props.put("serializer.class", "kafka.serializer.StringEncoder");
				props.put("metadata.broker.list", "127.0.0.1:9092,127.0.0.1:9092");
				props.put("partitioner.class", "com.zeus.event.SimplePartitioner");
				ProducerConfig config = new ProducerConfig(props);
				internalProducer = new Producer<String, String>(config);
				kafkaProducer = new KafkaEventProducer();
			}
		}

		return kafkaProducer;
	}
	
	public boolean send(String topicName, String data) {
		boolean success = false;
		KeyedMessage<String, String> message = new KeyedMessage<String, String>(topicName, data);
		try{
			internalProducer.send(message);
			success = true;
		} catch(Exception e){
			success = false;
			logger.error("Error while sending kafka message: " + e.getStackTrace());
		}
		
		return success;
	}

	public void disconnect() {
		internalProducer.close();
	}

}
