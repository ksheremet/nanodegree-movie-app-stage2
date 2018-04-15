package ch.sheremet.katarina.movieapp.favouritemovies.data;

import android.provider.BaseColumns;

/**
 * Created by katarina on 14.04.18.
 */
public final class MoviesContract {

    private MoviesContract() {

    }

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PLOT_SYNOPSIS = "plot_synopsis";
        public static final String COLUMN_RAITING = "raiting";
        public static final String COLUMN_RELEASE_DATE = "release";
        public static final String COLUMN_POSTER_PATH = "poster_path";
    }

}
