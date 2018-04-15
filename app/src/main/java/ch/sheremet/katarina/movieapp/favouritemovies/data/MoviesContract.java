package ch.sheremet.katarina.movieapp.favouritemovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by katarina on 14.04.18.
 */
public final class MoviesContract {

    public static final String AUTHORITY = "ch.sheremet.katarina.movieapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    private MoviesContract() {

    }

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PLOT_SYNOPSIS = "plot_synopsis";
        public static final String COLUMN_RAITING = "raiting";
        public static final String COLUMN_RELEASE_DATE = "release";
        public static final String COLUMN_POSTER_PATH = "poster_path";
    }

}
