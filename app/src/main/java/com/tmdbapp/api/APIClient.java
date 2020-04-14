package com.tmdbapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tmdbapp.api.deserializers.MovieJsonDeserializer;
import com.tmdbapp.models.MovieModel;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class APIClient {
    public static final String TMDB_URL = "https://api.themoviedb.org/3/";
    public static final String TMDB_IMAGE_URL = "https://image.tmdb.org/t/p/w300/";

    public static final String API_KEY_PARAM = "api_key";
    public static final String API_KEY_VALUE = "8d4627a88a0a7b0b1c056905c0053cca";

    public static final String LANGUAGE_REQUEST_PARAM = "language";
    public static final String LANGUAGE = "en";

    public static final String PAGE_REQUEST_PARAM = "page";

    public static final String ARRAY_RESULTS = "results";

    public static MovieAPI getClient(String URL) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        long cacheSize = 10 * 1024 * 1024; //10 Mo
        Cache cache = new Cache(new File("okhttp.cache"), cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                                              .addInterceptor(interceptor)
                                              .connectTimeout(10, TimeUnit.SECONDS)
                                              .readTimeout(10, TimeUnit.SECONDS)
                                              .cache(cache)
                                              .build();
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(new TypeToken<ArrayList<MovieModel>>(){}.getType(), new MovieJsonDeserializer())
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
