package com.zafranitechllcpc.flickrfinder.network;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.zafranitechllcpc.flickrfinder.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FlickrRequester extends AsyncTask<String, Void, String> {
    private final String TAG = getClass().getSimpleName();
    private String baseUrl;


    private ResponseHandler responseHandler;

    public FlickrRequester(Activity activity, ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        this.baseUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key="
                + activity.getResources().getString(R.string.flickr_api_key)
                + "&content_type=1&format=json&per_page=25&extras=url_n,url_l&safe_search=1&media=photos";
    }

    public void searchByText(String searchStr, int page) {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder
                .append("&text=")
                .append(searchStr)
                .append("&page=")
                .append(page);
        this.execute(urlBuilder.toString());
    }

    public interface ResponseHandler{
        void completion(JSONObject response);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            responseHandler.completion(new JSONObject(s));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return downloadUrl(new URL(urls[0]));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public String downloadUrl(URL url) throws IOException {
        Log.e(TAG, "Downloading from url, "+ url.toString());

        InputStream inputStream = null;
        HttpsURLConnection connection = null;
        String result = null;

        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(4000);
            connection.setConnectTimeout(4000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code:" + responseCode);
            }
            inputStream = connection.getInputStream();
            if (inputStream != null) {
                result = readStream(inputStream);
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) inputStream.close();
            if (connection !=null) connection.disconnect();
        }
        Log.e(TAG, "url result: " + result);
        return result;
    }

    private String readStream(InputStream stream) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(stream,"UTF-8");
        String newLine = System.getProperty("line.separator");
        BufferedReader reader = new BufferedReader(streamReader);
        StringBuilder result = new StringBuilder();
        String line;
        boolean flag = false;
        while ((line = reader.readLine()) != null) {
            result.append(flag? newLine : "").append(line);
            flag = true;
        }
        return result.substring(result.indexOf("{"), result.length()-1);
    }

}
