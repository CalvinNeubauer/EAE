package de.kl.fh.moviestar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import static android.R.attr.data;

/**
 * Created by Kalle on 31.05.2017.
 */

public class UserList extends AppCompatActivity implements View.OnClickListener {
    private DatabaseManager db;
    private ListView listView;
    private String type,action;
    private String[] from;
    private int[] to;
    private Button addListButton;
    private int ID;

    //If get the user lists from the database depending on the type
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        addListButton = (Button) findViewById(R.id.add_button);
        addListButton.setOnClickListener(this);

        //Get intent extras
        Intent myIntent = getIntent();
        type = myIntent.getStringExtra("type");
        ID = myIntent.getIntExtra("ID",-1);
        action = myIntent.getStringExtra("action");

        db = DatabaseManager.getInstance(this);
        listView = (ListView) findViewById(R.id.user_list_view);

        final Context ctx = this;
        int ItemLayout = R.layout.element_userlist;
        Cursor cursor;

        if (type.equals("Movies")) {
            type = "Movies";
            cursor = db.getMovieLists();
        } else {
            type = "Series";
            cursor = db.getSeriesLists();
        }

        from = new String[]{DatabaseManager.COLUMN_NAME, type.toUpperCase()};
        to = new int[]{R.id.list_name, R.id.list_item_count};

        UserListAdapter adapter = new UserListAdapter(ctx, ItemLayout, cursor, from, to, type, 0);
        listView.setAdapter(adapter);

        //If the action is Add and the ID is not -1, add the Movie/Series to the List
        if(ID>-1 && action.equals("add"))
        {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView listName = (TextView) view.findViewById(R.id.list_name);
                    if(type.equals("Movies"))
                        db.addMovieToList(listName.getText().toString(),ID);
                    else
                        db.addSeriesToList(listName.getText().toString(),ID);
                    finish();
                }
            });
        }
        else { //If a List was selected, open the intent that shows the items in that list
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView listName = (TextView) view.findViewById(R.id.list_name);
                    Intent shortList = new Intent(ctx, ShortList.class);
                    shortList.putExtra("listName", listName.getText());
                    shortList.putExtra("type", type);
                    startActivityForResult(shortList, 0);
                }
            });
        }
    }

    //Open the intent that allows the user to add a new list
    public void onClick(View v) {
        Intent newListIntent = new Intent(this, NewList.class);
        newListIntent.putExtra("type", type);
        startActivityForResult(newListIntent,0);
    }

    //"Refresh" this intent
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        finish();
        startActivity(getIntent());
    };
}