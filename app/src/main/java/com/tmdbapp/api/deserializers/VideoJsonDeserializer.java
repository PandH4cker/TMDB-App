package com.tmdbapp.api.deserializers;

import android.util.Log;
import com.google.gson.*;
import com.tmdbapp.api.APIClient;
import com.tmdbapp.models.VideoModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class VideoJsonDeserializer implements JsonDeserializer<ArrayList<VideoModel>> {

    private static final String TAG = VideoJsonDeserializer.class.getSimpleName();

    @Override
    public ArrayList<VideoModel> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<VideoModel> videos = null;
        try {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonArray videosJsonArray = jsonObject.getAsJsonArray(APIClient.ARRAY_RESULTS);
            videos = new ArrayList<>(videosJsonArray.size());
            for (int i = 0; i < videosJsonArray.size(); ++i) {
                VideoModel dematerialized = context.deserialize(videosJsonArray.get(i), VideoModel.class);
                videos.add(dematerialized);
            }
        } catch (JsonParseException e) {
            Log.e(TAG, String.format("Could not deserialize element: %s", json.toString()));
            e.printStackTrace();
        }

        return videos;
    }
}
