package com.tmdbapp.views;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.tmdbapp.R;
import com.tmdbapp.adapters.MovieAdapter;
import com.tmdbapp.viewmodels.MovieViewModel;
import com.tmdbapp.viewmodels.ViewModelFactory;

public class MainActivity extends AppCompatActivity {
    private MovieViewModel viewModel;
    private LinearLayout linearLayoutLastUpdate;
    private TextView textViewLastUpdate;
    private TextView emptyList;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        ViewModelFactory viewModelFactory = ViewModelFactory.createFactory(this);
        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(MovieViewModel.class);

        MovieAdapter adapter = new MovieAdapter(this);
        this.viewModel.getPopularMovies(this).observe(this, adapter::submitList);
        this.viewModel.isFirstTime().observe(this, this::displayFirstTimeNoConnection);
        this.viewModel.isRefreshing().observe(this, this::displayRefreshing);

        this.linearLayoutLastUpdate = findViewById(R.id.last_update);
        this.textViewLastUpdate = findViewById(R.id.list_last_update);
        this.emptyList = findViewById(R.id.empty_list);

        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        this.setLastUpdate();

        this.pullToRefresh = findViewById(R.id.refresh);
        this.pullToRefresh.setOnRefreshListener(() -> {
            this.viewModel.getPopularMoviesOnline();
            this.setLastUpdate();
        });
    }

    @Override
    protected void onDestroy() {
        this.viewModel.getCompositeDisposable().clear();
        super.onDestroy();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent settingIntent = new Intent(this, )
        }
    }*/

    private void setLastUpdate() {
        String lastUpdate = this.viewModel.getLastUpdate();
        this.textViewLastUpdate.setText(lastUpdate);
        this.linearLayoutLastUpdate.setVisibility(lastUpdate.isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void displayFirstTimeNoConnection(boolean noConnection) {
        if (noConnection) this.emptyList.setText(R.string.no_connection);
        else this.emptyList.setText("");
    }

    private void displayRefreshing(boolean refreshing) {
        if (!refreshing) pullToRefresh.setRefreshing(false);
    }
}
