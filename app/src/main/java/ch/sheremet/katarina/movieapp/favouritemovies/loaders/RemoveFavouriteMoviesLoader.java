package ch.sheremet.katarina.movieapp.favouritemovies.loaders;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class RemoveFavouriteMoviesLoader extends AsyncTaskLoader<Integer> {

    private static final String TAG = RemoveFavouriteMoviesLoader.class.getSimpleName();
    private Uri mUri;
    private Integer mNumberRemovedRows = null;

    public RemoveFavouriteMoviesLoader(@NonNull Context context, Uri uri) {
        super(context);
        this.mUri = uri;
    }

    @Override
    protected void onStartLoading() {
        if (mNumberRemovedRows == null) {
            forceLoad();
        } else {
            deliverResult(mNumberRemovedRows);
        }
    }

    @Nullable
    @Override
    public Integer loadInBackground() {
        return getContext().getContentResolver().delete(mUri, null, null);
    }

    @Override
    public void deliverResult(@Nullable Integer data) {
        mNumberRemovedRows = data;
        super.deliverResult(data);
    }
}
