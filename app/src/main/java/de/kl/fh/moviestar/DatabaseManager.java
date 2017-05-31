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
    private static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_RELEASE = "release";
    private static final String COLUMN_DESC_EN = "description_en";
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
    private static final String COLUMN_SEASON = "Season";
    private static final String TABLE_SEASON_EPISODES = "Season_Episodes";
    private static final String COLUMN_SEASON_ID = "Season_Id";
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
                        COLUMN_TITLE + " TEXT UNIQUE, "+
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

        insertMovieIntoDatabase("Guardians of the Galaxy", 4.2, 117, "24042014");
        insertMovieSequelIntoDatabase("Guardians of the Galaxy Vol. 2", 4.5, 136, "24042017", "Guardians of the Galaxy");
        insertMovieIntoDatabase("Fast & Furious 8", 2.1, 125, "04042017");
        setMovieToWatched(getMovieIDsByTitle("Fast & Furious 8")[0]);

        insertSeriesIntoDatabase("The Big Bang Theory", 4.3, 8, "2007");
        insertSeriesIntoDatabase("Breaking Bad", 4.9, 8, "2008");
        for(int i=1;i<=8;i++) {
            insertSeasonsIntoDatabase(getSeriesIDsByTitle("The Big Bang Theory")[0], i);
        }
        for(int i=1;i<=5;i++) {
            insertSeasonsIntoDatabase(getSeriesIDsByTitle("Breaking Bad")[0], i);
        }

        addMovieList("Watch List");
        addMovieToList("Watch List", "Fast & Furious 8");
        addMovieToList("Watch List", "Guardians of the Galaxy");
        addMovieToList("Watch List", "Guardians of the Galaxy Vol. 2");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        onCreate(db);
    }

    //------SET DATA------------------------------------------------------------------------------------------------------------
    private void insertMovieIntoDatabase(String title, double rating, int duration, String release)
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

    private void insertSeriesIntoDatabase(String title, double rating, int seasons, String release)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_SERIES + " (TITLE,RATING,SEASONS,RELEASE) VALUES ("+
                        "'"+title+"'" + "," +
                        rating + "," +
                        seasons + "," +
                        "'"+release+"'"
                        + ")"
        );
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

    private void insertActorIntoDatabase(String name)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_ACTORS + " (NAME) VALUES ("+
                        name
                        + ")"
        );
    }

    private void insertCreatorIntoDatabase(String name)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_CREATORS + " (NAME) VALUES ("+
                        name
                        + ")"
        );
    }

    private void insertDirectorIntoDatabase(String name)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_DIRECTORS + " (NAME) VALUES ("+
                        name
                        + ")"
        );
    }

    private void insertGenreIntoDatabase(String name)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_GENRES + " (NAME) VALUES ("+
                        name
                        + ")"
        );
    }

    private void addMovieList(String name)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_MOVIE_LISTS + " (NAME) VALUES ("+
                        "'"+name+"'"
                        + ")"
        );
    }

    private void addSeriesList(String name)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_SERIES_LISTS + " (NAME) VALUES ("+
                        name
                        + ")"
        );
    }

    private void addMovieToList(String listName, String movieName)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_MOVIE_LISTS_ELEMENTS + " (MOVIE_LIST_ID,MOVIE_ID) VALUES ("+
                        "(SELECT ID FROM "+TABLE_MOVIE_LISTS+" WHERE NAME LIKE '"+listName+"')" + "," +
                        "(SELECT ID FROM "+TABLE_MOVIES+" WHERE TITLE LIKE '"+movieName+"')"
                        + ")"
        );
    }

    private void addSeriesToList(String listName, String seriesName)
    {
        database.execSQL(
                "INSERT INTO " + TABLE_SERIES_LISTS_ELEMENTS + " (SERIES_LIST_ID,SERIES_ID) VALUES ("+
                        "(SELECT ID FROM "+TABLE_SERIES_LISTS+" WHERE NAME LIKE '"+listName+"')" + "," +
                        "(SELECT ID FROM "+TABLE_SERIES+" WHERE TITLE LIKE '"+seriesName+"')"
                        + ")"
        );
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
        String sql = "SELECT ROWID AS _id, TITLE, RATING, DURATION, RELEASE, DESCRIPTION_EN, DESCRIPTION_DE, SEQUEL_OF, WATCHED FROM "+TABLE_MOVIES;
        return getData(sql);
    }

    public Cursor getAllSeries()
    {
        String sql = "SELECT ROWID AS _id, TITLE, RATING, SEASONS, RELEASE, DESCRIPTION_EN, DESCRIPTION_DE FROM "+TABLE_SERIES;
        return getData(sql);
    }

    public Cursor getMovieLists()
    {
        String sql = "SELECT ID AS _id, NAME, COUNT(MOVIE_LIST_ID) AS MOVIES FROM " + TABLE_MOVIE_LISTS + " JOIN (SELECT MOVIE_LIST_ID FROM " + TABLE_MOVIE_LISTS_ELEMENTS + ") ON ID=MOVIE_LIST_ID  GROUP BY _id, NAME";
        return getData(sql);
    }

    public Cursor getSeriesLists()
    {
        String sql = "SELECT ID AS _id, a.NAME, COUNT(b.SERIES_ID) AS b.SERIES FROM " + TABLE_SERIES_LISTS + " AS a JOIN " + TABLE_SERIES_LISTS_ELEMENTS + " AS b ON a.ID=b.SERIES_ID GROUP BY b.SERIES_ID";
        return getData(sql);
    }

    public Cursor getMoviesFromList(String listName)
    {
        String sql = "SELECT a.TITLE , a.RATING, a.DURATION, a.RELEASE, a.DESCRIPTION_EN, a.DESCRIPTION_DE, a.SEQUEL_OF, a.WATCHED FROM "+TABLE_MOVIES+" a JOIN (SELECT MOVIE_ID FROM "+TABLE_MOVIE_LISTS_ELEMENTS+" a JOIN "+TABLE_MOVIE_LISTS+" b ON a.MOVIE_ID=b.ID WHERE UPPER(b.NAME)=?) b ON a.ID=b.MOVIE_ID";
        return getData(sql,new String[]{listName});
    }

    public Cursor getSeriesFromList(String listName)
    {
        String sql = "SELECT a.TITLE, a.RATING, a.SEASONS, a.RELEASE, a.DESCRIPTION_EN, a.DESCRIPTION_DE FROM "+TABLE_SERIES+" a JOIN (SELECT SERIES_ID FROM "+TABLE_SERIES_LISTS_ELEMENTS+" a JOIN "+TABLE_SERIES_LISTS+" b ON a.SERIES_ID=b.ID WHERE UPPER(b.NAME)=?) b ON a.ID=b.SERIES_ID";
        return getData(sql,new String[]{listName});
    }

    public Cursor getEpisodesFromSeason(int season, String series)
    {
        String sql = "SELECT a.TITLE, a.DURATION, a.DESCRIPTION_EN, a.DESCRIPTION_DE, a.SEQUEL_OF FROM "+TABLE_EPISODES+" a JOIN (SELECT EPISODE_ID FROM "+TABLE_SEASON_EPISODES+"a JOIN "+TABLE_SERIES_SEASONS+" b ON a.SEASON_ID=b.ID WHERE b.SEASON=? AND b.SERIES_ID=(SELECT ID FROM "+TABLE_SERIES+" WHERE UPPER(b.TITLE)=?) b ON a.ID=b.EPISODE_ID";
        return getData(sql,new String[]{String.valueOf(season),series});
    }

    public Cursor getNumberOfSeasons(String series)
    {
        String sql = "SELECT SEASONS FROM "+TABLE_SERIES+" WHERE UPPER(b.TITLE)=?";
        return getData(sql,new String[]{series});
    }

    public Cursor getCastFromMovie(String movie)
    {
        String sql = "SELECT a.NAME FROM "+TABLE_ACTORS+" a JOIN (SELECT a.ACTOR_ID FROM "+TABLE_MOVIE_ACTORS+" a JOIN "+TABLE_MOVIES+" b ON a.MOVIE_ID=b.ID WHERE UPPER(b.TITLE)=?) b ON b.ACTOR_ID=a.ID";
        return getData(sql,new String[]{movie});
    }

    public Cursor getDirectorOfMovie(String movie)
    {
        String sql = "SELECT a.NAME FROM "+TABLE_DIRECTORS+" a JOIN (SELECT a.DIRECTOR_ID FROM "+TABLE_MOVIE_DIRECTORS+" a JOIN "+TABLE_MOVIES+" b ON a.MOVIE_ID=b.ID WHERE UPPER(b.TITLE)=?) b ON b.DIRECTOR_ID=a.ID";
        return getData(sql,new String[]{movie});
    }

    public Cursor getGenreOfMovie(String movie)
    {
        String sql = "SELECT a.NAME FROM "+TABLE_GENRES+" a JOIN (SELECT a.GENRE_ID FROM "+TABLE_MOVIE_GENRES+" a JOIN "+TABLE_MOVIES+" b ON a.MOVIE_ID=b.ID WHERE UPPER(b.TITLE)=?) b ON b.GENRE_ID=a.ID";
        return getData(sql,new String[]{movie});
    }

    public Cursor getCastFromSeries(String series)
    {
        String sql = "SELECT a.NAME FROM "+TABLE_ACTORS+" a JOIN (SELECT a.ACTOR_ID FROM "+TABLE_SERIES_ACTORS+" a JOIN "+TABLE_SERIES+" b ON a.SERIES_ID=b.ID WHERE UPPER(b.TITLE)=?) b ON b.ACTOR_ID=a.ID";
        return getData(sql,new String[]{series});
    }

    public Cursor getCreatorOfSeries(String series)
    {
        String sql = "SELECT a.NAME FROM "+TABLE_CREATORS+" a JOIN (SELECT a.CREATOR_ID FROM "+TABLE_SERIES_CREATORS+" a JOIN "+TABLE_SERIES+" b ON a.SERIES_ID=b.ID WHERE UPPER(b.TITLE)=?) b ON b.CREATOR_ID=a.ID";
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
        String sql = "SELECT ID AS MOVIE_ID FROM "+TABLE_MOVIES + " a WHERE UPPER(a.TITLE) LIKE ?";
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
        String sql = "SELECT a.MOVIE_ID FROM "+TABLE_MOVIE_ACTORS+" a JOIN "+TABLE_ACTORS+" b ON a.ACTOR_ID=b.ID WHERE UPPER(b.NAME) LIKE ?";
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
        String sql = "SELECT a.MOVIE_ID FROM "+TABLE_MOVIE_DIRECTORS+" a JOIN "+TABLE_DIRECTORS+" b ON a.DIRECTOR_ID=b.ID UPPER(b.NAME) LIKE ?";
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
        String sql = "SELECT a.MOVIE_ID FROM "+TABLE_MOVIE_GENRES+" a JOIN "+TABLE_GENRES+" b ON a.GENRE_ID=b.ID WHERE UPPER(b.NAME) LIKE ?";
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
        String sql = "SELECT ID AS SERIES_ID FROM "+TABLE_SERIES+" a WHERE UPPER(a.TITLE) LIKE ?";
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
        String sql = "SELECT a.SERIES_ID FROM "+TABLE_SERIES_ACTORS+" a JOIN "+TABLE_ACTORS+" b ON a.ACTOR_ID=b.ID WHERE UPPER(b.NAME) LIKE ?";
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
        String sql = "SELECT a.SERIES_ID FROM "+TABLE_SERIES_CREATORS+" a JOIN "+TABLE_CREATORS+" b ON a.CREATOR_ID=b.ID WHERE UPPER(b.NAME) LIKE ?";
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

}
