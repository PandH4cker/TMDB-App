package com.tmdbapp.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.preference.PreferenceManager;
import com.tmdbapp.R;
import com.tmdbapp.api.APIClient;
import com.tmdbapp.api.MovieAPI;
import com.tmdbapp.data.DataRepository;
import com.tmdbapp.models.*;
import com.tmdbapp.utils.date.DateUtils;
import com.tmdbapp.utils.factory.Triplet;
import com.tmdbapp.utils.network.NetworkUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class MovieViewModel extends ViewModel {
    private static final int PAGE_SIZE = 20;
    private static final int TOTAL_PAGES = 3;
    private static final String DEFAULT_MOVIE_LIMIT = "50";
    private static final boolean PLACEHOLDERS = true;
    private final DataRepository repository;
    private final MovieAPI movieAPI;
    private final MutableLiveData<Boolean> firstTime = new MutableLiveData<>();
    private final MutableLiveData<Boolean> refreshing = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private WeakReference<Context> contextWeakReference;
    private SharedPreferences preferences;
    private boolean inactiveAll;

    public MovieViewModel(DataRepository repository) {
        this.repository = repository;
        this.movieAPI = APIClient.getClient(APIClient.TMDB_URL);
    }

    public LiveData<PagedList<MovieModel>> getPopularMovies(Context context) {
        this.contextWeakReference = new WeakReference<>(context);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this.contextWeakReference.get());

        getPopularMoviesOnline();

        int movieLimit = Integer.parseInt(DEFAULT_MOVIE_LIMIT);
        String prefKeyLimit = context.getString(R.string.pref_key_limit);
        String preferencesLimit = this.preferences.getString(prefKeyLimit, DEFAULT_MOVIE_LIMIT);

        if (preferencesLimit != null)
            movieLimit = Integer.parseInt(preferencesLimit);

        return new LivePagedListBuilder<>(this.repository.getMovies(movieLimit),
                                          new PagedList.Config.Builder()
                                                              .setPageSize(PAGE_SIZE)
                                                              .setEnablePlaceholders(PLACEHOLDERS)
                                                              .build())
                                          .build();
    }

    public void getPopularMoviesOnline() {
        if (NetworkUtils.isNetworkAvailable(this.contextWeakReference.get())) {
            for (int page = 1; page < TOTAL_PAGES; ++page) {
                this.compositeDisposable.add(
                        this.movieAPI.getPopular(APIClient.API_KEY_VALUE, APIClient.LANGUAGE, page)
                                     .flatMapIterable(movies -> {
                                         this.preferences.edit().putBoolean(contextWeakReference.get()
                                                                                                .getString(R.string.pref_key_first_time), false)
                                                                .apply();
                                         this.firstTime.postValue(false);

                                         if (!this.inactiveAll) {
                                             this.repository.inactiveAll();
                                             this.inactiveAll = true;
                                         }

                                         return movies;
                                     })
                                     .flatMap((Function<MovieModel, Observable<ArrayList<VideoModel>>>) movie -> this.movieAPI.getVideosOf(movie.getId(), APIClient.API_KEY_VALUE, APIClient.LANGUAGE), Pair::new)
                                     .flatMap((Function<Pair<MovieModel, ArrayList<VideoModel>>, Observable<Credits>>) movieAndVideos -> {
                                         MovieModel movie = movieAndVideos.first;
                                         if (movie != null)
                                             return this.movieAPI.getCreditsOf(movie.getId(), APIClient.API_KEY_VALUE, APIClient.LANGUAGE);
                                         return Observable.empty();
                                     }, (movieAndVideos, credits) -> Triplet.of(movieAndVideos.first, movieAndVideos.second, credits))
                                     .subscribeOn(Schedulers.io())
                                     .observeOn(AndroidSchedulers.mainThread())
                                     .subscribe(movieAndVideosAndCredits -> {
                                            MovieModel movie = movieAndVideosAndCredits.first;
                                            ArrayList<VideoModel> videos = movieAndVideosAndCredits.second;
                                            Credits credits = movieAndVideosAndCredits.third;

                                            ArrayList<CrewModel> directorAndProducer = Credits.getDirectorAndProducer(credits.getCrewModels());
                                            ArrayList<CastModel> actors = credits.getCastModels();

                                            if (movie != null) {
                                                //Fetching videos
                                                if (videos != null && !videos.isEmpty()) {
                                                    for (VideoModel video : videos)
                                                        if (video.getType().equals("Trailer") && video.getSite().equals("YouTube")) {
                                                            movie.setYoutubeKeyVideo(video.getKey());
                                                            break;
                                                        }
                                                    if (movie.getYoutubeKeyVideo() == null)
                                                        movie.setYoutubeKeyVideo(videos.get(0).getKey());
                                                } else
                                                    movie.setYoutubeKeyVideo("ScMzIvxBSi4");

                                                //Fetching Director and Producer
                                                if (directorAndProducer != null && !directorAndProducer.isEmpty()) {
                                                    if (directorAndProducer.get(0).getJob().equals("Director")) {
                                                        movie.setDirectorName(directorAndProducer.get(0).getName());
                                                        movie.setProducerName(directorAndProducer.get(1).getName());
                                                    } else {
                                                        movie.setProducerName(directorAndProducer.get(0).getName());
                                                        movie.setDirectorName(directorAndProducer.get(1).getName());
                                                    }
                                                }

                                                String[] actorsFullPosterPaths = Credits.getActorsFullPosterPaths(actors).toArray(new String[0]);
                                                String[] uniqueActors = new HashSet<>(Arrays.asList(actorsFullPosterPaths)).toArray(new String[0]);

                                                //Fetching Actors
                                                if (!actors.isEmpty())
                                                    movie.setActorsFullPosterPaths(uniqueActors);


                                                boolean isFavorite = this.repository.isMovieFavorite(movie.getId());
                                                movie.setFavorite(isFavorite);
                                                movie.setActive(true);
                                                this.repository.insertMovie(movie);
                                            }

                                            setLastUpdateNow();
                                            this.refreshing.setValue(false);
                                     }, throwable -> {
                                            String errorMessage;
                                            if (throwable.getMessage() == null)
                                                errorMessage = "Unknown Error";
                                            else
                                                errorMessage = throwable.getMessage();

                                            refreshing.setValue(false);
                                            Log.e("getPopularMoviesOnline", errorMessage);
                                     })
                );
            }
        } else {
            refreshing.setValue(false);
            firstTime.setValue(preferences.getBoolean(contextWeakReference.get()
                                                                           .getString(R.string.pref_key_first_time), true));
            Log.d("getPopularMoviesOnline", "No connection.");
        }
    }

    public String getLastUpdate() {
        if (NetworkUtils.isNetworkAvailable(this.contextWeakReference.get())) return "";
        return this.preferences.getString(this.contextWeakReference.get().getString(R.string.pref_key_last_update), "");
    }

    private void setLastUpdateNow() {
        this.preferences.edit()
                        .putString(this.contextWeakReference.get()
                                                            .getString(R.string.pref_key_last_update), DateUtils.getCurrentDateTime())
                        .apply();
    }

    public MutableLiveData<Boolean> isFirstTime() {
        return this.firstTime;
    }

    public MutableLiveData<Boolean> isRefreshing() {
        return this.refreshing;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.compositeDisposable.clear();
    }
}
