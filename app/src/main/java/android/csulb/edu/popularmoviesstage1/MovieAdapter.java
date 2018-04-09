package android.csulb.edu.popularmoviesstage1;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    //private static final String BASE_URL = "http://image.tmdb.org/t/p/";

    //These are the different poster image sizes. w185 is the recommended size for most phones.
    //private static final String[] SIZE = {"w92, w154, w185, w342, w500, w780, original"};

    private ArrayList<Movie> movies = new ArrayList();
    private Context context;

    public MovieAdapter(Context context){
        this.context = context;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View thumbView = inflater.inflate(R.layout.cards_layout, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(thumbView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        ImageView imageView = holder.thumbnail;
        String BASE_URL = context.getString(R.string.BASE_URL);
        String IMAGE_SIZE = context.getString(R.string.thumbSize3);
        Picasso.with(context).load(BASE_URL + IMAGE_SIZE + "/" + movie.getImage()).into(imageView);
    }

    @Override
    public int getItemCount() {
        if(movies == null)
            return 0;
        else
            return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        public final ImageView thumbnail;

        public MovieViewHolder(View itemView){
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.movie_image);
        }
    }

    public void setMovieData(ArrayList<Movie> movieData) {
        movies = movieData;
        notifyDataSetChanged();
    }
}
