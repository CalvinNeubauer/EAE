package de.kl.fh.moviestar;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Kalle on 31.05.2017.
 */
public class UserListAdapter extends CursorAdapter {

    //Variabeln
    private LayoutInflater myLayoutInflater;
    private int layout,id;
    private String[] from;
    private int[] to;
    private String type;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = myLayoutInflater.inflate(layout,parent,false);
        return v;
    }

    /*conection between Data and view
    0 = Name
    1 = Number of Movies*/
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //define name
        String name = cursor.getString(cursor.getColumnIndex(from[0]));
        int items = cursor.getInt(cursor.getColumnIndex(from[1]));
        if(name!=null) {
            TextView listName = (TextView) view.findViewById(to[0]);
            listName.setText(name);

            TextView listItems = (TextView) view.findViewById(to[1]);
            listItems.setText(items + " "+type+" in List");
        }
    }

    public UserListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, String type, int flags){
        super(context,c,flags);
        myLayoutInflater = LayoutInflater.from(context);
        this.layout = layout;
        this.from = from;
        this.to = to;
        this.type = type;
    }
}
