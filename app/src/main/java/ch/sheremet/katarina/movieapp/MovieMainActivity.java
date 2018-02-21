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
import java.util.Scanner;

import ch.sheremet.katarina.movieapp.utilities.NetworkUtil;

public class MovieMainActivity extends AppCompatActivity {

    private static final String TAG = MovieMainActivity.class.getSimpleName();

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);
        mTextView = findViewById(R.id.movieTV);
        new ExtractMovieAsyncTask().execute(NetworkUtil.getTopRatedMovieUrl());
    }


    // TODO: Implement Util class that get data from URL
    class ExtractMovieAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... url) {
            try {
                return NetworkUtil.getResponseFromHttpUrl(url[0]);
            } catch (IOException e) {
                Log.e(TAG,"Getting data exception", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mTextView.setText(s);
        }
    }

}
