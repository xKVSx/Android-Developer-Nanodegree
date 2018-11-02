package android.csulb.edu.popularmoviesstage1.backgroundtasks;

import android.content.Context;
import android.csulb.edu.popularmoviesstage1.Trailer;
import android.csulb.edu.popularmoviesstage1.utils.JsonUtils;
import android.csulb.edu.popularmoviesstage1.utils.NetworkUtils;
import android.os.AsyncTask;

import java.util.ArrayList;

public class FetchTrailerTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

    private Context context;
    private AsyncTaskCompleteListener<Trailer> listener;

    public FetchTrailerTask(Context context, AsyncTaskCompleteListener<Trailer> listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPostTask();
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        try {
            String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildMovieUrl(params[0]));
            ArrayList<Trailer> trailers = JsonUtils.parseTrailerJson(jsonTrailerResponse);

            return trailers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        super.onPostExecute(trailers);
        listener.onTaskComplete(trailers);
    }
}
