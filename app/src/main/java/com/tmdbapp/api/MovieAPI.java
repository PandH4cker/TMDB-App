package com.tmdbapp.api;

import com.tmdbapp.models.*;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.ArrayList;

public interface MovieAPI {

    @GET("movie/latest")
    Observable<ArrayList<MovieModel>> getLatest(@Query(APIClient.API_KEY_PARAM) String apiKey,
                                                @Query(APIClient.LANGUAGE_REQUEST_PARAM) String language,
                                                @Query(APIClient.PAGE_REQUEST_PARAM) int page);

    @GET("movie/now_playing")
    Observable<ArrayList<MovieModel>> getNowPlaying(@Query(APIClient.API_KEY_PARAM) String apiKey,
                                                    @Query(APIClient.LANGUAGE_REQUEST_PARAM) String language,
                                                    @Query(APIClient.PAGE_REQUEST_PARAM) int page);

    @GET("movie/popular")
    Observable<ArrayList<MovieModel>> getPopular(@Query(APIClient.API_KEY_PARAM) String apiKey,
                                                 @Query(APIClient.LANGUAGE_REQUEST_PARAM) String language,
                                                 @Query(APIClient.PAGE_REQUEST_PARAM) int page);

    @GET("movie/top_rated")
    Observable<ArrayList<MovieModel>> getTopRated(@Query(APIClient.API_KEY_PARAM) String apiKey,
                                                  @Query(APIClient.LANGUAGE_REQUEST_PARAM) String language,
                                                  @Query(APIClient.PAGE_REQUEST_PARAM) int page);

    @GET("movie/upcoming")
    Observable<ArrayList<MovieModel>> getUpcoming(@Query(APIClient.API_KEY_PARAM) String apiKey,
                                                  @Query(APIClient.LANGUAGE_REQUEST_PARAM) String language,
                                                  @Query(APIClient.PAGE_REQUEST_PARAM) int page);

    @GET("movie/{movie_id}/videos")
    Observable<ArrayList<VideoModel>> getVideosOf(@Path("movie_id") int movieId,
                                                  @Query(APIClient.API_KEY_PARAM) String apiKey,
                                                  @Query(APIClient.LANGUAGE_REQUEST_PARAM) String language);
    @GET("movie/{movie_id}/credits")
    Observable<Credits> getCreditsOf(@Path("movie_id") int movieId,
                                     @Query(APIClient.API_KEY_PARAM) String apiKey,
                                     @Query(APIClient.LANGUAGE_REQUEST_PARAM) String language);
}
