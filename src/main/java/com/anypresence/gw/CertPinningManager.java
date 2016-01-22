package com.anypresence.gw;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class CertPinningManager {

    private KeyStore keyStore;
    
    private static CertPinningManager instance;
    
    public static CertPinningManager getInstance() {
        if (instance == null) {
            instance = new CertPinningManager();
        }
        
        return instance;
    }
    
    public void setupCa(String alias, byte[] cert) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            InputStream caInput = new BufferedInputStream(new ByteArrayInputStream(cert));
            Certificate ca;
            
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca="
                        + ((X509Certificate) ca).getSubjectDN());           
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            
            keyStore.setCertificateEntry(alias, ca);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // Do nothing. No cert was found.
            e.printStackTrace();
        } catch (ClassCastException e) {
            // Not using an https url
        }
    }
    
    /**
     * Load CAs from an InputStream and pin them.
     */
    public HttpURLConnection useCertPinningSSLContext(HttpURLConnection connection) {

        try {
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(tmfAlgorithm);
            tmf.init(keyStore);
        
            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            // Tell the URLConnection to use a SocketFactory from our SSLContext
            ((HttpsURLConnection) connection).setSSLSocketFactory(context
                    .getSocketFactory());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            // Not using an https url
        }

        return connection;

    }
}
