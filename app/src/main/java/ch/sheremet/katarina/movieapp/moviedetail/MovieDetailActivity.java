package ch.sheremet.katarina.movieapp.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.sheremet.katarina.movieapp.MovieMainApplication;
import ch.sheremet.katarina.movieapp.R;
import ch.sheremet.katarina.movieapp.model.Movie;
import ch.sheremet.katarina.movieapp.model.Review;
import ch.sheremet.katarina.movieapp.model.ReviewsResponse;
import ch.sheremet.katarina.movieapp.model.Trailer;
import ch.sheremet.katarina.movieapp.model.TrailersResponse;
import ch.sheremet.katarina.movieapp.reviewdetail.ReviewDetailFragment;
import ch.sheremet.katarina.movieapp.utilities.UriUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity
        implements TrailerAdapter.TrailerAdapterOnClickHandler,
        ReviewAdapter.ReviewAdapterOnClickHandler {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private static final String MOVIE_PARAM = "movie";
    private static final String REVIEW_FRAGMENT_TAG = "review_detail";

    @BindView(R.id.detail_movie_poster_iv)
    ImageView mPosterIV;
    @BindView(R.id.detail_movie_title_tv)
    TextView mTitleTV;
    @BindView(R.id.detail_plot_summary_tv)
    TextView mPlotSummaryTV;
    @BindView(R.id.detail_rating_tv)
    TextView mRatingTV;
    @BindView(R.id.detail_release_date_tv)
    TextView mReleaseDateTV;
    @BindView(R.id.trailers_rv)
    RecyclerView mTrailersRecyclerView;
    @BindView(R.id.reviews_rv)
    RecyclerView mReviewsRecyclerView;

    private TrailerAdapter mTrailersAdapter;
    private ReviewAdapter mReviewsAdapter;
    private Movie mMovie;

    public static void start(final Context context, final Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MOVIE_PARAM, movie);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if ((getIntent() == null) || (!getIntent().hasExtra(MOVIE_PARAM))) {
            finish();
        }
        ButterKnife.bind(this);
        mMovie = getIntent().getParcelableExtra(MOVIE_PARAM);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(mMovie.getOriginalTitle());
        Picasso.get()
                .load(UriUtil.buildPosterUrl(mMovie.getPoster()))
                .into(mPosterIV);
        mPosterIV.setContentDescription(mMovie.getPlotSynopsis());
        mTitleTV.setText(mMovie.getOriginalTitle());
        mPlotSummaryTV.setText(mMovie.getPlotSynopsis());
        mRatingTV.setText(getString(R.string.rating_detail_tv, mMovie.getUserRating()));
        mReleaseDateTV.setText(getString(R.string.release_detail_tv, mMovie.getReleaseDate()));
        setTrailersView();
        setReviewsView();
    }

    private void setTrailersView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(linearLayoutManager);
        mTrailersAdapter = new TrailerAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);

        // TODO: implement this in Presenter
        MovieMainApplication.apiManager.getMovieTrailers(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(final Call<TrailersResponse> call,
                                   final Response<TrailersResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, response.body().getTrailers().toString());
                    mTrailersAdapter.setTrailers(response.body().getTrailers());
                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching trailers", t);
            }
        }, mMovie.getId());
    }

    private void setReviewsView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);
        mReviewsRecyclerView.setLayoutManager(linearLayoutManager);
        mReviewsAdapter = new ReviewAdapter(this);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);


        // TODO: implement this in Presenter
        MovieMainApplication.apiManager.getMovieReviews(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(final Call<ReviewsResponse> call,
                                   final Response<ReviewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, response.body().getReviews().toString());
                    mReviewsAdapter.setReviews(response.body().getReviews());
                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching trailers", t);
            }
        }, mMovie.getId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrailerClick(Trailer trailer) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(UriUtil.buildYoutubeVideoUrl(trailer.getKey()))));
    }

    @Override
    public void onReviewClick(Review review) {
        ReviewDetailFragment.newInstance(review)
                .show(getSupportFragmentManager(), REVIEW_FRAGMENT_TAG);
    }
}
