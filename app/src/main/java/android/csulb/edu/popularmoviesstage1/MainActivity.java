package android.csulb.edu.popularmoviesstage1;

import android.content.Intent;
import android.csulb.edu.popularmoviesstage1.utils.JsonUtils;
import android.csulb.edu.popularmoviesstage1.utils.NetworkUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private static final String MOVIE_LIST = "movie_list";
    private static final String SORT_BOOLEAN = "sort";

    private TextView mErrorMessageDisplay;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private ArrayList<Movie> movieData;
    private boolean popular = true; //if false, then pull highest rated films

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.mRecycler_view);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns(), GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        if(savedInstanceState == null)
            loadMovieData(setMovieURL(popular));
        else {
            movieData = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            popular = savedInstanceState.getBoolean(SORT_BOOLEAN);
            mMovieAdapter.setMovieData(movieData);
        }
    }

    private int numberOfColumns(){
        //method to dynamically calculate the number of columns for screen size and orientation
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if(nColumns < 2)
            return 2;

        return nColumns;
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private String setMovieURL(boolean popular){
        String movieString;

        if(popular) {
            movieString = this.getString(R.string.MOVIE_URL)
                    + this.getString(R.string.popular)
                    + "?api_key=" + this.getString(R.string.api_key);
        }
        else {
            movieString = this.getString(R.string.MOVIE_URL)
                    + this.getString(R.string.top_rated)
                    + "?api_key=" + this.getString(R.string.api_key);
        }

        return movieString;
    }

    private void loadMovieData(String movieURL){
        new FetchMovieTask().execute(movieURL);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildMovieUrl(params[0]));
                movieData = JsonUtils.parseMoviesJson(jsonMovieResponse);

                return movieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(movies != null) {
                mMovieAdapter.setMovieData(movies);

                if(mRecyclerView.getVisibility() == View.INVISIBLE)
                    mRecyclerView.setVisibility(View.VISIBLE);

                if(mErrorMessageDisplay.getVisibility() == View.VISIBLE)
                    mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            }
            else
                showErrorMessage();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST, movieData);
        outState.putBoolean(SORT_BOOLEAN, popular);

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
            if(movieData != null)
                movieData.clear();
            loadMovieData(setMovieURL(popular));

            return true;
        }
        else if(itemClicked == R.id.action_sort_top_rated && popular){
            popular = false;
            if(movieData != null)
                movieData.clear();
            loadMovieData(setMovieURL(popular));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, DetailActivity.class);
        Movie movie = movieData.get(clickedItemIndex);
        intent.putExtra(DetailActivity.EXTRA_POSITION, movie);
        startActivity(intent);
    }
}
