package de.kl.fh.moviestar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kalle on 14.06.2017.
 */

public class NewList extends AppCompatActivity implements View.OnClickListener {

    private Button newListButton;
    private EditText nameV;
    private String type,listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newlist);

        type = getIntent().getStringExtra("type");

        newListButton = (Button) findViewById(R.id.newListButton);
        newListButton.setOnClickListener(this);
    }

    /* Add a new list to the Database
     * If the listname is empty, inform the user.
     * Close intent after the click
     */
    public void onClick(View v){
        listName = nameV.getText().toString().trim();
        Intent returnIntent = new Intent();
        if(!listName.isEmpty()) {
            DatabaseManager db = DatabaseManager.getInstance(this);
            if (type.equals("Movies")) {
                db.addMovieList(listName);
            } else if (type.equals("Series")) {
                db.addSeriesList(listName);
            }
        }else
            Toast.makeText(this, "List name can't be empty.",
                    Toast.LENGTH_LONG).show();

        setResult(RESULT_OK,returnIntent);
        finish();
    }

}
