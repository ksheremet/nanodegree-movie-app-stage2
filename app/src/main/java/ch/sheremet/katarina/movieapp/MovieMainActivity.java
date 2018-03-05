package ch.sheremet.katarina.movieapp;

import android.content.Context;
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

    private static final int SPAN_COUNT = 2;

    private static final int MOVIE_LOADER_ID = 148;
    private static final String MOVIE_BUNDLE_PARAM = "movies";
    private static final String POPULAR_MOVIES_PARAM = "popular";
    private static final String TOP_RATED_PARAM = "top-rated";

    private static final String TAG = MovieMainActivity.class.getSimpleName();

    @BindView(R.id.movie_rv)
    RecyclerView mMoviesRecyclerView;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);
        ButterKnife.bind(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        loadMoviesData(POPULAR_MOVIES_PARAM);
    }

    private void loadMoviesData(String movie_pref) {
        Bundle queryBundle = new Bundle();
        if (POPULAR_MOVIES_PARAM.equals(movie_pref)) {
            queryBundle.putString(MOVIE_BUNDLE_PARAM, POPULAR_MOVIES_PARAM);
        }
        if (TOP_RATED_PARAM.equals(movie_pref)) {
            queryBundle.putString(MOVIE_BUNDLE_PARAM, TOP_RATED_PARAM);
        }
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieLoader = loaderManager.getLoader(MOVIE_LOADER_ID);
        if (movieLoader == null) {
            loaderManager.initLoader(MOVIE_LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(MOVIE_LOADER_ID, queryBundle, this);
        }
    }


    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(final int id, final Bundle args) {
        if (args == null) return new MovieListLoader(this, null);
        return new MovieListLoader(this, args.getString(MOVIE_BUNDLE_PARAM));
    }

    @Override
    public void onLoadFinished(@NonNull final Loader<List<Movie>> loader, final List<Movie> data) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_menu:
                loadMoviesData(POPULAR_MOVIES_PARAM);
                break;
            case R.id.top_rated_menu:
                loadMoviesData(TOP_RATED_PARAM);
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
                if (mMovieSearchParam.equals(POPULAR_MOVIES_PARAM)) {
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
