package ch.sheremet.katarina.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class for movie.
 */
public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private int mId;
    private String mOriginalTitle;
    private String mPlotSynopsis;
    private String mUserRating;
    private String mReleaseDate;
    private String mPoster;

    public Movie() {
    }

    private Movie(Parcel in) {
        mId = in.readInt();
        mOriginalTitle = in.readString();
        mPlotSynopsis = in.readString();
        mUserRating = in.readString();
        mReleaseDate = in.readString();
        mPoster = in.readString();
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mPlotSynopsis);
        parcel.writeString(mUserRating);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mPoster);
    }
}
