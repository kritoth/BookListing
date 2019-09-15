package com.example.tians.booklisting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private static final String GOOGLE_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.field_search);

        searchButton = findViewById(R.id.button_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String query = buildRequestTerm();

                BookAsyncTask asyncTask = new BookAsyncTask();
                asyncTask.execute(query);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread
     */
    private class BookAsyncTask extends AsyncTask<String, Void, ArrayList<Book>>{

        @Override
        protected ArrayList<Book> doInBackground(String... urls) {
            // Create URL object
            URL url = QueryUtils.createUrl(urls[0]);

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // search for the users preference for magnitude
        String results = sharedPreferences.getString(
                getString(R.string.settings_results_key),
                getString(R.string.settings_results_min));

        Uri baseUri = Uri.parse(GOOGLE_REQUEST_URL);
        Uri.Builder builder = baseUri.buildUpon();

        builder.appendQueryParameter("q", searchTerm);
        builder.appendQueryParameter("maxResults", results);

        return builder.toString();
    }

    //Opens the {@link ResultActivitiy} and sends the queried ArrayList of {@link Book}s with it
    private void openResultsList(View view, ArrayList<Book> books) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putParcelableArrayListExtra("books", books);
        startActivity(intent);
    }
}
