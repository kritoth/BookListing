package com.example.tians.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    public static final String LOG_TAG = BookLoader.class.getName();

    // to be used for http request
    private String mUrl;

    /**
     * To be called from the {@link MainActivity#onCreateLoader(int, Bundle)}}
     * @param context to save the activity
     * @param url the String representing the url to be used for http request
     */
    public BookLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public List<Book> loadInBackground() {

        // Create URL object
        URL url = QueryUtils.createUrl(mUrl);

        // Perform HTTP request with the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = QueryUtils.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with Http request: ", e);
        }
        //Log.d(LOG_TAG, "JSON Response is: \n" + jsonResponse + "\n");

        ArrayList<Book> books = (ArrayList<Book>) QueryUtils.extractBooks(jsonResponse);

        //Log.d(LOG_TAG, "Books queried: " + books.size() + "\nParsed data in 1st Book is: " + books.get(0).toString());

        return books;
    }
}
