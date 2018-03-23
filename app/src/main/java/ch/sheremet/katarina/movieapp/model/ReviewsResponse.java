package ch.sheremet.katarina.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by katarina on 22.03.18.
 */

public class ReviewsResponse {
    @SerializedName("id")
    private String mId;
    @SerializedName("results")
    List<Review> mReviews;

    public List<Review> getReviews() {
        return this.mReviews;
    }
}
