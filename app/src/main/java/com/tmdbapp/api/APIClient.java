package com.tmdbapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;

public class APIClient {
    public static final String TMDB_URL = "https://api.themoviedb.org/3/";
    public static final String TMDB_IMAGE_URL = "https://image.tmdb.org/t/p/original/";

    public static final String API_PARAM = "api_key";
    public static final String API_KEY_VALUE = "8d4627a88a0a7b0b1c056905c0053cca";

    public static final String LANGUAGE_REQUEST_PARAM = "language";
    public static final String LANGUAGE = "en";

    public static final String PAGE_REQUEST_PARAM = "page";

    public static final String ARRAY_RESULTS = "results";

    public static MovieAPI getClient(String URL) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                                              .addInterceptor(interceptor)
                                              .build();
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(ArrayList.class, new MovieJsonDeserializer())
                        .create();
        Retrofit.Builder builder = new Retrofit.Builder()
                                               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                               .addConverterFactory(GsonConverterFactory.create(gson))
                                               .client(client)
                                               .baseUrl(URL);
        return builder.build()
                      .create(MovieAPI.class);
    }

    public static String getFullPosterPath(String path) {
        return path != null ? APIClient.TMDB_IMAGE_URL + path : "";
    }
}
