package com.anypresence.gw;


public interface IRestClient {
	
	public void openConnection(String url, HTTPMethod method);
	
	public String readResponse();
	
	public void post(String body);

}
