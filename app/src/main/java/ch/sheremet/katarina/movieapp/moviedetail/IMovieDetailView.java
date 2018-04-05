package ch.sheremet.katarina.movieapp.moviedetail;

import java.util.List;

import ch.sheremet.katarina.movieapp.model.Review;
import ch.sheremet.katarina.movieapp.model.Trailer;

/**
 * Created by katarina on 05.04.18.
 */
public interface IMovieDetailView {
    void showReviews(List<Review> reviews);
    void showTrailers(List<Trailer> trailers);
}
