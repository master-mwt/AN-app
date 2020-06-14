package it.univaq.disim.mwt.android_native_app.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

public class Season implements Serializable {
    private long season_id;
    private long tv_show_id;
    private String name;
    private String overview;
    private String poster_path;
    private String air_date;
    private int episode_count;
    private int season_number;
    private List<Episode> episodes;
    private boolean watched;

    public Season() {
    }

    public Season(long season_id, String name, String poster_path) {
        this.season_id = season_id;
        this.name = name;
        this.poster_path = poster_path;
    }

    public long getSeason_id() {
        return season_id;
    }

    public void setSeason_id(long season_id) {
        this.season_id = season_id;
    }

    public long getTv_show_id() {
        return tv_show_id;
    }

    public void setTv_show_id(long tv_show_id) {
        this.tv_show_id = tv_show_id;
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

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public int getEpisode_count() {
        return episode_count;
    }

    public void setEpisode_count(int episode_count) {
        this.episode_count = episode_count;
    }

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Season){
            return this.season_id == ((Season) obj).getSeason_id();
        }
        return false;
    }
}
