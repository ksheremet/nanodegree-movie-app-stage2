package ch.sheremet.katarina.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by katarina on 22.03.18.
 */

public class Review implements Parcelable {
    @SerializedName("author")
    private String mAuthor;
    @SerializedName("content")
    private String mContent;

    protected Review(Parcel in) {
        mAuthor = in.readString();
        mContent = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public String toString() {
        return "Review{" +
                "mAuthor='" + mAuthor + '\'' +
                ", mContent='" + mContent + '\'' +
                '}';
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }
}
