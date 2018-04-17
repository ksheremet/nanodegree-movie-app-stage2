package ch.sheremet.katarina.movieapp.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.sheremet.katarina.movieapp.R;
import ch.sheremet.katarina.movieapp.base.BaseActivity;
import ch.sheremet.katarina.movieapp.di.DaggerMovieDetailComponent;
import ch.sheremet.katarina.movieapp.di.MovieDetailComponent;
import ch.sheremet.katarina.movieapp.di.MovieDetailModule;
import ch.sheremet.katarina.movieapp.favouritemovies.data.FavouriteMoviesUtil;
import ch.sheremet.katarina.movieapp.favouritemovies.data.MoviesContract;
import ch.sheremet.katarina.movieapp.favouritemovies.loaders.FetchFavouriteMoviesLoader;
import ch.sheremet.katarina.movieapp.favouritemovies.loaders.InsertFavouriteMoviesLoader;
import ch.sheremet.katarina.movieapp.favouritemovies.loaders.RemoveFavouriteMoviesLoader;
import ch.sheremet.katarina.movieapp.model.Movie;
import ch.sheremet.katarina.movieapp.model.Review;
import ch.sheremet.katarina.movieapp.model.Trailer;
import ch.sheremet.katarina.movieapp.reviewdetail.ReviewDetailFragment;
import ch.sheremet.katarina.movieapp.utilities.UriUtil;

