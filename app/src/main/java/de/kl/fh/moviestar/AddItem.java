package de.kl.fh.moviestar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kalle on 27.06.2017.
 */

public class AddItem extends AppCompatActivity implements View.OnClickListener {

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
    private Context ctx;


@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        ctx = getApplication();                                                 //get Context
        db = DatabaseManager.getInstance(this);                                 //get Database Instance
        type = getIntent().getStringExtra("type");                              //get current type: Movies or Series
        textViewDirector = (TextView) findViewById(R.id.textViewDirectors);

        addItemButton = (Button) findViewById(R.id.addToCollection);
        addItemButton.setOnClickListener(this);

        //init
        addItemButton = (Button) findViewById(R.id.addToCollection);
        addItemButton.setOnClickListener(this);                                 //add ClickEvent to Button
        titleV = (EditText) findViewById(R.id.editTextTitle);
        actorV = (EditText) findViewById(R.id.editTextActors);
        genreV = (EditText) findViewById(R.id.editTextGenres);
        releaseV = (EditText) findViewById(R.id.editTextRelease);
        runtimeV = (EditText) findViewById(R.id.editTextDuration);
        directorV = (EditText) findViewById(R.id.editTextDirectors);
        descriptionV = (EditText) findViewById(R.id.editTextGenres);
        textViewDuration = (TextView)findViewById(R.id.textViewRuntime);

        if(type.equals("Movies")) {                                             //if type is Movies; make Runtime-EditText and Duration-TextView visible
            textViewDirector.setText("Directors: (seperate with ,)");           //set Director-TextView text to "Directors: ...."
            runtimeV.setVisibility(View.VISIBLE);
            textViewDuration.setVisibility(View.VISIBLE);
        }
        else {                                                                  //if type is Series; make Runtime-EditText and Duration-TextView invisible
            textViewDirector.setText("Creators: (seperate with ,)");            //set Director-TextView text to "Creators: ...."
            runtimeV.setVisibility(View.GONE);
            textViewDuration.setVisibility(View.GONE);
        }
    }

    /* Check if all fields have been filled
        yes ->  insert new Series or Movie (depending on the current type) into the Database; finish Intent after insertion
        no -> show an AlertDialog to inform user of missing input
     */
    public void onClick(View v){
        String itemType = "";
        if(v == addItemButton ){
            if(notEmpty(genreV) && notEmpty(actorV) && notEmpty(directorV) && notEmpty(titleV) && notEmpty(runtimeV) && notEmpty(releaseV)) {
                if (type.equals("Movies")) {
                    itemType = "Movie";
                    db.insertMovieIntoDatabase(getString(titleV), 0.0, getInt(runtimeV), getDate(releaseV));
                    String genres[] = getString(genreV).split(",");
                    String actors[] = getString(actorV).split(",");
                    String director[] = getString(directorV).split(",");
                    for (String s : genres)
                        db.insertGenreIntoDatabase(s);
                    for (String s : actors)
                        db.insertActorIntoDatabase(s);
                    for (String s : director)
                        db.insertDirectorIntoDatabase(s);
                    db.addGenresToMovie(db.getMovieIDsByTitle(getString(titleV))[0], db.getGenreIDs(genres));
                    db.addActorsToMovie(db.getMovieIDsByTitle(getString(titleV))[0], db.getActorIDs(actors));
                    db.addDirectorsToMovie(db.getMovieIDsByTitle(getString(titleV))[0], db.getDirectorIDs(director));
                    db.addDescENToMovie(db.getMovieIDsByTitle(getString(titleV))[0], getString(descriptionV));
                } else {
                    itemType = "Series";
                    db.insertSeriesIntoDatabase(getString(titleV), 0.0, getInt(runtimeV), getDate(releaseV));
                    String genres[] = getString(genreV).split(",");
                    String actors[] = getString(actorV).split(",");
                    String director[] = getString(directorV).split(",");
                    for (String s : genres)
                        db.insertGenreIntoDatabase(s);
                    for (String s : actors)
                        db.insertActorIntoDatabase(s);
                    for (String s : director)
                        db.insertCreatorIntoDatabase(s);
                    db.addGenresToSeries(db.getSeriesIDsByTitle(getString(titleV))[0], db.getGenreIDs(genres));
                    db.addActorsToSeries(db.getSeriesIDsByTitle(getString(titleV))[0], db.getActorIDs(actors));
                    db.addCreatorsToSeries(db.getSeriesIDsByTitle(getString(titleV))[0], db.getCreatorIDs(director));
                    db.addDescENToSeries(db.getMovieIDsByTitle(getString(titleV))[0], getString(descriptionV));
                }
                Toast.makeText(ctx, itemType + " added to collection.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
            showDialog();
        }
    }

    //gets Text from EditText and trim all leading and following spaces
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

    //get Int Value from EditText
    private int getInt(EditText e)
    {
        return Integer.parseInt(getString(e));
    }

    //check if text from EditText is greater 0 after trim
    private boolean notEmpty(EditText e) {
        if(getString(e).length()>0)
            return true;
        else
            return false;
    }



    //AlertDialog to inform User of missing input
    private void showDialog()
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Can't complete task");
        alertDialog.setMessage("Every field must be filled.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.show();
    }
}

