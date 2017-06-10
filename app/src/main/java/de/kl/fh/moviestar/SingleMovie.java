package de.kl.fh.moviestar;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SingleMovie extends AppCompatActivity {

    private String ID;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        Intent myIntent = getIntent();
        ID = myIntent.getStringExtra("ID");

        fillXML();
    }

    private void fillXML (){
        db = DatabaseManager.getInstance(this);
        Cursor cursor = db.getMovieByID(ID);

        String name = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_NAME));
        TextView titleName = (TextView) this.findViewById(R.id.Name);
        titleName.setText(name);

    }
}
