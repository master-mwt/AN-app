package it.univaq.disim.mwt.android_native_app.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.univaq.disim.mwt.android_native_app.model.Episode;
import it.univaq.disim.mwt.android_native_app.model.Season;
import it.univaq.disim.mwt.android_native_app.model.TvShowCharacter;
import it.univaq.disim.mwt.android_native_app.model.TvShowDetails;
import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;

public class DataParserService extends IntentService {

    public static final String KEY_ACTION = "ACTION";
    public static final String KEY_DATA = "DATA";
    public static final String EXTRA = "EXTRA";
    // actions
    public static final int ACTION_PARSE_TV_SHOW_DETAILS = 0;
    public static final int ACTION_PARSE_TV_SHOWS_LIST = 1;
    public static final int ACTION_PARSE_TV_SHOW_SEASON = 2;
    public static final int ACTION_PARSE_TV_SHOW_EPISODE = 3;
    public static final int ACTION_PARSE_TV_SHOW_CREDITS = 4;
    // filters
    public static final String FILTER_PARSE_TV_SHOW_DETAILS = "it.univaq.disim.mwt.android_native_app.FILTER_PARSE_TV_SHOW_DETAILS";
    public static final String FILTER_PARSE_TV_SHOWS_LIST = "it.univaq.disim.mwt.android_native_app.FILTER_PARSE_TV_SHOWS_LIST";
    public static final String FILTER_PARSE_TV_SHOW_SEASON = "it.univaq.disim.mwt.android_native_app.FILTER_PARSE_TV_SHOW_SEASON";
    public static final String FILTER_PARSE_TV_SHOW_EPISODE = "it.univaq.disim.mwt.android_native_app.FILTER_PARSE_TV_SHOW_EPISODE";
    public static final String FILTER_PARSE_TV_SHOW_CREDITS = "it.univaq.disim.mwt.android_native_app.FILTER_PARSE_TV_SHOW_CREDITS";


    public DataParserService() {
        super("DataParserService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            int action = intent.getIntExtra(DataParserService.KEY_ACTION, -1);
            Intent responseIntent = null;
            String response = null;

            switch (action){
                case DataParserService.ACTION_PARSE_TV_SHOW_DETAILS:
                    response = intent.getStringExtra(DataParserService.KEY_DATA);

                    TvShowDetails tvShowDetails = parseTvShowDetails(response);

                    responseIntent = new Intent(FILTER_PARSE_TV_SHOW_DETAILS);
                    responseIntent.putExtra(EXTRA, tvShowDetails);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(responseIntent);

                    break;

                case DataParserService.ACTION_PARSE_TV_SHOWS_LIST:
                    response = intent.getStringExtra(DataParserService.KEY_DATA);

                    ArrayList<TvShowPreview> tvShows = parseTvShowsList(response);

                    responseIntent = new Intent(FILTER_PARSE_TV_SHOWS_LIST);
                    responseIntent.putParcelableArrayListExtra(EXTRA, tvShows);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(responseIntent);

                    break;

                case DataParserService.ACTION_PARSE_TV_SHOW_SEASON:
                    response = intent.getStringExtra(DataParserService.KEY_DATA);

                    Season season = parseSeason(response);

                    responseIntent = new Intent(FILTER_PARSE_TV_SHOW_SEASON);
                    responseIntent.putExtra(EXTRA, season);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(responseIntent);

                    break;

                case DataParserService.ACTION_PARSE_TV_SHOW_EPISODE:
                    response = intent.getStringExtra(DataParserService.KEY_DATA);

                    Episode episode = parseEpisode(response);

                    responseIntent = new Intent(FILTER_PARSE_TV_SHOW_EPISODE);
                    responseIntent.putExtra(EXTRA, episode);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(responseIntent);

                    break;

                case DataParserService.ACTION_PARSE_TV_SHOW_CREDITS:
                    response = intent.getStringExtra(DataParserService.KEY_DATA);

                    ArrayList<TvShowCharacter> tvShowCharacters = parseCredits(response);

                    responseIntent = new Intent(FILTER_PARSE_TV_SHOW_CREDITS);
                    responseIntent.putParcelableArrayListExtra(EXTRA, tvShowCharacters);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(responseIntent);

                    break;
                default:
                    break;
            }
        }
    }


