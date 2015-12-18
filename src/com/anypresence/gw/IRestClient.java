package com.anypresence.gw;

import com.anypresence.gw.exceptions.RequestException;


public interface IRestClient {
	
	public void openConnection(String url, HTTPMethod method) throws RequestException;
	
	public String readResponse();
	
	public void post(String body);

}
