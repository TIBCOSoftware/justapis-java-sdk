package com.anypresence.gw;

import com.anypresence.gw.exceptions.RequestException;

public interface IRestClient {

	public String readResponse();

	public void post(String url, String body) throws RequestException;
	
	public void get(String url) throws RequestException;

}
