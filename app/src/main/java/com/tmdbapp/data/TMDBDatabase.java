package com.tmdbapp.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.tmdbapp.models.MovieModel;

@Database(entities = {MovieModel.class}, version = 1)
public abstract class TMDBDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
    public static volatile TMDBDatabase INSTANCE = null;

    @VisibleForTesting
    private static final String DATABASE_NAME = "db_movie";

    @NonNull
    public static synchronized TMDBDatabase getInstance(final Context context) {
        if (INSTANCE == null)
            synchronized (TMDBDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, TMDBDatabase.class, DATABASE_NAME).build();
                }
            }
        return INSTANCE;
    }
}
