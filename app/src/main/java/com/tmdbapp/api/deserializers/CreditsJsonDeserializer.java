package com.tmdbapp.api.deserializers;

import android.util.Log;
import com.google.gson.*;
import com.tmdbapp.api.APIClient;
import com.tmdbapp.models.CastModel;
import com.tmdbapp.models.Credits;
import com.tmdbapp.models.CrewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CreditsJsonDeserializer implements JsonDeserializer<Credits> {

    private static final String TAG = CreditsJsonDeserializer.class.getSimpleName();

    @Override
    public Credits deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Credits credits = null;
        ArrayList<CastModel> casts;
        ArrayList<CrewModel> crews;
        try {
            JsonObject jsonObject = json.getAsJsonObject();

            JsonArray castsJsonArray = jsonObject.getAsJsonArray(APIClient.ARRAY_CAST);
            JsonArray crewsJsonArray = jsonObject.getAsJsonArray(APIClient.ARRAY_CREW);

            casts = new ArrayList<>(castsJsonArray.size());
            crews = new ArrayList<>(crewsJsonArray.size());

            for (int i = 0; i < castsJsonArray.size(); ++i) {
                CastModel dematerialized = context.deserialize(castsJsonArray.get(i), CastModel.class);
                casts.add(dematerialized);
            }
            for (int i = 0; i < crewsJsonArray.size(); ++i) {
                CrewModel dematerialized = context.deserialize(crewsJsonArray.get(i), CrewModel.class);
                crews.add(dematerialized);
            }

            credits = new Credits(casts, crews);
        } catch (JsonParseException e) {
            Log.e(TAG, String.format("Could not deserialize element: %s", json.toString()));
            e.printStackTrace();
        }

        return credits;
    }
}
