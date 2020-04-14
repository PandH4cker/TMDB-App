package com.tmdbapp.utils.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GenreConverter {

    @TypeConverter
    public static int[] toIntegerArrayList(String value) {
        if (value == null)
            return null;
        Type listType = new TypeToken<int[]>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromIntegerArrayList(int[] list) {
        if (list == null)
            return null;
       return new Gson().toJson(list);
    }

}
