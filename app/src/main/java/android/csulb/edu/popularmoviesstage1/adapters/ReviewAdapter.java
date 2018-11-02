package android.csulb.edu.popularmoviesstage1.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.csulb.edu.popularmoviesstage1.R;
import android.csulb.edu.popularmoviesstage1.Review;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<Review> reviews = new ArrayList();
    private Context context;

    public ReviewAdapter(Context context){
        this.context = context;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        public final TextView review_author;
        public final TextView review_content;

        public ReviewViewHolder(View itemView){
            super(itemView);
            review_author = itemView.findViewById(R.id.text_author);
            review_content = itemView.findViewById(R.id.text_review);
        }
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View reviewView = inflater.inflate(R.layout.review_layout, parent, false);
        ReviewViewHolder viewHolder = new ReviewViewHolder(reviewView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        TextView author = holder.review_author;
        TextView content = holder.review_content;

        author.setText(reviews.get(position).getAuthor());
        content.setText(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviewList(ArrayList<Review> reviewList) {
        reviews = reviewList;
        notifyDataSetChanged();
    }

    public static class DividerItemDecoration extends RecyclerView.ItemDecoration{
        private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

        private Drawable divider;

        public DividerItemDecoration(Context context){
            final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
            divider = styledAttributes.getDrawable(0);
            styledAttributes.recycle();
        }

        public DividerItemDecoration(Context context, int resId){
            divider = ContextCompat.getDrawable(context, resId);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            int childCount = parent.getChildCount();

            for(int i = 0; i < childCount; i++){
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }
}
