package ch.sheremet.katarina.movieapp.utilities;

import java.io.IOException;

import ch.sheremet.katarina.movieapp.BuildConfig;
import ch.sheremet.katarina.movieapp.model.MoviesResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static IMoviesApi service;
    private static ApiManager apiManager;

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static final String MOVIE_POPULAR_PATH = "popular";
    private static final String MOVIE_TOP_RATED_PATH = "top_rated";

    private ApiManager() {
        // For logging
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IMoviesApi.class);
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public void getPopularMovies(Callback<MoviesResponse> callback) {
        Call<MoviesResponse> popularMoviesCall = service.getMovies(MOVIE_POPULAR_PATH, BuildConfig.API_KEY);
        popularMoviesCall.enqueue(callback);

    }

    public void getTopRatedMovies(Callback<MoviesResponse> callback) {
        Call<MoviesResponse> topRatedMoviesCall = service.getMovies(MOVIE_TOP_RATED_PATH, BuildConfig.API_KEY);
        topRatedMoviesCall.enqueue(callback);
    }
}
