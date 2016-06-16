package com.anypresence.gw;

import java.net.URI;
import java.net.URISyntaxException;

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
import java.util.HashMap;
import java.io.OutputStreamWriter;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.json.JSONArray;

import com.anypresence.gw.APGateway;
import com.anypresence.gw.CertPinningManager;
import com.anypresence.gw.Config;
import com.anypresence.gw.HTTPMethod;
import com.anypresence.gw.RequestContext;
import com.anypresence.gw.ResponseFromRequest;
import com.anypresence.gw.exceptions.RequestException;

public class Utilities {
    public static String updateUrl(String baseUrl, String urlToAppendOrReplace) {
        try {
            URI uri;
            uri = new URI(urlToAppendOrReplace);

            if (uri.isAbsolute()) {
                // This is an absolute url and so we just return it
                return urlToAppendOrReplace;
            } else {
                uri = new URI(baseUrl + "/" + urlToAppendOrReplace);
                uri = uri.normalize();
                return uri.toString();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String parseDomainFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String str = uri.getScheme() + "://" + uri.getHost();
            if (uri.getPort() != 80) {
                str += ":" + uri.getPort();
            }

            return str;
        } catch(java.net.URISyntaxException ex) {
            return "";
        }

    }

    public static JSONObject transformMapToJsonObject(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject(map);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof HashMap) {
                try {
                    JSONArray array = new JSONArray(entry.getValue());
                    jsonObject.put(entry.getKey(), array);
                } catch(org.json.JSONException ex) {
                    //
                }
            }
        }

        return jsonObject;
    }
}
