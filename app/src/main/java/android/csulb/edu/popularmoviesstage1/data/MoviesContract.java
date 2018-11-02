package android.csulb.edu.popularmoviesstage1.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {
    public static final String AUTHORITY = "android.csulb.edu.popularmoviesstage1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_IMAGE = "movie_image";
        public static final String COLUMN_PLOT = "movie_plot";
        public static final String COLUMN_RATING = "movie_rating";
        public static final String COLUMN_RELEASE_DATE = "movie_release_date";
        public static final String COLUMN_USER_FAVORITE = "favorite";
        public static final String COLUMN_POPULAR_MOVIE = "popular";
        public static final String COLUMN_TOP_RATED_MOVIE = "top_rated";
    }
}
