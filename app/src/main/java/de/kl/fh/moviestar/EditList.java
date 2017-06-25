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
    private Vector<Integer> titleIDs = new Vector<>();
    private int t_id;
    private EditListAdapter adapter;
    private Context ctx;
    private EditText editText;
    private boolean editing;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editlist);

        deleteListButton = (Button) findViewById(R.id.delete_list_button);
        deleteListButton.setOnClickListener(this);
        editListNameButton = (FloatingActionButton) findViewById(R.id.editNameButton);
        editListNameButton.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.list_name);
        editing = false;
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


        db = DatabaseManager.getInstance(this);
        listView = (ListView) findViewById(R.id.edit_list_view);

        Intent myIntent = getIntent();
        type = myIntent.getStringExtra("type");
        listName = myIntent.getStringExtra("listName");
        editText.setText(listName);
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

        adapter = new EditListAdapter(ctx, ItemLayout, cursor, from, to, type, 0, listID);
        listView.setAdapter(adapter);
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
        }else if(v==editListNameButton){
            if(type.equals("Movies"))
                db.changeMovieListName(listID,editText.getText().toString().trim());
            else if(type.equals("Series"))
                db.changeSeriesListName(listID,editText.getText().toString().trim());
        }
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        finish();
        startActivity(getIntent());
    };
}
