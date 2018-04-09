package ch.sheremet.katarina.movieapp.di;

import ch.sheremet.katarina.movieapp.moviedetail.IMovieDetailPresenter;
import ch.sheremet.katarina.movieapp.moviedetail.IMovieDetailView;
import ch.sheremet.katarina.movieapp.moviedetail.MovieDetailPresenterImpl;
import ch.sheremet.katarina.movieapp.utilities.ApiManager;
import dagger.Module;
import dagger.Provides;

/**
 * Created by katarina on 09.04.18.
 */

@Module(includes = ApiModule.class)
public class MovieDetailModule {

    private IMovieDetailView movieDetailView;

    public MovieDetailModule(IMovieDetailView movieDetailView) {
        this.movieDetailView = movieDetailView;
    }

    @Provides
    public IMovieDetailView provideMovieDetailView() {
        return movieDetailView;
    }

    @Provides
    public IMovieDetailPresenter provideMovieDetailPresenter(IMovieDetailView movieDetailView,
                                                             ApiManager apiModule) {
        return new MovieDetailPresenterImpl(movieDetailView, apiModule);
    }
}
