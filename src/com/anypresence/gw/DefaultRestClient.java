package com.anypresence.gw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.OutputStreamWriter;

public class DefaultRestClient implements IRestClient {

	private HttpURLConnection connection;
	private int readTimeout = 15 * 1000;

	public void openConnection(String url, HTTPMethod method) {
		URL urlConnection;
		try {
			urlConnection = new URL(url);
			connection = (HttpURLConnection) urlConnection.openConnection();
			connection.setRequestMethod(method.name());
			if (method == HTTPMethod.POST) {
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type",
						"application/json");
				connection.setRequestProperty("Accept", "application/json");
			}
			connection.setReadTimeout(readTimeout);
			connection.connect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	public void post(String body) {
		if (body != null) {
			OutputStreamWriter osw;
			try {
				osw = new OutputStreamWriter(connection.getOutputStream());
				osw.write(body);
				osw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public String readResponse() {
		BufferedReader reader = null;
		List<String> lines = new ArrayList<String>();
		try {
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line = null;

			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}

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

}
