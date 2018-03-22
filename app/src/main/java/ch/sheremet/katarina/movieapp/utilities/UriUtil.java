package ch.sheremet.katarina.movieapp.utilities;

import android.net.Uri;

public class UriUtil {
    // For building poster url
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185";

    private static final String YOUTUBE_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi/";


    private static final String YOUTUBE_VIDEO_BASE_URL = "http://www.youtube.com/watch";
    private static final String YOUTUBE_VIDEO_KEY_PARAM = "v";

    public static String buildPosterUrl(String path) {
        Uri builtUri = Uri.parse(POSTER_BASE_URL)
                .buildUpon()
                .appendPath(POSTER_SIZE)
                .appendEncodedPath(path)
                .build();
        return builtUri.toString();
    }

    public static String buildYoutubeThumbnailUrl(String id) {
        Uri buildUri = Uri.parse(YOUTUBE_THUMBNAIL_BASE_URL)
                .buildUpon()
                .appendPath(id)
                .appendPath("0.jpg")
                .build();
        return buildUri.toString();
    }

    public static String buildYoutubeVideoUrl(String id) {
        Uri buildUri = Uri.parse(YOUTUBE_VIDEO_BASE_URL)
                .buildUpon()
                .appendQueryParameter(YOUTUBE_VIDEO_KEY_PARAM, id)
                .build();
        return buildUri.toString();
    }
}
