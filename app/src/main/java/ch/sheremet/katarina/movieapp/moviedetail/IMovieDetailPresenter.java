package ch.sheremet.katarina.movieapp.moviedetail;

/**
 * Interface for presenter.
 */
public interface IMovieDetailPresenter {
    void getReviews(int movieId);
    void getTrailers(int movieId);
}
