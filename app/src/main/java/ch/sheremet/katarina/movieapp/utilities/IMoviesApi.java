package ch.sheremet.katarina.movieapp.utilities;

import ch.sheremet.katarina.movieapp.model.MoviesResponse;
import ch.sheremet.katarina.movieapp.model.ReviewsResponse;
import ch.sheremet.katarina.movieapp.model.TrailersResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by katarina on 3/15/18.
 */
public interface IMoviesApi {
    @GET("movie/{preference}")
    Call<MoviesResponse> getMovies(@Path("preference") String preference, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailersResponse> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);
}
