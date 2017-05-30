package de.kl.fh.moviestar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ShortList extends AppCompatActivity {
    private DatabaseManager db;
    private ListView listView;
    private String type;
    private String[] from;
    private int[] to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortlist);

        //Get intent extra
        Intent myIntent = getIntent();
        type = myIntent.getStringExtra("type");

        db = DatabaseManager.getInstance(this);
        listView = (ListView)findViewById(R.id.mein_list_view);

        Context ctx = this;
        int ItemLayout = R.layout.element_shortlist;
        Cursor cursor;

        //Bestimmung der Liste
        if(type.equals("movies")){
            cursor = db.getAllMovies();
            from = new String[] {DatabaseManager.COLUMN_TITLE, DatabaseManager.COLUMN_DURATION, DatabaseManager.COLUMN_RATING};
        }
        else{
            cursor = db.getAllSeries();
            from = new String[] {DatabaseManager.COLUMN_TITLE, DatabaseManager.COLUMN_SEASONS, DatabaseManager.COLUMN_RATING};
        }

        to = new int[]{R.id.title_name, R.id.title_duration, R.id.title_rating, R.id.title_rating_text};
        ShortListAdapter adapter   = new ShortListAdapter(ctx,ItemLayout,cursor,from,to,type,0);
        listView.setAdapter(adapter);
    }
}
