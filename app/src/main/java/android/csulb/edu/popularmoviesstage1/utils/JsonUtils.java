package android.csulb.edu.popularmoviesstage1.utils;

import android.csulb.edu.popularmoviesstage1.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    private static final String RESULTS = "results";
    private static final String TITLE = "title";
    private static final String IMAGE = "poster_path";
    private static final String PLOT = "overview";
    private static final String RATING = "adult";
    private static final String RELEASE_DATE = "release_date";

    public static Movie parseMovieJson(String json) throws JSONException {

        Movie movie = new Movie();
        JSONObject movie_details = new JSONObject(json);

        //get the movie image thumbnail
        JSONArray results = movie_details.getJSONArray(RESULTS);
        JSONObject movie_results = results.getJSONObject(0);
        String image = movie_results.getString(IMAGE);
        movie.setImage(image);

        return movie;
    }
}