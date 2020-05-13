package com.tmdbapp.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import com.tmdbapp.data.daos.MovieDao;
import com.tmdbapp.models.MovieModel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataRepository {
    private final MovieDao movieDao;
    private final ExecutorService executorService;
    private static volatile DataRepository INSTANCE = null;

    private DataRepository(MovieDao movieDao, ExecutorService executorService) {
        this.movieDao = movieDao;
        this.executorService = executorService;
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
            return this.executorService.submit(() -> this.movieDao.getMovieById(id)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isMovieFavorite(int id) {
        try {
            return this.executorService.submit(() -> this.movieDao.isMovieFavorite(id)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertMovie(MovieModel movie) {
        this.executorService.execute(() -> this.movieDao.insert(movie));
    }

    public void inactiveAll() {
        this.executorService.execute(this.movieDao::inactiveAll);
    }

    public void updateMovieFavorite(int id, boolean favorite) {
        this.executorService.execute(() -> this.movieDao.update(id, favorite));
    }
}
