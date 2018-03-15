package ch.sheremet.katarina.movieapp.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.sheremet.katarina.movieapp.R;
import ch.sheremet.katarina.movieapp.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_PARAM="movie";

    @BindView(R.id.detail_movie_poster_iv)
    ImageView mPosterIV;
    @BindView(R.id.detail_movie_title_tv)
    TextView mTitleTV;
    @BindView(R.id.detail_plot_summary_tv)
    TextView mPlotSummaryTV;
    @BindView(R.id.detail_rating_tv)
    TextView mRatingTV;
    @BindView(R.id.detail_release_date_tv)
    TextView mReleaseDateTV;

    public static void start(final Context context, final Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MOVIE_PARAM, movie);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if ((getIntent() == null) || (!getIntent().hasExtra(MOVIE_PARAM))) {
            finish();
        }
        ButterKnife.bind(this);
        Movie movie = getIntent().getParcelableExtra(MOVIE_PARAM);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(movie.getOriginalTitle());
        Picasso.get()
                .load(movie.getPoster())
                .into(mPosterIV);
        mPosterIV.setContentDescription(movie.getPlotSynopsis());
        mTitleTV.setText(movie.getOriginalTitle());
        mPlotSummaryTV.setText(movie.getPlotSynopsis());
        mRatingTV.setText(getString(R.string.rating_detail_tv,movie.getUserRating()));
        mReleaseDateTV.setText(getString(R.string.release_detail_tv,movie.getReleaseDate()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
