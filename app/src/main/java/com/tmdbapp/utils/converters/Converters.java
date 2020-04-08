package com.tmdbapp.utils.converters;

import androidx.room.TypeConverter;
import java.util.ArrayList;

public class Converters {

    @TypeConverter
    public ArrayList<Integer> gettingListFromString(String genreIds) {
        ArrayList<Integer> list = new ArrayList<>();
        String[] array = genreIds.split(",");
        for (String s : array)
            if (!s.isEmpty())
                list.add(Integer.parseInt(s));
        return list;
    }

    @TypeConverter
    public String writingStringFromList(ArrayList<Integer> list) {
        StringBuilder genreIds = new StringBuilder();
        for (int i : list)
            genreIds.append(",").append(i);
        return genreIds.toString();
    }

}
