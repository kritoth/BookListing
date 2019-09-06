package com.example.tians.booklisting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        // Create an {@link BookAdapter}, whose data source is a list of {@link Book}s. The
        // adapter knows how to create list items for each item in the list.
        //final BookAdapter adapter = new BookAdapter(this, books);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // list_activity layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link BookAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Book} in the list.
        //listView.setAdapter(adapter);
    }
}
