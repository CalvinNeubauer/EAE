package de.kl.fh.moviestar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;


public class SingleMovie extends AppCompatActivity implements View.OnClickListener {

    private int ID;
    private String type, title, genre, cast, director;
    private DatabaseManager db;
    private Cursor cursor;
    private Button listAdd, deleteItem;
    private Context ctx;
    private TextView textViewDirector, textViewGenre, textViewCast,textViewDirectorLabel, textViewActorsLabel, textViewSeasonCount;

    //Get Extras and set ClickEvents
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);
        ctx = this;

        Intent myIntent = getIntent();
        ID=myIntent.getIntExtra("ID",-1);
        type=myIntent.getStringExtra("type");
        listAdd = (Button) findViewById(R.id.addToListButton);
        listAdd.setOnClickListener(this);
        deleteItem = (Button) findViewById(R.id.deleteItemButton);
        deleteItem.setOnClickListener(this);

        fillXML();
    }

    /* Show the number of Seasons available for the Series as a clickable item
     * Display various data depending on the type
     */
    private void fillXML (){
        db = DatabaseManager.getInstance(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.seasons_count_item);

        if(type.equals("Episodes")) {
            layout.setVisibility(View.GONE);
            cursor = db.getEpisodeDataByID(ID);
        }
        else if(type.equals("Series")) {
            layout.setVisibility(View.VISIBLE);
            cursor = db.getSeriesDataByID(ID);
        }
        else if(type.equals("Movies")) {
            layout.setVisibility(View.GONE);
            cursor = db.getMovieDataByID(ID);
        }

        title = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_TITLE));
        TextView textViewTitleName = (TextView) this.findViewById(R.id.Name);
        textViewTitleName.setText(title);

        if(type.equals("Series"))
        {
            String releaseDate = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_RELEASE));
            TextView release = (TextView) this.findViewById(R.id.Release);
            release.setText(releaseDate);
        }
        else {
            String releaseDate = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_RELEASE));
            String day = releaseDate.substring(0, 2);
            String month = releaseDate.substring(2, 4);
            String year = releaseDate.substring(4, 8);
            TextView release = (TextView) this.findViewById(R.id.Release);
            release.setText(day + "." + month + "." + year);

            int duration = cursor.getInt(cursor.getColumnIndex(DatabaseManager.COLUMN_DURATION));
            TextView textViewRuntime = (TextView) this.findViewById(R.id.Runtime);
            int minutes = duration % 60;
            int hours = (duration - minutes) / 60;
            if(hours==0) {
                textViewRuntime.setText(minutes + "m");
            }
            else
            {
                textViewRuntime.setText(hours + "h " + minutes + "m");
            }
        }

        String description = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_DESC_EN));
        TextView textViewDescription = (TextView) this.findViewById(R.id.Description);
        textViewDescription.setText(description);


        float rating = cursor.getFloat(cursor.getColumnIndex(DatabaseManager.COLUMN_RATING));
        RatingBar titleRating = (RatingBar) this.findViewById(R.id.ratingBar);
        titleRating.setRating(rating);


        textViewDirectorLabel = (TextView) this.findViewById(R.id.director_label);
        textViewGenre = (TextView) this.findViewById(R.id.Genre);
        textViewCast = (TextView) this.findViewById(R.id.Actors);
        textViewDirector = (TextView) this.findViewById(R.id.director);

        if(type.equals("Movies")){
            setMovie();
        } else if (type.equals("Series")){
            textViewSeasonCount = (TextView) findViewById(R.id.seasons_count_text);
            int seasons = cursor.getInt(cursor.getColumnIndex(DatabaseManager.COLUMN_SEASONS));
            textViewSeasonCount.setText(seasons+" Seasons");
            textViewSeasonCount.setOnClickListener(this);
            setSeries();
        }
        textViewGenre.setText(genre);
        textViewDirector.setText(director);
        textViewCast.setText(cast);
        cursor.close();
    }

    /* If listAdd was clicked add the Series/Movie to a UserList (that must be chosen in the next intent)
     * If deleteItem was clicked, ask the user to confirm the action
     * If SeasonCount (clickable item) was clicked, show the available Seasons for the Series in a ShortList
     */
    @Override
    public void onClick(View v){
        if(v == listAdd) {
            Intent listIntent = new Intent(this, UserList.class);
            listIntent.putExtra("ID", ID);
            listIntent.putExtra("type", type);
            listIntent.putExtra("action", "add");
            startActivity(listIntent);
        }
        else if(v == deleteItem)
        {
            showDeleteDialog();
        }
        else if (v == textViewSeasonCount){
            TextView ExtraId = (TextView) findViewById(R.id.title_id);
            Intent shortList = new Intent(ctx, ShortList.class);
            shortList.putExtra("ID", ID);
            shortList.putExtra("type", "Seasons");
            startActivity(shortList);
        }
    }

    //Get Directors, Genres and Actors from the Movie
    private void setMovie()
    {
        director="";
        cast="";
        genre="";

        textViewDirectorLabel.setText("Director:");
        cursor = db.getDirectorOfMovie(title);
        cursor.moveToFirst();
        int count = cursor.getCount();
        while (!cursor.isAfterLast()){
            director += cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_NAME));
            count--;
            if(count>0)
                director+=", ";
            cursor.moveToNext();
        }
        cursor.close();

        cursor = db.getCastFromMovie(title);
        cursor.moveToFirst();
        count = cursor.getCount();
        while (!cursor.isAfterLast()){
            cast += cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_NAME));
            count--;
            if(count>0)
                cast+=", ";
            cursor.moveToNext();
        }
        cursor.close();

        cursor = db.getGenreOfMovie(title);
        cursor.moveToFirst();
        count = cursor.getCount();
        while (!cursor.isAfterLast()){
            genre += cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_NAME));
            count--;
            if(count>0)
                genre+=", ";
            cursor.moveToNext();
        }
        cursor.close();
    }

    //Get Creators, Genres and Actors from the Series
    private void setSeries()
    {
        director="";
        cast="";
        genre="";

        textViewDirectorLabel.setText("Creator:");

        cursor = db.getCreatorOfSeries(title);
        cursor.moveToFirst();
        int count = cursor.getCount();
        while (!cursor.isAfterLast()){
            director += cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_NAME));
            count--;
            if(count>0)
                director+=", ";
            cursor.moveToNext();
        }
        cursor.close();

        cursor = db.getCastFromSeries(title);
        cursor.moveToFirst();
        count = cursor.getCount();
        while (!cursor.isAfterLast()){
            cast += cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_NAME));
            count--;
            if(count>0)
                cast+=", ";
            cursor.moveToNext();
        }
        cursor.close();


        cursor = db.getGenreOfSeries(title);
        cursor.moveToFirst();
        count = cursor.getCount();
        while (!cursor.isAfterLast()){
            genre += cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_NAME));
            count--;
            if(count>0)
                genre+=", ";
            cursor.moveToNext();
        }
        cursor.close();
    }

    /* Show a AlertDialog to ask the user to confirm the deleting of a Movie or Series
     * If "Yes" was clicked delete the object and close the Intent
     * If "No" was clicked do nothing
     */

    private void showDeleteDialog()
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Delete object?");
        alertDialog.setMessage("Are you sure you want to delete this object?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                if(type.equals("Series"))
                {
                    db.deleteSeriesFromDatabase(ID);
                    finish();
                }
                else if(type.equals("Movies"))
                {
                    db.deleteMovieFromDatabase(ID);
                    finish();
                }
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.show();
    }
}
