package ch.sheremet.katarina.movieapp.listmovies;

import android.util.Log;

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

    private IMovieMainView mView;
    private Callback<MoviesResponse> mMoviesCallback;
    // TODO(ksheremet): Dagger2
    private final ApiManager mApiManager = ApiManager.getInstance();


    public MovieMainPresenterImpl(final IMovieMainView view) {
        this.mView = view;
        mMoviesCallback = new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
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

    @Override
    public void getFavouriteMovies() {

    }
}
