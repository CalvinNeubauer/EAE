package de.kl.fh.moviestar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    private Button movies,series;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Buttons
        movies = (Button) findViewById(R.id.movies);
        series = (Button) findViewById(R.id.series);
        //Listener
        movies.setOnClickListener(this);
        series.setOnClickListener(this);
    }

    // git Test
    public void onClick(View v){

        //View movies
        if(v == movies){
            Intent collIntent = new Intent(this, Movies.class);
            startActivity(collIntent);
        }
        else if(v == series){
            Intent searchIntent = new Intent(this, Series.class);
            startActivity(searchIntent);
        }

    }
}
