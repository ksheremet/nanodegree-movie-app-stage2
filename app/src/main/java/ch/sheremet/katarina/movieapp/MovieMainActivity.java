package ch.sheremet.katarina.movieapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import ch.sheremet.katarina.movieapp.model.Movie;
import ch.sheremet.katarina.movieapp.utilities.MovieParseJsonUtil;
import ch.sheremet.katarina.movieapp.utilities.NetworkUtil;

public class MovieMainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final int SPAN_COUNT = 2;

    private static final int MOVIE_LOADER_ID = 148;
    private static final String MOVIE_BUNDLE_PARAM = "movies";
    private static final String POPULAR_MOVIES_PARAM = "popular";
    private static final String TOP_RATED_PARAM = "top-rated";

    private static final String TAG = MovieMainActivity.class.getSimpleName();

    private RecyclerView mMoviesRecyclerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);
        mMoviesRecyclerView = findViewById(R.id.movie_rv);
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        loadMoviesData();
    }

    private void loadMoviesData() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(MOVIE_BUNDLE_PARAM, POPULAR_MOVIES_PARAM);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieLoader = loaderManager.getLoader(MOVIE_LOADER_ID);
        if (movieLoader == null) {
            loaderManager.initLoader(MOVIE_LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(MOVIE_LOADER_ID, queryBundle, this);
        }
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(final int id, final Bundle args) {
        if (args == null) return new MovieListLoader(this, null);
        return new MovieListLoader(this, args.getString(MOVIE_BUNDLE_PARAM));
    }

    @Override
    public void onLoadFinished(final Loader<List<Movie>> loader, final List<Movie> data) {
        mMoviesRecyclerView.setAdapter(new MovieAdapter(data));
    }

    @Override
    public void onLoaderReset(final Loader<List<Movie>> loader) {

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
                Log.e(TAG,"Getting data exception", e);
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
