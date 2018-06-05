package android.csulb.edu.popularmoviesstage1;

import android.content.Intent;
import android.csulb.edu.popularmoviesstage1.adapters.MovieAdapter;
import android.csulb.edu.popularmoviesstage1.backgroundtasks.AsyncTaskCompleteListener;
import android.csulb.edu.popularmoviesstage1.backgroundtasks.FetchMovieTask;
import android.csulb.edu.popularmoviesstage1.utils.DbUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private static final String MOVIE_LIST = "movie_list";
    private static final String CURRENT_SORT = "sort";

    private TextView mErrorMessageDisplay;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private ArrayList<Movie> movieData;
    private static final int POPULAR = 0;
    private static final int TOP_RATED = 1;
    private static final int FAVORITE = 2;
    private int mCurrentSort = POPULAR;

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
            loadMovieData(setMovieURL(mCurrentSort)); //sort by popular movies by default
        else {
            movieData = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            mCurrentSort = savedInstanceState.getInt(CURRENT_SORT);
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

    private void loadMoviesWhenOffline(){
        //mRecyclerView.setVisibility(View.INVISIBLE);
        //mErrorMessageDisplay.setVisibility(View.VISIBLE);
        switch (mCurrentSort){
            case POPULAR:
                loadPopularMovies();
            case TOP_RATED:
                loadTopRatedMovies();
        }
    }

    private String setMovieURL(int sortBy){
        String movieString;

        switch (sortBy){
            case POPULAR:
                movieString = this.getString(R.string.MOVIE_URL)
                        + this.getString(R.string.popular)
                        + "?api_key=" + this.getString(R.string.api_key);
                mCurrentSort = POPULAR;
                break;
            case TOP_RATED:
                movieString = this.getString(R.string.MOVIE_URL)
                        + "top_rated"
                        + "?api_key=" + this.getString(R.string.api_key);
                mCurrentSort = TOP_RATED;
                break;
            case FAVORITE:
                movieString = "";
                loadFavoriteMovies();
                mCurrentSort = FAVORITE;
                break;
            default:
                throw new NullPointerException("movieString cannot be null");
        }

        return movieString;
    }

    private void loadMovieData(String movieURL){

        new FetchMovieTask(this, new AsyncTaskCompleteListener<Movie>() {

        @Override
        public void onTaskComplete(ArrayList<Movie> movies) {
            movieData = movies;
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(movies != null) {
                mMovieAdapter.setMovieData(movies);
                Log.d("MainActivity.class", Integer.toString(movies.size()));

                if(mRecyclerView.getVisibility() == View.INVISIBLE)
                    mRecyclerView.setVisibility(View.VISIBLE);

                if(mErrorMessageDisplay.getVisibility() == View.VISIBLE)
                    mErrorMessageDisplay.setVisibility(View.INVISIBLE);

                switch (mCurrentSort){
                    case POPULAR:
                        DbUtils.addPopular(MainActivity.this, movies);
                    case TOP_RATED:
                        DbUtils.addTopRated(MainActivity.this, movies);
                }
            }
            else
                loadMoviesWhenOffline();
        }

        @Override
        public void onPostTask() {
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }
        }).execute(movieURL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST, movieData);
        outState.putInt(CURRENT_SORT, mCurrentSort );

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

        if(itemClicked == R.id.action_sort_popular){
            if(movieData != null)
                movieData.clear();
            loadMovieData(setMovieURL(POPULAR));

            return true;
        }
        else if(itemClicked == R.id.action_sort_top_rated){
            if(movieData != null)
                movieData.clear();
            loadMovieData(setMovieURL(TOP_RATED));

            return true;
        }
        else if(itemClicked == R.id.action_sort_favorites){
            loadFavoriteMovies();
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

    public void loadFavoriteMovies(){
        ArrayList<Movie> tMovieData = DbUtils.loadFavorites(this);

        if(tMovieData != null)
        {
            if(movieData != null)
                movieData.clear();

            movieData = tMovieData;
            mMovieAdapter.setMovieData(movieData);
        }
    }

    public void loadPopularMovies(){
        ArrayList<Movie> tMovieData = DbUtils.loadPopular(this);

        if(tMovieData != null)
        {
            if(movieData != null)
                movieData.clear();

            movieData = tMovieData;
            mMovieAdapter.setMovieData(movieData);
        }
    }

    public void loadTopRatedMovies(){
        ArrayList<Movie> tMovieData = DbUtils.loadTopRated(this);

        if(tMovieData != null)
        {
            if(movieData != null)
                movieData.clear();

            movieData = tMovieData;
            mMovieAdapter.setMovieData(movieData);
        }
    }
}
