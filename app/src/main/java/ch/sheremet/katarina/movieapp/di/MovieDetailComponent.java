package ch.sheremet.katarina.movieapp.di;

import javax.inject.Singleton;

import ch.sheremet.katarina.movieapp.moviedetail.MovieDetailActivity;
import dagger.Component;

/**
 * Created by katarina on 09.04.18.
 */
@Singleton
@Component(modules = MovieDetailModule.class)
public interface MovieDetailComponent {
    void injectMovieDetailActivity(MovieDetailActivity detailActivity);
}
