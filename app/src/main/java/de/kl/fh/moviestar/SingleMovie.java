package de.kl.fh.moviestar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SingleMovie extends AppCompatActivity {

    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        Intent myIntent = getIntent();
        ID = myIntent.getStringExtra("ID");
    }
}
