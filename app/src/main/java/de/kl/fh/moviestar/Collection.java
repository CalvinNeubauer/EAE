package de.kl.fh.moviestar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Collection extends AppCompatActivity implements View.OnClickListener {

    private Button movies;
    private Button series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        movies = (Button) findViewById(R.id.movies);
        series = (Button) findViewById(R.id.series);

        movies.setOnClickListener(this);
        series.setOnClickListener(this);
    }

    public void onClick(View v){

        //start movie list

        Intent movieIntent = new Intent(this, FilmList.class);
        if(v == movies){
            movieIntent.putExtra("type", "movies");
            startActivity(movieIntent);
        }

        //start series list
        if(v == series){
            movieIntent.putExtra("type","series");
            startActivity(movieIntent);

        }
    }




}
