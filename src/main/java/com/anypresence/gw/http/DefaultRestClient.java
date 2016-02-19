package com.anypresence.gw.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ResponseCache;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.OutputStreamWriter;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;

import com.anypresence.gw.APGateway;
import com.anypresence.gw.CertPinningManager;
import com.anypresence.gw.Config;
import com.anypresence.gw.HTTPMethod;
import com.anypresence.gw.RequestContext;
import com.anypresence.gw.ResponseFromRequest;
import com.anypresence.gw.exceptions.RequestException;

/**
 * A default rest client
 *
 */
public class DefaultRestClient implements IRestClient {

    private HttpURLConnection connection;
    private int readTimeout = 15 * 1000;
    private boolean shouldUseCertPinning = false;
    
    public DefaultRestClient() {
    }
    
    public DefaultRestClient useCertPinning(boolean shouldUseCertPinning) {
        this.shouldUseCertPinning = shouldUseCertPinning;
        return this;
    }

    private void openConnection(RequestContext<?> request)
            throws RequestException {
        String url = request.getUrl();
        HTTPMethod method = request.getMethod(); 
        URL urlConnection;
        try {
            Config.getLogger().log("URL connecting to: " + url);
            urlConnection = new URL(url);
            connection = (HttpURLConnection) urlConnection.openConnection();
            
            // Cache the result?
            if (Config.getCacheManager() != null) {
                if (request.getGateway().getUseCaching()) {
                    connection.setUseCaches(true);
                } else {
                    connection.setUseCaches(false);
                }                
            }
            
            if (urlConnection.getProtocol().toLowerCase().equals("https")) {
                // Use cert pinning?
                 if (shouldUseCertPinning) {
                     connection = APGateway.getCertPinningManager().useCertPinningSSLContext(connection);
                 }
            }
            
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
            e.printStackTrace();
            throw new RequestException(e);
        }
    }
    
    private void get(RequestContext<?> request) throws RequestException {
        openConnection(request);
    }
    
    private void post(RequestContext<?> request) throws RequestException {
        openConnection(request);

        if (request.getPostParam() != null) {
            JSONObject jsonObject = new JSONObject(request.getPostParam());
            OutputStreamWriter osw;
            try {
                osw = new OutputStreamWriter(connection.getOutputStream());
                System.out.println("@@@@@ body is: " + jsonObject.toString());
                osw.write(jsonObject.toString());
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ResponseFromRequest readResponse() {
        BufferedReader reader = null;
        List<String> lines = new ArrayList<String>();
        String result = "";       
        String url = ""; // URL used for the last request
        
        try {
            reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line = null;

            url = connection.getURL().toString();
            
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                result += line;
            }

            for (String s : lines) {
                Config.getLogger().log(" " + s);
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
        
        try {
            return new ResponseFromRequest(connection.getResponseCode(), result);
        } catch (IOException e) {
            return new ResponseFromRequest(-1, result);
        }
    }

    public void executeRequest(RequestContext<?> request) throws RequestException {
       if (request.getMethod() == HTTPMethod.POST) {
           post(request);
       } else {
           get(request);
       }
        
    }

}
