package ch.sheremet.katarina.movieapp.favouritemovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#",
                MOVIE_WITH_ID);
        return uriMatcher;
    }

    private MovieDbHelper mMovieDbHelper;

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        Cursor returnCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                returnCursor = db.query(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                returnCursor = db.query(MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);

        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (buildUriMatcher().match(uri)) {
            case MOVIES:
                return "vnd.android.cursor.dir" + "/"
                        + MoviesContract.AUTHORITY + "/" + MoviesContract.PATH_MOVIES;
            case MOVIE_WITH_ID:
                return "vnd.android.cursor.item" + "/"
                        + MoviesContract.AUTHORITY + "/" + MoviesContract.PATH_MOVIES;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                long id = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.MovieEntry.CONTENT_URI, id);

                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        // Notify Content Resolver that uri has been changed
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int rowsAdded;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                rowsAdded = db.delete(MoviesContract.MovieEntry.TABLE_NAME,
                        MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAdded;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Unsupported operation");
    }
}
