package ch.sheremet.katarina.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.sheremet.katarina.movieapp.model.Movie;
import ch.sheremet.katarina.movieapp.moviedetail.MovieDetailActivity;
import ch.sheremet.katarina.movieapp.utilities.MovieParseJsonUtil;
import ch.sheremet.katarina.movieapp.utilities.NetworkUtil;

public class MovieMainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Movie>>,
        MovieAdapter.MovieAdapterOnClickHandler {

    private static final int MOVIE_LOADER_ID = 148;
    private static final String MOVIE_BUNDLE_PARAM = "movies";
    private static final String TAG = MovieMainActivity.class.getSimpleName();
    @BindView(R.id.movie_rv)
    RecyclerView mMoviesRecyclerView;
    @BindView(R.id.error_message_tv)
    TextView mErrorMessage;
    @BindView(R.id.loading_movies_pb)
    ProgressBar mLoadingMoviesIndicator;
    private MovieAdapter mMovieAdapter;
    private SharedPreferences mMoviePref;

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
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        String movie_pref = mMoviePref.getString(getString(R.string.movie_pref_key),
                getString(R.string.popular_movies_pref));
        loadMoviesData(movie_pref);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void loadMoviesData(String movie_pref) {
        if (!isOnline()) {
            showErrorMessage(getString(R.string.offline_user_message));
            return;
        }
        Bundle queryBundle = new Bundle();
        if (movie_pref.equals(getString(R.string.top_rated_movies_pref))) {
            queryBundle.putString(MOVIE_BUNDLE_PARAM, getString(R.string.top_rated_movies_pref));
        } else {
            queryBundle.putString(MOVIE_BUNDLE_PARAM, getString(R.string.popular_movies_pref));
        }
        showMoviesData();
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieLoader = loaderManager.getLoader(MOVIE_LOADER_ID);
        if (movieLoader == null) {
            loaderManager.initLoader(MOVIE_LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(MOVIE_LOADER_ID, queryBundle, this);
        }
    }

    private void showMoviesData() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String error) {
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setText(error);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(final int id, final Bundle args) {
        mLoadingMoviesIndicator.setVisibility(View.VISIBLE);
        if (args == null) return new MovieListLoader(this, null);
        return new MovieListLoader(this, args.getString(MOVIE_BUNDLE_PARAM));
    }

    @Override
    public void onLoadFinished(@NonNull final Loader<List<Movie>> loader, final List<Movie> data) {
        mLoadingMoviesIndicator.setVisibility(View.INVISIBLE);
        if (data == null) {
            showErrorMessage(getString(R.string.error_occurred_error_message));
        }
        mMovieAdapter.setMovies(data);
    }

    @Override
    public void onLoaderReset(@NonNull final Loader<List<Movie>> loader) {

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

    private void saveUserPref(String pref) {
        SharedPreferences.Editor editor = mMoviePref.edit();
        editor.putString(getString(R.string.movie_pref_key), pref);
        // handle in background
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_menu:
                saveUserPref(getString(R.string.popular_movies_pref));
                loadMoviesData(getString(R.string.popular_movies_pref));
                break;
            case R.id.top_rated_menu:
                saveUserPref(getString(R.string.top_rated_movies_pref));
                loadMoviesData(getString(R.string.top_rated_movies_pref));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public static class MovieListLoader extends AsyncTaskLoader<List<Movie>> {
        private final String mMovieSearchParam;
        private List<Movie> mMovieList;

        MovieListLoader(final Context context, final String param) {
            super(context);
            this.mMovieSearchParam = param;
        }

        @Override
        protected void onStartLoading() {
            if (mMovieSearchParam == null) {
                return;
            }
            if (mMovieList == null) {
                forceLoad();
            } else {
                deliverResult(mMovieList);
            }
        }

        @Override
        public List<Movie> loadInBackground() {
            try {
                URL url;
                if (mMovieSearchParam.equals(getContext().getString(R.string.popular_movies_pref))) {
                    url = NetworkUtil.getPopularMovieUrl();
                } else {
                    url = NetworkUtil.getTopRatedMovieUrl();
                }
                return MovieParseJsonUtil.getPopularMovies(
                        NetworkUtil.getResponseFromHttpUrl(url));
            } catch (IOException e) {
                Log.e(TAG, "Getting data exception", e);
            }
            return null;
        }

        @Override
        public void deliverResult(final List<Movie> data) {
            mMovieList = data;
            super.deliverResult(data);
        }
    }
}
