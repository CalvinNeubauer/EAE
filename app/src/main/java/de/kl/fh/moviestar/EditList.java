package de.kl.fh.moviestar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.StringTokenizer;
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
    private Button deleteListButton;
    private FloatingActionButton editListNameButton;
    private EditListAdapter adapter;
    private Context ctx;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editlist);

        deleteListButton = (Button) findViewById(R.id.delete_list_button);
        deleteListButton.setOnClickListener(this);                                          //Add ClickEvent to "Delete List"-Button
        editListNameButton = (FloatingActionButton) findViewById(R.id.editNameButton);
        editListNameButton.setOnClickListener(this);                                        //Add ClickEvent to "Edit List"-Button
        editText = (EditText) findViewById(R.id.list_name);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editText.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });


        db = DatabaseManager.getInstance(this);                                             //Get DatabaseManager Instance
        listView = (ListView) findViewById(R.id.edit_list_view);

        Intent myIntent = getIntent();
        type = myIntent.getStringExtra("type");                                             //Get type: "Movies" or "Series"
        listName = myIntent.getStringExtra("listName");                                     //Get listname
        editText.setText(listName);                                                           //Set editText text to listname
        if(type.equals("Movies"))                                                           //If type is Movies
            listID = db.getMovieListID(listName).getInt(0);                                   //Get MoviesID from the list
        else if(type.equals("Series"))                                                      //If type is Series
                listID = db.getSeriesListID(listName).getInt(0);                              //Get SeriesID from the list

        ctx = this;
        int ItemLayout = R.layout.element_editlist;
        Cursor cursor;

        //Bestimmung der Liste
        if (type.equals("Movies")) {
            cursor = db.getMoviesFromList(listID);                                          //Get Movies from the list
        } else {
            cursor = db.getSeriesFromList(listID);                                          //Get Series from the list
        }

        from = new String[]{DatabaseManager.COLUMN_TITLE, "_id"};                           //From: get Title and ID from database result
        to = new int[]{R.id.title_name, R.id.title_id};                                     //To: target for the database data

        adapter = new EditListAdapter(ctx, ItemLayout, cursor, from, to, type, 0, listID);
        listView.setAdapter(adapter);
    }

    public void onClick(View v) {
        if(v==deleteListButton)                                                             //If deleteList was clicked
        {
            if(type.equals("Movies")) {                                                         //If type is Movies
                db.deleteMovieList(listID);                                                     //delete Movie List
                Toast.makeText(ctx, "List deleted",                                             //Inform User, that his/her List was deleted
                        Toast.LENGTH_SHORT).show();
            }
            if(type.equals("Series")) {                                                         //If type is Series
                db.deleteSeriesList(listID);                                                    //delete Series List
                Toast.makeText(ctx, "List deleted",                                             //Inform User, that his/her List was deleted
                        Toast.LENGTH_SHORT).show();
            }
        }else if(v==editListNameButton){                                                    //If editList-Button was clicked
            if(type.equals("Movies"))                                                       //If type is Movies
                db.changeMovieListName(listID,editText.getText().toString().trim());            //change the name of the List in the database
            else if(type.equals("Series"))                                                  //If type is Series
                db.changeSeriesListName(listID,editText.getText().toString().trim());           //change the name of the List in the database
        }
        finish();                                                                           //finish this intent;
    }

    //Close this Intent and reopen it to display the new data
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        finish();
        startActivity(getIntent());
    };
}
