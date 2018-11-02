package android.csulb.edu.popularmoviesstage1.adapters;

import android.content.Context;
import android.csulb.edu.popularmoviesstage1.R;
import android.csulb.edu.popularmoviesstage1.Trailer;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private ArrayList<Trailer> trailers = new ArrayList();
    private Context context;
    final private TrailerAdapter.ListItemClickListener mOnClickListener;

    public TrailerAdapter(Context context, TrailerAdapter.ListItemClickListener listener){
        this.context = context;
        mOnClickListener = listener;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView trailer_image;

        public TrailerViewHolder(View itemView){
            super(itemView);
            trailer_image = (ImageView) itemView.findViewById(R.id.trailer_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View thumbView = inflater.inflate(R.layout.trailer_layout, parent, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(thumbView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, int position) {
        ImageView imageView = holder.trailer_image;
        String videoClip = "http://img.youtube.com/vi/" + trailers.get(position).getKey() + "/0.jpg";
        Picasso.with(context).load(videoClip).into(imageView);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public void setTrailerList(ArrayList<Trailer> trailerList) {
        trailers = trailerList;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    public static class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration{
        private int HorizontalSpace;

        public HorizontalSpaceItemDecoration(int HorizontalSpace){
            this.HorizontalSpace = HorizontalSpace;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.right = HorizontalSpace;
        }
    }
}
