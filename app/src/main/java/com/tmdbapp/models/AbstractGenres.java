package com.tmdbapp.models;

interface AbstractGenres {
    default boolean isAction() {
        return false;
    }
    default boolean isAdventure() {
        return false;
    }
    default boolean isAnimation() {
        return false;
    }
    default boolean isComedy() {
        return false;
    }
    default boolean isCrime() {
        return false;
    }
    default boolean isDocumentary() {
        return false;
    }
    default boolean isDrama() {
        return false;
    }
    default boolean isFamily() {
        return false;
    }
    default boolean isFantasy() {
        return false;
    }
    default boolean isHistory() {
        return false;
    }
    default boolean isHorror() {
        return false;
    }
    default boolean isMusic() {
        return false;
    }
    default boolean isMystery() {
        return false;
    }
    default boolean isRomance() {
        return false;
    }
    default boolean isScienceFiction() {
        return false;
    }
    default boolean isTVMovie() {
        return false;
    }
    default boolean isThriller() {
        return false;
    }
    default boolean isWar() {
        return false;
    }
    default boolean isWestern() {
        return false;
    }
}
