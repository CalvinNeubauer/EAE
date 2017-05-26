package de.kl.fh.moviestar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class FilmList extends AppCompatActivity {
    DatabaseManager db;
    ListView listView;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        //Get intent extra
        Intent myIntent = getIntent();
        type = myIntent.getStringExtra("type");

        db = new DatabaseManager(this);
        listView = (ListView)findViewById(R.id.mein_list_view);

        Context ctx = this;
        int ItemLayout = R.layout.activity_list_element;
        Cursor cursor;

        //Bestimmung der Liste
        if(type == "movies"){
            cursor = db.getAllMovies();
        }
        else{
            cursor = db.getAllSeries();
        }

        String[] from = new String[] {db.COLUMN_NAME, db.COLUMN_DURATION};
        int[] to = new int[]{R.id.Film_Image, R.id.Film_Duration};

        film_list_adapter adapter   = new film_list_adapter(ctx,ItemLayout,cursor,from,to,0);
        listView.setAdapter(adapter);



    }
}
