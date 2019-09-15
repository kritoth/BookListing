package com.example.tians.booklisting;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        // Retrieve the ArrayList of {@link Book}s sent by the calling Activity
        Intent intent = getIntent();
        ArrayList<Book> books = intent.getParcelableArrayListExtra("books");

        // Create an {@link BookAdapter}, whose data source is a list of {@link Book}s. The
        // adapter knows how to create list items for each item in the list.
        final BookAdapter adapter = new BookAdapter(getApplicationContext(), 0, books);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // list_activity layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link BookAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Book} in the list.
        listView.setAdapter(adapter);

        // On each item listen for click and initiate intent for opening the item's url
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBookWeb(adapter.getItem(position).getUrl());
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
     * build the implicit {@link Intent} for any Action to show where the {@param url} is point to
     */
    private void openBookWeb (String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
