package de.kl.fh.moviestar;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;



public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    private Button collect;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Buttons
        collect = (Button) findViewById(R.id.collection);
        search = (Button) findViewById(R.id.wsearch);

        //Listener
        collect.setOnClickListener(this);
        search.setOnClickListener(this);
    }

    // git Test
    public void onClick(View v){

        //View collection
        if(v == collect){
            Intent collIntent = new Intent(this, Collection.class);
            startActivity(collIntent);
        }

        //start websearch
        if(v == search){
            Intent searchIntent = new Intent(this, Websearch.class);
            startActivity(searchIntent);
        }
    }
}
