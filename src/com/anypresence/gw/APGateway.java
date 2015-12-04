package com.anypresence.gw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 *
 */
public class APGateway {
	private String url;
	private HTTPMethod method;
	
	private HttpURLConnection connection;
	private IRestClient restClient;
	
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
		if (getRestClient() == null) {
			URL urlConnection;
			try {
				urlConnection = new URL(url);
						
				connection = (HttpURLConnection) urlConnection.openConnection();
				connection.setRequestMethod(method.name());		
				
				connection.setReadTimeout(15*1000);
				connection.connect();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			getRestClient().openConnection();
		}
		
	}
	
	/**
	 * Reads the response.
	 * 
	 * @return the response
	 */
	public String readResponse() {
		try {
			new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public IRestClient getRestClient() {
		return restClient;
	}

	protected void setRestClient(IRestClient restClient) {
		this.restClient = restClient;
	}

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
