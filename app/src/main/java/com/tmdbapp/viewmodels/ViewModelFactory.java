package com.tmdbapp.viewmodels;

import android.app.Activity;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.tmdbapp.data.DataRepository;

import java.lang.reflect.InvocationTargetException;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final DataRepository repository;

    private ViewModelFactory(DataRepository repository) {
        this.repository = repository;
    }

    public static ViewModelFactory createFactory(Activity activity) {
        Application app = activity.getApplication();
        if (app == null) throw new IllegalStateException("Not yet attached to Application");
        return new ViewModelFactory(DataRepository.getInstance(app));
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(DataRepository.class).newInstance(this.repository);
        } catch (InstantiationException | InvocationTargetException |
                 NoSuchMethodException  | IllegalAccessException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }
}
