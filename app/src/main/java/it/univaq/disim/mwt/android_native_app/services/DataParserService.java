package it.univaq.disim.mwt.android_native_app.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    // TODO: implement all remaining parse methods (json -> object)

    private TvShowDetails parseTvShowDetails(String response){
        return null;
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
                        item.getLong("tv_show_id"),
                        item.getString("name"),
                        item.getString("poster_path"));

                tvShowPreviews.add(tvShowPreview);
            }
        } catch (JSONException e) {
            Logger.getLogger(DataParserService.class.getName()).log(Level.SEVERE, (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
        }

        return tvShowPreviews;
    }

    private Season parseSeason(String response){
        return null;
    }

    private Episode parseEpisode(String response){
        return null;
    }

    private ArrayList<TvShowCharacter> parseCredits(String response){
        return null;
    }
}
