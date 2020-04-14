package com.tmdbapp.models;

import org.jetbrains.annotations.NotNull;

public enum Genres implements AbstractGenres {

    ACTION(28, "Action") {
        @Override
        public boolean isAction() {
            return true;
        }
    },
    ADVENTURE(12, "Adventure") {
        @Override
        public boolean isAdventure() {
            return true;
        }
    },
    ANIMATION(16, "Animation") {
        @Override
        public boolean isAnimation() {
            return true;
        }
    },
    COMEDY(35, "Comedy") {
        @Override
        public boolean isComedy() {
            return true;
        }
    },
    CRIME(80, "Crime"){
        @Override
        public boolean isCrime() {
            return true;
        }
    },
    DOCUMENTARY(99, "Documentary") {
        @Override
        public boolean isDocumentary() {
            return true;
        }
    },
    DRAMA(18, "Drama") {
        @Override
        public boolean isDrama() {
            return true;
        }
    },
    FAMILY(10751, "Family") {
        @Override
        public boolean isFamily() {
            return true;
        }
    },
    FANTASY(14, "Fantasy") {
        @Override
        public boolean isFantasy() {
            return true;
        }
    },
    HISTORY(36, "History") {
        @Override
        public boolean isHistory() {
            return true;
        }
    },
    HORROR(27, "Horror") {
        @Override
        public boolean isHorror() {
            return true;
        }
    },
    MUSIC(10402, "Music") {
        @Override
        public boolean isMusic() {
            return true;
        }
    },
    MYSTERY(9648, "Mystery") {
        @Override
        public boolean isMystery() {
            return true;
        }
    },
    ROMANCE(10749, "Romance") {
        @Override
        public boolean isRomance() {
            return true;
        }
    },
    SCIENCEFICTION(878, "Science Fiction") {
        @Override
        public boolean isScienceFiction() {
            return true;
        }
    },
    TVMOVIE(10770, "TV Movie") {
        @Override
        public boolean isTVMovie() {
            return true;
        }
    },
    THRILLER(53, "Thriller") {
        @Override
        public boolean isThriller() {
            return true;
        }
    },
    WAR(10752, "War") {
        @Override
        public boolean isWar() {
            return true;
        }
    },
    WESTERN(37, "Western") {
        @Override
        public boolean isWestern() {
            return true;
        }
    };

    private int id;
    private String name;

    Genres(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @NotNull
    @Override
    public String toString() {
        return this.name;
    }

    public static String getNameById(int id) {
        for (Genres g : Genres.values())
            if (g.id == id)
                return g.name;
        return "";
    }

}
