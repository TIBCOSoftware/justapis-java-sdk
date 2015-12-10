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
	
	public <T extends APObject> T readResponseQuery(T obj) {
		String response = readResponse();
		
		Map<String, String> data = JSONParser.parseMap(response);
		
		for (Entry<String, String> entry : data.entrySet()) {
			obj.set(entry.getKey(), entry.getValue().toString());
		}
		
		return obj;	
	}
	
	/**
	 * Reads the response.
	 * 
	 * @return the response
	 */
	public String readResponse() {
		BufferedReader reader = null;
		List<String> lines = new ArrayList<String>();
		try {
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;

            while((line = reader.readLine()) != null) {
                lines.add(line);
            }
            

        	System.out.println("@@@ Response is: ");
        	for (String s : lines) {
        		System.out.println(" " + s);
        	}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
	            try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        }
		if (lines.isEmpty()) {
			return "";
		} else {
			return lines.get(0);	
		}
		
	}

	public IRestClient getRestClient() {
		return restClient;
	}

	protected void setRestClient(IRestClient restClient) {
		this.restClient = restClient;
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
