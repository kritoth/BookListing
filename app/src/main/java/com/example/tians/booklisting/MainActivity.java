package com.example.tians.booklisting;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String LOG_TAG = MainActivity.class.getName();

    private EditText searchField;
    private TextView searchButton;
    private String searchTerm;

    // for testing purposes
    private String googleRequestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.field_search);

        searchButton = findViewById(R.id.button_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buildRequestTerm();

                BookAsyncTask asyncTask = new BookAsyncTask();
                asyncTask.execute();
            }
        });
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread
     */
    private class BookAsyncTask extends AsyncTask<URL, Void, ArrayList<Book>>{

        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {
            // Create URL object
            URL url = QueryUtils.createUrl(googleRequestUrl);

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
            if (books == null || books.size()==0) {
                return;
            }
            openResultsList(searchButton, books);
        }
    }

    //Build the request Url
    private String buildRequestTerm(){
        searchTerm = searchField.getText().toString().trim();
        googleRequestUrl = "https://www.googleapis.com/books/v1/volumes?q=" + searchTerm + "&maxResults=3";
        return googleRequestUrl;
    }

    //Opens the {@link ResultActivitiy} and sends the queried ArrayList of {@link Book}s with it
    private void openResultsList(View view, ArrayList<Book> books) {
        Intent intent = new Intent(this, ResultActivity.class);
        Bundle queriedBooks = new Bundle();
        queriedBooks.putParcelableArrayList("books", books);
        intent.putExtras(queriedBooks);
        startActivity(intent);
    }
}
