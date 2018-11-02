package android.csulb.edu.popularmoviesstage1.backgroundtasks;

import android.content.Context;
import android.csulb.edu.popularmoviesstage1.Movie;
import android.csulb.edu.popularmoviesstage1.utils.JsonUtils;
import android.csulb.edu.popularmoviesstage1.utils.NetworkUtils;
import android.os.AsyncTask;

import java.util.ArrayList;

public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private Context context;
    private AsyncTaskCompleteListener<Movie> listener;

    public FetchMovieTask(Context context, AsyncTaskCompleteListener<Movie> listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPostTask();
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        try {
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildMovieUrl(params[0]));
            ArrayList<Movie> movieData = JsonUtils.parseMoviesJson(jsonMovieResponse);

            return movieData;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
       super.onPostExecute(movies);
       listener.onTaskComplete(movies);
    }
}