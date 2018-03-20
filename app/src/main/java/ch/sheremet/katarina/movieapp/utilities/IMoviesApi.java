package ch.sheremet.katarina.movieapp.utilities;

import java.util.List;

import ch.sheremet.katarina.movieapp.model.Movie;
import ch.sheremet.katarina.movieapp.model.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by katarina on 3/15/18.
 */
public interface IMoviesApi {
    @POST("movie/{preference}")
    Call<MoviesResponse> getMovies(@Path("preference") String preference, @Query("api_key") String apiKey);

    @POST("movie/{id}")
    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
