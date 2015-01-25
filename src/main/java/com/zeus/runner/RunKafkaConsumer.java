package com.zeus.runner;

import java.io.UnsupportedEncodingException;

import com.zeus.event.KafkaEventConsumer;

public class RunKafkaConsumer {
	public static void main(String[] argv) throws UnsupportedEncodingException {
		KafkaEventConsumer helloKafkaConsumer = new KafkaEventConsumer();
		helloKafkaConsumer.run();
	}
}
