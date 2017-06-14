package de.kl.fh.moviestar;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SingleMovie extends AppCompatActivity {

    private int ID;
    private String type;
    private DatabaseManager db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        Intent myIntent = getIntent();
        ID=myIntent.getIntExtra("ID",-1);
        type=myIntent.getStringExtra("type");


        fillXML();
    }

    private void fillXML (){
        db = DatabaseManager.getInstance(this);
        if(type.equals("Episodes"))
            cursor = db.getEpisodeDataByID(ID);
        else if(type.equals("Movies"))
            cursor = db.getMovieDataByID(ID);

        String name = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_TITLE));
        TextView titleName = (TextView) this.findViewById(R.id.Name);
        titleName.setText(name);

    }
}
