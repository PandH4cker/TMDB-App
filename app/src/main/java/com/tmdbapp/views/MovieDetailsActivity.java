package com.tmdbapp.views;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tmdbapp.R;
import com.tmdbapp.api.APIClient;
import com.tmdbapp.models.MovieModel;
import com.tmdbapp.utils.date.DateUtils;
import com.tmdbapp.utils.transformations.blur.BlurStackOptimized;
import com.tmdbapp.utils.video.VideoViewUtils;
import com.tmdbapp.viewmodels.MovieDetailsViewModel;
import com.tmdbapp.viewmodels.ViewModelFactory;
import org.jetbrains.annotations.NotNull;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private MovieDetailsViewModel movieDetailsViewModel;
    private MovieModel movie;
    private Toast toastMessage;
    private Target target;
    private int position = 0;

    private ScrollView scrollView;
    private ConstraintLayout constraintLayout;
    private LinearLayout topRatedBackButtonLayout;
    private ImageView backButton;
    private TextView topRatedText;
    private LinearLayout ratingLayout;
    private TextView rating;
    private ImageView posterImage;
    private TextView firstProducer;
    private TextView roleFirstProducer;
    private TextView secondProducer;
    private TextView roleSecondProducer;
    private TextView genreTimeDate;
    private TextView movieTitle;
    private TextView yearReleased;
    private ImageView bookmarkFavorite;

    //private VideoView videoTrailer;
    //private MediaController mediaController;
    private YouTubePlayerView youTubePlayerView;

    private TextView overviewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        int id = getIntent().getIntExtra("id", 0);

        ViewModelFactory viewModelFactory = ViewModelFactory.createFactory(this);
        this.movieDetailsViewModel = new ViewModelProvider(this, viewModelFactory).get(MovieDetailsViewModel.class);

        this.scrollView = findViewById(R.id.scrollview_movie_details);
        this.constraintLayout = findViewById(R.id.constraint_layout_movie_details);
        this.topRatedBackButtonLayout = findViewById(R.id.top_rated_back_button);
        this.backButton = findViewById(R.id.back_button_movie_details);
        this.topRatedText = findViewById(R.id.top_rated_text);
        this.ratingLayout = findViewById(R.id.rating_movie_details);
        this.rating = findViewById(R.id.rating);
        this.posterImage = findViewById(R.id.poster_image);
        this.firstProducer = findViewById(R.id.first_producer);
        this.roleFirstProducer = findViewById(R.id.role_first_producer);
        this.secondProducer = findViewById(R.id.second_producer);
        this.roleSecondProducer = findViewById(R.id.role_second_producer);
        this.genreTimeDate = findViewById(R.id.genre_time_date);
        this.movieTitle = findViewById(R.id.title_movie);
        this.yearReleased = findViewById(R.id.release_year);
        this.bookmarkFavorite = findViewById(R.id.bookmark_favorite);

        //this.videoTrailer = findViewById(R.id.video_trailer);
        this.youTubePlayerView = findViewById(R.id.video_trailer);

        this.overviewDescription = findViewById(R.id.overview_description);

        /*if (this.mediaController == null) {
            this.mediaController = new MediaController(MovieDetailsActivity.this);
            this.mediaController.setAnchorView(this.videoTrailer);
            this.videoTrailer.setMediaController(this.mediaController);
        }*/

        /*this.videoTrailer.setOnPreparedListener(mp -> {
            this.videoTrailer.seekTo(this.position);
            if (this.position == 0) this.videoTrailer.start();

            mp.setOnVideoSizeChangedListener((mp1, width, height) -> this.mediaController.setAnchorView(this.videoTrailer));
        });*/

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
        Picasso.get().load(backgroundPoster).into(this.target);

        this.bookmarkFavorite.setImageResource(this.movie.isFavorite() ? R.drawable.ic_bookmark_red_24dp : R.drawable.ic_bookmark_black_24dp);
        this.movieTitle.setText(this.movie.getTitle());

        String date = DateUtils.formatDate(this.movie.getReleaseDate());
        this.yearReleased.setText("(" + date.substring(date.length() - 4) + ")");
        this.rating.setText(String.valueOf(this.movie.getVoteAverage()));
        this.overviewDescription.setText(this.movie.getOverview());
        this.genreTimeDate.setText("Action | 2h 17min | " + DateUtils.formatDate(this.movie.getReleaseDate()));

        //String videoURLSample = "https://www.youtube.com/watch?v=ny3hScFgCIQ";
        //VideoViewUtils.playURLVideo(MovieDetailsActivity.this, this.videoTrailer, videoURLSample);
        this.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                String videoId = "ny3hScFgCIQ";
                youTubePlayer.cueVideo(videoId, 0);
            }
        });
    }

    private void displayFavoriteToast(String message) {
        if (this.toastMessage != null)
            this.toastMessage.cancel();

        this.toastMessage = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        this.toastMessage.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

       /* outState.putInt("Current Position", this.videoTrailer.getCurrentPosition());
        this.videoTrailer.pause();*/
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        /*this.position = savedInstanceState.getInt("Current Position");
        this.videoTrailer.seekTo(position);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.youTubePlayerView.release();
    }
}
