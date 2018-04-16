package ch.sheremet.katarina.movieapp.listmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.sheremet.katarina.movieapp.R;
import ch.sheremet.katarina.movieapp.base.BaseActivity;
import ch.sheremet.katarina.movieapp.di.DaggerMovieMainComponent;
import ch.sheremet.katarina.movieapp.di.MovieMainComponent;
import ch.sheremet.katarina.movieapp.di.MovieMainModule;
import ch.sheremet.katarina.movieapp.favouritemovies.data.MoviesContract;
import ch.sheremet.katarina.movieapp.favouritemovies.loaders.FetchFavouriteMoviesLoader;
import ch.sheremet.katarina.movieapp.model.Movie;
import ch.sheremet.katarina.movieapp.moviedetail.MovieDetailActivity;

public class MovieMainActivity extends BaseActivity
        implements MovieAdapter.MovieAdapterOnClickHandler,
        IMovieMainView,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MovieMainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 123;
    @BindView(R.id.movie_rv)
    RecyclerView mMoviesRecyclerView;
    @BindView(R.id.error_message_tv)
    TextView mErrorMessage;
    @BindView(R.id.loading_movies_pb)
    ProgressBar mLoadingMoviesIndicator;
    private MovieAdapter mMovieAdapter;
    private SharedPreferences mMoviePref;
    @Inject
    IMovieMainPresenter mPresenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);
        ButterKnife.bind(this);
        int spanCount = getResources().getInteger(R.integer.grid_span_count);
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        mMoviePref = getPreferences(Context.MODE_PRIVATE);

        MovieMainComponent component = DaggerMovieMainComponent.builder()
                .movieMainModule(new MovieMainModule(this)).build();
        component.injectMovieMainActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String movie_pref = mMoviePref.getString(getString(R.string.movie_pref_key),
                getString(R.string.popular_movies_pref));
        loadMoviesData(movie_pref);
    }

    private void loadMoviesData(String movie_pref) {
        mLoadingMoviesIndicator.setVisibility(View.VISIBLE);
        if (!isOnline()) {
            showErrorMessage(getString(R.string.offline_user_message));
            return;
        }
        if (movie_pref.equals(getString(R.string.top_rated_movies_pref))) {
            mPresenter.getTopRatedMovies();

        }
        if (movie_pref.equals(getString(R.string.popular_movies_pref))) {
            mPresenter.getPopularMovies();

        }
        if (movie_pref.equals(getString(R.string.favourite_movies_pref))) {
            mLoadingMoviesIndicator.setVisibility(View.INVISIBLE);
            Loader<Cursor> movieLoader = getSupportLoaderManager().getLoader(MOVIE_LOADER_ID);
            if (movieLoader == null) {
                getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
            } else {
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            }

        }
    }

    private void showErrorMessage(String error) {
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setText(error);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        MovieDetailActivity.start(this, movie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }

    private void saveMoviesUserPref(String pref) {
        SharedPreferences.Editor editor = mMoviePref.edit();
        editor.putString(getString(R.string.movie_pref_key), pref);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_menu:
                saveMoviesUserPref(getString(R.string.popular_movies_pref));
                loadMoviesData(getString(R.string.popular_movies_pref));
                break;
            case R.id.top_rated_menu:
                saveMoviesUserPref(getString(R.string.top_rated_movies_pref));
                loadMoviesData(getString(R.string.top_rated_movies_pref));
                break;
            case R.id.favourite_menu:
                saveMoviesUserPref(getString(R.string.favourite_movies_pref));
                loadMoviesData(getString(R.string.favourite_movies_pref));
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void showMovieData(List<Movie> movies) {
        mLoadingMoviesIndicator.setVisibility(View.INVISIBLE);
        mMovieAdapter.setMovies(movies);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorOccurredMessage() {
        showErrorMessage(getString(R.string.error_occurred_error_message));
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new FetchFavouriteMoviesLoader(this, MoviesContract.MovieEntry.CONTENT_URI, true);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null) {
            showErrorOccurredMessage();
            return;
        }
        List<Movie> movieList = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID)));
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE)));
            movie.setPlotSynopsis(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_PLOT_SYNOPSIS)));
            movie.setUserRating(cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RAITING)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE)));
            movie.setPoster(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH)));

            movieList.add(movie);
        }
        if (movieList.isEmpty()) {
            showErrorMessage(getString(R.string.no_favourite_movies_user_message));
        } else {
            showMovieData(movieList);
        }
        cursor.close();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
