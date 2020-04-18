package com.tmdbapp.utils.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ActorsFullPosterPathsConverter {

    @TypeConverter
    public static String[] toStringArrayList(String value) {
        if (value == null)
            return null;
        Type listType = new TypeToken<String[]>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromStringArrayList(String[] list) {
        if (list == null)
            return null;
        return new Gson().toJson(list);
    }
}