public class MovieDetailActivity extends BaseActivity
        implements TrailerAdapter.TrailerAdapterOnClickHandler,
        ReviewAdapter.ReviewAdapterOnClickHandler,
        IMovieDetailView {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private static final String MOVIE_PARAM = "movie";
    private static final String REVIEW_FRAGMENT_TAG = "review_detail";
    private static final int FAVOURITE_MOVIE_LOADER_ID = 111;
    private static final int REMOVE_FAVOURITE_MOVIE_LOADER_ID = 222;
    private static final int ADD_FAVOURITE_MOVIE_LOADER_ID = 333;

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
    @BindView(R.id.add_to_favourite_iv)
    ImageView mFavouriteMovieImageView;
    @Inject
    IMovieDetailPresenter mPresenter;
    private TrailerAdapter mTrailersAdapter;
    private ReviewAdapter mReviewsAdapter;
    private Movie mMovie;
    private boolean mIsMovieFavourite;
    private Uri mFirstTrailerUri = null;
    private LoaderManager.LoaderCallbacks<Cursor> mFavouriteMovieLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @NonNull
                @Override
                public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                    Uri favouriteMovieUri = MoviesContract.MovieEntry.CONTENT_URI.buildUpon()
                            .appendPath(String.valueOf(mMovie.getId())).build();
                    return new FetchFavouriteMoviesLoader(MovieDetailActivity.this,
                            favouriteMovieUri, false);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
                    mIsMovieFavourite = cursor != null && cursor.moveToFirst();
                    setFavouriteButtonBackground(mIsMovieFavourite);
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Cursor> loader) {

                }
            };
    private LoaderManager.LoaderCallbacks<Integer> mRemoveMovieFromFavouriteLoader =
            new LoaderManager.LoaderCallbacks<Integer>() {
                @NonNull
                @Override
                public Loader<Integer> onCreateLoader(final int id,
                                                      final @Nullable Bundle args) {
                    Uri favouriteMovieUri = MoviesContract.MovieEntry.CONTENT_URI.buildUpon()
                            .appendPath(String.valueOf(mMovie.getId())).build();
                    return new RemoveFavouriteMoviesLoader(MovieDetailActivity.this, favouriteMovieUri);
                }

                @Override
                public void onLoadFinished(@NonNull final Loader<Integer> loader,
                                           final Integer numberOfRemovedRows) {
                    if (numberOfRemovedRows == 1) {
                        Toast.makeText(MovieDetailActivity.this,
                                R.string.remove_from_favourite_user_message, Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(MovieDetailActivity.this,
                                R.string.error_occurred_error_message, Toast.LENGTH_SHORT)
                                .show();
                        Log.e(TAG, "Movie wasn't removed from favourite");
                    }

                }

                @Override
                public void onLoaderReset(final @NonNull Loader<Integer> loader) {

                }
            };
    private LoaderManager.LoaderCallbacks<Uri> mAddFavouriteMovieLoader =
            new LoaderManager.LoaderCallbacks<Uri>() {
                @NonNull
                @Override
                public Loader<Uri> onCreateLoader(final int id, final @Nullable Bundle args) {
                    return new InsertFavouriteMoviesLoader(MovieDetailActivity.this,
                            MoviesContract.MovieEntry.CONTENT_URI,
                            FavouriteMoviesUtil.movieToContentValues(mMovie));
                }

                @Override
                public void onLoadFinished(final @NonNull Loader<Uri> loader, final Uri data) {
                    if (data != null) {
                        Toast.makeText(MovieDetailActivity.this,
                                R.string.movie_added_to_favourite_user_message, Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(MovieDetailActivity.this,
                                R.string.error_occurred_error_message, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onLoaderReset(final @NonNull Loader<Uri> loader) {

                }
            };

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

        setToolbar();
        setMovieDetailsViews();

        MovieDetailComponent component = DaggerMovieDetailComponent
                .builder()
                .movieDetailModule(new MovieDetailModule(this))
                .build();
        component.injectMovieDetailActivity(this);
        setTrailersView();
        setReviewsView();
        isMovieFavourite();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(mMovie.getOriginalTitle());
        setTitle(mMovie.getOriginalTitle());
        Picasso.get()
                .load(UriUtil.buildPosterUrl(mMovie.getPoster()))
                .error(R.drawable.film_reel)
                .placeholder(R.drawable.film_reel)
                .into((ImageView) findViewById(R.id.app_bar_image));
    }

    private void setMovieDetailsViews() {
        Picasso.get()
                .load(UriUtil.buildPosterUrl(mMovie.getPoster()))
                .error(R.drawable.film_reel)
                .placeholder(R.drawable.film_reel)
                .into(mPosterIV);
        mPosterIV.setContentDescription(mMovie.getPlotSynopsis());
        mTitleTV.setText(mMovie.getOriginalTitle());
        mPlotSummaryTV.setText(mMovie.getPlotSynopsis());
        mRatingTV.setText(getString(R.string.rating_detail_tv,
                String.valueOf(mMovie.getUserRating())));
        mReleaseDateTV.setText(getString(R.string.release_detail_tv, mMovie.getReleaseDate()));
    }

    private void setTrailersView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(linearLayoutManager);
        mTrailersAdapter = new TrailerAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
        mPresenter.getTrailers(mMovie.getId());
    }

    private void setReviewsView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(linearLayoutManager);
        mReviewsAdapter = new ReviewAdapter(this);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
        mPresenter.getReviews(mMovie.getId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.share_movie_menu:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                if (mFirstTrailerUri != null) {
                    emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_trailer,
                            mMovie.getOriginalTitle(), mFirstTrailerUri));
                } else {
                    emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_name,
                            mMovie.getOriginalTitle()));
                }
                startActivity(Intent.createChooser(emailIntent,
                        getString(R.string.share_movie_chooser)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void showReviews(final List<Review> reviews) {
        mReviewsAdapter.setReviews(reviews);
    }

    @Override
    public void showTrailers(final List<Trailer> trailers) {
        if (!trailers.isEmpty()) {
            mFirstTrailerUri = Uri.parse(UriUtil.buildYoutubeVideoUrl(trailers.get(0).getKey()));
        }
        mTrailersAdapter.setTrailers(trailers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_detail_menu, menu);
        return true;
    }

    @OnClick(R.id.add_to_favourite_iv)
    protected void onFavouriteMovieClick() {
        if (mIsMovieFavourite) {
            mIsMovieFavourite = false;
            removeMovieFromFavourite();
        } else {
            mIsMovieFavourite = true;
            addMovieToFavourite();
        }
        setFavouriteButtonBackground(mIsMovieFavourite);
    }

    private void setFavouriteButtonBackground(boolean isFavourite) {
        if (isFavourite) {
            mFavouriteMovieImageView.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            mFavouriteMovieImageView.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }

    private void addMovieToFavourite() {
        if (getSupportLoaderManager().getLoader(ADD_FAVOURITE_MOVIE_LOADER_ID) == null) {
            getSupportLoaderManager()
                    .initLoader(ADD_FAVOURITE_MOVIE_LOADER_ID, null, mAddFavouriteMovieLoader);
        } else {
            getSupportLoaderManager()
                    .restartLoader(ADD_FAVOURITE_MOVIE_LOADER_ID, null, mAddFavouriteMovieLoader);
        }
    }

    private void removeMovieFromFavourite() {
        if (getSupportLoaderManager().getLoader(REMOVE_FAVOURITE_MOVIE_LOADER_ID) == null) {
            getSupportLoaderManager()
                    .initLoader(REMOVE_FAVOURITE_MOVIE_LOADER_ID, null, mRemoveMovieFromFavouriteLoader);
        } else {
            getSupportLoaderManager()
                    .restartLoader(REMOVE_FAVOURITE_MOVIE_LOADER_ID, null, mRemoveMovieFromFavouriteLoader);
        }
    }

    private void isMovieFavourite() {
        if (getSupportLoaderManager().getLoader(FAVOURITE_MOVIE_LOADER_ID) == null) {
            getSupportLoaderManager().initLoader(FAVOURITE_MOVIE_LOADER_ID, null, mFavouriteMovieLoader);
        } else {
            getSupportLoaderManager().restartLoader(FAVOURITE_MOVIE_LOADER_ID, null, mFavouriteMovieLoader);
        }
    }
}
