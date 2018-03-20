package ch.sheremet.katarina.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {
    @SerializedName("mPage")
    private int mPage;
    @SerializedName("results")
    private List<Movie> mMovies;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public List<Movie> getMovies() {
        return mMovies;
    }
}
