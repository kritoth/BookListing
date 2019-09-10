package com.example.tians.booklisting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.HashMap;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    // Store {@link Bitmap}s of {@link Book}s' thumbnail image parsed from the URL for each query
    public static final HashMap<String, Bitmap> bookCoverImgs = new HashMap<String, Bitmap>();

    /**
     * Returns new {@link URL} object from the given String url
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
     * @return String jsonResponse parsed from the url
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Log.d(LOG_TAG, "Server Response code: " + urlConnection.getResponseCode());

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
     * Extracts data of Books from the JSON response of the server and creates {@link Book} objects from them.
     * These objects are stored into a {@link List<Book>} and returned.
     * In addition the data for the coverimage of the Book is stored locally instead of into the Book object as a {@link Bitmap}.
     * This is to provide memory efficient sending of images between Activities.
     * @param jsonResponse containing the JSON
     * @return a List of Book objects are in the JSON response
     */
    public static List<Book> extractBooks(String jsonResponse) {
        List<Book> books = new ArrayList<Book>();
        // To ensure a new HashMap of images is created for each request not to let it grow too big
        if(bookCoverImgs != null) bookCoverImgs.clear();

        try {
            JSONObject response = new JSONObject(jsonResponse);
            JSONArray items = response.getJSONArray("items");
            for (int i=0; i<items.length(); i++){
                JSONObject volume = items.getJSONObject(i);
                JSONObject volumeInfo = volume.getJSONObject("volumeInfo");

                String id = volume.getString("id");

                String title = volumeInfo.getString("title");

                JSONArray authors = volumeInfo.getJSONArray("authors");
                String[] authorsToBook = new String[authors.length()];
                for(int j=0;j<authors.length();j++){
                    authorsToBook[j] = authors.getString(j);
                }

                String url = volumeInfo.getString("infoLink");

                // Parsing Bitmap from a URL resulting from a JSON parsing by establishing a network connection and store it to the field HashMap
                try {
                    extractImages(volumeInfo, id);
                }
                catch (IOException e) {
                    Log.e(LOG_TAG, "Error with inputstream for image: ", e);
                }

                books.add(new Book(id, title, authorsToBook, url));
            }
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, "Error with parsing JSON: ", e);
        }

        return books;
    }

    /**
     * Extract the thumbnail image as a {@link Bitmap} from the {@link URL} stored inside the given JSON object.
     * The extracted image then will be stored into a HashMap as a value for the key of the corresponding {@link Book}'s id
     * @param source The JSON object which stores the URL pointing to the thumbnail image
     * @param key The String id of the {@link Book} to which the image should correspond to
     * @throws IOException if networking is unsuccessful
     */
    private static void extractImages(JSONObject source, String key) throws IOException {

        Bitmap thumbnail = null;
        JSONObject images = null;
        try {
            images = source.getJSONObject("imageLinks");
            thumbnail = getBitmapFromUrl(images.getString("smallThumbnail"));
            //thumbnail = Bitmap.createScaledBitmap(thumbnail, 120, 120, false);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error with parsing JSON for image: ", e);
        }
        bookCoverImgs.put(key, thumbnail);
    }

    /**
     * Parsing {@link Bitmap} from url source provided as a String
     * @param source A url as String
     * @return a Bitmap
     * @throws IOException if connection doesn't work
     */
    private static Bitmap getBitmapFromUrl(String source) throws IOException {
        Bitmap bmp = null;

        URL url = createUrl(source);

        HttpURLConnection connection = null;
        InputStream iStream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            //Log.d(LOG_TAG, "Server Response code for image: " + connection.getResponseCode());

            iStream = connection.getInputStream();
            bmp = BitmapFactory.decodeStream(iStream);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error with opening http connection for image: ", e);
        }
        finally {
            if(connection != null) connection.disconnect();
            if(iStream != null) iStream.close();
        }

        return bmp;
    }

    /**
     * Provides the cover image of the {@link Book} of which id has been provided
     * @param key The id of the Book
     * @return the {@link Bitmap} thumbnail
     */
    public static Bitmap getBooksImage(String key){
        return bookCoverImgs.get(key);
    }
}
