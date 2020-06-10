package it.univaq.disim.mwt.android_native_app.model;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Episode implements Serializable {
    private long episode_id;
    private String name;
    private String overview;
    private String still_path;
    private String air_date;
    private int episode_number;
    private int season_number;
    private double vote_average;
    private long vote_count;
    private long tv_show_id;
    private boolean watched;

    public Episode() {
    }

    public Episode(long episode_id, String name, String still_path) {
        this.episode_id = episode_id;
        this.name = name;
        this.still_path = still_path;
    }

    public long getEpisode_id() {
        return episode_id;
    }

    public void setEpisode_id(long episode_id) {
        this.episode_id = episode_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getStill_path() {
        return still_path;
    }

    public void setStill_path(String still_path) {
        this.still_path = still_path;
    }

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public int getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(int episode_number) {
        this.episode_number = episode_number;
    }

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public long getVote_count() {
        return vote_count;
    }

    public void setVote_count(long vote_count) {
        this.vote_count = vote_count;
    }

    public long getTv_show_id() {
        return tv_show_id;
    }

    public void setTv_show_id(long tv_show_id) {
        this.tv_show_id = tv_show_id;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Episode){
            return this.episode_id == ((Episode) obj).getEpisode_id();
        }
        return false;
    }
}
