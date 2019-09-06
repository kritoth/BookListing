package com.example.tians.booklisting;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static String LOG_TAG = QueryUtils.class.getName();

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        }
        catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating Url: ", e);
        }
        return url;
    }

    /**
     * Creates a String json from the response from the url
     * @param url where the request is to be sent
     * @return jsonResponse received from the url
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            Log.d(LOG_TAG, "Server Response code: " + urlConnection.getResponseCode());

            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error with opening http connection: ", e);
        }
        finally {
            if(urlConnection != null) urlConnection.disconnect();
            if(inputStream != null) inputStream.close();
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder string = new StringBuilder();

        if(inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                string.append(line);
                line = bufferedReader.readLine();
            }
        }
        return string.toString();
    }

    /**
     * Extracts a List of Books from the JSON response of the server
     * @param jsonResponse containing the JSON
     * @return a List of Book objects are in the JSON response
     */
    public static List<Book> extractBooks(String jsonResponse) {
        List<Book> books = new ArrayList<Book>();

        try {
            JSONObject response = new JSONObject(jsonResponse);
            JSONArray items = response.getJSONArray("items");
            for (int i=0; i<items.length(); i++){
                JSONObject volume = items.getJSONObject(i);
                JSONObject volumeInfo = volume.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");

                JSONArray authors = volumeInfo.getJSONArray("authors");
                String[] authorsToBook = new String[authors.length()];
                for(int j=0;j<authors.length();j++){
                    authorsToBook[j] = authors.getString(j);
                }

                String url = volumeInfo.getString("infoLink");

                books.add(new Book(title,authorsToBook,url));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error with parsing JSON: ", e);
        }

        return books;
    }
}
