package android.csulb.edu.popularmoviesstage1.utils;

import android.csulb.edu.popularmoviesstage1.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String IMAGE = "poster_path";
    private static final String PLOT = "overview";
    private static final String RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";

    private static ArrayList<Movie> movies = new ArrayList<>();

    /*public static ArrayList<Movie> parsePopularMoviesJson(String json) throws JSONException {

        ArrayList<Movie> movies = new ArrayList<>();
        JSONObject movie_details = new JSONObject(json);

        //get the movie image thumbnail
        JSONArray results = movie_details.getJSONArray(RESULTS);

        for(int i = 0; i < results.length(); i++){
            Movie movie = new Movie();
            JSONObject movie_results = results.getJSONObject(i);
            String image = movie_results.getString(IMAGE);
            movie.setImage(image);
            movies.add(movie);
        }

        return movies;
    }*/

    public static ArrayList<Movie> parseMoviesJson(String json) throws JSONException{

        JSONObject movie_details = new JSONObject(json);
        JSONArray results = movie_details.getJSONArray(RESULTS);

        for (int i = 0; i < results.length(); i++){
            Movie movie = new Movie();
            parseMovieIds(results, movie, i);
            parseMovieImages(results, movie, i);
            parseMovieTitles(results, movie, i);
            parseMoviePlots(results, movie, i);
            parseMovieRating(results, movie, i);
            parseMovieReleaseData(results, movie, i);
            movies.add(movie);
        }

        return movies;
    }

    private static void parseMovieIds(JSONArray results, Movie movie, int index) throws JSONException{

        //get the movie id

        JSONObject movie_results = results.getJSONObject(index);
        String id = movie_results.getString(ID);
        movie.setId(id);
    }

    private static void parseMovieTitles(JSONArray results, Movie movie, int index) throws JSONException{

        //get the movie title

        JSONObject movie_results = results.getJSONObject(index);
        String title = movie_results.getString(TITLE);
        movie.setTitle(title);
    }

    private static void parseMovieImages(JSONArray results, Movie movie, int index) throws JSONException {

        //get the movie image thumbnail

        JSONObject movie_results = results.getJSONObject(index);
        String image = movie_results.getString(IMAGE);
        movie.setImage(image);
    }

    private static void parseMoviePlots(JSONArray results, Movie movie, int index) throws JSONException{

        //get the movie plot

        JSONObject movie_results = results.getJSONObject(index);
        String plot = movie_results.getString(PLOT);
        movie.setPlot(plot);
    }

    public static void parseMovieRating(JSONArray results, Movie movie, int index) throws JSONException{

        //get the movies rating

        JSONObject movie_results = results.getJSONObject(index);
        String rating = movie_results.getString(RATING);
        movie.setRating(rating);

    }

    public static void parseMovieReleaseData(JSONArray results, Movie movie, int index) throws JSONException{

        //get the movie release date

        JSONObject movie_results = results.getJSONObject(index);
        String release_date = movie_results.getString(RELEASE_DATE);
        movie.setRelease(release_date);
    }

}