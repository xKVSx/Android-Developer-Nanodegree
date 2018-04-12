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

    //{"w92, w154, w185, w342, w500, w780, original"} are different poster image sizes.
    // w185 is the recommended size for most phones.

    private ArrayList<Movie> movies = new ArrayList();
    private Context context;
    final private ListItemClickListener mOnClickListener;

    public MovieAdapter(Context context, ListItemClickListener listener){
        this.context = context;
        mOnClickListener = listener;
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

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView thumbnail;

        public MovieViewHolder(View itemView){
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public void setMovieData(ArrayList<Movie> movieData) {
        movies = movieData;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }
}
