package org.rdap.port43.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author jiashuo
 * 
 */
public final class RestClient {
    private static RestClient restClient = new RestClient();

    public static RestClient getInstance() {
        return restClient;
    }

    /**
     * 
     * @param url
     * @return
     */
    public static String execute(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/rdap+json");
        HttpResponse response = null;
        String result = null;
        try {
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inStream = entity.getContent();
                result = convertStreamToString(inStream);
                inStream.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * convert the inputStream to string.
     * 
     * @param inStream
     *            input stream.
     * @return string.
     */
    private static String convertStreamToString(InputStream inStream) {

        BufferedReader reader = null;
        try {
            reader =
                    new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        StringBuilder strBuilder = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strBuilder.toString();
    }

}