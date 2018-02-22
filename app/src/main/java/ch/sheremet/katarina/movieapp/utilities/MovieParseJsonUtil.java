package ch.sheremet.katarina.movieapp.utilities;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.sheremet.katarina.movieapp.model.Movie;

public class MovieParseJsonUtil {

    private static final String TAG = MovieParseJsonUtil.class.getSimpleName();

    private static final String ID_PARAM = "id";
    private static final String MOVIES_RESULTS_PARAM = "results";
    private static final String ORIGINAL_TITLE_PARAM = "original_title";
    private static final String POSTER_PATH_PARAM = "poster_path";
    private static final String PLOT_SYNOPSIS_PARAM = "overview";
    private static final String USER_RAITING_PARAM = "vote_average";
    private static final String RELEASE_DATE_PARAM = "release_date";

    public static List<Movie> getPopularMovies(String popularMoviesJson) {
        try {
            JSONObject movieJson = new JSONObject(popularMoviesJson);
            if (movieJson.has(MOVIES_RESULTS_PARAM)) {
                JSONArray resutlsJsonArray = movieJson.getJSONArray(MOVIES_RESULTS_PARAM);
                List<Movie> movies = new ArrayList<>(resutlsJsonArray.length());
                for (int i = 0; i< resutlsJsonArray.length(); i++) {
                    JSONObject movieObj = resutlsJsonArray.getJSONObject(i);
                    movies.add(parseMovieObjJson(movieObj));
                }
                return movies;
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing exception", e);
        }
        return null;
    }

    private static Movie parseMovieObjJson(JSONObject movieObj) throws JSONException {
        Movie movie = new Movie();
        if (movieObj.has(ID_PARAM)) {
            movie.setId(movieObj.getInt(ID_PARAM));
        }
        if (movieObj.has(ORIGINAL_TITLE_PARAM)) {
            movie.setOriginalTitle(movieObj.getString(ORIGINAL_TITLE_PARAM));
        }
        if (movieObj.has(POSTER_PATH_PARAM)) {
            movie.setPoster(NetworkUtil.buildPosterUrl(movieObj.getString(POSTER_PATH_PARAM)));
        }
        if (movieObj.has(PLOT_SYNOPSIS_PARAM)) {
            movie.setPlotSynopsis(movieObj.getString(PLOT_SYNOPSIS_PARAM));
        }
        if (movieObj.has(USER_RAITING_PARAM)) {
            movie.setUserRating(movieObj.getString(USER_RAITING_PARAM));
        }
        if (movieObj.has(RELEASE_DATE_PARAM)) {
            movie.setReleaseDate(movieObj.getString(RELEASE_DATE_PARAM));
        }
        return movie;
    }

}
