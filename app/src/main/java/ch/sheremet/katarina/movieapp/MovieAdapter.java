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
    private final MovieAdapterOnClickHandler mMovieOnClickHandler;
    private List<Movie> mMovies;

    public MovieAdapter(final MovieAdapterOnClickHandler onClickHandler) {
        this.mMovieOnClickHandler = onClickHandler;
    }

    public void setMovies(final List<Movie> movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(final ViewGroup parent,
                                                     final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieAdapterViewHolder holder,
                                 final int position) {
        Picasso.get()
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

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final ImageView mMoviePoster;

        MovieAdapterViewHolder(final View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.movie_poster_iv);
            mMoviePoster.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            mMovieOnClickHandler.onClick(mMovies.get(getAdapterPosition()));
        }
    }
}
