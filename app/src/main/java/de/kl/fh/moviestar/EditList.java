package de.kl.fh.moviestar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Vector;

/**
 * Created by Kalle on 20.06.2017.
 */

public class EditList extends AppCompatActivity implements View.OnClickListener {
    private DatabaseManager db;
    private ListView listView;
    private String type,listName;
    private String[] from;
    private int[] to;
    private int listID;
    private Button deleteListButton, deleteTitlesButton;
    private Vector<Integer> titleIDs = new Vector<>();
    private int t_id;
    private EditListAdapter adapter;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editlist);

        deleteListButton = (Button) findViewById(R.id.delete_list_button);
        deleteListButton.setOnClickListener(this);

        deleteTitlesButton = (Button) findViewById(R.id.delete_title_button);
        deleteTitlesButton.setOnClickListener(this);

        db = DatabaseManager.getInstance(this);
        listView = (ListView) findViewById(R.id.edit_list_view);

        Intent myIntent = getIntent();
        type = myIntent.getStringExtra("type");
        listName = myIntent.getStringExtra("listName");
        if(type.equals("Movies"))
            listID = db.getMovieListID(listName).getInt(0);
        else if(type.equals("Series"))
                listID = db.getSeriesListID(listName).getInt(0);

        ctx = this;
        int ItemLayout = R.layout.element_editlist;
        Cursor cursor;

        //Bestimmung der Liste
        if (type.equals("Movies")) {
            cursor = db.getMoviesFromList(listID);
        } else {
            cursor = db.getSeriesFromList(listID);
        }

        from = new String[]{DatabaseManager.COLUMN_TITLE, "_id"};
        to = new int[]{R.id.title_name, R.id.title_id};

        adapter = new EditListAdapter(ctx, ItemLayout, cursor, from, to, type, 0);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void onClick(View v) {
        if(v==deleteListButton)
        {
            if(type.equals("Movies")) {
                db.deleteMovieList(listID);
            }
            if(type.equals("Series")) {
                db.deleteSeriesList(listID);
            }
        }else if(v==deleteTitlesButton)
        {
            if(type.equals("Movies")) {
                for(int i: titleIDs)
                    db.deleteMovieFromList(listID,i);
            }
            if(type.equals("Series")) {
                for(int i: titleIDs)
                    db.deleteSeriesFromList(listID,i);
            }
        }
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        finish();
        startActivity(getIntent());
    };
}
