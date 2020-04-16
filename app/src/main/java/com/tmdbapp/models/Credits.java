package com.tmdbapp.models;

import java.util.ArrayList;

public class Credits {

    private ArrayList<CastModel> castModels;

    private ArrayList<CrewModel> crewModels;

    public Credits(ArrayList<CastModel> castModels, ArrayList<CrewModel> crewModels) {
        this.castModels = castModels;
        this.crewModels = crewModels;
    }

    public ArrayList<CastModel> getCastModels() {
        return this.castModels;
    }

    public void setCastModels(ArrayList<CastModel> castModels) {
        this.castModels = castModels;
    }

    public ArrayList<CrewModel> getCrewModels() {
        return this.crewModels;
    }

    public void setCrewModels(ArrayList<CrewModel> crewModels) {
        this.crewModels = crewModels;
    }

    public static ArrayList<CrewModel> getDirectorAndProducer(final ArrayList<CrewModel> crews) {
        CrewModel director = null;
        CrewModel producer = null;
        ArrayList<CrewModel> directorAndProducer = new ArrayList<>();

        for (CrewModel crew : crews) {
            if (crew.getJob().equals("Director"))
                directorAndProducer.add(crew);
            if (crew.getJob().equals("Producer"))
                directorAndProducer.add(crew);
            if (directorAndProducer.size() == 2)
                return directorAndProducer;
        }
        return null;
    }

    public static ArrayList<String> getActorsFullPosterPaths(final ArrayList<CastModel> actors) {
        ArrayList<String> fullPosterPaths = new ArrayList<>();
        for (CastModel actor : actors)
            fullPosterPaths.add(actor.getProfilePath());
        return fullPosterPaths;
    }
}