    private TvShowDetails parseTvShowDetails(String response){
        TvShowDetails tvShowDetails = null;
        JSONObject res = null;

        try {
            res = new JSONObject(response);
            tvShowDetails = new TvShowDetails();

            tvShowDetails.setTv_show_id(res.optLong("id"));
            tvShowDetails.setName(res.optString("name"));
            tvShowDetails.setOverview(res.optString("overview"));
            tvShowDetails.setPoster_path(res.optString("poster_path"));
            tvShowDetails.setFirst_air_date(res.optString("first_air_date"));
            tvShowDetails.setLast_air_date(res.optString("last_air_date"));
            tvShowDetails.setStatus(res.optString("status"));
            tvShowDetails.setType(res.optString("type"));
            tvShowDetails.setVote_average(res.optDouble("vote_average"));
            tvShowDetails.setVote_count(res.optLong("vote_count"));
            tvShowDetails.setPopularity(res.optDouble("popularity"));
            tvShowDetails.setNumber_of_episodes(res.optInt("number_of_episodes"));
            tvShowDetails.setNumber_of_seasons(res.optInt("number_of_seasons"));

            JSONArray jsoncountries = res.optJSONArray("origin_country");

            if(jsoncountries != null){
                List<String> countries = new ArrayList<>();
                for(int i = 0; i < jsoncountries.length(); i++){
                    countries.add(jsoncountries.getString(i));
                }
                tvShowDetails.setOrigin_country(countries);
            }

            JSONArray jsongenres = res.optJSONArray("genres");

            if(jsongenres != null){
                List<String> genres = new ArrayList<>();
                for(int i = 0; i < jsongenres.length(); i++){
                    genres.add(jsongenres.getJSONObject(i).getString("name"));
                }
                tvShowDetails.setGenres(genres);
            }

            tvShowDetails.setIn_production(res.optBoolean("in_production"));

            JSONArray jsonlanguages = res.optJSONArray("languages");

            if(jsonlanguages != null){
                List<String> languages = new ArrayList<>();
                for(int i = 0; i < jsonlanguages.length(); i++){
                    languages.add(jsonlanguages.getString(i));
                }
                tvShowDetails.setLanguages(languages);
            }

            JSONArray jsonseasons = res.optJSONArray("seasons");

            if(jsonseasons != null){
                List<Season> seasons = new ArrayList<>();
                for(int i = 0; i < jsonseasons.length(); i++){
                    JSONObject jsonseason = jsonseasons.getJSONObject(i);
                    Season season = new Season();

                    season.setAir_date(jsonseason.optString("air_date"));
                    season.setEpisode_count(jsonseason.optInt("episode_count"));
                    season.setSeason_id(jsonseason.optLong("id"));
                    season.setName(jsonseason.optString("name"));
                    season.setOverview(jsonseason.optString("overview"));
                    season.setPoster_path(jsonseason.optString("poster_path"));
                    season.setSeason_number(jsonseason.optInt("season_number"));

                    seasons.add(season);
                }
                tvShowDetails.setSeasons(seasons);
            }

            if(res.optJSONObject("last_episode_to_air") != null){
                Episode lastEpisodeToAir = new Episode();
                lastEpisodeToAir.setAir_date(res.getJSONObject("last_episode_to_air").optString("air_date"));
                lastEpisodeToAir.setEpisode_number(res.getJSONObject("last_episode_to_air").optInt("episode_number"));
                lastEpisodeToAir.setSeason_number(res.getJSONObject("last_episode_to_air").optInt("season_number"));
                lastEpisodeToAir.setName(res.getJSONObject("last_episode_to_air").optString("name"));
                tvShowDetails.setLast_episode_to_air(lastEpisodeToAir);
            }

            if(res.optJSONObject("next_episode_to_air") != null){
                Episode nextEpisodeToAir = new Episode();
                nextEpisodeToAir.setAir_date(res.getJSONObject("next_episode_to_air").optString("air_date"));
                nextEpisodeToAir.setEpisode_number(res.getJSONObject("next_episode_to_air").optInt("episode_number"));
                nextEpisodeToAir.setSeason_number(res.getJSONObject("next_episode_to_air").optInt("season_number"));
                nextEpisodeToAir.setName(res.getJSONObject("next_episode_to_air").optString("name"));
                tvShowDetails.setNext_episode_to_air(nextEpisodeToAir);
            }

        } catch (JSONException e) {
            tvShowDetails = null;
            Logger.getLogger(DataParserService.class.getName()).log(Level.SEVERE, (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
        }
        return tvShowDetails;
    }

    private ArrayList<TvShowPreview> parseTvShowsList(String response){
        ArrayList<TvShowPreview> tvShowPreviews = new ArrayList<>();

        JSONObject res = null;
        JSONArray array = null;

        try {
            res = new JSONObject(response);
            array = res.optJSONArray("results");
            if(array == null){
                return tvShowPreviews;
            }

            for(int i = 0; i < array.length(); i++){
                JSONObject item = array.optJSONObject(i);
                if(item == null){
                    continue;
                }

                TvShowPreview tvShowPreview = new TvShowPreview(
                        item.optLong("id"),
                        item.optString("name"),
                        item.optString("poster_path"));

                tvShowPreviews.add(tvShowPreview);
            }

        } catch (JSONException e) {
            Logger.getLogger(DataParserService.class.getName()).log(Level.SEVERE, (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
        }

        return tvShowPreviews;
    }

    private Season parseSeason(String response){
        Season season = null;
        JSONObject res = null;
        JSONArray array = null;

        try {
            res = new JSONObject(response);
            season = new Season();

            season.setSeason_id(res.optLong("id"));
            season.setName(res.optString("name"));
            season.setOverview(res.optString("overview"));
            season.setPoster_path(res.optString("poster_path"));
            season.setAir_date(res.optString("air_date"));
            season.setEpisode_count(res.optInt("episode_count"));
            season.setSeason_number(res.optInt("season_number"));

            array = res.optJSONArray("episodes");

            if(array != null){
                List<Episode> episodes = new ArrayList<>();

                for(int i = 0; i < array.length(); i++){
                    JSONObject jsonEpisode = array.getJSONObject(i);
                    Episode episode = new Episode(
                            jsonEpisode.optLong("id"),
                            jsonEpisode.optString("name"),
                            jsonEpisode.optString("still_path"));

                    episodes.add(episode);
                }
                season.setEpisodes(episodes);
            }

        } catch (JSONException e) {
            season = null;
            Logger.getLogger(DataParserService.class.getName()).log(Level.SEVERE, (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
        }

        return season;
    }

    private Episode parseEpisode(String response){
        Episode episode = null;
        JSONObject res = null;

        try {
            res = new JSONObject(response);
            episode = new Episode();

            episode.setEpisode_id(res.optLong("id"));
            episode.setName(res.optString("name"));
            episode.setOverview(res.optString("overview"));
            episode.setStill_path(res.optString("still_path"));
            episode.setAir_date(res.optString("air_date"));
            episode.setEpisode_number(res.optInt("episode_number"));
            episode.setSeason_number(res.optInt("season_number"));
            episode.setVote_average(res.optDouble("vote_average"));
            episode.setVote_count(res.optLong("vote_count"));

        } catch (JSONException e) {
            episode = null;
            Logger.getLogger(DataParserService.class.getName()).log(Level.SEVERE, (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
        }

        return episode;
    }

    private ArrayList<TvShowCharacter> parseCredits(String response){
        ArrayList<TvShowCharacter> tvShowCharacters = new ArrayList<>();

        JSONObject res = null;
        JSONArray cast = null;

        try {
            res = new JSONObject(response);
            cast = res.optJSONArray("cast");

            if(cast == null){
                return tvShowCharacters;
            }

            for(int i = 0; i < cast.length(); i++){
                JSONObject item = cast.getJSONObject(i);

                if(item == null){
                    continue;
                }

                TvShowCharacter tvShowCharacter = new TvShowCharacter(
                        item.optLong("id"),
                        item.optString("character"),
                        item.optString("name"),
                        item.optString("profile_path"));

                tvShowCharacters.add(tvShowCharacter);
            }

        } catch (JSONException e) {
            Logger.getLogger(DataParserService.class.getName()).log(Level.SEVERE, (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
        }

        return tvShowCharacters;
    }
}
