package com.example.tians.booklisting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchField = findViewById(R.id.field_search);
        //TODO: Get the query parameters to use for creating URL request

        Button searchButton = findViewById(R.id.button_search);
        //TODO: create intent for new ListView Activity
        //TODO: Start fetching
    }
}
