package android.csulb.edu.popularmoviesstage1;

import android.content.Intent;
import android.csulb.edu.popularmoviesstage1.adapters.ReviewAdapter;
import android.csulb.edu.popularmoviesstage1.adapters.TrailerAdapter;
import android.csulb.edu.popularmoviesstage1.backgroundtasks.AsyncTaskCompleteListener;
import android.csulb.edu.popularmoviesstage1.backgroundtasks.FetchReviewTask;
import android.csulb.edu.popularmoviesstage1.backgroundtasks.FetchTrailerTask;
import android.csulb.edu.popularmoviesstage1.utils.DbUtils;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener {

    public static final String EXTRA_POSITION = "extra_position";
    public static final int ADD_FAVORITE = 0; //if column is 0, it's not a favorite, thus add it
    public static final int REMOVE_FAVORITE = 1; //if column is 1, it is a favorite, so remove it
    public static final String REVIEW_LIST = "review_list";
    public static final String MOVIE_ID = "movie_id";
    public static final String TRAILER_LIST = "trailer_list";
    public static final String TRAILER_SCROLL_POSITION = "trailer_scroll_position";
    public static final String SCROLLY = "scroll_y";
    public static final String SCROLLX = "scroll_x";

    private String trailerUrlString;
    private String reviewUrlString;
    private ArrayList<Trailer> trailers = new ArrayList<>();
    private ArrayList<Review> reviews = new ArrayList<>();
    private ImageButton addFavoriteButton;
    private Movie movie;

    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;

    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;

    private Parcelable mTrailerListState; //used to save scroll position when screen is rotated
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imageDetail = findViewById(R.id.detail_image);
        TextView title = findViewById(R.id.movie_title);
        TextView release_date = findViewById(R.id.release_date);
        TextView ratingView = findViewById(R.id.rating);
        TextView plotView = findViewById(R.id.plot);
        TextView trailerHeader = findViewById(R.id.trailerHeading);
        addFavoriteButton = findViewById(R.id.addFavorite);
        mScrollView = findViewById(R.id.detail_scroll_view);

        //Set up the trailer recycler view and adapter
        mTrailerRecyclerView = findViewById(R.id.trailer_recyclerView);
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerRecyclerView.addItemDecoration(new TrailerAdapter.HorizontalSpaceItemDecoration(4));
        mTrailerAdapter = new TrailerAdapter(this, this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        //Set up the review recycler view and adapter
        mReviewRecyclerView = findViewById(R.id.review_recyclerView);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.addItemDecoration(new ReviewAdapter.DividerItemDecoration(this));
        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
        mReviewAdapter = new ReviewAdapter(this);
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        //Display the movie image
        movie = intent.getParcelableExtra(EXTRA_POSITION);
        String BASE_URL = this.getString(R.string.BASE_URL);
        String IMAGE_SIZE = this.getString(R.string.thumbSize6);
        Picasso.with(this).load(BASE_URL + IMAGE_SIZE + "/" + movie.getImage()).into(imageDetail);

        //Set the addFavoriteButton image. If it's currently a favorite, set to red, if not, set to white
        int favorite = DbUtils.isFavorite(this, movie);
        if(favorite == 1)
            addFavoriteButton.setImageResource(R.drawable.ic_favorite_red);
        else if(favorite == 0)
            addFavoriteButton.setImageResource(R.drawable.ic_favorite_white);

        //Display the title
        title.setText(movie.getTitle());

        //Display the release data
        String boldTextDate = this.getString(R.string.release_date);
        SpannableString date = new SpannableString(boldTextDate + movie.getRelease());
        date.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTextDate.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        release_date.setText(date);

        //Display movie rating
        String boldTextRating = this.getString(R.string.rating);
        SpannableString rating = new SpannableString(boldTextRating + movie.getRating());
        rating.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTextRating.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ratingView.setText(rating);

        //Display plot
        String boldTextPlot = this.getString(R.string.plot);
        SpannableString plot = new SpannableString(boldTextPlot + movie.getPlot());
        plot.setSpan(new StyleSpan(Typeface.BOLD),0, boldTextPlot.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        plotView.setText(plot);

        //Build trailer Url
        trailerUrlString = this.getString(R.string.MOVIE_URL) + movie.getId() + "/videos?api_key=" + getString(R.string.api_key);
        //Build review Url
        reviewUrlString = this.getString(R.string.MOVIE_URL) + movie.getId() + "/reviews?api_key=" + getString(R.string.api_key);

        if(savedInstanceState == null){
            loadReviewData(reviewUrlString);
        }
        else {
            String currentId = savedInstanceState.getString(MOVIE_ID);

            if(currentId.equals(movie.getId())){
                reviews = savedInstanceState.getParcelableArrayList(REVIEW_LIST);
                mReviewAdapter.setReviewList(reviews);

                trailers = savedInstanceState.getParcelableArrayList(TRAILER_LIST);
                mTrailerAdapter.setTrailerList(trailers);

                mTrailerListState = savedInstanceState.getParcelable(TRAILER_SCROLL_POSITION);
                mTrailerRecyclerView.getLayoutManager().onRestoreInstanceState(mTrailerListState);

                int x = savedInstanceState.getInt(SCROLLX);
                int y = savedInstanceState.getInt(SCROLLY);
                System.out.println(x);
                System.out.println(y);
                mScrollView.scrollTo(x, y);
            }
        }

        loadTrailerData(trailerUrlString);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    public void toggleFavorite(View view){
        switch (DbUtils.isFavorite(this, movie)){
            case ADD_FAVORITE:
                DbUtils.addFavorite(this, movie);
                addFavoriteButton.setImageResource(R.drawable.ic_favorite_red);
                break;
            case REMOVE_FAVORITE:
                DbUtils.removeFavorite(this, movie);
                addFavoriteButton.setImageResource(R.drawable.ic_favorite_white);
                break;
            default:
                Log.d("DetailActivity.java", "Movie has not been added to the database");
        }
    }

    private void loadTrailerData(String trailerUrlString){
        new FetchTrailerTask(this, new AsyncTaskCompleteListener<Trailer>() {

            @Override
            public void onTaskComplete(ArrayList<Trailer> result) {
                trailers = result;

                if(trailers != null)
                    mTrailerAdapter.setTrailerList(trailers);
            }

            @Override
            public void onPostTask() {

            }
        }).execute(trailerUrlString);
    }

    private void loadReviewData(String reviewUrlString){
        new FetchReviewTask(this, new AsyncTaskCompleteListener<Review>() {

            @Override
            public void onTaskComplete(ArrayList<Review> result) {
                reviews = result;

                if(reviews != null) {
                    mReviewAdapter.setReviewList(reviews);
                }
            }

            @Override
            public void onPostTask() {

            }
        }).execute(reviewUrlString);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String movieKey = trailers.get(clickedItemIndex).getKey();
        Intent launchYouTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_url) + movieKey));
        launchYouTubeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(launchYouTubeIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(REVIEW_LIST, reviews);
        outState.putParcelableArrayList(TRAILER_LIST, trailers);

        /*the movie id is used to determine when to clear the review and trailer lists. If it is
        the same move (user rotated screen) no need to clear, just save and reload. If it is a
        different movie, then we must clear and load the new data*/
        outState.putString(MOVIE_ID, movie.getId());

        mTrailerListState = mTrailerRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(TRAILER_SCROLL_POSITION, mTrailerListState);

        outState.putInt(SCROLLY, mScrollView.getScrollY());
        outState.putInt(SCROLLX, mScrollView.getScrollX());

        super.onSaveInstanceState(outState);
    }
}

