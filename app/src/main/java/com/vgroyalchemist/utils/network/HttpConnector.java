package com.vgroyalchemist.utils.network;


import com.vgroyalchemist.utils.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;


public class HttpConnector {

    public static JSONObject getJSONObject(String serviceUrl, JSONObject requestObject) throws Exception {
        InputStreamReader inStream = null;
        BufferedReader reader = null;
        JSONObject jsonObject = null;
        long startTime = System.currentTimeMillis();
        long seconds;

        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        HttpResponse response;

        try {
            HttpPost post = new HttpPost(serviceUrl);

            StringEntity se = new StringEntity(requestObject.toString(), "UTF-8");
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                String result = EntityUtils.toString(response.getEntity());
                jsonObject = new JSONObject(result);
                long elapsedTime = System.currentTimeMillis() - startTime;
                seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
                Utility.debugger("Android time in seconds>>>" + seconds);
            }
            return jsonObject;
        } catch (MalformedURLException mue) {
            Utility.debugger("Android getJSONObject2 MalformedURLException message..." + mue.toString());
            throw mue;
        } catch (SocketTimeoutException ste) {
            Utility.debugger("Android getJSONObject2 SocketTimeoutException message..." + ste.getMessage());
            Utility.debugger("Android getJSONObject2 SocketTimeoutException string..." + ste.toString());
            throw ste;
        } catch (IOException ioe) {
            Utility.debugger("Android getJSONObject2 IOException message..." + ioe.toString());
            throw ioe;
        } catch (JSONException je) {
            Utility.debugger("Android getJSONObject2 JSONException message..." + je.toString());
            throw je;
        } catch (Exception e) {
            Utility.debugger("Android getJSONObject2 Exception message..." + e.toString());
            throw e;
        } finally {
            if (inStream != null)
                inStream.close();
            if (reader != null)
                reader.close();
        }
    }
}