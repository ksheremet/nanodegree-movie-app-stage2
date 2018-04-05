package ch.sheremet.katarina.movieapp.moviedetail;

import android.util.Log;

import ch.sheremet.katarina.movieapp.model.ReviewsResponse;
import ch.sheremet.katarina.movieapp.model.TrailersResponse;
import ch.sheremet.katarina.movieapp.utilities.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Presenter for MovieDetailActivity.
 */
public class MovieDetailPresenterImpl implements IMovieDetailPresenter {

    private static final String TAG = MovieDetailPresenterImpl.class.getSimpleName();

    private IMovieDetailView mView;
    private ApiManager mApiManager = ApiManager.getInstance();

    public MovieDetailPresenterImpl(IMovieDetailView view) {
        this.mView = view;
    }

    @Override
    public void getReviews(int movieId) {
        mApiManager.getMovieReviews(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(final Call<ReviewsResponse> call,
                                   final Response<ReviewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, response.body().getReviews().toString());
                    mView.showReviews(response.body().getReviews());
                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching trailers", t);
            }
        }, movieId);
    }

    @Override
    public void getTrailers(int movieId) {
        mApiManager.getMovieTrailers(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(final Call<TrailersResponse> call,
                                   final Response<TrailersResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, response.body().getTrailers().toString());
                    mView.showTrailers(response.body().getTrailers());
                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching trailers", t);
            }
        }, movieId);
    }
}
