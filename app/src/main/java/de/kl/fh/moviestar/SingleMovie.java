package de.kl.fh.moviestar;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class SingleMovie extends AppCompatActivity implements View.OnClickListener {

    private int ID;
    private String type;
    private DatabaseManager db;
    private Cursor cursor;
    private Button listAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        Intent myIntent = getIntent();
        ID=myIntent.getIntExtra("ID",-1);
        type=myIntent.getStringExtra("type");
        listAdd = (Button) findViewById(R.id.addToListButton);
        listAdd.setOnClickListener(this);

        fillXML();
    }

    private void fillXML (){
        db = DatabaseManager.getInstance(this);
        if(type.equals("Episodes"))
            cursor = db.getEpisodeDataByID(ID);
        else if(type.equals("Movies"))
            cursor = db.getMovieDataByID(ID);

        String title = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_TITLE));
        TextView textViewTitleName = (TextView) this.findViewById(R.id.Name);
        textViewTitleName.setText(title);

        String releaseDate = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_RELEASE));
        String day = releaseDate.substring(0,2);
        String month = releaseDate.substring(2,4);
        String year = releaseDate.substring(4,8);
        TextView release = (TextView) this.findViewById(R.id.Release);
        release.setText(day+"."+month+"."+year);

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

        String description = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_DESC_EN));
        TextView textViewDescription = (TextView) this.findViewById(R.id.Description);
        textViewDescription.setText(description);


        float rating = cursor.getFloat(cursor.getColumnIndex(DatabaseManager.COLUMN_RATING));
        RatingBar titleRating = (RatingBar) this.findViewById(R.id.ratingBar);
        titleRating.setRating(rating);
    }

    @Override
    public void onClick(View v){
        Intent listIntent = new Intent(this,UserList.class);
        listIntent.putExtra("ID",ID);
        listIntent.putExtra("type",type);
        startActivity(listIntent);
    }
}
