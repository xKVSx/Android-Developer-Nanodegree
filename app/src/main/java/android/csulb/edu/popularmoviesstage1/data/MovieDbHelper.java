package android.csulb.edu.popularmoviesstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " +
                MoviesContract.MovieEntry.TABLE_NAME + " (" +
                MoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL UNIQUE ON CONFLICT IGNORE, " +
                MoviesContract.MovieEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_USER_FAVORITE + " INTEGER DEFAULT 0, " +
                MoviesContract.MovieEntry.COLUMN_POPULAR_MOVIE + " INTEGER DEFAULT 0, " +
                MoviesContract.MovieEntry.COLUMN_TOP_RATED_MOVIE + " INTEGER DEFAULT 0" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);

        /*for the favorite, popular, and top rated columns, the default of 0 means that the movie
        is not a favorite, popular, or top rated movie, respectively. 1 means that they are*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
