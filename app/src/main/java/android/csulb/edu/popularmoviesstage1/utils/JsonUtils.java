package android.csulb.edu.popularmoviesstage1.utils;

import android.csulb.edu.popularmoviesstage1.Movie;
import android.csulb.edu.popularmoviesstage1.Review;
import android.csulb.edu.popularmoviesstage1.Trailer;

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

    private static final String KEY = "key";
    private static final String NAME = "name";

    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";
    private static final String REVIEW_ID = "id";

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

    private static void parseMovieRating(JSONArray results, Movie movie, int index) throws JSONException{

        //get the movie rating

        JSONObject movie_results = results.getJSONObject(index);
        String rating = movie_results.getString(RATING);
        movie.setRating(rating);

    }

    private static void parseMovieReleaseData(JSONArray results, Movie movie, int index) throws JSONException{

        //get the movie release date

        JSONObject movie_results = results.getJSONObject(index);
        String release_date = movie_results.getString(RELEASE_DATE);
        movie.setRelease(release_date);
    }

    public static ArrayList<Trailer> parseTrailerJson(String json) throws JSONException{
        //get the trailer name and key
        ArrayList<Trailer> trailers = new ArrayList<>();

        JSONObject trailer_details = new JSONObject(json);
        JSONArray results = trailer_details.getJSONArray(RESULTS);

        for (int i = 0; i < results.length(); i++){
            JSONObject trailer_results = results.getJSONObject(i);
            String name = trailer_results.getString(NAME);
            String key = trailer_results.getString(KEY);
            Trailer trailer = new Trailer(name, key);
            trailers.add(trailer);
        }

        return trailers;
    }

    public static ArrayList<Review> parseReviewJson(String json) throws JSONException{
        //get the review content, id, and authors name
        ArrayList<Review> reviews = new ArrayList<>();

        JSONObject review_details = new JSONObject(json);
        JSONArray results = review_details.getJSONArray(RESULTS);

        for(int i = 0; i < results.length(); i++){
            JSONObject review_results = results.getJSONObject(i);
            String author = review_results.getString(REVIEW_AUTHOR);
            String content = review_results.getString(REVIEW_CONTENT);
            String id = review_results.getString(REVIEW_ID);
            Review review = new Review(author, content, id);
            reviews.add(review);
        }

        return reviews;
    }
}