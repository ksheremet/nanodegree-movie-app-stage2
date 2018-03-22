package ch.sheremet.katarina.movieapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by katarina on 21.03.18.
 */
public class Trailer {
    @SerializedName("id")
    private String mId;
    @SerializedName("key")
    private String mKey;
    @SerializedName("name")
    private String mName;
    @SerializedName("site")
    private String mSite;

    public String getId() {
        return mId;
    }

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

    public String getSite() {
        return mSite;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "mId='" + mId + '\'' +
                ", mKey='" + mKey + '\'' +
                ", mName='" + mName + '\'' +
                ", mSite='" + mSite + '\'' +
                '}';
    }
}
