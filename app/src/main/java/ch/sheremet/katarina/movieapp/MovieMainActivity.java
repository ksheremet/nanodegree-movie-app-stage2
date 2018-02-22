package ch.sheremet.katarina.movieapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import ch.sheremet.katarina.movieapp.model.Movie;
import ch.sheremet.katarina.movieapp.utilities.MovieParseJsonUtil;
import ch.sheremet.katarina.movieapp.utilities.NetworkUtil;

public class MovieMainActivity extends AppCompatActivity {

    private static final String TAG = MovieMainActivity.class.getSimpleName();

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);
        mTextView = findViewById(R.id.movieTV);
        new ExtractMovieAsyncTask().execute(NetworkUtil.getPopularMovieUrl());
    }


    class ExtractMovieAsyncTask extends AsyncTask<URL, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(URL... url) {
            try {
                return MovieParseJsonUtil.getPopularMovies(
                        NetworkUtil.getResponseFromHttpUrl(url[0]));
            } catch (IOException e) {
                Log.e(TAG,"Getting data exception", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            StringBuilder stringBuilder = new StringBuilder();
            for (Movie movie: movies) {
               stringBuilder.append(movie.toString());
               stringBuilder.append("\n");
            }
            mTextView.setText(stringBuilder.toString());
            Log.v(TAG, stringBuilder.toString());
        }
    }

}
