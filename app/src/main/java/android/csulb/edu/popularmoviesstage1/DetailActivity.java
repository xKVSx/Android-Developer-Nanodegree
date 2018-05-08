package android.csulb.edu.popularmoviesstage1;

import android.content.Intent;
import android.csulb.edu.popularmoviesstage1.utils.DbUtils;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    public static final int ADD_FAVORITE = 0; //if column is 0, it's not a favorite, thus add it
    public static final int REMOVE_FAVORITE = 1; //if column is 1, it is a favorite, so remove it

    ImageButton addFavoriteButton;

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imageDetail = findViewById(R.id.detail_image);
        TextView title = findViewById(R.id.movie_title);
        TextView release_date = findViewById(R.id.release_date);
        TextView ratingView = findViewById(R.id.rating);
        TextView plotView = findViewById(R.id.plot);
        addFavoriteButton = findViewById(R.id.addFavorite);

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
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private boolean checkMovie(){
        /*this method will check if the movie has already been added to the database*/
        return true;
    }

    private void addMovie(){

    }

    private void removeFavorite(){
        /*this method updates a movie currently in the database, removing it as a favorite. Same
        * as addFavorite with the 0 instead of 1*/

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
}

