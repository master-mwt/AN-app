package it.univaq.disim.mwt.android_native_app.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class DataParserService extends IntentService {

    public static final String KEY_ACTION = "ACTION";
    public static final String KEY_DATA = "DATA";
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

            switch (action){
                // TODO: All switch cases
                case DataParserService.ACTION_PARSE_TV_SHOW_DETAILS:
                    String response = intent.getStringExtra(DataParserService.KEY_DATA);
                    // TODO: parse data (json -> object) and sendBroadcast()

                    break;
                default:
                    break;
            }
        }
    }
}
