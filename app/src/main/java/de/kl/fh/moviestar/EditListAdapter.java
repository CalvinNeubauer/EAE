package de.kl.fh.moviestar;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Marcus on 12.04.2017.
 */
public class EditListAdapter extends CursorAdapter implements View.OnClickListener {

    //Variabeln
    private LayoutInflater myLayoutInflater;
    private int layout, listID;
    private String[] from;
    private int[] to;
    private String type, ID;
    private Context ctx;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = myLayoutInflater.inflate(layout,parent,false);
        return v;
    }

    /* Add a Element to the List
     * Set ClickEvent for FloatingButton
     * Set Title-TextView to Movie/Series Name
     * Set ID-TextView to Movie/Series ID (is hidden)
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        FloatingActionButton floatingButton = (FloatingActionButton) view.findViewById(R.id.deleteItemButton);
        floatingButton.setOnClickListener(this);

        //define name
        String name = cursor.getString(cursor.getColumnIndex(from[0]));
        TextView titleName = (TextView) view.findViewById(to[0]);
        titleName.setText(name);

        ID = cursor.getString(cursor.getColumnIndex(from[1]));
        TextView Idview = (TextView) view.findViewById(to[1]);
        Idview.setText(ID);
    }

    public EditListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, String type, int flags, int listID){
        super(context,c,flags);
        myLayoutInflater = LayoutInflater.from(context);
        this.layout = layout;
        this.from = from;
        this.to = to;
        this.type = type;
        ctx = context;
        this.listID = listID;
    }

    /* Get DatabaseManager Instance and delete a Series/Movie from the current List
     * "Refresh" this Parent Intent
     */
    public void onClick(View v) {
        DatabaseManager db = DatabaseManager.getInstance(ctx);
        if(type.equals("Movies")) {
            db.deleteMovieFromList(listID, Integer.parseInt(ID));
            ((Activity)ctx).finish();
            (ctx).startActivity(((Activity)ctx).getIntent());
        }
        else if(type.equals("Series")) {
            db.deleteSeriesFromList(listID, Integer.parseInt(ID));
            ((Activity)ctx).finish();
            (ctx).startActivity(((Activity)ctx).getIntent());
        }
    }
}
