package com.tmdbapp.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import com.tmdbapp.data.names.DataMovieName;

import java.util.Arrays;
import java.util.Objects;

@Entity(tableName = DataMovieName.TABLE_NAME)
public class MovieModel {
    public static final String IS_ADULT = "Yes";
    public static final String IS_NOT_ADULT = "No";

    public static final String FAVORITE_ADDED = "Movie added to favorite list.";
    public static final String FAVORITE_REMOVED = "Movie removed from favorite list.";

    @ColumnInfo(name = DataMovieName.COL_POSTER_PATH)
    @SerializedName("poster_path")
    private String posterPath;

    @ColumnInfo(name = DataMovieName.COL_ADULT)
    @SerializedName("adult")
    private boolean adult;

    @ColumnInfo(name = DataMovieName.COL_OVERVIEW)
    @SerializedName("overview")
    private String overview;

    @ColumnInfo(name = DataMovieName.COL_RELEASE_DATE)
    @SerializedName("release_date")
    private String releaseDate;

    @ColumnInfo(name = DataMovieName.COL_GENRE_IDS)
    @SerializedName("genre_ids")
    private int[] genreIds;

    @PrimaryKey
    @ColumnInfo(name = DataMovieName.COL_ID)
    @SerializedName("id")
    private Integer id;

    @ColumnInfo(name = DataMovieName.COL_ORIGINAL_TITLE)
    @SerializedName("original_title")
    private String originalTitle;

    @ColumnInfo(name = DataMovieName.COL_ORIGINAL_LANGUAGE)
    @SerializedName("original_language")
    private String originalLanguage;

    @ColumnInfo(name = DataMovieName.COL_TITLE)
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = DataMovieName.COL_BACKDROP_PATH)
    @SerializedName("backdrop_path")
    private String backdropPath;

    @ColumnInfo(name = DataMovieName.COL_POPULARITY)
    @SerializedName("popularity")
    private Double popularity;

    @ColumnInfo(name = DataMovieName.COL_VOTE_COUNT)
    @SerializedName("vote_count")
    private Integer voteCount;

    @ColumnInfo(name = DataMovieName.COL_VIDEO)
    @SerializedName("video")
    private boolean video;

    @ColumnInfo(name = DataMovieName.COL_VOTE_AVERAGE)
    @SerializedName("vote_average")
    private Double voteAverage;

    @ColumnInfo(name = DataMovieName.COL_ACTIVE)
    private boolean active;

    @ColumnInfo(name = DataMovieName.COL_FAVORITE)
    private boolean favorite;

    @ColumnInfo(name = DataMovieName.COL_YOUTUBE_KEY_VIDEO)
    private String youtubeKeyVideo;

    @ColumnInfo(name = DataMovieName.COL_DIRECTOR_NAME)
    private String directorName;

    @ColumnInfo(name = DataMovieName.COL_PRODUCER_NAME)
    private String producerName;

    @ColumnInfo(name = DataMovieName.COL_ACTORS_FULL_POSTER_PATHS)
    private String[] actorsFullPosterPaths;

    public String getPosterPath() {
        return this.posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return this.adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return this.originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return this.originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return this.backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getPopularity() {
        return this.popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return this.voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return this.video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return this.voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieModel that = (MovieModel) o;
        return adult == that.adult &&
                video == that.video &&
                active == that.active &&
                favorite == that.favorite &&
                Objects.equals(posterPath, that.posterPath) &&
                overview.equals(that.overview) &&
                releaseDate.equals(that.releaseDate) &&
                Arrays.equals(genreIds, that.genreIds) &&
                id.equals(that.id) &&
                originalTitle.equals(that.originalTitle) &&
                originalLanguage.equals(that.originalLanguage) &&
                title.equals(that.title) &&
                Objects.equals(backdropPath, that.backdropPath) &&
                popularity.equals(that.popularity) &&
                voteCount.equals(that.voteCount) &&
                voteAverage.equals(that.voteAverage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(posterPath, adult, overview, releaseDate, genreIds, id, originalTitle, originalLanguage, title, backdropPath, popularity, voteCount, video, voteAverage, active, favorite);
    }

    public int[] getGenreIds() {
        return this.genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    public String getYoutubeKeyVideo() {
        return this.youtubeKeyVideo;
    }

    public void setYoutubeKeyVideo(String youtubeKeyVideo) {
        this.youtubeKeyVideo = youtubeKeyVideo;
    }

    public String getDirectorName() {
        return this.directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getProducerName() {
        return this.producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String[] getActorsFullPosterPaths() {
        return this.actorsFullPosterPaths;
    }

    public void setActorsFullPosterPaths(String[] actorsFullPosterPaths) {
        this.actorsFullPosterPaths = actorsFullPosterPaths;
    }
}
