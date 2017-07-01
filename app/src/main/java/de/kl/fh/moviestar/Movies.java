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
    private Button lists,collection,websearch,addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        //Buttons
        lists = (Button) findViewById(R.id.myLists);
        collection = (Button) findViewById(R.id.collection);
        websearch = (Button) findViewById(R.id.websearch);
        addItem = (Button) findViewById(R.id.addItem);

        //Listener
        lists.setOnClickListener(this);
        collection.setOnClickListener(this);
        websearch.setOnClickListener(this);
        addItem.setOnClickListener(this);
    }

    //Set Intents to be called when the corresponding Button is clicked
    public void onClick(View v){

        //View Lists
        if(v == lists){
            Intent listIntent = new Intent(this, UserList.class);
            listIntent.putExtra("type","Movies");
            startActivity(listIntent);
        }
        else if(v == collection){
            Intent collectionIntent = new Intent(this, ShortList.class);
            collectionIntent.putExtra("type","Movies");
            startActivity(collectionIntent);
        }
        else if(v == websearch){
            Intent searchIntent = new Intent(this, Websearch.class);
            startActivity(searchIntent);
        }
        else if(v == addItem){
            Intent addIntent = new Intent(this, AddItem.class);
            addIntent.putExtra("type","Movies");
            startActivity(addIntent);
        }
    }
}
