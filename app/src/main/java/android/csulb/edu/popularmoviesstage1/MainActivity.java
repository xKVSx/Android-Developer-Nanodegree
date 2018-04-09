package android.csulb.edu.popularmoviesstage1;

import android.csulb.edu.popularmoviesstage1.utils.JsonUtils;
import android.csulb.edu.popularmoviesstage1.utils.NetworkUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String MOVIE_LIST = "movie_list";

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> movieData;
    private boolean popular = true; //if false, then pull highest rated films

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.mRecycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        if(savedInstanceState == null)
            loadMovieData(setMovieURL(popular));
        else {
            movieData = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            mMovieAdapter.setMovieData(movieData);
        }

    }

    private String setMovieURL(boolean popular){
        String movieURL;

        if(popular) {
            movieURL = this.getString(R.string.MOVIE_URL)
                    + this.getString(R.string.popular)
                    + "?api_key=" + this.getString(R.string.api_key);
        }
        else {
            movieURL = this.getString(R.string.MOVIE_URL)
                    + this.getString(R.string.top_rated)
                    + "?api_key=" + this.getString(R.string.api_key);
        }

        return movieURL;
    }

    private void loadMovieData(String movieURL){
        new FetchMovieTask().execute(movieURL);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildPopularUrl(params[0]));
                movieData = JsonUtils.parseMoviesJson(jsonMovieResponse);

                return movieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mMovieAdapter.setMovieData(movies);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST, movieData);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();

        /*If the movies are already sorted by popularity when action_sort_popular is clicked
        then do nothing (same with action_sort_top_rated), otherwise clear the movieData ArrayList
        and grab the new movie data*/

        if(itemClicked == R.id.action_sort_popular && !popular){
            popular = true;
            movieData.clear();
            loadMovieData(setMovieURL(popular));
            return true;
        }
        else if(itemClicked == R.id.action_sort_top_rated && popular){
            popular = false;
            movieData.clear();
            loadMovieData(setMovieURL(popular));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
