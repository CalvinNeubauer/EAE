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
 * Created by Marcus on 12.04.2017.
 */
public class ShortListAdapter extends CursorAdapter {

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

    /*conection between Data and view
    0 = image
    1 = title
    2 = duration*/
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //define picture
        //int image = cursor.getInt(cursor.getColumnIndex(from[1]));
        //ImageView moviePic = (ImageView) view.findViewById(to[1]);
        //moviePic.setImageResource(image);

        //define name
        String name = cursor.getString(cursor.getColumnIndex(from[0]));
        TextView titleName = (TextView) view.findViewById(to[0]);
        titleName.setText(name);

        //define duration/seasons
        if(type.equals("movies")) {
            int duration = cursor.getInt(cursor.getColumnIndex(from[1]));
            TextView titleDuration = (TextView) view.findViewById(to[1]);
            int minutes = duration % 60;
            int hours = (duration - minutes) / 60;
            titleDuration.setText(hours + "h " + minutes + "m");
        }
        else if(type.equals("series"))
        {
            int seasons = cursor.getInt(cursor.getColumnIndex(from[1]));
            TextView titleSeasons = (TextView) view.findViewById(to[1]);
            titleSeasons.setText(seasons+" Seasons");
        }

        //define rating
        float rating = cursor.getFloat(cursor.getColumnIndex(from[2]));
        RatingBar titleRating = (RatingBar) view.findViewById(to[2]);
        titleRating.setRating(rating);

        //define rating text
        TextView ratingText = (TextView) view.findViewById(to[3]);
        ratingText.setText(""+rating);

    }

    public ShortListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, String type, int flags){
        super(context,c,flags);
        myLayoutInflater = LayoutInflater.from(context);
        this.layout = layout;
        this.from = from;
        this.to = to;
        this.type = type;
    }
}