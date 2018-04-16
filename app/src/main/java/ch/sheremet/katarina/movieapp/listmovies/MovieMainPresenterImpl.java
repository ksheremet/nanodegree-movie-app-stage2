package ch.sheremet.katarina.movieapp.listmovies;

import android.util.Log;

import javax.inject.Inject;

import ch.sheremet.katarina.movieapp.model.MoviesResponse;
import ch.sheremet.katarina.movieapp.utilities.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Presenter for MovieMainActivity.
 */
public class MovieMainPresenterImpl implements IMovieMainPresenter {

    private static final String TAG = MovieMainPresenterImpl.class.getSimpleName();

    private Callback<MoviesResponse> mMoviesCallback;
    private ApiManager mApiManager;
    private IMovieMainView mView;

    @Inject
    public MovieMainPresenterImpl(final IMovieMainView view, final ApiManager apiManager) {
        this.mView = view;
        this.mApiManager = apiManager;
        mMoviesCallback = new Callback<MoviesResponse>() {
            @Override
            public void onResponse(final Call<MoviesResponse> call,
                                   final Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    mView.showMovieData(response.body().getMovies());
                } else {
                    mView.showErrorOccurredMessage();
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                mView.showErrorOccurredMessage();
                Log.e(TAG, "Error getting movies", t);
            }
        };
    }

    @Override
    public void getPopularMovies() {
        mApiManager.getPopularMovies(mMoviesCallback);
    }

    @Override
    public void getTopRatedMovies() {
        mApiManager.getTopRatedMovies(mMoviesCallback);
    }
}
