package ch.sheremet.katarina.movieapp.favouritemovies.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

public class InsertFavouriteMoviesLoader extends AsyncTaskLoader<Uri> {

    private Uri mUri;
    ContentValues mContentValues;
    private Uri mReturnedUri = null;

    public InsertFavouriteMoviesLoader(final Context context, final Uri uri,
                                       final ContentValues cv) {
        super(context);
        this.mUri = uri;
        this.mContentValues = cv;
    }

    @Override
    protected void onStartLoading() {
        if (mReturnedUri == null) {
            forceLoad();
        } else {
            deliverResult(mReturnedUri);
        }
    }

    @Override
    public Uri loadInBackground() {
        return getContext().getContentResolver()
                .insert(mUri, mContentValues);
    }

    @Override
    public void deliverResult(Uri data) {
        mReturnedUri = data;
        super.deliverResult(data);
    }
}
