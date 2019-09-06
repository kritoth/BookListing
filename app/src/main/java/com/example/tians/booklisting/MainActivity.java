package com.example.tians.booklisting;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String LOG_TAG = MainActivity.class.getName();

    // for testing purposes
    private String GOOGLE_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchField = findViewById(R.id.field_search);
        //TODO: Get the query parameters to use for creating URL request

        Button searchButton = findViewById(R.id.button_search);
        //TODO: create intent for new ListView Activity
        //TODO: Start fetching

        BookAsyncTask asyncTask = new BookAsyncTask();
        asyncTask.execute();
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread
     */
    private class BookAsyncTask extends AsyncTask<URL, Void, ArrayList<Book>>{

        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {
            // Create URL object
            URL url = QueryUtils.createUrl(GOOGLE_REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
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

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            super.onPostExecute(books);
        }
    }
}
