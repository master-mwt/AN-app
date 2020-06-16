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
    public static final int ACTION_DELETE_TV_SHOW_FROM_COLLECTION = 3;
    public static final int ACTION_IS_TV_SHOW_IN_COLLECTION = 4;
    // filters
    public static final String FILTER_GET_USER_COLLECTION = "it.univaq.disim.mwt.android_native_app.FILTER_GET_USER_COLLECTION";
    public static final String FILTER_IS_TV_SHOW_IN_COLLECTION = "it.univaq.disim.mwt.android_native_app.FILTER_IS_TV_SHOW_IN_COLLECTION";


    public UserCollectionService() {
        super("UserCollectionService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            int action = intent.getIntExtra(UserCollectionService.KEY_ACTION, -1);
            Intent responseIntent = null;
            TvShowPreview tvShowPreview = null;

            switch (action){
                case UserCollectionService.ACTION_GET_USER_COLLECTION:
                    responseIntent = new Intent(FILTER_GET_USER_COLLECTION);
                    responseIntent.putParcelableArrayListExtra(UserCollectionService.EXTRA, getUserCollection());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(responseIntent);

                    break;
                case UserCollectionService.ACTION_SAVE_TV_SHOW_TO_COLLECTION:
                    tvShowPreview = intent.getParcelableExtra(UserCollectionService.KEY_DATA);
                    saveTvShowToCollection(tvShowPreview);

                    break;
                case UserCollectionService.ACTION_DELETE_TV_SHOW_FROM_COLLECTION:
                    tvShowPreview = intent.getParcelableExtra(UserCollectionService.KEY_DATA);
                    deleteTvShowFromCollection(tvShowPreview);

                    break;
                case UserCollectionService.ACTION_SAVE_EPISODE_TO_COLLECTION:
                    Episode episode = (Episode) intent.getSerializableExtra(UserCollectionService.KEY_DATA);
                    saveEpisodeToCollection(episode);

                    break;
                case UserCollectionService.ACTION_IS_TV_SHOW_IN_COLLECTION:
                    long tv_show_id = intent.getLongExtra(UserCollectionService.KEY_DATA, -1);

                    if(tv_show_id != -1){
                        responseIntent = new Intent(FILTER_IS_TV_SHOW_IN_COLLECTION);
                        responseIntent.putExtra(UserCollectionService.EXTRA, isTvShowInCollection(tv_show_id));
                        LocalBroadcastManager.getInstance(this).sendBroadcast(responseIntent);
                    }

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

    private void deleteTvShowFromCollection(TvShowPreview tvShowPreview){
        AppRoomDatabase.getInstance(this).getTvShowPreviewDao().delete(tvShowPreview.getTv_show_id());
        // TODO: delete episodes (cascade)!!
    }

    private void saveEpisodeToCollection(Episode episode){
        AppRoomDatabase.getInstance(this).getEpisodeDao().save(episode);
    }

    private boolean isTvShowInCollection(long tv_show_id){
        return (AppRoomDatabase.getInstance(this).getTvShowPreviewDao().findByTvShowId(tv_show_id) != null);
    }
}
