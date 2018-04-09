package ch.sheremet.katarina.movieapp.di;

import javax.inject.Singleton;

import ch.sheremet.katarina.movieapp.listmovies.MovieMainActivity;
import dagger.Component;

/**
 * Created by katarina on 06.04.18.
 */
@Singleton
@Component(modules = MovieMainModule.class)
public interface MovieMainComponent {
    void injectMovieMainActivity(MovieMainActivity mainActivity);
}
