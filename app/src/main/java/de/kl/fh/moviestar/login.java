package de.kl.fh.moviestar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity implements View.OnClickListener {

    private Button Login;
    private EditText Username;
    private EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login = (Button) findViewById(R.id.login);
        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);

        //listener
        Login.setOnClickListener(this);
    }

    private boolean checkLogin(){
        String name =  Username.getText().toString();
        String pw = Password.getText().toString();

        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == Login){
            if(checkLogin()){
                Intent logedIn = new Intent(this, Collection.class);
                logedIn.putExtra("username", Username.getText().toString());
                logedIn.putExtra("password", Password.getText().toString());
                startActivity(logedIn);

            }
        }

    }
}
