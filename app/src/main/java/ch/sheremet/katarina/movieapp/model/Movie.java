package ch.sheremet.katarina.movieapp.model;

/**
 * Model class for movie.
 */
public class Movie {
    private int mId;
    private String mOriginalTitle;
    private String mPlotSynopsis;
    private String mUserRating;
    private String mReleaseDate;
    private String mPoster;

    public Movie() {
    }

    public Movie(int mId, String mOriginalTitle, String mPlotSynopsis, String mUserRating,
                 String mReleaseDate, String mPoster) {
        this.mId = mId;
        this.mOriginalTitle = mOriginalTitle;
        this.mPlotSynopsis = mPlotSynopsis;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
        this.mPoster = mPoster;
    }
}
