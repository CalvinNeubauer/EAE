package de.kl.fh.moviestar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Marcus on 14.04.2017.
 */
public class DatabaseManager extends SQLiteOpenHelper{

    //Basics
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CouchPotato.db";
    private static SQLiteDatabase database;
    private static DatabaseManager instance;

    //Table
    private static final String TABLE_MOVIES = "Movies";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_RELEASE = "release";
    public static final String COLUMN_DESC_EN = "description_en";
    private static final String COLUMN_DESC_DE = "description_de";
    private static final String COLUMN_SEQUEL_OF = "sequel_of";
    private static final String COLUMN_WATCHED = "watched";              //Muss 1 oder 0 sein


    private static final String TABLE_SERIES = "Series";
    public static final String COLUMN_SEASONS = "seasons";

    private static final String TABLE_MOVIE_LISTS = "Movie_Lists";
    private static final String TABLE_SERIES_LISTS = "Series_Lists";
    public static final String COLUMN_NAME = "name";


    private static final String TABLE_MOVIE_LISTS_ELEMENTS = "Movie_Lists_Elements";
    private static final String TABLE_SERIES_LISTS_ELEMENTS = "Series_Lists_Elements";
    private static final String COLUMN_MOVIE_LIST_ID = "movie_list_id";
    private static final String COLUMN_SERIES_LIST_ID = "series_list_id";
    private static final String COLUMN_MOVIE_ID = "movie_id";
    private static final String COLUMN_SERIES_ID = "series_id";

    private static final String TABLE_SERIES_SEASONS = "Series_Seasons";
    public static final String COLUMN_SEASON = "Season";
    private static final String TABLE_SEASON_EPISODES = "Season_Episodes";
    public static final String COLUMN_SEASON_ID = "Season_Id";
    private static final String COLUMN_EPISODE_ID = "Episode_Id";
    private static final String TABLE_EPISODES = "Episodes";


    private static final String TABLE_ACTORS = "Actors";
    private static final String TABLE_GENRES = "Genres";
    private static final String TABLE_DIRECTORS = "Directors";
    private static final String TABLE_CREATORS = "Creators";

    private static final String TABLE_MOVIE_ACTORS = "Movie_Actors";
    private static final String TABLE_MOVIE_GENRES = "Movie_Genres";
    private static final String TABLE_MOVIE_DIRECTORS = "Movie_Directors";
    private static final String TABLE_SERIES_ACTORS = "Series_Actors";
    private static final String TABLE_SERIES_GENRES = "Series_Genres";
    private static final String TABLE_SERIES_CREATORS = "Series_Creators";
    private static final String COLUMN_ACTOR_ID = "actor_id";
    private static final String COLUMN_DIRECTOR_ID = "director_id";
    private static final String COLUMN_GENRE_ID = "genre_id";
    private static final String COLUMN_CREATOR_ID = "creator_id";

    public static DatabaseManager getInstance(Context ctx){
        if(instance == null) {
            instance = new DatabaseManager(ctx);
            if(database == null)
                database = instance.getWritableDatabase();
        }
        return instance;
    }

    private DatabaseManager(Context ctx) { super(ctx,DATABASE_NAME,null,DATABASE_VERSION); }

    private void dropAllTables(SQLiteDatabase db)
    {
        // query to obtain the names of all tables in your database
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> tables = new ArrayList<>();

        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            Log.d("Delete", c.getString(0));
            tables.add(c.getString(0));
        }

        // call DROP TABLE on every table name
        for (String table : tables) {
            String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL(dropQuery);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(db != null)
            database = db;
        else
            database = this.getWritableDatabase();
        Log.d("Create","Database");
        db.execSQL(
                "CREATE TABLE "+TABLE_MOVIES+"("+
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COLUMN_TITLE + " TEXT NOT NULL UNIQUE, "+
                        COLUMN_RATING + " REAL DEFAULT 0, "+
                        COLUMN_DURATION + " INTEGER, " +
                        COLUMN_RELEASE + " TEXT, " +
                        COLUMN_DESC_EN + " TEXT, " +
                        COLUMN_DESC_DE + " TEXT, " +
                        COLUMN_SEQUEL_OF + " INTEGER, " +
                        COLUMN_WATCHED + " INTEGER DEFAULT 0"+
                        ")"
        );

        db.execSQL(
                "CREATE TABLE "+TABLE_SERIES+"("+
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        COLUMN_TITLE + " TEXT NOT NULL UNIQUE, "+
                        COLUMN_RATING + " REAL DEFAULT 0, "+
                        COLUMN_SEASONS + " INTEGER DEFAULT 0, " +
                        COLUMN_RELEASE + " TEXT, " +
                        COLUMN_DESC_EN + " TEXT, " +
                        COLUMN_DESC_DE + " TEXT " +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE "+TABLE_EPISODES+"("+
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        COLUMN_TITLE + " TEXT, "+
                        COLUMN_DURATION + " INTEGER, " +
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
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL UNIQUE " +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_SERIES_LISTS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT NOT NULL UNIQUE" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_MOVIE_LISTS_ELEMENTS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
                        COLUMN_NAME + " TEXT NOT NULL UNIQUE" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_CREATORS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT NOT NULL UNIQUE" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_GENRES + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT NOT NULL UNIQUE" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_DIRECTORS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT NOT NULL UNIQUE" +
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
                "CREATE TABLE " + TABLE_SERIES_GENRES + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_SERIES_ID + " INTEGER, " +
                        COLUMN_GENRE_ID + " INTEGER, " +
                        " FOREIGN KEY ("+COLUMN_SERIES_ID+") REFERENCES "+TABLE_SERIES+"("+COLUMN_ID+")," +
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
        
        setBaseData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        onCreate(db);
    }

    //------SET DATA------------------------------------------------------------------------------------------------------------
    public void insertMovieIntoDatabase(String title, double rating, int duration, String release)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_MOVIES + " (TITLE,RATING,DURATION,RELEASE) VALUES ("+
                        "'"+title+"'" + "," +
                        rating + "," +
                        duration + "," +
                        "'"+release+"'"
                        + ")"
        );
    }

