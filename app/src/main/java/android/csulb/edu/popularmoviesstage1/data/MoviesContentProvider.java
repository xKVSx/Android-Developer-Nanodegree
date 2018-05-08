package android.csulb.edu.popularmoviesstage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MoviesContentProvider extends ContentProvider {
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;
    public static final int MOVIES_WITH_MOVIE_ID = 102;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match){
            case MOVIES:
                retCursor = db.query(MoviesContract.MovieEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case  MOVIES_WITH_ID:
                retCursor = db.query(MoviesContract.MovieEntry.TABLE_NAME, projection,
                        MoviesContract.MovieEntry._ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null, null, sortOrder);
                break;
            case MOVIES_WITH_MOVIE_ID:
                String mId = uri.getPathSegments().get(2);
                retCursor = db.query(MoviesContract.MovieEntry.TABLE_NAME, projection,
                        MoviesContract.MovieEntry.COLUMN_MOVIE_ID +"=?", new String[]{mId},
                        null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIES:
                long id = db.insertWithOnConflict(MoviesContract.MovieEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                if(id > 0)
                    returnUri = ContentUris.withAppendedId(MoviesContract.MovieEntry.CONTENT_URI, id);
                else
                    //throw new android.database.SQLException("Failed to insert row into " + uri);
                    Log.d("MoviesContentProvider", "Failed to insert row into " + uri);
                    returnUri = MoviesContract.MovieEntry.CONTENT_URI;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int moviesDeleted = 0;

        switch (match){
            case MOVIES:
                moviesDeleted = db.delete(MoviesContract.MovieEntry.TABLE_NAME, s, strings);
                break;
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                moviesDeleted = db.delete(MoviesContract.MovieEntry.TABLE_NAME,
                        MoviesContract.MovieEntry._ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (moviesDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        if (contentValues == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(MoviesContract.MovieEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case MOVIES_WITH_ID:
                rowsUpdated = db.update(MoviesContract.MovieEntry.TABLE_NAME, contentValues,
                        MoviesContract.MovieEntry._ID + " = ?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);

        String movieId = MoviesContract.PATH_MOVIES + "/" + MoviesContract.MovieEntry.COLUMN_MOVIE_ID;
        uriMatcher.addURI(MoviesContract.AUTHORITY, movieId + "/*", MOVIES_WITH_MOVIE_ID);

        return uriMatcher;
    }
}
