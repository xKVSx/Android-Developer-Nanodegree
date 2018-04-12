package android.csulb.edu.popularmoviesstage1;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imageDetail = findViewById(R.id.detail_image);
        TextView title = findViewById(R.id.movie_title);
        TextView release_date = findViewById(R.id.release_date);
        TextView ratingView = findViewById(R.id.rating);
        TextView plotView = findViewById(R.id.plot);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        //Display the movie image
        Movie movie = intent.getParcelableExtra(EXTRA_POSITION);
        String BASE_URL = this.getString(R.string.BASE_URL);
        String IMAGE_SIZE = this.getString(R.string.thumbSize6);
        Log.d("DetailActivity.class", BASE_URL + IMAGE_SIZE + "/" + movie.getImage());
        Picasso.with(this).load(BASE_URL + IMAGE_SIZE + "/" + movie.getImage()).into(imageDetail);

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
}

