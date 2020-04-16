package com.tmdbapp.views;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tmdbapp.R;
import com.tmdbapp.adapters.CastAdapter;
import com.tmdbapp.api.APIClient;
import com.tmdbapp.models.Genres;
import com.tmdbapp.models.MovieModel;
import com.tmdbapp.utils.date.DateUtils;
import com.tmdbapp.utils.transformations.blur.BlurStackOptimized;
import com.tmdbapp.viewmodels.MovieDetailsViewModel;
import com.tmdbapp.viewmodels.ViewModelFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private MovieDetailsViewModel movieDetailsViewModel;
    private MovieModel movie;
    private Toast toastMessage;
    private Target target;
    private String fromWhere;

    private ScrollView scrollView;
    private ConstraintLayout constraintLayout;
    private LinearLayout topRatedBackButtonLayout;
    private ImageView backButton;
    private TextView fromWhereText;
    private LinearLayout ratingLayout;
    private TextView rating;
    private ImageView posterImage;
    private TextView firstProducer;
    private TextView secondProducer;
    private TextView genreTimeDate;
    private TextView movieTitle;
    private TextView yearReleased;
    private ImageView bookmarkFavorite;
    private YouTubePlayerView youTubePlayerView;
    private TextView overviewDescription;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        int id = this.getIntent().getIntExtra("id", 0);
        this.fromWhere = this.getIntent().getStringExtra("fromWhere");

        ViewModelFactory viewModelFactory = ViewModelFactory.createFactory(this);
        this.movieDetailsViewModel = new ViewModelProvider(this, viewModelFactory).get(MovieDetailsViewModel.class);

        this.scrollView = findViewById(R.id.scrollview_movie_details);
        this.constraintLayout = findViewById(R.id.constraint_layout_movie_details);
        this.topRatedBackButtonLayout = findViewById(R.id.top_rated_back_button);
        this.backButton = findViewById(R.id.back_button_movie_details);
        this.fromWhereText = findViewById(R.id.from_where_text);
        this.ratingLayout = findViewById(R.id.rating_movie_details);
        this.rating = findViewById(R.id.rating);
        this.posterImage = findViewById(R.id.poster_image);
        this.firstProducer = findViewById(R.id.first_producer);
        this.secondProducer = findViewById(R.id.second_producer);
        this.genreTimeDate = findViewById(R.id.genre_time_date);
        this.movieTitle = findViewById(R.id.title_movie);
        this.yearReleased = findViewById(R.id.release_year);
        this.bookmarkFavorite = findViewById(R.id.bookmark_favorite);
        this.youTubePlayerView = findViewById(R.id.video_trailer);
        this.overviewDescription = findViewById(R.id.overview_description);

        this.recyclerView = findViewById(R.id.rv_cast);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        this.bookmarkFavorite.setOnClickListener(view -> this.movieDetailsViewModel
                                                             .updateMovieFavorite(this.movie.getId(), !this.movie.isFavorite()));
        this.backButton.setOnClickListener(view -> finishAfterTransition());

        this.movieDetailsViewModel.setMovie(id).observe(this, this::loadMovie);
        this.movieDetailsViewModel.getFavoriteMessage().observe(this, this::displayFavoriteToast);
    }

    private void loadMovie(MovieModel movie) {
        if (movie == null)
            return;

        this.movie = movie;
        this.fromWhereText.setText(this.fromWhere);
        this.firstProducer.setText(movie.getDirectorName());
        this.secondProducer.setText(movie.getProducerName());
        String poster = APIClient.getFullPosterPath(this.movie.getPosterPath());
        String backgroundPoster = APIClient.getFullPosterPath(this.movie.getBackdropPath());
        Picasso.get().load(poster).into(this.posterImage);

        final Bitmap[] background = new Bitmap[1];
        this.target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                background[0] = bitmap;
                BitmapDrawable backgroundDrawable = new BitmapDrawable(getResources(), background[0]);
                BlurStackOptimized blurrer = new BlurStackOptimized();
                Bitmap blurredImage = blurrer.blur(backgroundDrawable.getBitmap(), 100);
                blurredImage = blurrer.blur(blurredImage, 100);
                scrollView.setBackground(new BitmapDrawable(getResources(), blurredImage));

                constraintLayout.setBackground(backgroundDrawable);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.d(TAG, "Error Picasso get bitmap: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Error Picasso get bitmap: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        if (!backgroundPoster.isEmpty())
            Picasso.get().load(backgroundPoster).into(this.target);
        else
            Picasso.get().load(poster).into(this.target);

        this.bookmarkFavorite.setImageResource(this.movie.isFavorite() ? R.drawable.ic_bookmark_red_24dp : R.drawable.ic_bookmark_black_24dp);
        this.movieTitle.setText(this.movie.getTitle());

        String date = DateUtils.formatDate(this.movie.getReleaseDate());
        this.yearReleased.setText(String.format("(%s)", date.substring(date.length() - 4)));
        this.rating.setText(String.valueOf(this.movie.getVoteAverage()));

        if (!(this.movie.getOverview() != null && this.movie.getOverview().isEmpty()))
            this.overviewDescription.setText(this.movie.getOverview());
        else
            this.overviewDescription.setText(this.getResources().getString(R.string.no_description));

        //Justify Text for Android O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.overviewDescription.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }

        StringBuilder genres = new StringBuilder();
        for (int id : this.movie.getGenreIds())
            genres.append(Genres.getNameById(id)).append(", ");
        genres = new StringBuilder(genres.substring(0, genres.length() - 2));

        this.genreTimeDate.setText(String.format("%s | 2h 17min | %s", genres, DateUtils.formatDate(this.movie.getReleaseDate())));
        this.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(movie.getYoutubeKeyVideo(), 0);
            }
        });

        //Log.d(TAG, Arrays.toString(movie.getActorsFullPosterPaths()));
        CastAdapter adapter = new CastAdapter(this, Arrays.asList(movie.getActorsFullPosterPaths()));
        this.recyclerView.setAdapter(adapter);
    }

    private void displayFavoriteToast(String message) {
        if (this.toastMessage != null)
            this.toastMessage.cancel();

        this.toastMessage = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        this.toastMessage.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.youTubePlayerView.release();
    }
}
