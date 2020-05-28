package it.univaq.disim.mwt.android_native_app.model;

import java.io.Serializable;
import java.util.List;

// TODO: POJO -> RoomDB entities: decide which classes become RoomDB entities [only TvShowPreview (?)]
public class TvShowDetails implements Serializable {
    private long tv_show_id;
    private String name;
    private String overview;
    private String poster_path;
    private String first_air_date;
    private String last_air_date;
    private String status;
    private String type;
    private double vote_average;
    private long vote_count;
    private double popularity;
    private int number_of_episodes;
    private int number_of_seasons;
    private List<String> origin_country;
    private List<String> genres;
    private boolean in_production;
    private List<String> languages;
    private String original_language;
    private List<Season> seasons;
    private Episode last_episode_to_air;
    private Episode next_episode_to_air;

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

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public String getLast_air_date() {
        return last_air_date;
    }

    public void setLast_air_date(String last_air_date) {
        this.last_air_date = last_air_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getNumber_of_episodes() {
        return number_of_episodes;
    }

    public void setNumber_of_episodes(int number_of_episodes) {
        this.number_of_episodes = number_of_episodes;
    }

    public int getNumber_of_seasons() {
        return number_of_seasons;
    }

    public void setNumber_of_seasons(int number_of_seasons) {
        this.number_of_seasons = number_of_seasons;
    }

    public List<String> getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(List<String> origin_country) {
        this.origin_country = origin_country;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public boolean isIn_production() {
        return in_production;
    }

    public void setIn_production(boolean in_production) {
        this.in_production = in_production;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public Episode getLast_episode_to_air() {
        return last_episode_to_air;
    }

    public void setLast_episode_to_air(Episode last_episode_to_air) {
        this.last_episode_to_air = last_episode_to_air;
    }

    public Episode getNext_episode_to_air() {
        return next_episode_to_air;
    }

    public void setNext_episode_to_air(Episode next_episode_to_air) {
        this.next_episode_to_air = next_episode_to_air;
    }
}
