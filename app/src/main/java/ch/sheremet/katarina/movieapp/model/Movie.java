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

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public void setPlotSynopsis(String mPlotSynopsis) {
        this.mPlotSynopsis = mPlotSynopsis;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public void setUserRating(String mUserRating) {
        this.mUserRating = mUserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String mPoster) {
        this.mPoster = mPoster;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mId=" + mId +
                ", mOriginalTitle='" + mOriginalTitle + '\'' +
                ", mPlotSynopsis='" + mPlotSynopsis + '\'' +
                ", mUserRating='" + mUserRating + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mPoster='" + mPoster + '\'' +
                '}';
    }
}
