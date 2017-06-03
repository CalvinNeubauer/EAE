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
        listView = (ListView)findViewById(R.id.short_list_view);

        final Context ctx = this;
        int ItemLayout = R.layout.element_shortlist;
        Cursor cursor;

        //Bestimmung der Liste
        if(type.equals("Movies")){
            cursor = db.getAllMovies();
            from = new String[] {DatabaseManager.COLUMN_TITLE, DatabaseManager.COLUMN_DURATION, DatabaseManager.COLUMN_RATING,DatabaseManager.COLUMN_ID};
        }
        else{
            cursor = db.getAllSeries();
            from = new String[] {DatabaseManager.COLUMN_TITLE, DatabaseManager.COLUMN_SEASONS, DatabaseManager.COLUMN_RATING,DatabaseManager.COLUMN_ID};
        }

        to = new int[]{R.id.title_name, R.id.title_duration, R.id.title_rating, R.id.title_rating_text, R.id.title_id};
        ShortListAdapter adapter   = new ShortListAdapter(ctx,ItemLayout,cursor,from,to,type,0);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView ExtraId = (TextView) view.findViewById(R.id.title_id);
                Intent showMovie = new Intent(ctx, SingleMovie.class);
                showMovie.putExtra("ID",ExtraId.getText());
                startActivity(showMovie);
                }});
    }
}
