package ch.sheremet.katarina.movieapp.favouritemovies.data;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ch.sheremet.katarina.movieapp.model.Movie;

public class FavouriteMoviesUtil {

    private FavouriteMoviesUtil() {
      // No need to create an instance of util class.
    }

    public static ContentValues movieToContentValues(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        cv.put(MoviesContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
        cv.put(MoviesContract.MovieEntry.COLUMN_PLOT_SYNOPSIS, movie.getPlotSynopsis());
        cv.put(MoviesContract.MovieEntry.COLUMN_RAITING, movie.getUserRating());
        cv.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPoster());
        return cv;
    }

    public static List<Movie> cursorToMovies(Cursor cursor) {
        if (cursor == null) {
            return null;
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
        cursor.close();
        return movieList;
    }
}
