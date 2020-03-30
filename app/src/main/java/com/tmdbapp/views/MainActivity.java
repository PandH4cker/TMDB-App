package com.tmdbapp.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.tmdbapp.R;
import com.tmdbapp.utils.transformations.blur.BlurBoxOptimized;
import com.tmdbapp.utils.transformations.blur.BlurStackOptimized;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

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
        this.genreTimeDate = findViewById(R.id.genre_time_date);

        BlurStackOptimized blurrer = new BlurStackOptimized();
        Bitmap blurredImage = blurrer.blur(BitmapFactory.decodeResource(this.getResources(), R.drawable.logan_background), 100);
        blurredImage = blurrer.blur(blurredImage, 100);
        this.scrollView.setBackground(new BitmapDrawable(getResources(), blurredImage));
    }
}
