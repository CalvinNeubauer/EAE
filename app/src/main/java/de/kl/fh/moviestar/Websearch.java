package de.kl.fh.moviestar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Websearch extends AppCompatActivity implements View.OnClickListener {

    //Viewelements
    private Button searchButton;
    private EditText titleV;
    private EditText actorV;
    private EditText genreV;
    private EditText yearV;

    /* The orginal Intent was to have the User search of a Series or a Movie
     * the User would select that which he was looking for from the resulting list.
     * He/She would then have the possibility to add this Item to their collection.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_websearch);

        //init
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        titleV = (EditText) findViewById(R.id.titleET);
        actorV = (EditText) findViewById(R.id.actorET);
        genreV = (EditText) findViewById(R.id.genreET);
        yearV = (EditText) findViewById(R.id.yearET);
    }

    public void onClick(View v){
        if(v == searchButton ){
            Intent startSearch = new Intent(this, websearchList.class);
            startSearch.putExtra("title", titleV.getText());
            startSearch.putExtra("actor", actorV.getText());
            startSearch.putExtra("genre", genreV.getText());
            startSearch.putExtra("year", yearV.getText());
            startActivity(startSearch);
        }
    }
}
