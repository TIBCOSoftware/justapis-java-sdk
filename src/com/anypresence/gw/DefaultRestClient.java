package com.anypresence.gw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.io.OutputStreamWriter;

import com.anypresence.gw.exceptions.RequestException;

public class DefaultRestClient implements IRestClient {

    private HttpURLConnection connection;
    private int readTimeout = 15 * 1000;

    private void openConnection(String url, HTTPMethod method)
            throws RequestException {
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
            throw new RequestException(e);
        } catch (IOException e) {
            throw new RequestException(e);
        }
    }

    public void post(String url, String body) throws RequestException {
        openConnection(url, HTTPMethod.POST);

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
        String result = "";
        try {
            reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line = null;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
                result += line;
            }

            for (String s : lines) {
                Setup.getLogger().log(" " + s);
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

        return result;
    }

    public void get(String url) throws RequestException {
        openConnection(url, HTTPMethod.GET);
    }

}
