package android.csulb.edu.popularmoviesstage1.backgroundtasks;

import android.content.Context;
import android.csulb.edu.popularmoviesstage1.Review;
import android.csulb.edu.popularmoviesstage1.utils.JsonUtils;
import android.csulb.edu.popularmoviesstage1.utils.NetworkUtils;
import android.os.AsyncTask;

import java.util.ArrayList;

public class FetchReviewTask extends AsyncTask<String, Void, ArrayList<Review>> {

        private Context context;
        private AsyncTaskCompleteListener<Review> listener;

        public FetchReviewTask(Context context, AsyncTaskCompleteListener<Review> listener){
            this.context = context;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onPostTask();
        }

        @Override
        protected ArrayList<Review> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            try {
                String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildMovieUrl(params[0]));
                ArrayList<Review> reviews = JsonUtils.parseReviewJson(jsonTrailerResponse);

                return reviews;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            super.onPostExecute(reviews);
            listener.onTaskComplete(reviews);
        }
}