    private void insertMovieSequelIntoDatabase(String title, double rating, int duration, String release, String prequel_name)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_MOVIES + " (TITLE,RATING,DURATION,RELEASE,SEQUEL_OF) VALUES ("+
                        "'"+title+"'" + "," +
                        rating + "," +
                        duration + "," +
                        "'"+release+"'" + "," +
                        "(SELECT ID FROM MOVIES WHERE TITLE LIKE '"+prequel_name+"')"
                        + ")"
        );
    }

    private void setMovieToWatched(int id)
    {
        database.execSQL("UPDATE "+TABLE_MOVIES+" SET WATCHED=1 WHERE ID="+id);
    }

    public void insertSeriesIntoDatabase(String title, double rating, int seasons, String release)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_SERIES + " (TITLE,RATING,SEASONS,RELEASE) VALUES ("+
                        "'"+title+"'" + "," +
                        rating + "," +
                        seasons + "," +
                        "'"+release+"'"
                        + ")"
        );
        for(int i=1;i<=seasons;i++) {
            database.execSQL(
                    "INSERT INTO " + TABLE_SERIES_SEASONS + " (SERIES_ID,SEASON) VALUES (" +
                            "(SELECT ID FROM SERIES WHERE TITLE LIKE '"+title+"')" + "," +
                            i
                            + ")"
            );
        }
    }

    private void insertSeasonsIntoDatabase(int series_id, int season_number)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_SERIES_SEASONS + " (SERIES_ID,SEASON) VALUES ("+
                        series_id + "," +
                        season_number
                        + ")"
        );
    }

    public void insertActorIntoDatabase(String name)
    {
        database.execSQL(
                "INSERT OR REPLACE INTO " + TABLE_ACTORS + " (NAME) VALUES ('"+
                        name
                        + "')"
        );
    }

    public void insertCreatorIntoDatabase(String name)
    {
        database.execSQL(
                "INSERT OR REPLACE INTO " + TABLE_CREATORS + " (NAME) VALUES ('"+
                        name
                        + "')"
        );
    }

    public void insertDirectorIntoDatabase(String name)
    {
        database.execSQL(
                "INSERT OR REPLACE INTO " + TABLE_DIRECTORS + " (NAME) VALUES ('"+
                        name
                        + "')"
        );
    }

    public void insertGenreIntoDatabase(String name)
    {
        database.execSQL(
                "INSERT OR REPLACE INTO " + TABLE_GENRES + " (NAME) VALUES ('"+
                        name
                        + "')"
        );
    }

    public void addMovieList(String name)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_MOVIE_LISTS + " (NAME) VALUES ('"+
                        name
                        + "')"
        );
    }

    public void addSeriesList(String name)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_SERIES_LISTS + " (NAME) VALUES ('"+
                        name
                        + "')"
        );
    }

    public void addMovieToList(String listName, String movieName)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_MOVIE_LISTS_ELEMENTS + " (MOVIE_LIST_ID,MOVIE_ID) VALUES ("+
                        "(SELECT ID FROM "+TABLE_MOVIE_LISTS+" WHERE NAME LIKE '"+listName+"')" + "," +
                        "(SELECT ID FROM "+TABLE_MOVIES+" WHERE TITLE LIKE '"+movieName+"')"
                        + ")"
        );
    }

    public void addSeriesToList(String listName, String seriesName)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_SERIES_LISTS_ELEMENTS + " (SERIES_LIST_ID,SERIES_ID) VALUES ("+
                        "(SELECT ID FROM "+TABLE_SERIES_LISTS+" WHERE NAME LIKE '"+listName+"')" + "," +
                        "(SELECT ID FROM "+TABLE_SERIES+" WHERE TITLE LIKE '"+seriesName+"')"
                        + ")"
        );
    }



    public void addMovieToList(String listName, int movieID)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_MOVIE_LISTS_ELEMENTS + " (MOVIE_LIST_ID,MOVIE_ID) VALUES ("+
                        "(SELECT ID FROM "+TABLE_MOVIE_LISTS+" WHERE NAME LIKE '"+listName+"')" + "," +
                        movieID
                        + ")"
        );
    }

    public void addSeriesToList(String listName, int seriesID)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_SERIES_LISTS_ELEMENTS + " (SERIES_LIST_ID,SERIES_ID) VALUES ("+
                        "(SELECT ID FROM "+TABLE_SERIES_LISTS+" WHERE NAME LIKE '"+listName+"')" + "," +
                        seriesID
                        + ")"
        );
    }

    private void addEpisodesToSeason(String series,int season, int num)
    {
        String title;
        for(int i=1;i<=num;i++) {
            title = "S"+season+" E"+i;
            database.execSQL(
                    "INSERT INTO " + TABLE_EPISODES + " (TITLE,DURATION) VALUES ('"+title+"',22)"
            );
        }
        database.execSQL("INSERT INTO " + TABLE_SEASON_EPISODES + " (SEASON_ID,EPISODE_ID) "+
                "SELECT a.ID, b.id FROM (SELECT ID FROM "+TABLE_SERIES_SEASONS+" WHERE SERIES_ID=(SELECT ID FROM "+TABLE_SERIES+" WHERE TITLE LIKE '"+series+"') AND SEASON="+season+") a JOIN "+TABLE_EPISODES+" b WHERE b.TITLE LIKE 'S"+season+"%'"
        );
    }

    public void addDescENToMovie(int id, String description)
    {
        database.execSQL(
                "UPDATE " + TABLE_MOVIES + " SET "+COLUMN_DESC_EN+" = '"+
                        description.replaceAll("'","''")
                        + "' WHERE ID = "+id
        );
    }

    public void addDescENToSeries(int id, String description)
    {
        database.execSQL(
                "UPDATE " + TABLE_SERIES + " SET "+COLUMN_DESC_EN+" = '"+
                        description.replaceAll("'","''")
                        + "' WHERE ID = "+id
        );
    }

    public void addActorsToMovie(int movie_id, int[] actors)
    {
        String sql = "INSERT INTO " + TABLE_MOVIE_ACTORS + " (MOVIE_ID,ACTOR_ID) VALUES ";
        int i=0;
        for (int j: actors) {
            sql +="(" + movie_id + ", " + j + ")";
            if(i<actors.length-1)
                sql+=",";
            i++;
        }
        database.execSQL(sql);
    }

    public void addActorsToSeries(int series_id, int[] actors)
    {
        String sql = "INSERT INTO " + TABLE_SERIES_ACTORS + " (SERIES_ID,ACTOR_ID) VALUES ";
        int i=0;
        for (int j: actors) {
            sql +="(" + series_id + ", " + j + ")";
            if(i<actors.length-1)
                sql+=",";
            i++;
        }
        database.execSQL(sql);
    }

    public void addDirectorsToMovie(int movie_id, int[] directors)
    {
        String sql = "INSERT INTO " + TABLE_MOVIE_DIRECTORS + " (MOVIE_ID,DIRECTOR_ID) VALUES ";
        int i=0;
        for (int j: directors) {
            sql +="(" + movie_id + ", " + j + ")";
            if(i<directors.length-1)
                sql+=",";
            i++;
        }
        database.execSQL(sql);
    }

    public void addCreatorsToSeries(int series_id, int[] creator)
    {
        String sql = "INSERT INTO " + TABLE_SERIES_CREATORS + " (SERIES_ID,CREATOR_ID) VALUES ";
        int i=0;
        for (int j: creator) {
            sql +="(" + series_id + ", " + j + ")";
            if(i<creator.length-1)
                sql+=",";
            i++;
        }
        database.execSQL(sql);
    }

    public void addGenresToMovie(int movie_id, int[] genres)
    {
        String sql = "INSERT INTO " + TABLE_MOVIE_GENRES + " (MOVIE_ID,GENRE_ID) VALUES ";
        int i=0;
        for (int j: genres) {
            sql +="(" + movie_id + ", " + j + ")";
            if(i<genres.length-1)
                sql+=",";
            i++;
        }
        database.execSQL(sql);
    }

    public void addGenresToSeries(int series_id, int[] genres)
    {
        String sql = "INSERT INTO " + TABLE_SERIES_GENRES + " (SERIES_ID,GENRE_ID) VALUES ";
        int i=0;
        for (int j: genres) {
            sql +="(" + series_id + ", " + j + ")";
            if(i<genres.length-1)
                sql+=",";
            i++;
        }
        database.execSQL(sql);
    }

    //------CHANGE DATA---------------------------------------------------------------------------------------------------------

    public void changeMovieListName(int listID, String newName )
    {
        database.execSQL("UPDATE "+TABLE_MOVIE_LISTS+" SET "+COLUMN_NAME+"='"+newName+"' WHERE "+COLUMN_ID+"="+listID);
    }

    public void changeSeriesListName(int listID, String newName )
    {
        database.execSQL("UPDATE "+TABLE_SERIES_LISTS+" SET "+COLUMN_NAME+"='"+newName+"' WHERE "+COLUMN_ID+"="+listID);
    }

    //------DELETE DATA---------------------------------------------------------------------------------------------------------
    public void deleteMovieFromList(int listID, int movieID)
    {
        database.execSQL("DELETE FROM "+TABLE_MOVIE_LISTS_ELEMENTS+" WHERE "+COLUMN_MOVIE_LIST_ID+"="+listID+" AND "+COLUMN_MOVIE_ID+"="+movieID);
    }

    public void deleteMovieList(int listID)
    {
        Cursor c = getData("SELECT ID AS _id, "+COLUMN_MOVIE_ID+" FROM "+TABLE_MOVIE_LISTS_ELEMENTS + " WHERE "+COLUMN_MOVIE_LIST_ID+"="+listID);
        Integer[] IDs = new Integer[c.getCount()];
        int i = 0;
        while (!c.isAfterLast())
        {
            IDs[i] = c.getInt(1);
            i++;
            c.moveToNext();
        }
        c.close();
        for(int j: IDs) {
            deleteMovieFromList(listID, j);
        }
        database.execSQL("DELETE FROM "+TABLE_MOVIE_LISTS+" WHERE "+COLUMN_ID+"="+listID);
    }

    public void deleteSeriesFromList(int listID, int seriesID)
    {
        database.execSQL("DELETE FROM "+TABLE_SERIES_LISTS_ELEMENTS+" WHERE "+COLUMN_SERIES_LIST_ID+"="+listID+" AND "+COLUMN_SERIES_ID+"="+seriesID);
    }

    public void deleteSeriesList(int listID)
    {
        Cursor c = getData("SELECT ID AS _id, "+COLUMN_SERIES_ID+" FROM "+TABLE_SERIES_LISTS_ELEMENTS + " WHERE "+COLUMN_SERIES_LIST_ID+"="+listID);
        Integer[] IDs = new Integer[c.getCount()];
        int i = 0;
        while (!c.isAfterLast())
        {
            IDs[i] = c.getInt(1);
            i++;
            c.moveToNext();
        }
        c.close();
        for(int j: IDs) {
            deleteSeriesFromList(listID, j);
        }
        database.execSQL("DELETE FROM "+TABLE_SERIES_LISTS+" WHERE "+COLUMN_ID+"="+listID);
    }

    public void deleteMovieFromDatabase(int id)
    {
        database.execSQL("DELETE FROM "+TABLE_MOVIE_GENRES+" WHERE "+COLUMN_MOVIE_ID+"="+id);
        database.execSQL("DELETE FROM "+TABLE_MOVIE_DIRECTORS+" WHERE "+COLUMN_MOVIE_ID+"="+id);
        database.execSQL("DELETE FROM "+TABLE_MOVIE_ACTORS+" WHERE "+COLUMN_MOVIE_ID+"="+id);
        database.execSQL("DELETE FROM "+TABLE_MOVIE_LISTS_ELEMENTS+" WHERE "+COLUMN_MOVIE_ID+"="+id);
        database.execSQL("DELETE FROM "+TABLE_MOVIES+" WHERE "+COLUMN_ID+"="+id);
    }

    public void deleteSeriesFromDatabase(int id)
    {
        database.execSQL("DELETE FROM "+TABLE_EPISODES+" WHERE ID IN (SELECT "+COLUMN_EPISODE_ID+" FROM "+TABLE_SEASON_EPISODES+" WHERE "+COLUMN_SERIES_ID+"="+id+")");
        database.execSQL("DELETE FROM "+TABLE_SEASON_EPISODES+" WHERE "+COLUMN_SERIES_ID+"="+id);
        database.execSQL("DELETE FROM "+TABLE_SERIES_SEASONS+" WHERE "+COLUMN_SERIES_ID+"="+id);
        database.execSQL("DELETE FROM "+TABLE_SERIES_ACTORS+" WHERE "+COLUMN_SERIES_ID+"="+id);
        database.execSQL("DELETE FROM "+TABLE_SERIES_CREATORS+" WHERE "+COLUMN_SERIES_ID+"="+id);
        database.execSQL("DELETE FROM "+TABLE_SERIES_LISTS_ELEMENTS+" WHERE "+COLUMN_SERIES_ID+"="+id);
        database.execSQL("DELETE FROM "+TABLE_SERIES+" WHERE "+COLUMN_ID+"="+id);
    }


    //------GET DATA------------------------------------------------------------------------------------------------------------
    public Cursor getData(String sql)
    {
        return getData(sql, new String[]{});
    }

    public Cursor getData(String sql,String[] params)
    {
        Cursor point;
        point = database.rawQuery(sql,params);
        point.moveToFirst();
        return point;
    }


    public Cursor getAllMovies()
    {
        String sql = "SELECT ROWID AS _id, TITLE, RATING, DURATION, RELEASE, DESCRIPTION_EN, DESCRIPTION_DE, SEQUEL_OF, WATCHED FROM "+TABLE_MOVIES+" ORDER BY TITLE";
        return getData(sql);
    }

    public Cursor getAllSeries()
    {
        String sql = "SELECT ROWID AS _id, TITLE, RATING, SEASONS, RELEASE, DESCRIPTION_EN, DESCRIPTION_DE FROM "+TABLE_SERIES+" ORDER BY TITLE";
        return getData(sql);
    }

    public Cursor getMovieLists()
    {
        String sql = "SELECT ID AS _id, NAME, COUNT(MOVIE_LIST_ID) AS MOVIES FROM " + TABLE_MOVIE_LISTS + " LEFT JOIN (SELECT MOVIE_LIST_ID FROM " + TABLE_MOVIE_LISTS_ELEMENTS + ") ON ID=MOVIE_LIST_ID  GROUP BY _id, NAME ORDER BY NAME";
        return getData(sql);
    }

    public Cursor getSeriesLists()
    {
        String sql = "SELECT ID AS _id, NAME, COALESCE(COUNT(SERIES_LIST_ID),0) AS SERIES FROM " + TABLE_SERIES_LISTS + " LEFT JOIN (SELECT SERIES_LIST_ID FROM " + TABLE_SERIES_LISTS_ELEMENTS + ") ON ID=SERIES_LIST_ID GROUP BY _id, NAME ORDER BY NAME";
        return getData(sql);
    }

    public Cursor getMoviesFromList(String listName)
    {
        String sql = "SELECT MOVIE_ID AS _id, a.TITLE AS "+COLUMN_TITLE+", a.RATING AS "+COLUMN_RATING+", a.DURATION AS "+COLUMN_DURATION+", a.RELEASE AS "+COLUMN_RELEASE+", a.DESCRIPTION_EN AS "+COLUMN_DESC_EN+", a.DESCRIPTION_DE AS "+COLUMN_DESC_DE+", a.SEQUEL_OF AS "+COLUMN_SEQUEL_OF+", a.WATCHED AS "+COLUMN_WATCHED+" FROM "+TABLE_MOVIES+" a JOIN (SELECT MOVIE_ID FROM "+TABLE_MOVIE_LISTS_ELEMENTS+" a JOIN "+TABLE_MOVIE_LISTS+" b ON a.MOVIE_LIST_ID=b.ID WHERE UPPER(b.NAME) LIKE ?) b ON a.ID=b.MOVIE_ID";
        return getData(sql,new String[]{listName});
    }

    public Cursor getSeriesFromList(String listName)
    {
        String sql = "SELECT SERIES_ID AS _id, a.TITLE AS "+COLUMN_TITLE+", a.RATING AS "+COLUMN_RATING+", a.SEASONS AS "+COLUMN_SEASONS+", a.RELEASE AS "+COLUMN_RELEASE+", a.DESCRIPTION_EN AS "+COLUMN_DESC_EN+", a.DESCRIPTION_DE AS "+COLUMN_DESC_DE+" FROM "+TABLE_SERIES+" a JOIN (SELECT SERIES_ID FROM "+TABLE_SERIES_LISTS_ELEMENTS+" a JOIN "+TABLE_SERIES_LISTS+" b ON a.SERIES_LIST_ID=b.ID WHERE UPPER(b.NAME) LIKE ?) b ON a.ID=b.SERIES_ID";
        return getData(sql,new String[]{listName});
    }

    public Cursor getMoviesFromList(int listID)
    {
        String sql = "SELECT MOVIE_ID AS _id, a.TITLE AS "+COLUMN_TITLE+", a.RATING AS "+COLUMN_RATING+", a.DURATION AS "+COLUMN_DURATION+", a.RELEASE AS "+COLUMN_RELEASE+", a.DESCRIPTION_EN AS "+COLUMN_DESC_EN+", a.DESCRIPTION_DE AS "+COLUMN_DESC_DE+", a.SEQUEL_OF AS "+COLUMN_SEQUEL_OF+", a.WATCHED AS "+COLUMN_WATCHED+" FROM "+TABLE_MOVIES+" a JOIN (SELECT MOVIE_ID FROM "+TABLE_MOVIE_LISTS_ELEMENTS+" a JOIN "+TABLE_MOVIE_LISTS+" b ON a.MOVIE_LIST_ID=b.ID WHERE b.ID = "+listID+") b ON a.ID=b.MOVIE_ID";
        return getData(sql);
    }

    public Cursor getSeriesFromList(int listID)
    {
        String sql = "SELECT SERIES_ID AS _id, a.TITLE AS "+COLUMN_TITLE+", a.RATING AS "+COLUMN_RATING+", a.SEASONS AS "+COLUMN_SEASONS+", a.RELEASE AS "+COLUMN_RELEASE+", a.DESCRIPTION_EN AS "+COLUMN_DESC_EN+", a.DESCRIPTION_DE AS "+COLUMN_DESC_DE+" FROM "+TABLE_SERIES+" a JOIN (SELECT SERIES_ID FROM "+TABLE_SERIES_LISTS_ELEMENTS+" a JOIN "+TABLE_SERIES_LISTS+" b ON a.SERIES_LIST_ID=b.ID WHERE b.ID = "+listID+") b ON a.ID=b.SERIES_ID";
        return getData(sql);
    }

    public Cursor getSeasonsFromSeries(int series_id)
    {
        String sql = "SELECT a.ID AS _id, b."+COLUMN_SEASON_ID+" AS "+COLUMN_SEASON_ID+", b.Episodes AS Episodes FROM "+TABLE_SERIES_SEASONS+" a JOIN (SELECT "+COLUMN_SEASON_ID+", COUNT("+COLUMN_EPISODE_ID+") AS Episodes FROM "+TABLE_SEASON_EPISODES+" GROUP BY "+COLUMN_SEASON_ID+") b ON b."+COLUMN_SEASON_ID+"=a."+COLUMN_SEASON+" WHERE "+COLUMN_SERIES_ID+"=?";
        return getData(sql,new String[]{String.valueOf(series_id)});
    }

    public Cursor getEpisodesFromSeason(int season_id)
    {
        String sql = "SELECT a.ID AS _id, a.TITLE AS "+COLUMN_TITLE+", a.DURATION AS "+COLUMN_DURATION+", a.DESCRIPTION_EN AS "+COLUMN_DESC_EN+", a.DESCRIPTION_DE AS "+COLUMN_DESC_DE+", a.SEQUEL_OF AS "+COLUMN_SEQUEL_OF+" FROM "+TABLE_EPISODES+" a JOIN (SELECT EPISODE_ID FROM "+TABLE_SEASON_EPISODES+" a JOIN "+TABLE_SERIES_SEASONS+" b ON a.SEASON_ID=b.ID WHERE b.ID=?) b WHERE a.ID=b.EPISODE_ID ORDER BY _id";
        return getData(sql,new String[]{String.valueOf(season_id)});
    }

    public Cursor getNumberOfSeasons(String series)
    {
        String sql = "SELECT "+COLUMN_SEASONS+" FROM "+TABLE_SERIES+" WHERE UPPER(b.TITLE)=?";
        return getData(sql,new String[]{series});
    }

    public Cursor getCastFromMovie(String movie)
    {
        String sql = "SELECT a.ID AS _id, a.NAME AS "+COLUMN_NAME+" FROM "+TABLE_ACTORS+" a JOIN (SELECT a.ACTOR_ID FROM "+TABLE_MOVIE_ACTORS+" a JOIN "+TABLE_MOVIES+" b ON a.MOVIE_ID=b.ID WHERE UPPER(b.TITLE) LIKE ?) b ON b.ACTOR_ID=a.ID";
        return getData(sql,new String[]{movie});
    }

    public Cursor getDirectorOfMovie(String movie)
    {
        String sql = "SELECT a.ID AS _id, a.NAME AS "+COLUMN_NAME+" FROM "+TABLE_DIRECTORS+" a JOIN (SELECT a.DIRECTOR_ID FROM "+TABLE_MOVIE_DIRECTORS+" a JOIN "+TABLE_MOVIES+" b ON a.MOVIE_ID=b.ID WHERE UPPER(b.TITLE) LIKE ?) b ON b.DIRECTOR_ID=a.ID";
        return getData(sql,new String[]{movie});
    }

    public Cursor getGenreOfMovie(String movie)
    {
        String sql = "SELECT a.ID AS _id, a.NAME AS "+COLUMN_NAME+" FROM "+TABLE_GENRES+" a JOIN (SELECT a.GENRE_ID FROM "+TABLE_MOVIE_GENRES+" a JOIN "+TABLE_MOVIES+" b ON a.MOVIE_ID=b.ID WHERE UPPER(b.TITLE) LIKE ?) b ON b.GENRE_ID=a.ID";
        return getData(sql,new String[]{movie});
    }

    public Cursor getGenreOfSeries(String series)
    {
        String sql = "SELECT a.ID AS _id, a.NAME AS "+COLUMN_NAME+" FROM "+TABLE_GENRES+" a JOIN (SELECT a.GENRE_ID FROM "+TABLE_SERIES_GENRES+" a JOIN "+TABLE_SERIES+" b ON a.SERIES_ID=b.ID WHERE UPPER(b.TITLE) LIKE ?) b ON b.GENRE_ID=a.ID";
        return getData(sql,new String[]{series});
    }

    public Cursor getCastFromSeries(String series)
    {
        String sql = "SELECT a.ID AS _id, a.NAME AS "+COLUMN_NAME+" FROM "+TABLE_ACTORS+" a JOIN (SELECT a.ACTOR_ID FROM "+TABLE_SERIES_ACTORS+" a JOIN "+TABLE_SERIES+" b ON a.SERIES_ID=b.ID WHERE UPPER(b.TITLE) LIKE ?) b ON b.ACTOR_ID=a.ID";
        return getData(sql,new String[]{series});
    }

    public Cursor getCreatorOfSeries(String series)
    {
        String sql = "SELECT a.ID AS _id, a.NAME AS "+COLUMN_NAME+" FROM "+TABLE_CREATORS+" a JOIN (SELECT a.CREATOR_ID FROM "+TABLE_SERIES_CREATORS+" a JOIN "+TABLE_SERIES+" b ON a.SERIES_ID=b.ID WHERE UPPER(b.TITLE) LIKE ?) b ON b.CREATOR_ID=a.ID";
        return getData(sql,new String[]{series});
    }

    public Integer[] getMovieIDsByFilter(String title, String actor, String genre, String director)      //Search Terms: Actors,Genre,Director,Title
    {
        int arrays = 0;
        Integer[] ret;
        Integer[][] a = new Integer[4][0];
        if(title!=null) {
            arrays++;
            a[arrays-1]=removeDoubles(getMovieIDsByTitle(title.toUpperCase()));
        }
        if(actor!=null){
            arrays++;
            a[arrays-1]=removeDoubles(getMovieIDsByActor(actor.toUpperCase()));
        }
        if(genre!=null){
            arrays++;
            a[arrays-1]=removeDoubles(getMovieIDsByGenre(genre.toUpperCase()));
        }
        if(director!=null){
            arrays++;
            a[arrays-1]=removeDoubles(getMovieIDsByDirector(director.toUpperCase()));
        }

        ret = a[0];
        if(arrays==1)
        {
            return ret;
        }
        for(int i=1;i<arrays;i++)
        {
            ret=getIntegerIntersect(ret,a[i]);
        }
        return ret;
    }

    public Integer[] getMovieIDsByTitle(String title)
    {
        String sql = "SELECT ID AS _id  FROM "+TABLE_MOVIES + " a WHERE UPPER(a.TITLE) LIKE ?";
        Cursor c = getData(sql, new String[]{title});
        Integer[] ret = new Integer[c.getCount()];
        int i = 0;
        while (!c.isAfterLast())
        {
            ret[i] = c.getInt(0);
            i++;
            c.moveToNext();
        }
        c.close();
        return ret;
    }

    public Integer[] getMovieIDsByActor(String actor)
    {
        String sql = "SELECT a.MOVIE_ID AS _id  FROM "+TABLE_MOVIE_ACTORS+" a JOIN "+TABLE_ACTORS+" b ON a.ACTOR_ID=b.ID WHERE UPPER(b.NAME) LIKE ?";
        Cursor c = getData(sql,new String[]{actor});
        Integer[] ret = new Integer[c.getCount()];
        int i = 0;
        while (!c.isAfterLast())
        {
            ret[i] = c.getInt(0);
            i++;
            c.moveToNext();
        }
        c.close();
        return ret;
    }

    public Integer[] getMovieIDsByDirector(String director)
    {
        String sql = "SELECT a.MOVIE_ID AS _id  FROM "+TABLE_MOVIE_DIRECTORS+" a JOIN "+TABLE_DIRECTORS+" b ON a.DIRECTOR_ID=b.ID UPPER(b.NAME) LIKE ?";
        Cursor c = getData(sql,new String[]{director});
        Integer[] ret = new Integer[c.getCount()];
        int i = 0;
        while (!c.isAfterLast())
        {
            ret[i] = c.getInt(0);
            i++;
            c.moveToNext();
        }
        c.close();
        return ret;
    }

    public Integer[] getMovieIDsByGenre(String genre)
    {
        String sql = "SELECT a.MOVIE_ID AS _id FROM "+TABLE_MOVIE_GENRES+" a JOIN "+TABLE_GENRES+" b ON a.GENRE_ID=b.ID WHERE UPPER(b.NAME) LIKE ?";
        Cursor c = getData(sql,new String[]{genre});
        Integer[] ret = new Integer[c.getCount()];
        int i = 0;
        while (!c.isAfterLast())
        {
            ret[i] = c.getInt(0);
            i++;
            c.moveToNext();
        }
        c.close();
        return ret;
    }


    public Cursor getMovieDataByID(int id)
    {
        String sql = "SELECT "+COLUMN_ID+" AS _id, "+COLUMN_TITLE+", "+COLUMN_RATING+", "+COLUMN_DURATION+", "+COLUMN_RELEASE+", "+COLUMN_DESC_EN+", "+COLUMN_DESC_DE+", "+COLUMN_SEQUEL_OF+", "+COLUMN_WATCHED+" FROM "+TABLE_MOVIES+" WHERE "+COLUMN_ID+"=?";
        Cursor c = getData(sql,new String[]{String.valueOf(id)});
        return c;
    }

    public Cursor getSeriesDataByID(int id)
    {
        String sql = "SELECT "+COLUMN_ID+" AS _id, "+COLUMN_TITLE+", "+COLUMN_RATING+", "+COLUMN_SEASONS+", "+COLUMN_RELEASE+", "+COLUMN_DESC_EN+", "+COLUMN_DESC_DE+" FROM "+TABLE_SERIES+" WHERE "+COLUMN_ID+"=?";
        Cursor c = getData(sql,new String[]{String.valueOf(id)});
        return c;
    }

    public Cursor getEpisodeDataByID(int id)
    {
        String sql = "SELECT "+COLUMN_ID+" AS _id, "+COLUMN_TITLE+", "+COLUMN_DURATION+", "+COLUMN_DESC_EN+", "+COLUMN_DESC_DE+", "+COLUMN_SEQUEL_OF+" FROM "+TABLE_EPISODES+" WHERE "+COLUMN_ID+"=?";
        Cursor c = getData(sql,new String[]{String.valueOf(id)});
        return c;
    }

    public Cursor getMovieListID(String name)
    {
        String sql = "SELECT ID as _id FROM "+TABLE_MOVIE_LISTS+" WHERE NAME LIKE ?";
        return getData(sql, new String[]{name});
    }

    public Cursor getSeriesListID(String name)
    {
        String sql = "SELECT ID as _id FROM "+TABLE_SERIES_LISTS+" WHERE NAME LIKE ?";
        return getData(sql, new String[]{name});
    }

    public Integer[] getSeriesIDsByFilter(String title, String creator, String actor)     //Search Terms: Actors,Creators,Title
    {
        int arrays = 0;
        Integer[] ret;
        Integer[][] a = new Integer[4][0];
        if(title!=null) {
            arrays++;
            a[arrays-1]=removeDoubles(getSeriesIDsByTitle(title.toUpperCase()));
        }
        if(actor!=null){
            arrays++;
            a[arrays-1]=removeDoubles(getSeriesIDsByActor(actor.toUpperCase()));
        }
        if(creator!=null){
            arrays++;
            a[arrays-1]=removeDoubles(getSeriesIDByCreator(creator.toUpperCase()));
        }

        ret = a[0];
        if(arrays==1)
        {
            return ret;
        }
        for(int i=1;i<arrays;i++)
        {
            ret=getIntegerIntersect(ret,a[i]);
        }
        return ret;
    }

    public Integer[] getSeriesIDsByTitle(String title)
    {
        String sql = "SELECT ID AS _id FROM "+TABLE_SERIES+" a WHERE UPPER(a.TITLE) LIKE ?";
        Cursor c = getData(sql,new String[]{title});
        Integer[] ret = new Integer[c.getCount()];
        int i = 0;
        while (!c.isAfterLast())
        {
            ret[i] = c.getInt(0);
            i++;
            c.moveToNext();
        }
        c.close();
        return ret;
    }

    public Integer[] getSeriesIDsByActor(String actor)
    {
        String sql = "SELECT a.SERIES_ID AS _id FROM "+TABLE_SERIES_ACTORS+" a JOIN "+TABLE_ACTORS+" b ON a.ACTOR_ID=b.ID WHERE UPPER(b.NAME) LIKE ?";
        Cursor c = getData(sql,new String[]{actor});
        Integer[] ret = new Integer[c.getCount()];
        int i = 0;
        while (!c.isAfterLast())
        {
            ret[i] = c.getInt(0);
            i++;
            c.moveToNext();
        }
        c.close();
        return ret;
    }

    public Integer[] getSeriesIDByCreator(String creator)
    {
        String sql = "SELECT a.SERIES_ID AS _id FROM "+TABLE_SERIES_CREATORS+" a JOIN "+TABLE_CREATORS+" b ON a.CREATOR_ID=b.ID WHERE UPPER(b.NAME) LIKE ?";
        Cursor c = getData(sql,new String[]{creator});
        Integer[] ret = new Integer[c.getCount()];
        int i = 0;
        while (!c.isAfterLast())
        {
            ret[i] = c.getInt(0);
            i++;
            c.moveToNext();
        }
        c.close();
        return ret;
    }


    public Integer[] getIntegerIntersect(Integer[] a, Integer [] b)
    {
        Integer[] ret = new Integer[0];
        Integer[] tmp;
        for(int i=0;i<a.length;i++)
        {
            for (int j = 0; j < b.length; j++)
            {
                if(a[i].equals(b[j]))
                {
                    tmp = ret;
                    ret = new Integer[tmp.length+1];
                    for(int k=0;k<tmp.length;k++)
                    {
                        ret[k]=ret[k];
                    }
                    ret[ret.length-1] = a[i];
                }
            }
        }
        tmp = null;
        return ret;
    }

    public Integer[] removeDoubles(Integer[] a)
    {
        Set<Integer> set = new HashSet<Integer>();
        for (int i:a) {
            set.add(i);
        }
        Integer[] ret = new Integer[set.size()];
        int i=0;
        Iterator it = set.iterator();
        while (it.hasNext())
        {
            ret[i]=(Integer)it.next();
        }
        return ret;
    }

    private int[] cursorToIntArray(Cursor c)
    {
        int i;
        int[] ret = new int[c.getCount()];
        i = 0;
        while (!c.isAfterLast())
        {
            ret[i] = c.getInt(0);
            i++;
            c.moveToNext();
        }
        c.close();
        return  ret;
    }
    
    public int[] getActorIDs(String[] actors)
    {
        int i=0;
        String sql = "SELECT ID AS _id FROM "+TABLE_ACTORS+" WHERE NAME IN (";
        for (String s:actors){
            sql+="'"+s+"'";
            if(i<actors.length-1)
                sql+=",";
            i++;
        }
        sql+=")";
        return cursorToIntArray(getData(sql));
    }

    public int[] getCreatorIDs(String[] creators)
    {
        int i=0;
        String sql = "SELECT ID AS _id FROM "+TABLE_CREATORS+" WHERE NAME IN (";
        for (String s:creators){
            sql+="'"+s+"'";
            if(i<creators.length-1)
                sql+=",";
            i++;
        }
        sql+=")";
        return cursorToIntArray(getData(sql));
    }

    public int[] getDirectorIDs(String[] directors)
    {
        int i=0;
        String sql = "SELECT ID AS _id FROM "+TABLE_DIRECTORS+" WHERE NAME IN (";
        for (String s:directors){
            sql+="'"+s+"'";
            if(i<directors.length-1)
                sql+=",";
            i++;
        }
        sql+=")";
        return cursorToIntArray(getData(sql));
    }

    public int[] getGenreIDs(String[] genres)
    {
        int i=0;
        String sql = "SELECT ID AS _id FROM "+TABLE_GENRES+" WHERE NAME IN (";
        for (String s:genres){
            sql+="'"+s+"'";
            if(i<genres.length-1)
                sql+=",";
            i++;
        }
        sql+=")";
        return cursorToIntArray(getData(sql));
    }
    
    private void setBaseData()
    {
        //Base Movie Collection
        insertMovieIntoDatabase("Batman Begins", 4.7, 140, "16062005");
        insertMovieSequelIntoDatabase("Batman - The Dark Knight", 4.5, 152, "21082008", "Batman Begins");
        insertMovieSequelIntoDatabase("Batman - The Dark Knight Rises", 4.8, 164, "26072012", "Batman - The Dark Knight");
        insertMovieIntoDatabase("Sherlock Holmes", 3.4, 128, "28012010");
        insertMovieSequelIntoDatabase("Sherlock Holmes: A Game of Shadows", 3.6, 128, "22122011", "Sherlock Holmes");
        insertMovieIntoDatabase("Captain America: The First Avenger", 4.4, 124, "18082011");
        insertMovieSequelIntoDatabase("Captain America: The Winter Soldier", 4.8, 136, "27032014", "Captain America: The First Avenger");
        insertMovieSequelIntoDatabase("Captain America: Civil War", 4.3, 147, "28042016", "Captain America: The Winter Soldier");
        insertMovieIntoDatabase("Guardians of the Galaxy", 4.2, 121, "28082014");
        insertMovieSequelIntoDatabase("Guardians of the Galaxy Vol. 2", 4.5, 136, "24042017", "Guardians of the Galaxy");
        insertMovieIntoDatabase("Doctor Strange", 4.2, 115, "27102016");
        insertMovieIntoDatabase("The Avengers", 4.1, 143, "26042012");
        insertMovieSequelIntoDatabase("Avengers: Age of Ultron", 4.2, 141, "23042015", "The Avengers");
        insertMovieIntoDatabase("Thor", 3.9, 115, "28042011");
        insertMovieSequelIntoDatabase("Thor: The Dark World", 3.2, 112, "31102013", "Thor");
        insertMovieIntoDatabase("Deadpool", 4.9, 108, "11012016");
        insertMovieIntoDatabase("Ant-Man", 4.8, 117, "23072015");
        insertMovieIntoDatabase("The Incredible Hulk", 3.8, 112, "10072008");
        insertMovieIntoDatabase("Iron Man", 4.6, 126, "01052008");
        insertMovieSequelIntoDatabase("Iron Man 2", 4.5, 124, "06052010", "Iron Man");
        insertMovieSequelIntoDatabase("Iron Man 3", 4.7, 130, "01052013", "Iron Man 2");

        //Movie Descriptions
        addDescENToMovie(getMovieIDsByTitle("Batman Begins")[0],"After training with his mentor, Batman begins his fight to free crime-ridden Gotham City from the corruption that Scarecrow and the League of Shadows have cast upon it.");
        addDescENToMovie(getMovieIDsByTitle("Batman - The Dark Knight")[0],"When the menace known as the Joker emerges from his mysterious past, he wreaks havoc and chaos on the people of Gotham, the Dark Knight must accept one of the greatest psychological and physical tests of his ability to fight injustice.");
        addDescENToMovie(getMovieIDsByTitle("Batman - The Dark Knight Rises")[0],"Eight years after the Joker's reign of anarchy, the Dark Knight, with the help of the enigmatic Selina, is forced from his imposed exile to save Gotham City, now on the edge of total annihilation, from the brutal guerrilla terrorist Bane.");
        addDescENToMovie(getMovieIDsByTitle("Sherlock Holmes")[0],"Detective Sherlock Holmes and his stalwart partner Watson engage in a battle of wits and brawn with a nemesis whose plot is a threat to all of England.");
        addDescENToMovie(getMovieIDsByTitle("Sherlock Holmes: A Game of Shadows")[0],"Sherlock Holmes and his sidekick Dr. Watson join forces to outwit and bring down their fiercest adversary, Professor Moriarty.");
        addDescENToMovie(getMovieIDsByTitle("Captain America: The First Avenger")[0],"Steve Rogers, a rejected military soldier transforms into Captain America after taking a dose of a \"Super-Soldier serum\". But being Captain America comes at a price as he attempts to take down a war monger and a terrorist organization.");
        addDescENToMovie(getMovieIDsByTitle("Captain America: The Winter Soldier")[0],"As Steve Rogers struggles to embrace his role in the modern world, he teams up with a fellow Avenger and S.H.I.E.L.D agent, Black Widow, to battle a new threat from history: an assassin known as the Winter Soldier.");
        addDescENToMovie(getMovieIDsByTitle("Captain America: Civil War")[0],"Political interference in the Avengers' activities causes a rift between former allies Captain America and Iron Man.");
        addDescENToMovie(getMovieIDsByTitle("Guardians of the Galaxy")[0],"A group of intergalactic criminals are forced to work together to stop a fanatical warrior from taking control of the universe.");
        addDescENToMovie(getMovieIDsByTitle("Guardians of the Galaxy Vol. 2")[0],"The Guardians must fight to keep their newfound family together as they unravel the mystery of Peter Quill's true parentage.");
        addDescENToMovie(getMovieIDsByTitle("Doctor Strange")[0],"While on a journey of physical and spiritual healing, a brilliant neurosurgeon is drawn into the world of the mystic arts.");
        addDescENToMovie(getMovieIDsByTitle("The Avengers")[0],"Earth's mightiest heroes must come together and learn to fight as a team if they are to stop the mischievous Loki and his alien army from enslaving humanity.");
        addDescENToMovie(getMovieIDsByTitle("Avengers: Age of Ultron")[0],"When Tony Stark and Bruce Banner try to jump-start a dormant peacekeeping program called Ultron, things go horribly wrong and it's up to Earth's mightiest heroes to stop the villainous Ultron from enacting his terrible plan.");
        addDescENToMovie(getMovieIDsByTitle("Thor")[0],"The powerful but arrogant god Thor is cast out of Asgard to live amongst humans in Midgard (Earth), where he soon becomes one of their finest defenders.");
        addDescENToMovie(getMovieIDsByTitle("Thor: The Dark World")[0],"When Dr. Jane Foster gets cursed with a powerful entity known as the Aether, Thor is heralded of the cosmic event known as the Convergence and the genocidal Dark Elves.");
        addDescENToMovie(getMovieIDsByTitle("Deadpool")[0],"A fast-talking mercenary with a morbid sense of humor is subjected to a rogue experiment that leaves him with accelerated healing powers and a quest for revenge.");
        addDescENToMovie(getMovieIDsByTitle("Ant-Man")[0],"Armed with a super-suit with the astonishing ability to shrink in scale but increase in strength, cat burglar Scott Lang must embrace his inner hero and help his mentor, Dr. Hank Pym, plan and pull off a heist that will save the world.");
        addDescENToMovie(getMovieIDsByTitle("The Incredible Hulk")[0],"Bruce Banner, a scientist on the run from the U.S. Government, must find a cure for the monster he emerges whenever he loses his temper.");
        addDescENToMovie(getMovieIDsByTitle("Iron Man")[0],"After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.");
        addDescENToMovie(getMovieIDsByTitle("Iron Man 2")[0],"With the world now aware of his identity as Iron Man, Tony Stark must contend with both his declining health and a vengeful mad man with ties to his father's legacy.");
        addDescENToMovie(getMovieIDsByTitle("Iron Man 3")[0],"When Tony Stark's world is torn apart by a formidable terrorist called the Mandarin, he starts an odyssey of rebuilding and retribution.");

        //Genres
        insertGenreIntoDatabase("Action");
        insertGenreIntoDatabase("Adventure");
        insertGenreIntoDatabase("Sci-Fi");
        insertGenreIntoDatabase("Fantasy");
        insertGenreIntoDatabase("Crime");
        insertGenreIntoDatabase("Drama");
        insertGenreIntoDatabase("Romance");
        insertGenreIntoDatabase("Comedy");
        insertGenreIntoDatabase("Thriller");

        //Actors
        insertActorIntoDatabase("Christian Bale");
        insertActorIntoDatabase("Michael Caine");
        insertActorIntoDatabase("Liam Neeson");
        insertActorIntoDatabase("Katie Holmes");
        insertActorIntoDatabase("Gary Oldman");
        insertActorIntoDatabase("Cillian Murphy");
        insertActorIntoDatabase("Morgan Freeman");
        insertActorIntoDatabase("Heath Ledger");
        insertActorIntoDatabase("Aaron Eckhart");
        insertActorIntoDatabase("Maggie Gyllenhaal");
        insertActorIntoDatabase("Tom Hardy");
        insertActorIntoDatabase("Anne Hathaway");
        insertActorIntoDatabase("Joseph Gordon-Levitt");
        insertActorIntoDatabase("Marion Cotillard");
        insertActorIntoDatabase("Robert Downey Jr.");
        insertActorIntoDatabase("Jude Law");
        insertActorIntoDatabase("Mark Strong");
        insertActorIntoDatabase("Rachel McAdams");
        insertActorIntoDatabase("Eddie Marsan");
        insertActorIntoDatabase("Noomi Rapace");
        insertActorIntoDatabase("Jared Harris");
        insertActorIntoDatabase("Stephen Fry");
        insertActorIntoDatabase("Chris Evans");
        insertActorIntoDatabase("Hayley Atwell");
        insertActorIntoDatabase("Sebastian Stan");
        insertActorIntoDatabase("Tommy Lee Jones");
        insertActorIntoDatabase("Hugo Weaving");
        insertActorIntoDatabase("Dominic Cooper");
        insertActorIntoDatabase("Richard Armitage");
        insertActorIntoDatabase("Stanley Tucci");
        insertActorIntoDatabase("Samuel L. Jackson");
        insertActorIntoDatabase("Toby Jones");
        insertActorIntoDatabase("Neal McDonough");
        insertActorIntoDatabase("Derek Luke");
        insertActorIntoDatabase("Kenneth Choi");
        insertActorIntoDatabase("JJ Feild");
        insertActorIntoDatabase("Scarlett Johansson");
        insertActorIntoDatabase("Robert Redford");
        insertActorIntoDatabase("Anthony Mackie");
        insertActorIntoDatabase("Cobie Smulders");
        insertActorIntoDatabase("Don Cheadle");
        insertActorIntoDatabase("Jeremy Renner");
        insertActorIntoDatabase("Chadwick Boseman");
        insertActorIntoDatabase("Paul Bettany");
        insertActorIntoDatabase("Elizabeth Olsen");
        insertActorIntoDatabase("Paul Rudd");
        insertActorIntoDatabase("Emily VanCamp");
        insertActorIntoDatabase("Tom Holland");
        insertActorIntoDatabase("Daniel Brühl");
        insertActorIntoDatabase("Frank Grillo");
        insertActorIntoDatabase("William Hurt");
        insertActorIntoDatabase("Martin Freeman");
        insertActorIntoDatabase("Marisa Tomei");
        insertActorIntoDatabase("John Slattery");
        insertActorIntoDatabase("Chris Pratt");
        insertActorIntoDatabase("Zoe Saldana");
        insertActorIntoDatabase("Dave Bautista");
        insertActorIntoDatabase("Vin Diesel");
        insertActorIntoDatabase("Bradley Cooper");
        insertActorIntoDatabase("Lee Pace");
        insertActorIntoDatabase("Michael Rooker");
        insertActorIntoDatabase("Karen Gillan");
        insertActorIntoDatabase("Benicio Del Toro");
        insertActorIntoDatabase("John C. Reilly");
        insertActorIntoDatabase("Pom Klementieff");
        insertActorIntoDatabase("Kurt Russel");
        insertActorIntoDatabase("Sylvester Stallone");
        insertActorIntoDatabase("Benedict Cumberbatch");
        insertActorIntoDatabase("Chiwetel Ejiofor");
        insertActorIntoDatabase("Benedict Wong");
        insertActorIntoDatabase("Mads Mikkelsen");
        insertActorIntoDatabase("Tilda Swinton");
        insertActorIntoDatabase("Mark Ruffalo");
        insertActorIntoDatabase("Chris Hemsworth");
        insertActorIntoDatabase("Tom Hiddleston");
        insertActorIntoDatabase("Clark Gregg");
        insertActorIntoDatabase("Stellan Skarsgard");
        insertActorIntoDatabase("Gwyneth Paltrow");
        insertActorIntoDatabase("James Spader");
        insertActorIntoDatabase("Aaron Taylor-Johnson");
        insertActorIntoDatabase("Natalie Portman");
        insertActorIntoDatabase("Anthony Hopkins");
        insertActorIntoDatabase("Kat Dennings");
        insertActorIntoDatabase("Com Feore");
        insertActorIntoDatabase("Idris Elba");
        insertActorIntoDatabase("Ray Stevenson");
        insertActorIntoDatabase("Tadanobu Asano");
        insertActorIntoDatabase("Josh Dallas");
        insertActorIntoDatabase("Jaimie Alexander");
        insertActorIntoDatabase("Rene Russo");
        insertActorIntoDatabase("Christopher Eccleston");
        insertActorIntoDatabase("Zachary Levi");
        insertActorIntoDatabase("Adewale Akinnuoye-Agbaje");
        insertActorIntoDatabase("Ryan Reynolds");
        insertActorIntoDatabase("Karan Sodi");
        insertActorIntoDatabase("Ed Skrein");
        insertActorIntoDatabase("Michael Benyaer");
        insertActorIntoDatabase("Stefan Kapicic");
        insertActorIntoDatabase("Brianna Hildebrand");
        insertActorIntoDatabase("TJ Miller");
        insertActorIntoDatabase("Michael Douglas");
        insertActorIntoDatabase("Evangeline Lilly");
        insertActorIntoDatabase("Corey Stoll");
        insertActorIntoDatabase("Bobby Cannavale");
        insertActorIntoDatabase("Judy Greer");
        insertActorIntoDatabase("Abby Ryder Fortson");
        insertActorIntoDatabase("Michael Pena");
        insertActorIntoDatabase("John Atwell");
        insertActorIntoDatabase("Terrence Howard");
        insertActorIntoDatabase("Jeff Bridges");
        insertActorIntoDatabase("Leslie Bibb");
        insertActorIntoDatabase("Shaun Toub");
        insertActorIntoDatabase("Faran Tahir");
        insertActorIntoDatabase("Jon Favreau");
        insertActorIntoDatabase("Sam Rockwell");
        insertActorIntoDatabase("Mickey Rourke");
        insertActorIntoDatabase("Garry Shandling");
        insertActorIntoDatabase("Kate Mara");
        insertActorIntoDatabase("Guy Pearce");
        insertActorIntoDatabase("Rebecca Hall");
        insertActorIntoDatabase("Ben Kingsley");
        insertActorIntoDatabase("James Badge Dale");
        insertActorIntoDatabase("Stephanie Szostak");

        //Movie Directors
        insertDirectorIntoDatabase("Cristopher Nolan");
        insertDirectorIntoDatabase("Guy Ritchie");
        insertDirectorIntoDatabase("Joe Johnston");
        insertDirectorIntoDatabase("Anthony Russo");
        insertDirectorIntoDatabase("Joe Russo");
        insertDirectorIntoDatabase("James Gunn");
        insertDirectorIntoDatabase("Scott Derrickson");
        insertDirectorIntoDatabase("Joss Whedon");
        insertDirectorIntoDatabase("Kenneth Branagh");
        insertDirectorIntoDatabase("Alan Taylor");
        insertDirectorIntoDatabase("Tim Miller");
        insertDirectorIntoDatabase("Peyton Reed");
        insertDirectorIntoDatabase("Louis Leterrier");
        insertDirectorIntoDatabase("Jon Favreau");
        insertDirectorIntoDatabase("Shane Black");

        //Movie Actors
        addActorsToMovie(getMovieIDsByTitle("Batman Begins")[0],getActorIDs(new String[]{"Christian Bale", "Michael Caine", "Liam Neeson", "Katie Holmes", "Gary Oldman", "Cillian Murphy", "Morgan Freeman"}));
        addActorsToMovie(getMovieIDsByTitle("Batman - The Dark Knight")[0],getActorIDs(new String[]{"Christian Bale","Michael Caine","Heath Ledger","Aaron Eckhart","Gary Oldman","Maggie Gyllenhaal","Morgan Freeman"}));
        addActorsToMovie(getMovieIDsByTitle("Batman - The Dark Knight Rises")[0],getActorIDs(new String[]{"Christian Bale","Michael Caine","Tom Hardy","Anne Hathaway","Gary Oldman","Joseph Gordon-Levitt","Morgan Freeman","Marion Cotillard"}));
        addActorsToMovie(getMovieIDsByTitle("Sherlock Holmes")[0],getActorIDs(new String[]{"Robert Downey Jr.","Jude Law","Mark Strong","Rachel McAdams","Eddie Marsan"}));
        addActorsToMovie(getMovieIDsByTitle("Sherlock Holmes: A Game of Shadows")[0],getActorIDs(new String[]{"Robert Downey Jr.","Jude Law","Noomi Rapace","Rachel McAdams","Jared Harris","Stephen Fry","Eddie Marsan"}));
        addActorsToMovie(getMovieIDsByTitle("Captain America: The First Avenger")[0],getActorIDs(new String[]{"Chris Evans","Hayley Atwell","Sebastian Stan","Tommy Lee Jones","Hugo Weaving","Dominic Cooper","Richard Armitage","Stanley Tucci","Samuel L. Jackson","Toby Jones","Neal McDonough","Derek Luke","Kenneth Choi","JJ Feild"}));
        addActorsToMovie(getMovieIDsByTitle("Captain America: The Winter Soldier")[0],getActorIDs(new String[]{"Chris Evans","Samuel L. Jackson","Scarlett Johansson","Robert Redford","Sebastian Stan","Anthony Mackie","Cobie Smulders"}));
        addActorsToMovie(getMovieIDsByTitle("Captain America: Civil War")[0],getActorIDs(new String[]{"Chris Evans","Robert Downey Jr.","Scarlett Johansson","Sebastian Stan","Anthony Mackie","Don Cheadle","Jeremy Renner","Chadwick Boseman","Paul Bettany","Elizabeth Olsen","Paul Rudd","Emily VanCamp","Tom Holland","Daniel Brühl","Frank Grillo","William Hurt","Martin Freeman","Marisa Tomei","John Slattery"}));
        addActorsToMovie(getMovieIDsByTitle("Guardians of the Galaxy")[0],getActorIDs(new String[]{"Chris Pratt","Zoe Saldana","Dave Bautista","Vin Diesel","Bradley Cooper","Lee Pace","Michael Rooker","Karen Gillan","Benicio Del Toro","John C. Reilly"}));
        addActorsToMovie(getMovieIDsByTitle("Guardians of the Galaxy Vol. 2")[0],getActorIDs(new String[]{"Chris Pratt","Zoe Saldana","Dave Bautista","Vin Diesel","Bradley Cooper","Pom Klementieff","Michael Rooker","Karen Gillan","Kurt Russel","Sylvester Stallone"}));
        addActorsToMovie(getMovieIDsByTitle("Doctor Strange")[0],getActorIDs(new String[]{"Benedict Cumberbatch","Chiwetel Ejiofor","Rachel McAdams","Benedict Wong","Mads Mikkelsen","Tilda Swinton"}));
        addActorsToMovie(getMovieIDsByTitle("The Avengers")[0],getActorIDs(new String[]{"Robert Downey Jr.","Chris Evans","Mark Ruffalo","Chris Hemsworth","Scarlett Johansson","Jeremy Renner","Tom Hiddleston","Clark Gregg","Cobie Smulders","Stellan Skarsgard","Samuel L. Jackson","Gwyneth Paltrow","Paul Bettany","Hayley Atwell"}));
        addActorsToMovie(getMovieIDsByTitle("Avengers: Age of Ultron")[0],getActorIDs(new String[]{"Robert Downey Jr.","Chris Evans","Mark Ruffalo","Chris Hemsworth","Scarlett Johansson","Jeremy Renner","James Spader","Don Cheadle","Aaron Taylor-Johnson","Elizabeth Olsen","Samuel L. Jackson","Gwyneth Paltrow","Paul Bettany","Anthony Mackie","Cobie Smulders","Hayley Atwell"}));
        addActorsToMovie(getMovieIDsByTitle("Thor")[0],getActorIDs(new String[]{"Chris Hemsworth","Natalie Portman","Tom Hiddleston","Anthony Hopkins","Stellan Skarsgard","Kat Dennings","Clark Gregg","Com Feore","Idris Elba","Ray Stevenson","Tadanobu Asano","Josh Dallas","Jaimie Alexander","Rene Russo"}));
        addActorsToMovie(getMovieIDsByTitle("Thor: The Dark World")[0],getActorIDs(new String[]{"Chris Hemsworth","Natalie Portman","Tom Hiddleston","Anthony Hopkins","Christopher Eccleston","Jaimie Alexander","Zachary Levi","Ray Stevenson","Tadanobu Asano","Idris Elba","Rene Russo","Adewale Akinnuoye-Agbaje","Kat Dennings","Stellan Skarsgard"}));
        addActorsToMovie(getMovieIDsByTitle("Deadpool")[0],getActorIDs(new String[]{"Ryan Reynolds","Karan Sodi","Ed Skrein","Michael Benyaer","Stefan Kapicic","Brianna Hildebrand","TJ Miller"}));
        addActorsToMovie(getMovieIDsByTitle("Ant-Man")[0],getActorIDs(new String[]{"Paul Rudd","Michael Douglas","Evangeline Lilly","Corey Stoll","Bobby Cannavale","Anthony Mackie","Judy Greer","Abby Ryder Fortson","Michael Pena","Hayley Atwell","John Atwell"}));
        addActorsToMovie(getMovieIDsByTitle("The Incredible Hulk")[0],getActorIDs(new String[]{"Edward Norton","Liv Tyler","Tim Roth","William Hurt","Tim Blak","Tim Blake Nelson","Ty Burrell"}));
        addActorsToMovie(getMovieIDsByTitle("Iron Man")[0],getActorIDs(new String[]{"Robert Downey Jr.","Terrence Howard","Jeff Bridges","Gwyneth Paltrow","Leslie Bibb","Shaun Toub","Faran Tahir","Clark Gregg","Paul Bettany","Jon Favreau"}));
        addActorsToMovie(getMovieIDsByTitle("Iron Man 2")[0],getActorIDs(new String[]{"Robert Downey Jr.","Gwyneth Paltrow","Don Cheadle","Scarlett Johansson","Sam Rockwell","Mickey Rourke","Samuel L. Jackson","Clark Gregg","John Slattery","Garry Shandling","Paul Bettany","Kate Mara","Leslie Bibb","Jon Favreau"}));
        addActorsToMovie(getMovieIDsByTitle("Iron Man 3")[0],getActorIDs(new String[]{"Robert Downey Jr.","Gwyneth Paltrow","Don Cheadle","Guy Pearce","Rebecca Hall","Jon Favreau","Ben Kingsley","James Badge Dale","Stephanie Szostak","Paul Bettany"}));

        //addDirectorToMovie(String[])
        addDirectorsToMovie(getMovieIDsByTitle("Batman Begins")[0],getDirectorIDs(new String[]{"Cristopher Nolan"}));
        addDirectorsToMovie(getMovieIDsByTitle("Batman - The Dark Knight")[0],getDirectorIDs(new String[]{"Cristopher Nolan"}));
        addDirectorsToMovie(getMovieIDsByTitle("Batman - The Dark Knight Rises")[0],getDirectorIDs(new String[]{"Cristopher Nolan"}));
        addDirectorsToMovie(getMovieIDsByTitle("Sherlock Holmes")[0],getDirectorIDs(new String[]{"Guy Ritchie"}));
        addDirectorsToMovie(getMovieIDsByTitle("Sherlock Holmes: A Game of Shadows")[0],getDirectorIDs(new String[]{"Guy Ritchie"}));
        addDirectorsToMovie(getMovieIDsByTitle("Captain America: The First Avenger")[0],getDirectorIDs(new String[]{"Joe Johnston"}));
        addDirectorsToMovie(getMovieIDsByTitle("Captain America: The Winter Soldier")[0],getDirectorIDs(new String[]{"Anthony Russo","Joe Russo"}));
        addDirectorsToMovie(getMovieIDsByTitle("Captain America: Civil War")[0],getDirectorIDs(new String[]{"Anthony Russo","Joe Russo"}));
        addDirectorsToMovie(getMovieIDsByTitle("Guardians of the Galaxy")[0],getDirectorIDs(new String[]{"James Gunn"}));
        addDirectorsToMovie(getMovieIDsByTitle("Guardians of the Galaxy Vol. 2")[0],getDirectorIDs(new String[]{"James Gunn"}));
        addDirectorsToMovie(getMovieIDsByTitle("Doctor Strange")[0],getDirectorIDs(new String[]{"Scott Derrickson"}));
        addDirectorsToMovie(getMovieIDsByTitle("The Avengers")[0],getDirectorIDs(new String[]{"Joss Whedon"}));
        addDirectorsToMovie(getMovieIDsByTitle("Avengers: Age of Ultron")[0],getDirectorIDs(new String[]{"Joss Whedon"}));
        addDirectorsToMovie(getMovieIDsByTitle("Thor")[0],getDirectorIDs(new String[]{"Kenneth Branagh"}));
        addDirectorsToMovie(getMovieIDsByTitle("Thor: The Dark World")[0],getDirectorIDs(new String[]{"Alan Taylor"}));
        addDirectorsToMovie(getMovieIDsByTitle("Deadpool")[0],getDirectorIDs(new String[]{"Tim Miller"}));
        addDirectorsToMovie(getMovieIDsByTitle("Ant-Man")[0],getDirectorIDs(new String[]{"Peyton Reed"}));
        addDirectorsToMovie(getMovieIDsByTitle("The Incredible Hulk")[0],getDirectorIDs(new String[]{"Louis Leterrier"}));
        addDirectorsToMovie(getMovieIDsByTitle("Iron Man")[0],getDirectorIDs(new String[]{"Jon Favreau"}));
        addDirectorsToMovie(getMovieIDsByTitle("Iron Man 2")[0],getDirectorIDs(new String[]{"Jon Favreau"}));
        addDirectorsToMovie(getMovieIDsByTitle("Iron Man 3")[0],getDirectorIDs(new String[]{"Shane Black"}));

        //addGenreToMovie(String[])
        addGenresToMovie(getMovieIDsByTitle("Batman Begins")[0],getGenreIDs(new String[]{"Drama","Action","Crime"}));
        addGenresToMovie(getMovieIDsByTitle("Batman - The Dark Knight")[0],getGenreIDs(new String[]{"Drama","Action","Crime"}));
        addGenresToMovie(getMovieIDsByTitle("Batman - The Dark Knight Rises")[0],getGenreIDs(new String[]{"Drama","Action","Crime"}));
        addGenresToMovie(getMovieIDsByTitle("Sherlock Holmes")[0],getGenreIDs(new String[]{"Action","Adventure","Crime"}));
        addGenresToMovie(getMovieIDsByTitle("Sherlock Holmes: A Game of Shadows")[0],getGenreIDs(new String[]{"Action","Adventure","Crime"}));
        addGenresToMovie(getMovieIDsByTitle("Captain America: The First Avenger")[0],getGenreIDs(new String[]{"Action","Adventure","Sci-Fi"}));
        addGenresToMovie(getMovieIDsByTitle("Captain America: The Winter Soldier")[0],getGenreIDs(new String[]{"Action","Adventure","Sci-Fi"}));
        addGenresToMovie(getMovieIDsByTitle("Captain America: Civil War")[0],getGenreIDs(new String[]{"Action","Adventure","Sci-Fi"}));
        addGenresToMovie(getMovieIDsByTitle("Guardians of the Galaxy")[0],getGenreIDs(new String[]{"Action","Adventure","Sci-Fi"}));
        addGenresToMovie(getMovieIDsByTitle("Guardians of the Galaxy Vol. 2")[0],getGenreIDs(new String[]{"Action","Adventure","Sci-Fi"}));
        addGenresToMovie(getMovieIDsByTitle("Doctor Strange")[0],getGenreIDs(new String[]{"Action","Adventure","Fantasy"}));
        addGenresToMovie(getMovieIDsByTitle("The Avengers")[0],getGenreIDs(new String[]{"Action","Sci-Fi"}));
        addGenresToMovie(getMovieIDsByTitle("Avengers: Age of Ultron")[0],getGenreIDs(new String[]{"Action","Adventure","Sci-Fi"}));
        addGenresToMovie(getMovieIDsByTitle("Thor")[0],getGenreIDs(new String[]{"Action","Adventure","Fantasy"}));
        addGenresToMovie(getMovieIDsByTitle("Thor: The Dark World")[0],getGenreIDs(new String[]{"Action","Adventure","Fantasy"}));
        addGenresToMovie(getMovieIDsByTitle("Deadpool")[0],getGenreIDs(new String[]{"Action","Adventure","Comedy"}));
        addGenresToMovie(getMovieIDsByTitle("Ant-Man")[0],getGenreIDs(new String[]{"Action","Adventure","Comedy"}));
        addGenresToMovie(getMovieIDsByTitle("The Incredible Hulk")[0],getGenreIDs(new String[]{"Action","Adventure","Sci-Fi"}));
        addGenresToMovie(getMovieIDsByTitle("Iron Man")[0],getGenreIDs(new String[]{"Action","Adventure","Sci-Fi"}));
        addGenresToMovie(getMovieIDsByTitle("Iron Man 2")[0],getGenreIDs(new String[]{"Action","Adventure","Sci-Fi"}));
        addGenresToMovie(getMovieIDsByTitle("Iron Man 3")[0],getGenreIDs(new String[]{"Action","Adventure","Sci-Fi"}));


        insertSeriesIntoDatabase("The Big Bang Theory", 4.3, 12, "2007");
        insertSeriesIntoDatabase("Breaking Bad", 4.9, 5, "2008");

        addDescENToSeries(getSeriesIDsByTitle("Breaking Bad")[0],"When chemistry teacher Walter White is diagnosed with Stage III cancer and given only two years to live, he decides he has nothing to lose. He lives with his teenage son, who has cerebral palsy, and his wife, in New Mexico. Determined to ensure that his family will have a secure future, Walt embarks on a career of drugs and crime. He proves to be remarkably proficient in this new world as he begins manufacturing and selling methamphetamine with one of his former students. The series tracks the impacts of a fatal diagnosis on a regular, hard working man, and explores how a fatal diagnosis affects his morality and transforms him into a major player of the drug trade.");
        addDescENToSeries(getSeriesIDsByTitle("The Big Bang Theory")[0],"Leonard Hofstadter and Sheldon Cooper are both brilliant physicists working at Cal Tech in Pasadena, California. They are colleagues, best friends, and roommates, although in all capacities their relationship is always tested primarily by Sheldon's regimented, deeply eccentric, and non-conventional ways. They are also friends with their Cal Tech colleagues mechanical engineer Howard Wolowitz and astrophysicist Rajesh Koothrappali. The foursome spend their time working on their individual work projects, playing video games, watching science-fiction movies, or reading comic books. As they are self-professed nerds, all have little or no luck with women. When Penny, a pretty woman and an aspiring actress from Omaha, moves into the apartment across the hall from Leonard and Sheldon's, Leonard has another aspiration in life, namely to get Penny to be his girlfriend.");

        insertActorIntoDatabase("Bryan Cranston");
        insertActorIntoDatabase("Anna Gunn");
        insertActorIntoDatabase("Aaron Paul");
        insertActorIntoDatabase("Dean Norris");
        insertActorIntoDatabase("Betsy Brandt");
        insertActorIntoDatabase("RJ Mitte");
        insertActorIntoDatabase("Bob Odenkirk");
        insertActorIntoDatabase("Steven Michael Quezada");
        insertActorIntoDatabase("Jonathan Banks");
        insertActorIntoDatabase("Giancarlo Esposito");
        insertActorIntoDatabase("Johnny Galecki");
        insertActorIntoDatabase("Jim Parsons");
        insertActorIntoDatabase("Kaley Cuoco");
        insertActorIntoDatabase("Simon Helberg");
        insertActorIntoDatabase("Kunal Nayyar");
        insertActorIntoDatabase("Melissa Rauch");
        insertActorIntoDatabase("Mayim Bialik");
        insertActorIntoDatabase("Kevin Sussman");
        insertActorIntoDatabase("John Ross Bowie");
        insertActorIntoDatabase("Wil Wheaton");

        insertCreatorIntoDatabase("Chuck Lorre");
        insertCreatorIntoDatabase("Bill Prady");
        insertCreatorIntoDatabase("Vince Gilligan");

        addActorsToSeries(getSeriesIDsByTitle("Breaking Bad")[0],getActorIDs(new String[]{"Bryan Cranston","Anna Gunn","Aaron Paul","Dean Norris","Betsy Brandt","RJ Mitte","Bob Odenkirk","Steven Michael Quezada","Jonathan Banks","Giancarlo Esposito"}));
        addActorsToSeries(getSeriesIDsByTitle("The Big Bang Theory")[0],getActorIDs(new String[]{"Johnny Galecki","Jim Parsons","Kaley Cuoco","Simon Helberg","Kunal Nayyar","Melissa Rauch","Mayim Bialik","Kevin Sussman","John Ross Bowie","Wil Wheaton"}));

        addCreatorsToSeries(getSeriesIDsByTitle("Breaking Bad")[0],getCreatorIDs(new String[]{"Vince Gilligan"}));
        addCreatorsToSeries(getSeriesIDsByTitle("The Big Bang Theory")[0],getCreatorIDs(new String[]{"Chuck Lorre","Bill Prady"}));

        addGenresToSeries(getSeriesIDsByTitle("Breaking Bad")[0],getGenreIDs(new String[]{"Crime","Drama","Thriller"}));
        addGenresToSeries(getSeriesIDsByTitle("The Big Bang Theory")[0],getGenreIDs(new String[]{"Comedy","Romance"}));

        addMovieList("Watch List");
        addMovieToList("Watch List", "Guardians of the Galaxy");
        addMovieToList("Watch List", "Guardians of the Galaxy Vol. 2");
        addEpisodesToSeason("The Big Bang Theory",1,21);
        addEpisodesToSeason("The Big Bang Theory",2,20);
        addEpisodesToSeason("The Big Bang Theory",3,19);
        addEpisodesToSeason("The Big Bang Theory",4,18);
        addEpisodesToSeason("The Big Bang Theory",5,18);
        addEpisodesToSeason("The Big Bang Theory",6,18);
        addEpisodesToSeason("The Big Bang Theory",7,18);
        addEpisodesToSeason("The Big Bang Theory",8,18);
        addEpisodesToSeason("The Big Bang Theory",9,18);
        addEpisodesToSeason("The Big Bang Theory",10,18);
        addEpisodesToSeason("The Big Bang Theory",11,18);
        addEpisodesToSeason("The Big Bang Theory",12,18);


        addEpisodesToSeason("Breaking Bad",1,18);
        addEpisodesToSeason("Breaking Bad",2,18);
        addEpisodesToSeason("Breaking Bad",3,18);
        addEpisodesToSeason("Breaking Bad",4,18);
        addEpisodesToSeason("Breaking Bad",5,18);
    }
}
