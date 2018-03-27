package ch.sheremet.katarina.movieapp.reviewdetail;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ch.sheremet.katarina.movieapp.R;
import ch.sheremet.katarina.movieapp.model.Review;

/**
 * Shows full review.
 */
public class ReviewDetailFragment extends DialogFragment {

    public static final String REVIEW_PARAM = "review";

    public static ReviewDetailFragment newInstance(final Review review) {
        ReviewDetailFragment dialogFragment = new ReviewDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ReviewDetailFragment.REVIEW_PARAM, review);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.review_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null || !getArguments().containsKey(REVIEW_PARAM)) {
            dismiss();
        }
        Review review = getArguments().getParcelable(REVIEW_PARAM);
        TextView author = view.findViewById(R.id.review_author_detail_tv);
        author.setText(review.getAuthor());
        TextView content =  view.findViewById(R.id.review_content_detail_tv);
        content.setText(review.getContent());
        final Button dismiss = view.findViewById(R.id.dismiss_button);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
