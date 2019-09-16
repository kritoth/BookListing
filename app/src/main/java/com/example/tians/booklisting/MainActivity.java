package com.example.tians.booklisting;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
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
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

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
                getLoaderManager().initLoader(0, null, MainActivity.this).forceLoad();
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
     * Build the request Url
     * The query parameters are built up partly from {@link SharedPreferences} chosen by the user
     * and partly from the searched term typed in by the user
     */
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

    /**
     * Creates the {@link BookLoader} and sends the query parameters as String to it
     * The query parameters are built up by the {@link MainActivity#buildRequestTerm()}
     *
     * @param id
     * @param args
     * @return {@link BookLoader(android.content.Context, String)}
     */
    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, buildRequestTerm());
    }

    /**
     * It is called by the {@link BookLoader#loadInBackground()} to update the UI with the fetched data
     * @param loader
     * @param data is the list of {@link Book}s
     */
    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        if (data == null || data.size()==0) {
            return;
        }
        openResultsList(searchButton, (ArrayList<Book>) data);
    }

    /**
     * Resets the loader into a new empty {@link BookAdapter}
     * @param loader is to be reseted
     */
    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        new BookAdapter(this, 0, new ArrayList<Book>());
    }

    /**
     * Opens the {@link ResultActivity} and sends the queried ArrayList of {@link Book}s with it
     */
    private void openResultsList(View view, ArrayList<Book> books) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putParcelableArrayListExtra("books", books);
        startActivity(intent);
    }
}
