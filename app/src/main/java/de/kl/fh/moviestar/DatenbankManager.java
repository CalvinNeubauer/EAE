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
    public static final String DATENBANK_NAME = "CouchPotato.db";

    //Table
    public static final String TABLE_MOVIES = "Movies";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_RELEASE = "release";
    public static final String COLUMN_DESC_EN = "description_en";
    public static final String COLUMN_DESC_DE = "description_de";
    public static final String COLUMN_SEQUEL_OF = "sequel_of";
    public static final String COLUMN_WATCHED = "watched";              //Muss 1 oder 0 sein


    public static final String TABLE_SERIES = "Series";
    public static final String COLUMN_SEASONS = "seasons";

    public static final String TABLE_MOVIE_LISTS = "Movie_Lists";
    public static final String TABLE_SERIES_LISTS = "Series_Lists";
    public static final String COLUMN_NAME = "name";


    public static final String TABLE_MOVIE_LISTS_ELEMENTS = "Movie_Lists_Elements";
    public static final String TABLE_SERIES_LISTS_ELEMENTS = "Series_Lists_Elements";
    public static final String COLUMN_MOVIE_LIST_ID = "movie_list_id";
    public static final String COLUMN_SERIES_LIST_ID = "series_list_id";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_SERIES_ID = "series_id";

    public static final String TABLE_SERIES_SEASONS = "Series_Seasons";
    public static final String COLUMN_SEASON = "Season";
    public static final String TABLE_SEASON_EPISODES = "Season_Episodes";
    public static final String COLUMN_SEASON_ID = "Season_Id";
    public static final String COLUMN_EPISODE_ID = "Episode_Id";
    public static final String TABLE_EPISODES = "Episodes";


    public static final String TABLE_ACTORS = "Actors";
    public static final String TABLE_GENRES = "Genres";
    public static final String TABLE_DIRECTORS = "Directors";
    public static final String TABLE_CREATORS = "Creators";

    public static final String TABLE_MOVIE_ACTORS = "Movie_Actors";
    public static final String TABLE_MOVIE_GENRES = "Movie_Genres";
    public static final String TABLE_MOVIE_DIRECTORS = "Movie_Directors";
    public static final String TABLE_SERIES_ACTORS = "Series_Actors";
    public static final String TABLE_SERIES_CREATORS = "Series_Creators";
    public static final String COLUMN_ACTOR_ID = "actor_id";
    public static final String COLUMN_DIRECTOR_ID = "director_id";
    public static final String COLUMN_GENRE_ID = "genre_id";
    public static final String COLUMN_CREATOR_ID = "creator_id";


    public DatenbankManager (Context ctx){
        super(ctx,DATENBANK_NAME,null,DATENBANK_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+TABLE_MOVIES+"("+
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        COLUMN_TITLE + " TEXT NOT NULL, "+
                        COLUMN_RATING + " REAL DEFAULT 0, "+
                        COLUMN_DURATION + "INTEGER, " +
                        COLUMN_RELEASE + " INTEGER, "+
                        COLUMN_DESC_EN + " TEXT, " +
                        COLUMN_DESC_DE + " TEXT, " +
                        COLUMN_SEQUEL_OF + " INTEGER, " +
                        COLUMN_WATCHED + " INTEGER DEFAULT 0"+
                        ")"
        );

        db.execSQL(
                "CREATE TABLE "+TABLE_SERIES+"("+
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        COLUMN_TITLE + " TEXT, "+
                        COLUMN_RATING + " REAL DEFAULT 0, "+
                        COLUMN_SEASONS + "INTEGER DEFAULT 0, " +
                        COLUMN_RELEASE + " INTEGER, " +
                        COLUMN_DESC_EN + " TEXT, " +
                        COLUMN_DESC_DE + " TEXT " +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE "+TABLE_EPISODES+"("+
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        COLUMN_TITLE + " TEXT, "+
                        COLUMN_DURATION + "INTEGER, " +
                        COLUMN_DESC_EN + " TEXT, " +
                        COLUMN_DESC_DE + " TEXT, " +
                        COLUMN_SEQUEL_OF + " INTEGER " +
                        COLUMN_WATCHED + " INTEGER DEFAULT 0"+
                        ")"
        );

        db.execSQL(
                "CREATE TABLE "+TABLE_SERIES_SEASONS+"("+
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COLUMN_SERIES_ID + " INTEGER,"+
                        COLUMN_SEASON + " INTEGER, " +
                        " FOREIGN KEY ("+COLUMN_SERIES_ID+") REFERENCES "+TABLE_SERIES+"("+COLUMN_ID+")" +
                        ")"
        );


        db.execSQL(
                "CREATE TABLE "+TABLE_SEASON_EPISODES+"("+
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COLUMN_SEASON_ID + " INTEGER, "+
                        COLUMN_EPISODE_ID + " INTEGER," +
                        " FOREIGN KEY ("+COLUMN_SEASON_ID+") REFERENCES "+TABLE_SERIES_SEASONS+"("+COLUMN_ID+")," +
                        " FOREIGN KEY ("+COLUMN_EPISODE_ID+") REFERENCES "+TABLE_EPISODES+"("+COLUMN_ID+")" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_MOVIE_LISTS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT NOT NULL " +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_SERIES_LISTS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT NOT NULL " +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_MOVIE_LISTS_ELEMENTS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_MOVIE_LIST_ID + " INTEGER, " +
                        COLUMN_MOVIE_ID + " INTEGER, " +
                        " FOREIGN KEY ("+COLUMN_MOVIE_LIST_ID+") REFERENCES "+TABLE_MOVIE_LISTS+"("+COLUMN_ID+")," +
                        " FOREIGN KEY ("+COLUMN_MOVIE_ID+") REFERENCES "+TABLE_MOVIES+"("+COLUMN_ID+")" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_SERIES_LISTS_ELEMENTS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_SERIES_LIST_ID + " INTEGER, " +
                        COLUMN_SERIES_ID + " INTEGER, " +
                        " FOREIGN KEY ("+COLUMN_SERIES_LIST_ID+") REFERENCES "+TABLE_SERIES_LISTS+"("+COLUMN_ID+")," +
                        " FOREIGN KEY ("+COLUMN_SERIES_ID+") REFERENCES "+TABLE_SERIES+"("+COLUMN_ID+")" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_ACTORS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT NOT NULL " +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_CREATORS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT NOT NULL " +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_GENRES + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT NOT NULL " +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_DIRECTORS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT NOT NULL " +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_MOVIE_ACTORS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_MOVIE_ID + " INTEGER, " +
                        COLUMN_ACTOR_ID + " INTEGER, " +
                        " FOREIGN KEY ("+COLUMN_MOVIE_ID+") REFERENCES "+TABLE_MOVIES+"("+COLUMN_ID+")," +
                        " FOREIGN KEY ("+COLUMN_ACTOR_ID+") REFERENCES "+TABLE_ACTORS+"("+COLUMN_ID+")" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_MOVIE_GENRES + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_MOVIE_ID + " INTEGER, " +
                        COLUMN_GENRE_ID + " INTEGER, " +
                        " FOREIGN KEY ("+COLUMN_MOVIE_ID+") REFERENCES "+TABLE_MOVIES+"("+COLUMN_ID+")," +
                        " FOREIGN KEY ("+COLUMN_GENRE_ID+") REFERENCES "+TABLE_GENRES+"("+COLUMN_ID+")" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_MOVIE_DIRECTORS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_MOVIE_ID + " INTEGER, " +
                        COLUMN_DIRECTOR_ID + " INTEGER, " +
                        " FOREIGN KEY ("+COLUMN_MOVIE_ID+") REFERENCES "+TABLE_MOVIES+"("+COLUMN_ID+")," +
                        " FOREIGN KEY ("+COLUMN_DIRECTOR_ID+") REFERENCES "+TABLE_DIRECTORS+"("+COLUMN_ID+")" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_SERIES_ACTORS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_SERIES_ID + " INTEGER, " +
                        COLUMN_ACTOR_ID + " INTEGER, " +
                        " FOREIGN KEY ("+COLUMN_SERIES_ID+") REFERENCES "+TABLE_SERIES+"("+COLUMN_ID+")," +
                        " FOREIGN KEY ("+COLUMN_ACTOR_ID+") REFERENCES "+TABLE_ACTORS+"("+COLUMN_ID+")" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_SERIES_CREATORS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_SERIES_ID + " INTEGER, " +
                        COLUMN_CREATOR_ID + " INTEGER, " +
                        " FOREIGN KEY ("+COLUMN_SERIES_ID+") REFERENCES "+TABLE_SERIES+"("+COLUMN_ID+")," +
                        " FOREIGN KEY ("+COLUMN_CREATOR_ID+") REFERENCES "+TABLE_CREATORS+"("+COLUMN_ID+")" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getData(String sql)
    {
        return getData(sql,null);
    }

    public Cursor getData(String sql,String[] params)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor point;
        point = DB.rawQuery(sql,params);
        point.moveToFirst();
        return point;
    }

    public Cursor getAllMovies()
    {
        String sql = "SELECT Title,Rating,Duration,Release,Description_EN,Description_DE,SEQUEL_OF,WATCHED FROM "+TABLE_MOVIES;
        return getData(sql);
    }

    public Cursor getAllSeries()
    {
        String sql = "SELECT * FROM "+TABLE_SERIES;
        return getData(sql);
    }

    public Cursor getMoviesFromList(String listName)
    {
        String sql = "SELECT Title,Rating,Duration,Release,Description_EN,Description_DE,SEQUEL_OF,WATCHED FROM "+TABLE_MOVIES+" a JOIN (SELECT MOVIE_ID FROM "+TABLE_MOVIE_LISTS_ELEMENTS+" a JOIN "+TABLE_MOVIE_LISTS+" b ON a.MOVIE_ID=b.ID WHERE b.NAME=?) b ON a.ID=b.MOVIE_ID";
        return getData(sql,new String[]{listName});
    }

    public Cursor getSeriesFromList(String listName)
    {
        String sql = "SELECT Title,Rating,Seasons,Release,Description_EN,Description_DE FROM "+TABLE_SERIES+" a JOIN (SELECT SERIES_ID FROM "+TABLE_SERIES_LISTS_ELEMENTS+" a JOIN "+TABLE_SERIES_LISTS+" b ON a.SERIES_ID=b.ID WHERE b.NAME=?) b ON a.ID=b.SERIES_ID";
        return getData(sql,new String[]{listName});
    }

    public Cursor getEpisodesFromSeason(int season, String series)
    {
        String sql = "SELECT Title,Duration,Description_EN,Description_DE,Sequel_of FROM "+TABLE_EPISODES+" a JOIN (SELECT EPISODE_ID FROM "+TABLE_SEASON_EPISODES+"a JOIN "+TABLE_SERIES_SEASONS+" b ON a.SEASON_ID=b.ID WHERE b.SEASON=? AND b.SERIES_ID=(SELECT ID FROM "+TABLE_SERIES+" WHERE TITLE=?) b ON a.ID=b.EPISODE_ID";
        return getData(sql,new String[]{String.valueOf(season),series});
    }

    public Cursor getNumberOfSeasons(String series)
    {
        String sql = "SELECT SEASONS FROM "+TABLE_SERIES+" WHERE TITLE=?";
        return getData(sql,new String[]{series});
    }

    public Cursor getCastFromMovie(String movie){return null;}

    public Cursor getDirectorOfMovie(String movie){return null;}

    public Cursor getGenreOfMovie(String movie){return null;}

    public Cursor getCastFromSeries(String series){return null;}

    public Cursor getCreatorOfSeries(String series){return null;}



}
