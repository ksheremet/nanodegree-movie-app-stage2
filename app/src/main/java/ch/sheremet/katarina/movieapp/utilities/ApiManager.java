package ch.sheremet.katarina.movieapp.utilities;

import ch.sheremet.katarina.movieapp.BuildConfig;
import ch.sheremet.katarina.movieapp.model.MoviesResponse;
import ch.sheremet.katarina.movieapp.model.ReviewsResponse;
import ch.sheremet.katarina.movieapp.model.TrailersResponse;
import retrofit2.Call;
import retrofit2.Callback;

public class ApiManager {
    private final IMoviesApi service;

    private static final String MOVIE_POPULAR_PATH = "popular";
    private static final String MOVIE_TOP_RATED_PATH = "top_rated";
    private static final String API_KEY = BuildConfig.API_KEY;

    public ApiManager(IMoviesApi moviesApi) {
        this.service = moviesApi;
    }

    public void getPopularMovies(final Callback<MoviesResponse> callback) {
        Call<MoviesResponse> popularMoviesCall = service.getMovies(MOVIE_POPULAR_PATH, API_KEY);
        popularMoviesCall.enqueue(callback);

    }

    public void getTopRatedMovies(final Callback<MoviesResponse> callback) {
        Call<MoviesResponse> topRatedMoviesCall = service.getMovies(MOVIE_TOP_RATED_PATH, API_KEY);
        topRatedMoviesCall.enqueue(callback);
    }

    public void getMovieTrailers(final Callback<TrailersResponse> callback, final int movieId) {
        Call<TrailersResponse> trailersCall = service.getTrailers(movieId, API_KEY);
        trailersCall.enqueue(callback);
    }

    public void getMovieReviews(final Callback<ReviewsResponse> callback, final int movieId) {
        Call<ReviewsResponse> reviewsCall = service.getReviews(movieId, API_KEY);
        reviewsCall.enqueue(callback);
    }
}
