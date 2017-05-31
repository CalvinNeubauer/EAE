package de.kl.fh.moviestar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShortList extends AppCompatActivity {
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

        db = DatabaseManager.getInstance(this);
        listView = (ListView)findViewById(R.id.mein_list_view);

        final Context ctx = this;
        int ItemLayout = R.layout.film_list_adapter;
        Cursor cursor;

        //Bestimmung der Liste
        if(type.equals("movies")){
            cursor = db.getAllMovies();
        }
        else{
            cursor = db.getAllSeries();
        }

        String[] from = new String[] {DatabaseManager.COLUMN_TITLE, DatabaseManager.COLUMN_DURATION};
        int[] to = new int[]{R.id.movie_name, R.id.movie_duration};

        film_list_adapter adapter   = new film_list_adapter(ctx,ItemLayout,cursor,from,to,0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView test = (TextView) view.findViewById(R.id.movie_duration);
                test.setText("Versuch");

            }
        });




    }


}
