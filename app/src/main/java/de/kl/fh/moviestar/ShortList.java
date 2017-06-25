package de.kl.fh.moviestar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ShortList extends AppCompatActivity implements View.OnClickListener{
    private DatabaseManager db;
    private ListView listView;
    private String type, listName;
    private String[] from;
    private int[] to;
    private int series_id;
    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortlist);

        //Get intent extra
        myIntent = getIntent();
        type = myIntent.getStringExtra("type");
        listName = myIntent.getStringExtra("listName");

        Button editButton = (Button) findViewById(R.id.edit_button);
        TextView postionTextView = (TextView) findViewById(R.id.position);
        postionTextView.setText(type);
        editButton.setOnClickListener(this);
        if (listName == null || listName.isEmpty()) {
            editButton.setVisibility(View.GONE);
            postionTextView.setVisibility(View.VISIBLE);
        } else {
            editButton.setVisibility(View.VISIBLE);
            postionTextView.setVisibility(View.GONE);
        }

        db = DatabaseManager.getInstance(this);
        listView = (ListView) findViewById(R.id.short_list_view);

        final Context ctx = this;
        int ItemLayout = R.layout.element_shortlist;
        Cursor cursor = null;

        //Bestimmung der Liste
        if (type.equals("Movies") && listName == null) {
            cursor = db.getAllMovies();
            from = new String[]{DatabaseManager.COLUMN_TITLE, DatabaseManager.COLUMN_DURATION, DatabaseManager.COLUMN_RATING, "_id"};
            to = new int[]{R.id.title_name, R.id.title_duration, R.id.title_rating, R.id.title_rating_text, R.id.title_id};

        } else if (type.equals("Series") && listName == null) {
            cursor = db.getAllSeries();
            from = new String[]{DatabaseManager.COLUMN_TITLE, DatabaseManager.COLUMN_SEASONS, DatabaseManager.COLUMN_RATING, "_id"};
            to = new int[]{R.id.title_name, R.id.title_duration, R.id.title_rating, R.id.title_rating_text, R.id.title_id};

        } else if (type.equals("Seasons")) {
            series_id = myIntent.getIntExtra("ID", -1);
            cursor = db.getSeasonsFromSeries(series_id);
            from = new String[]{DatabaseManager.COLUMN_SEASON_ID, "Episodes", "_id"};
            to = new int[]{R.id.title_name, R.id.title_duration, R.id.title_id};

        } else if (type.equals("Episodes") && listName == null) {
            int season_id = myIntent.getIntExtra("ID", -1);
            cursor = db.getEpisodesFromSeason(season_id);
            from = new String[]{DatabaseManager.COLUMN_TITLE, DatabaseManager.COLUMN_DURATION, "_id"};
            to = new int[]{R.id.title_name, R.id.title_duration, R.id.title_id};

        } else {
            if (!listName.isEmpty()) {
                if (type.equals("Movies")) {
                    cursor = db.getMoviesFromList(listName);
                    from = new String[]{DatabaseManager.COLUMN_TITLE, DatabaseManager.COLUMN_DURATION, DatabaseManager.COLUMN_RATING, "_id"};
                } else if (type.equals("Series")) {
                    cursor = db.getSeriesFromList(listName);
                    from = new String[]{DatabaseManager.COLUMN_TITLE, DatabaseManager.COLUMN_SEASONS, DatabaseManager.COLUMN_RATING, "_id"};
                }
                to = new int[]{R.id.title_name, R.id.title_duration, R.id.title_rating, R.id.title_rating_text, R.id.title_id};

            }
        }

        if (cursor != null) {
            ShortListAdapter adapter = new ShortListAdapter(ctx, ItemLayout, cursor, from, to, type, 0);
            listView.setAdapter(adapter);


            if (type.equals("Seasons")) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView ExtraId = (TextView) view.findViewById(R.id.title_id);
                        Intent shortList = new Intent(ctx, ShortList.class);
                        shortList.putExtra("ID", Integer.parseInt(ExtraId.getText().toString()));
                        shortList.putExtra("type", "Episodes");
                        startActivity(shortList);
                    }
                });
            }else if(!type.equals("Episodes")){
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView ExtraId = (TextView) view.findViewById(R.id.title_id);
                        Intent showMovie = new Intent(ctx, SingleMovie.class);
                        showMovie.putExtra("ID", Integer.parseInt(ExtraId.getText().toString()));
                        showMovie.putExtra("type", type);
                        startActivityForResult(showMovie,0);
                    }
                });
            }
        }
    }

    public void onClick(View v) {
        Intent editListIntent = new Intent(this, EditList.class);
        editListIntent.putExtra("type", type);
        editListIntent.putExtra("listName", listName);
        startActivityForResult(editListIntent, 0);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        startActivity(myIntent);
    }
}