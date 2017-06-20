package de.kl.fh.moviestar;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Marcus on 12.04.2017.
 */
public class EditListAdapter extends CursorAdapter {

    //Variabeln
    private LayoutInflater myLayoutInflater;
    private int layout;
    private String[] from;
    private int[] to;
    private String type;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = myLayoutInflater.inflate(layout,parent,false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //define name
        String name = cursor.getString(cursor.getColumnIndex(from[0]));
        TextView titleName = (TextView) view.findViewById(to[0]);
        titleName.setText(name);

        String Id = cursor.getString(cursor.getColumnIndex(from[1]));
        TextView Idview = (TextView) view.findViewById(to[1]);
        Idview.setText(Id);
    }

    public EditListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, String type, int flags){
        super(context,c,flags);
        myLayoutInflater = LayoutInflater.from(context);
        this.layout = layout;
        this.from = from;
        this.to = to;
        this.type = type;
    }
}
