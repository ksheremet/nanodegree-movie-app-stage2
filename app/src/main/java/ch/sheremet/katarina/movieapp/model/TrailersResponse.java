package ch.sheremet.katarina.movieapp.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResponse {
    @SerializedName("id")
    private int mMovieId;
    @SerializedName("results")
    List<Trailer>  mTrailers;

    public List<Trailer> getTrailers() {
        return mTrailers;
    }
}
