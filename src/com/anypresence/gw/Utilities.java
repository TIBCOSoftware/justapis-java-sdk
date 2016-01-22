package com.anypresence.gw;

import java.net.URI;
import java.net.URISyntaxException;

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
}
