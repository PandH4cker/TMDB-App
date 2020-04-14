package com.tmdbapp.api.deserializers;

import android.util.Log;
import com.google.gson.*;
import com.tmdbapp.api.APIClient;
import com.tmdbapp.models.MovieModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MovieJsonDeserializer implements JsonDeserializer<ArrayList<MovieModel>> {

    private static final String TAG = MovieJsonDeserializer.class.getSimpleName();

    @Override
    public ArrayList<MovieModel> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<MovieModel> movies = null;
        try {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonArray moviesJsonArray = jsonObject.getAsJsonArray(APIClient.ARRAY_RESULTS);
            movies = new ArrayList<>(moviesJsonArray.size());
            for (int i = 0; i < moviesJsonArray.size(); ++i) {
                MovieModel dematerialized = context.deserialize(moviesJsonArray.get(i), MovieModel.class);
                movies.add(dematerialized);
            }
        } catch (JsonParseException e) {
            Log.e(TAG, String.format("Could not deserialize element: %s", json.toString()));
            e.printStackTrace();
        }

        return movies;
    }
}
