package com.tmdbapp.data.daos;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.tmdbapp.data.names.DataMovieName;
import com.tmdbapp.models.MovieModel;

@Dao
public interface MovieDao {

    @Query("SELECT * " +
           "FROM " + DataMovieName.TABLE_NAME +
           " WHERE " + DataMovieName.COL_ACTIVE + " = 1 " +
           "ORDER BY " + DataMovieName.COL_POPULARITY + " DESC")
    DataSource.Factory<Integer, MovieModel> getAll();

    @Query("SELECT * " +
           "FROM " + DataMovieName.TABLE_NAME +
           " ORDER BY " + DataMovieName.COL_POPULARITY + " DESC " +
           "LIMIT :limit")
    DataSource.Factory<Integer, MovieModel> getAllLimited(int limit);

    @Query("SELECT * " +
           "FROM " + DataMovieName.TABLE_NAME +
           " WHERE " + DataMovieName.COL_ID + " = :id")
    LiveData<MovieModel> getMovieById(int id);

    @Query("SELECT " + DataMovieName.COL_FAVORITE +
           " FROM " + DataMovieName.TABLE_NAME +
           " WHERE " + DataMovieName.COL_ID + " = :id")
    boolean isMovieFavorite(int id);

    @Query("UPDATE " + DataMovieName.TABLE_NAME +
           " SET " + DataMovieName.COL_ACTIVE + " = 0")
    void inactiveAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieModel movie);

    @Query("UPDATE " + DataMovieName.TABLE_NAME +
           " SET " + DataMovieName.COL_FAVORITE + " = :favorite" +
           " WHERE " + DataMovieName.COL_ID + " = :id")
    void update(int id, boolean favorite);
}
