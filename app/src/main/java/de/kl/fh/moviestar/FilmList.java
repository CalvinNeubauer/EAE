package de.kl.fh.moviestar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FilmList extends AppCompatActivity {

    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        //Get intent extra
        Intent myIntent = getIntent();
        type = myIntent.getStringExtra("type");
    }
}
