package ch.sheremet.katarina.movieapp.moviedetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.sheremet.katarina.movieapp.R;
import ch.sheremet.katarina.movieapp.model.Review;

/**
 * Created by katarina on 22.03.18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private final ReviewAdapter.ReviewAdapterOnClickHandler mOnReviewClickHandler;
    private List<Review> mReviews;

    public ReviewAdapter(ReviewAdapter.ReviewAdapterOnClickHandler onReviewClickHandler) {
        this.mOnReviewClickHandler = onReviewClickHandler;
    }

    public void setReviews(List<Review> reviews) {
        this.mReviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewAdapter.ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        holder.mAuthorReview.setText(mReviews.get(position).getAuthor());
        holder.mContentReview.setText(mReviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) {
            return 0;
        }
        return mReviews.size();
    }

    public interface ReviewAdapterOnClickHandler {
        void onReviewClick(Review review);
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final TextView mAuthorReview;
        private final TextView mContentReview;

        ReviewAdapterViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mAuthorReview = itemView.findViewById(R.id.review_author_tv);
            mContentReview = itemView.findViewById(R.id.review_content_detail_tv);
        }

        @Override
        public void onClick(View v) {
            mOnReviewClickHandler.onReviewClick(mReviews.get(getAdapterPosition()));
        }
    }
}
