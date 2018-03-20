package ch.sheremet.katarina.movieapp;

import android.app.Application;

import ch.sheremet.katarina.movieapp.utilities.ApiManager;

/**
 * Created by katarina on 3/15/18.
 */

public class MovieMainApplication extends Application {
    public static ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        apiManager = ApiManager.getInstance();
    }
}
