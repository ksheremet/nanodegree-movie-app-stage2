package ch.sheremet.katarina.movieapp.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import ch.sheremet.katarina.movieapp.BuildConfig;

public class NetworkUtil {

    // For fetching movies
    private static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String MOVIE_POPULAR_PATH = "popular";
    private static final String MOVIE_TOP_RATED_PATH = "top_rated";
    private static final String API_KEY_VALUE = BuildConfig.API_KEY;

    // For building poster url
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185";

    private static final String API_KEY_PARAM = "api_key";


    private static Uri.Builder buildBaseUrl() {
        return Uri.parse(MOVIE_BASE_URL)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE);
    }

    public static URL getPopularMovieUrl() {
        Uri builtUri = buildBaseUrl()
                .appendPath(MOVIE_POPULAR_PATH)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getTopRatedMovieUrl() {
        Uri builtUri = buildBaseUrl()
                .appendPath(MOVIE_TOP_RATED_PATH)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String buildPosterUrl(String path) {
        Uri builtUri = Uri.parse(POSTER_BASE_URL)
                .buildUpon()
                .appendPath(POSTER_SIZE)
                .appendEncodedPath(path)
                .build();
        return builtUri.toString();
    }
}
