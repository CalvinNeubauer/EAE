package de.kl.fh.moviestar;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class websearchList extends AppCompatActivity {

    String title;
    String actor;
    String genre;
    String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_websearch_list);

        Intent myIntent = getIntent();

        title = myIntent.getStringExtra("title");
        actor = myIntent.getStringExtra("actor");
        genre = myIntent.getStringExtra("genre");
        year = myIntent.getStringExtra("year");

        //Datenbank aufruf hier
    }
}
