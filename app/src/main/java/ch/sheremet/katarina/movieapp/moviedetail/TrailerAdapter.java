package ch.sheremet.katarina.movieapp.moviedetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ch.sheremet.katarina.movieapp.R;
import ch.sheremet.katarina.movieapp.model.Trailer;
import ch.sheremet.katarina.movieapp.utilities.UriUtil;

/**
 * Created by katarina on 21.03.18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final TrailerAdapter.TrailerAdapterOnClickHandler mTrailerOnClickHandler;
    private List<Trailer> mTrailers;

    public TrailerAdapter(final TrailerAdapterOnClickHandler trailerAdapterOnClickHandler) {
        this.mTrailerOnClickHandler = trailerAdapterOnClickHandler;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.mTrailers = trailers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);
        return new TrailerAdapter.TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapterViewHolder holder, int position) {
        Picasso.get()
                .load(UriUtil.buildYoutubeThumbnailUrl(mTrailers.get(position).getKey()))
                .into(holder.mTrailerPoster);
        holder.mTrailerName.setText(mTrailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) {
            return 0;
        }
        return mTrailers.size();
    }

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final ImageView mTrailerPoster;
        private final TextView mTrailerName;

        TrailerAdapterViewHolder(final View itemView) {
            super(itemView);
            mTrailerPoster = itemView.findViewById(R.id.trailer_poster_iv);
            mTrailerPoster.setOnClickListener(this);
            mTrailerName = itemView.findViewById(R.id.trailer_name_tv);
        }

        @Override
        public void onClick(final View view) {
            mTrailerOnClickHandler.onClick(mTrailers.get(getAdapterPosition()));
        }
    }
}
