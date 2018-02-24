package ch.sheremet.katarina.movieapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ch.sheremet.katarina.movieapp.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> mMovies;

    public MovieAdapter(List<Movie> movies) {
        this.mMovies = movies;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moview_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Log.v(TAG, position + ":" + mMovies.get(position).getPoster());
        Picasso.with(holder.itemView.getContext())
                .load(mMovies.get(position).getPoster())
                .into(holder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mMoviePoster;

        MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.movie_poster_iv);
        }


    }
}
