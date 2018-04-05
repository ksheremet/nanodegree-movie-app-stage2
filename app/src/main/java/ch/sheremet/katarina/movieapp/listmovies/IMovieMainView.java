package ch.sheremet.katarina.movieapp.listmovies;

import java.util.List;

import ch.sheremet.katarina.movieapp.model.Movie;

/**
 * Created by katarina on 27.03.18.
 */

public interface IMovieMainView {
    void showMovieData(List<Movie> movies);
    void showErrorOccurredMessage();
}
