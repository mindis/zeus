package com.zeus.runner;

import java.io.UnsupportedEncodingException;

import com.zeus.event.EventConsumer;
import com.zeus.event.KafkaEventConsumer;

/**
 * This can be a multi-threaded process which will enable a much faster ingestion from kafka.
 * For simplicty sake, its single threaded for now.
 * @author Utkarsh
 *
 */
public class RunKafkaConsumer {
	public static void main(String[] argv) throws UnsupportedEncodingException {
		EventConsumer consumer = new KafkaEventConsumer();
		consumer.run();
	}
}
