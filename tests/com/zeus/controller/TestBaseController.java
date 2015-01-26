package com.zeus.controller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class TestBaseController {

	@Test
	public void test() {
	    HttpClient httpClient = new DefaultHttpClient();

	    try {
	        HttpPost request = new HttpPost("http://127.0.0.1:8080/zeus/ingest/");
	        StringEntity params =new StringEntity("{ \"name\" : \"Event Name\", \"timeInMs\" : \"1422179845\" }");
	        request.addHeader("content-type", "application/javascript");
	        request.setEntity(params);
	        HttpResponse response = httpClient.execute(request);
	        System.out.println(response.getAllHeaders());
	        
	    }catch (Exception ex) {
	        System.out.println(ex.toString());
	    }
	}

}
