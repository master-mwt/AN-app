package it.univaq.disim.mwt.android_native_app.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

import it.univaq.disim.mwt.android_native_app.model.Episode;
import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;
import it.univaq.disim.mwt.android_native_app.roomdb.AppRoomDatabase;

public class UserCollectionService extends IntentService {

    public static final String KEY_ACTION = "ACTION";
    public static final String KEY_DATA = "DATA";
    public static final String EXTRA = "EXTRA";
    // actions
    public static final int ACTION_GET_USER_COLLECTION = 0;
    public static final int ACTION_SAVE_TV_SHOW_TO_COLLECTION = 1;
    public static final int ACTION_SAVE_EPISODE_TO_COLLECTION = 2;
    // filters
    public static final String FILTER_GET_USER_COLLECTION = "it.univaq.disim.mwt.android_native_app.FILTER_GET_USER_COLLECTION";


    public UserCollectionService() {
        super("UserCollectionService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            int action = intent.getIntExtra(UserCollectionService.KEY_ACTION, -1);
            Intent responseIntent = null;
            switch (action){
                case UserCollectionService.ACTION_GET_USER_COLLECTION:
                    responseIntent = new Intent(FILTER_GET_USER_COLLECTION);
                    responseIntent.putParcelableArrayListExtra(UserCollectionService.EXTRA, getUserCollection());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(responseIntent);

                    break;
                case UserCollectionService.ACTION_SAVE_TV_SHOW_TO_COLLECTION:
                    TvShowPreview tvShowPreview = intent.getParcelableExtra(UserCollectionService.KEY_DATA);
                    saveTvShowToCollection(tvShowPreview);

                    break;
                case UserCollectionService.ACTION_SAVE_EPISODE_TO_COLLECTION:
                    Episode episode = (Episode) intent.getSerializableExtra(UserCollectionService.KEY_DATA);
                    saveEpisodeToCollection(episode);

                    break;
                default:
                    break;
            }
        }
    }

    private ArrayList<TvShowPreview> getUserCollection(){
        return new ArrayList<>(AppRoomDatabase.getInstance(this).getTvShowPreviewDao().findAll());
    }

    private void saveTvShowToCollection(TvShowPreview tvShowPreview){
        AppRoomDatabase.getInstance(this).getTvShowPreviewDao().save(tvShowPreview);
    }

    private void saveEpisodeToCollection(Episode episode){
        AppRoomDatabase.getInstance(this).getEpisodeDao().save(episode);
    }
}
