package android.csulb.edu.popularmoviesstage1.utils;

import android.content.ContentValues;
import android.content.Context;
import android.csulb.edu.popularmoviesstage1.Movie;
import android.csulb.edu.popularmoviesstage1.data.MoviesContract;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

public class DbUtils {

    public static ArrayList<Movie> loadAllMovies(Context context) {
        ArrayList movieData = new ArrayList();

        Cursor cursor = context.getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI, null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID)));
                movie.setImage(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_IMAGE)));
                movie.setPlot(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_PLOT)));
                movie.setRating(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING)));
                movie.setRelease(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE)));

                movieData.add(movie);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return movieData;
    }

    public static ArrayList<Movie> loadFavorites(Context context){
        ArrayList movieData = new ArrayList();
        String selection = "favorite=?";
        String selectionArgs[] = new String[]{"1"};

        Cursor cursor = context.getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI, null,
                selection, selectionArgs, null, null);

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie();
                    movie.setId(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID)));
                    movie.setImage(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_IMAGE)));
                    movie.setPlot(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_PLOT)));
                    movie.setRating(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING)));
                    movie.setRelease(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE)));

                    int favorite = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_USER_FAVORITE)));

                    movieData.add(movie);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();

        return movieData;
    }

    public static ArrayList<Movie> loadPopular(Context context){
        ArrayList movieData = new ArrayList();
        String selection = "popular=?";
        String selectionArgs[] = new String[]{"1"};

        Cursor cursor = context.getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI, null,
                selection, selectionArgs, null, null);

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie();
                    movie.setId(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID)));
                    movie.setImage(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_IMAGE)));
                    movie.setPlot(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_PLOT)));
                    movie.setRating(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING)));
                    movie.setRelease(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE)));

                    int popular = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POPULAR_MOVIE)));

                    movieData.add(movie);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();

        return movieData;
    }

    public static ArrayList<Movie> loadTopRated(Context context) {
        ArrayList movieData = new ArrayList();
        String selection = "top_rated=?";
        String selectionArgs[] = new String[]{"1"};

        Cursor cursor = context.getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI, null,
                selection, selectionArgs, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie();
                    movie.setId(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID)));
                    movie.setImage(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_IMAGE)));
                    movie.setPlot(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_PLOT)));
                    movie.setRating(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING)));
                    movie.setRelease(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE)));

                    int top_rated = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TOP_RATED_MOVIE)));

                    movieData.add(movie);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();

        return movieData;
    }

    public static void addFavorite(Context context, Movie movie){
         /*this method updates a movie currently in the database as a favorite. Note that the default
        value of COLUMN_USER_FAVORITE is 0, meaning that by default it is not a favorite*/

        ContentValues mUpdateFavorites = new ContentValues();
        String mSelectionClause = MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] mSelectionArgs = {movie.getId()};
        int mRowsUpdated = 0;

        mUpdateFavorites.put(MoviesContract.MovieEntry.COLUMN_USER_FAVORITE, 1);

        mRowsUpdated = context.getContentResolver().update(MoviesContract.MovieEntry.CONTENT_URI,
                mUpdateFavorites, mSelectionClause, mSelectionArgs);

    }

    public static void removeFavorite(Context context, Movie movie){
         /*this method updates a movie currently in the database, removing it as a favorite. Note
         that the default value of COLUMN_USER_FAVORITE is 0, meaning that by default it is not a favorite*/

        ContentValues mUpdateFavorites = new ContentValues();
        String mSelectionClause = MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] mSelectionArgs = {movie.getId()};
        int mRowsUpdated = 0;

        mUpdateFavorites.put(MoviesContract.MovieEntry.COLUMN_USER_FAVORITE, 0);

        mRowsUpdated = context.getContentResolver().update(MoviesContract.MovieEntry.CONTENT_URI,
                mUpdateFavorites, mSelectionClause, mSelectionArgs);
    }

    public static int isFavorite(Context context, Movie movie){
        Cursor cursor;
        int favorite = -1;

        Uri uri = MoviesContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(MoviesContract.MovieEntry.COLUMN_MOVIE_ID).build();
        uri = uri.buildUpon().appendPath(movie.getId()).build();

        String[] favoriteColumn = new String[]{MoviesContract.MovieEntry.COLUMN_USER_FAVORITE};

        cursor = context.getContentResolver().query(uri, favoriteColumn, null, null,
                null);
        cursor.moveToFirst();

        if(cursor.getCount() > 0)
            favorite = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_USER_FAVORITE)));

        cursor.close();

        return favorite;
    }

    public static void addPopular(Context context, ArrayList<Movie> movies){
        /*this method adds a movie currently not in the database as a popular movie. Note that the
        default value of COLUMN_POPULAR_MOVIE is 0, meaning that by default it is not popular.*/

        int popular = 1;

        for(Movie movie: movies){
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_IMAGE, movie.getImage());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_PLOT, movie.getPlot());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_RATING, movie.getRating());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_POPULAR_MOVIE, popular);

            Uri uri = context.getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, contentValues);
        }
    }

    public static void addTopRated(Context context, ArrayList<Movie> movies){
        /*this method adds a movie currently not in the database as a top rated movie. Note that the
        default value of COLUMN_TOP_RATED_MOVIE is 0, meaning that by default it is not top rated.*/

        int top_rated = 1;

        for(Movie movie: movies){
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_IMAGE, movie.getImage());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_PLOT, movie.getPlot());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_RATING, movie.getRating());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_TOP_RATED_MOVIE, top_rated);

            Uri uri = context.getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, contentValues);
        }
    }
}
