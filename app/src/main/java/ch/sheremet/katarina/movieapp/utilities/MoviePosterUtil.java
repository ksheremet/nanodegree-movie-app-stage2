package ch.sheremet.katarina.movieapp.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import ch.sheremet.katarina.movieapp.BuildConfig;

public class MoviePosterUtil {
    // For building poster url
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185";

    public static String buildPosterUrl(String path) {
        Uri builtUri = Uri.parse(POSTER_BASE_URL)
                .buildUpon()
                .appendPath(POSTER_SIZE)
                .appendEncodedPath(path)
                .build();
        return builtUri.toString();
    }
}
