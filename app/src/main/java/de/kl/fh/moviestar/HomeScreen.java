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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Buttons
        collect = (Button) findViewById(R.id.collection);

        //Listener
        collect.setOnClickListener(this);
    }

    // git Test
    public void onClick(View v){
        if(v == collect){
            Intent collIntent = new Intent(this, Collection.class);
            startActivity(collIntent);
        }
    }
}
