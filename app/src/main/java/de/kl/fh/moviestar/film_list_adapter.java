package de.kl.fh.moviestar;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Marcus on 12.04.2017.
 */
public class film_list_adapter extends CursorAdapter {

    //Variabeln
    LayoutInflater myLayoutInflater;
    int layout;
    String[] from;
    int[] to;


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = myLayoutInflater.inflate(layout,parent,false);
        return v;
    }

    /*conection between Data and view
    0 = image
    1 = title
    2 = duration*/
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //define picture
        int image = cursor.getInt(cursor.getColumnIndex(from[0]));
        ImageView moviePic = (ImageView) view.findViewById(to[0]);
        moviePic.setImageResource(image);

        //define name
        String movieN = cursor.getString(cursor.getColumnIndex(from[1]));
        TextView movieName = (TextView) view.findViewById(to[1]);
        movieName.setText(movieN);

        //define duration
        String duration = cursor.getString(cursor.getColumnIndex(from[2]));
        TextView movieDuration = (TextView) view.findViewById(to[2]);
        movieDuration.setText(duration);


    }

    public  film_list_adapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags){
        super(context,c,flags);
        myLayoutInflater = LayoutInflater.from(context);
        this.layout = layout;
        this.from = from;
        this.to = to;

    }
}
