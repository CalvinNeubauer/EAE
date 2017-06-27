package de.kl.fh.moviestar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextClock;
import android.widget.TextView;

/**
 * Created by Kalle on 27.06.2017.
 */

public class AddList extends AppCompatActivity implements View.OnClickListener {

//Viewelements
    private Button addItemButton;
    private TextView textViewDirector;
    private String type;
    private EditText titleV;
    private EditText actorV;
    private EditText genreV;
    private EditText releaseV;
    private EditText runtimeV;
    private EditText directorV;
    private EditText descriptionV;
    private TextView textViewDuration;
    private DatabaseManager db;


@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        db = DatabaseManager.getInstance(this);
        type = getIntent().getStringExtra("type");
        textViewDirector = (TextView) findViewById(R.id.textViewDirectors);

        addItemButton = (Button) findViewById(R.id.addToCollection);
        addItemButton.setOnClickListener(this);

        //init
        addItemButton = (Button) findViewById(R.id.addToCollection);
        addItemButton.setOnClickListener(this);
        titleV = (EditText) findViewById(R.id.editTextTitle);
        actorV = (EditText) findViewById(R.id.editTextActors);
        genreV = (EditText) findViewById(R.id.editTextGenres);
        releaseV = (EditText) findViewById(R.id.editTextRelease);
        runtimeV = (EditText) findViewById(R.id.editTextDuration);
        directorV = (EditText) findViewById(R.id.editTextDirectors);
        descriptionV = (EditText) findViewById(R.id.editTextGenres);
        textViewDuration = (TextView)findViewById(R.id.textViewRuntime);

        if(type.equals("Movies")) {
            textViewDirector.setText("Directors: (seperate with ,)");
            runtimeV.setVisibility(View.VISIBLE);
            textViewDuration.setVisibility(View.VISIBLE);
        }
        else {
            textViewDirector.setText("Creators: (seperate with ,)");
            runtimeV.setVisibility(View.GONE);
            textViewDuration.setVisibility(View.GONE);
        }
    }

    public void onClick(View v){
        if(v == addItemButton ){
            if(type.equals("Movies")) {
                db.insertMovieIntoDatabase(getString(titleV),0.0,getInt(runtimeV),getDate(releaseV));
                String genres[] = genreV.getText().toString().split(",");
                String actors[] = actorV.getText().toString().split(",");
                String director[] = directorV.getText().toString().split(",");
                for(String s:genres)
                    db.insertGenreIntoDatabase(s);
                for(String s: actors)
                    db.insertActorIntoDatabase(s);
                for(String s: director)
                    db.insertDirectorIntoDatabase(s);
                db.addGenresToMovie(db.getMovieIDsByTitle(getString(titleV))[0],db.getGenreIDs(genres));
                db.addActorsToMovie(db.getMovieIDsByTitle(getString(titleV))[0],db.getActorIDs(actors));
                db.addDirectorsToMovie(db.getMovieIDsByTitle(getString(titleV))[0],db.getDirectorIDs(director));
                db.addDescENToMovie(db.getMovieIDsByTitle(getString(titleV))[0],getString(descriptionV));
            }
            else {
                db.insertSeriesIntoDatabase(getString(titleV),0.0,getInt(runtimeV),getDate(releaseV));
                String genres[] = genreV.getText().toString().split(",");
                String actors[] = actorV.getText().toString().split(",");
                String director[] = directorV.getText().toString().split(",");
                for(String s:genres)
                    db.insertGenreIntoDatabase(s);
                for(String s: actors)
                    db.insertActorIntoDatabase(s);
                for(String s: director)
                    db.insertCreatorIntoDatabase(s);
                db.addGenresToSeries(db.getSeriesIDsByTitle(getString(titleV))[0],db.getGenreIDs(genres));
                db.addActorsToSeries(db.getSeriesIDsByTitle(getString(titleV))[0],db.getActorIDs(actors));
                db.addCreatorsToSeries(db.getSeriesIDsByTitle(getString(titleV))[0],db.getCreatorIDs(director));
                db.addDescENToSeries(db.getMovieIDsByTitle(getString(titleV))[0],getString(descriptionV));
            }
            finish();
        }
    }

    private String getString(EditText e)
    {
        return e.getText().toString().trim();
    }

    private String getDate(EditText e)
    {
        String date = e.getText().toString().trim().replaceAll(".","");
        if(date.length()<8)
            return "00000000";
        return date;
    }

    private int getInt(EditText e)
    {
        return Integer.parseInt(e.getText().toString());
    }
}

