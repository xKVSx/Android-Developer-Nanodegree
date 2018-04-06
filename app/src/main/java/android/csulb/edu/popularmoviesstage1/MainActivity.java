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

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.mRecycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);

        loadMovieData();
    }

    private void loadMovieData(){
        new FetchMovieTask().execute("https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=9079da83c387d9c6a26c335a693dabbf");
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Movie doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            //This is for testing purposes

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildPopularUrl(params[0]));
                Movie movieData = JsonUtils.parseMovieJson(jsonMovieResponse);
                //return ArrayList of movies?

                return movieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Movie movies) {
            mMovieAdapter.setMovieData(movies);
        }
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //sort the movie thumbnails here
        return super.onOptionsItemSelected(item);
    }
}
