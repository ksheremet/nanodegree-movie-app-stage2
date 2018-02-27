package ch.sheremet.katarina.movieapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ch.sheremet.katarina.movieapp.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private List<Movie> mMovies;

    public MovieAdapter() {
    }

    public void setMovies(List<Movie> movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moview_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Picasso.with(holder.itemView.getContext())
                .load(mMovies.get(position).getPoster())
                .into(holder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) {
            return 0;
        }
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
