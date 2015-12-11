package com.anypresence.gw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 *
 */
public class APGateway {
	private String url;
	private HTTPMethod method;
	
	private HttpURLConnection connection;
	private IRestClient restClient;
	
	private IParser jsonParser = new JSONParser();
	
	private String body;
	
	private APGateway() {
	}
	
	public String getUrl() {
		return url;
	}

	protected void setUrl(String url) {
		this.url = url;
	}

	public HTTPMethod getMethod() {
		return method;
	}

	public void setMethod(HTTPMethod method) {
		this.method = method;
	}
	
	// Execute the request
	public void execute() {	
		getRestClient().openConnection(url, method);
		
		switch (method) {
		case POST:
			getRestClient().post(getBody());
		}
	}
	
	public void post() {
		execute();
		getRestClient().post(body);
	}
	
	public void get() {
		execute();
	}
	
	public <T extends APObject> T readResponseQuery(T obj) {
		String response = readResponse();
		
		Map<String, String> data = jsonParser.parseMap(response);
		
		if (data != null) {
			for (Entry<String, String> entry : data.entrySet()) {
				obj.set(entry.getKey(), entry.getValue().toString());
			}
		}
		
		return obj;	
	}
	
	/**
	 * Reads the response.
	 * 
	 * @return the response
	 */
	public String readResponse() {
		return getRestClient().readResponse();		
	}

	public IRestClient getRestClient() {
		if (restClient == null) {
			restClient = new DefaultRestClient();
		}
		
		return restClient;
	}

	protected void setRestClient(IRestClient restClient) {
		this.restClient = restClient;
	}
	

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}


	/**
	 * Builder for APGateway
	 *
	 */
	public static class Builder {
		String url;
		HTTPMethod method;
		
		public Builder() {}
		
		public Builder url(String url) {
			this.url = url;
			return this;
		}
		
		public Builder method(HTTPMethod method) {
			this.method = method;
			return this;
		}
		
		public APGateway build() {
			APGateway gw = new APGateway();
			gw.setUrl(url);
			gw.setMethod(method);
			
			return gw;
		}
	}

}
