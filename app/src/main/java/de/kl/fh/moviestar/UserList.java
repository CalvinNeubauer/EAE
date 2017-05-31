package de.kl.fh.moviestar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

/**
 * Created by Kalle on 31.05.2017.
 */

public class UserList extends AppCompatActivity {
    private DatabaseManager db;
    private ListView listView;
    private String type;
    private String[] from;
    private int[] to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        //Get intent extra
        Intent myIntent = getIntent();
        type = myIntent.getStringExtra("type");

        db = DatabaseManager.getInstance(this);
        listView = (ListView) findViewById(R.id.user_list_view);

        Context ctx = this;
        int ItemLayout = R.layout.element_userlist;
        Cursor cursor;

        //Bestimmung der Liste
        if (type.equals("Movies")) {
            type = "Movies";
            cursor = db.getMovieLists();
        } else {
            type = "Series";
            cursor = db.getSeriesLists();
        }

        from = new String[]{DatabaseManager.COLUMN_NAME.toUpperCase(), type.toUpperCase()};
        to = new int[]{R.id.list_name, R.id.list_item_number};

        UserListAdapter adapter = new UserListAdapter(ctx, ItemLayout, cursor, from, to, type, 0);
        listView.setAdapter(adapter);
    }
}