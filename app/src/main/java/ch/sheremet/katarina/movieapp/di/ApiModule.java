package ch.sheremet.katarina.movieapp.di;

import javax.inject.Singleton;

import ch.sheremet.katarina.movieapp.utilities.ApiManager;
import ch.sheremet.katarina.movieapp.utilities.IMoviesApi;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by katarina on 09.04.18.
 */

@Module
public class ApiModule {
    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    @Singleton
    @Provides
    ApiManager apiManager(Retrofit retrofit) {
        return new ApiManager(retrofit.create(IMoviesApi.class));
    }

    @Singleton
    @Provides
    public Retrofit retrofit(OkHttpClient okHttpClient,
                             GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Singleton
    @Provides
    public OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Singleton
    @Provides
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Singleton
    @Provides
    public GsonConverterFactory gsonConverterFactory() {
        return GsonConverterFactory.create();
    }
}
