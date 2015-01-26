package com.zeus.event;

public interface EventProducer {
	public boolean send(String topicName, String data);
	public void disconnect();
}
