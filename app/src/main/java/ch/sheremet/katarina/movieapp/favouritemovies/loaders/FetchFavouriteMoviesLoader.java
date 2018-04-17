package ch.sheremet.katarina.movieapp.favouritemovies.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import ch.sheremet.katarina.movieapp.favouritemovies.data.MoviesContract;

public class FetchFavouriteMoviesLoader extends AsyncTaskLoader<Cursor> {

    private static final String TAG = FetchFavouriteMoviesLoader.class.getSimpleName();

    private Cursor mFavouriteMovies = null;
    private Uri mUri;
    private boolean mSortByPopularity;

    public FetchFavouriteMoviesLoader(@NonNull final Context context, @NonNull final Uri uri,
                                      final boolean sortByPopularity) {
        super(context);
        this.mUri = uri;
        this.mSortByPopularity = sortByPopularity;
    }

    @Override
    protected void onStartLoading() {
        if (mFavouriteMovies != null) {
            deliverResult(mFavouriteMovies);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public Cursor loadInBackground() {
        Cursor cursor;
        String sortOrder;
        if (mSortByPopularity) {
            sortOrder = MoviesContract.MovieEntry.COLUMN_RATING + " DESC";
        } else {
            sortOrder = null;
        }
        try {
            cursor = getContext()
                    .getContentResolver().query(mUri,
                            null,
                            null,
                            null,
                            sortOrder);

            return cursor;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load data", e);
            return null;
        }
    }

    @Override
    public void deliverResult(@Nullable Cursor data) {
        mFavouriteMovies = data;
        super.deliverResult(data);
    }
}
