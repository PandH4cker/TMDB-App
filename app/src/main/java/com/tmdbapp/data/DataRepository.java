package com.tmdbapp.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import com.tmdbapp.models.MovieModel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataRepository {
    private final MovieDao movieDao;
    private final ExecutorService movieIOExecutor;
    private static volatile DataRepository INSTANCE = null;

    public DataRepository(MovieDao movieDao, ExecutorService movieIOExecutor) {
        this.movieDao = movieDao;
        this.movieIOExecutor = movieIOExecutor;
    }

    public static DataRepository getInstance(Application app) {
        if (INSTANCE == null)
            synchronized (DataRepository.class) {
                if (INSTANCE == null) {
                    TMDBDatabase database = TMDBDatabase.getInstance(app);
                    INSTANCE = new DataRepository(database.movieDao(), Executors.newSingleThreadExecutor());
                }
            }
        return INSTANCE;
    }

    public DataSource.Factory<Integer, MovieModel> getMovies(int limit) {
        if (limit > 0)
            return this.movieDao.getAllLimited(limit);
        return this.movieDao.getAll();
    }

    public LiveData<MovieModel> getMovie(int id) {
        try {
            return this.movieIOExecutor.submit(() -> this.movieDao.getMovieById(id)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isMovieFavorite(int id) {
        try {
            return this.movieIOExecutor.submit(() -> this.movieDao.isMovieFavorite(id)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insert(MovieModel movie) {
        this.movieIOExecutor.execute(() -> this.movieDao.insert(movie));
    }

    public void inactiveAll() {
        this.movieIOExecutor.execute(this.movieDao::inactiveAll);
    }

    public void updateMovieFavorite(int id, boolean favorite) {
        this.movieIOExecutor.execute(() -> this.movieDao.update(id, favorite));
    }
}
