package de.kl.fh.moviestar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Marcus on 14.04.2017.
 */
public class DatenbankManager extends SQLiteOpenHelper {

    //Basics
    public static final int DATENBANK_VERSION = 1;
    public static final String DATENBANK_NAME = "Movie_Datenbank";

    //Table
    public static final String TABEL_Name = "movies";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_TIME = "runtime";
    public static final String COLUMN_AUTO_KEY = "primekey";
    public static final String COLUMN_TYPE = "type";


    public DatenbankManager (Context ctx){
        super(ctx,DATENBANK_NAME,null,DATENBANK_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+TABEL_Name+"("+
                        COLUMN_TITLE + " TEXT, "+
                        COLUMN_IMAGE + " TEXT, "+
                        COLUMN_TYPE + " TEXT, "+
                        COLUMN_TIME + "TEXT" + //Format entscheiden
                        COLUMN_AUTO_KEY+ " INTEGER PRIMARY KEY AUTOINCREMENT "+
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //Abrufen aller Filmer oder Serien
    public Cursor showAll (String Type){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor zeiger = DB.rawQuery("SELECT * FROM "+TABEL_Name+"WHERE "+COLUMN_TYPE+"="+Type,null);
        zeiger.moveToFirst();
        return zeiger;
    }
}
