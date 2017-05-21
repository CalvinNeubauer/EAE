package de.kl.fh.moviestar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Kalle on 21.05.2017.
 */

public class Movies extends AppCompatActivity implements View.OnClickListener {
    private Button lists,collection,websearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        //Buttons
        lists = (Button) findViewById(R.id.myLists);
        collection = (Button) findViewById(R.id.collection);
        websearch = (Button) findViewById(R.id.websearch);

        //Listener
        lists.setOnClickListener(this);
        collection.setOnClickListener(this);
        websearch.setOnClickListener(this);
    }

    // git Test
    public void onClick(View v){

        //View Lists
        if(v == lists){
            //Intent collIntent = new Intent(this, Lists.class);
            //startActivity(collIntent);
        }

        //start Collection
        if(v == collection){
            Intent searchIntent = new Intent(this, Collection.class);
            startActivity(searchIntent);
        }

        //start Websearch
        if(v == websearch){
            Intent searchIntent = new Intent(this, Websearch.class);
            startActivity(searchIntent);
        }
    }
}