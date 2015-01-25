package com.zeus.controller;

import java.io.Serializable;

public class ZeusEventRequest implements Serializable {
	private static final long serialVersionUID = -8650616886390531568L;
	private String name;
	private String timeInMs;

	public ZeusEventRequest(){
	}
	
	public ZeusEventRequest(String name,String timeInMs){
		super();
		this.name = name;
		this.timeInMs = timeInMs;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimeInMs() {
		return timeInMs;
	}

	public void setTimeInMs(String timeInMs) {
		this.timeInMs = timeInMs;
	}

	@Override
	public String toString() {
		return "Event [name=" + name + ", timeInMs=" + timeInMs + "]";
	}

}
