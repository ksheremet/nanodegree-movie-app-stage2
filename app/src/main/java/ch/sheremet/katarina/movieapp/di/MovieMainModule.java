package ch.sheremet.katarina.movieapp.di;

import ch.sheremet.katarina.movieapp.listmovies.IMovieMainPresenter;
import ch.sheremet.katarina.movieapp.listmovies.IMovieMainView;
import ch.sheremet.katarina.movieapp.listmovies.MovieMainPresenterImpl;
import ch.sheremet.katarina.movieapp.utilities.ApiManager;
import dagger.Module;
import dagger.Provides;

/**
 * Created by katarina on 06.04.18.
 */
@Module(includes = ApiModule.class)
public class MovieMainModule {

    IMovieMainView mView;

    public MovieMainModule(IMovieMainView mView) {
        this.mView = mView;
    }

    @Provides
    IMovieMainPresenter movieMainPresenter(IMovieMainView movieMainView, ApiManager apiManager) {
        return new MovieMainPresenterImpl(movieMainView, apiManager);
    }

    @Provides
    IMovieMainView movieMainView() {
        return mView;
    }
}
