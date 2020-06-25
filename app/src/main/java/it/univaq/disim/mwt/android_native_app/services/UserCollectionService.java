package it.univaq.disim.mwt.android_native_app.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.mwt.android_native_app.model.Episode;
import it.univaq.disim.mwt.android_native_app.model.Season;
import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;
import it.univaq.disim.mwt.android_native_app.roomdb.AppRoomDatabase;
import it.univaq.disim.mwt.android_native_app.utils.DataContainerObject;
import it.univaq.disim.mwt.android_native_app.utils.FileHandler;
import it.univaq.disim.mwt.android_native_app.utils.FirestoreDB;
import it.univaq.disim.mwt.android_native_app.utils.JSONDealer;

public class UserCollectionService extends IntentService {

    public static final String KEY_ACTION = "ACTION";
    public static final String KEY_DATA = "DATA";
    public static final String EXTRA = "EXTRA";
    // actions
    public static final int ACTION_GET_USER_COLLECTION = 0;
    public static final int ACTION_SAVE_TV_SHOW_TO_COLLECTION = 1;
    public static final int ACTION_DELETE_TV_SHOW_FROM_COLLECTION = 2;
    public static final int ACTION_SAVE_EPISODE_TO_COLLECTION = 3;
    public static final int ACTION_DELETE_EPISODE_FROM_COLLECTION = 4;
    public static final int ACTION_GET_EPISODES_BY_SEASON = 5;
    public static final int ACTION_IS_TV_SHOW_IN_COLLECTION = 6;
    public static final int ACTION_DB_EXPORT = 7;
    public static final int ACTION_DB_IMPORT = 8;
    public static final int ACTION_DB_FIRESTORE_EXPORT = 9;
    public static final int ACTION_DB_FIRESTORE_IMPORT = 10;
    public static final int ACTION_MARK_SEASON_AS_UNSEEN = 11;
    public static final int ACTION_MARK_SEASON_AS_SEEN = 12;
    // filters
    public static final String FILTER_GET_USER_COLLECTION = "it.univaq.disim.mwt.android_native_app.FILTER_GET_USER_COLLECTION";
    public static final String FILTER_IS_TV_SHOW_IN_COLLECTION = "it.univaq.disim.mwt.android_native_app.FILTER_IS_TV_SHOW_IN_COLLECTION";
    public static final String FILTER_GET_EPISODES_BY_SEASON = "it.univaq.disim.mwt.android_native_app.FILTER_GET_EPISODES_BY_SEASON";

    public UserCollectionService() {
        super("UserCollectionService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            int action = intent.getIntExtra(UserCollectionService.KEY_ACTION, -1);
            Intent responseIntent = null;
            TvShowPreview tvShowPreview = null;
            Season season = null;
            Episode episode = null;

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
                    episode = (Episode) intent.getSerializableExtra(UserCollectionService.KEY_DATA);
                    saveEpisodeToCollection(episode);

                    break;
                case UserCollectionService.ACTION_DELETE_EPISODE_FROM_COLLECTION:
                    episode = (Episode) intent.getSerializableExtra(UserCollectionService.KEY_DATA);
                    deleteEpisodeFromCollection(episode);

                    break;
                case UserCollectionService.ACTION_GET_EPISODES_BY_SEASON:
                    season = (Season) intent.getSerializableExtra(UserCollectionService.KEY_DATA);

                    responseIntent = new Intent(FILTER_GET_EPISODES_BY_SEASON);
                    responseIntent.putExtra(UserCollectionService.EXTRA, getEpisodesBySeason(season));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(responseIntent);

                    break;
                case UserCollectionService.ACTION_IS_TV_SHOW_IN_COLLECTION:
                    long tv_show_id = intent.getLongExtra(UserCollectionService.KEY_DATA, -1);

                    if(tv_show_id != -1){
                        responseIntent = new Intent(FILTER_IS_TV_SHOW_IN_COLLECTION);
                        responseIntent.putExtra(UserCollectionService.EXTRA, isTvShowInCollection(tv_show_id));
                        LocalBroadcastManager.getInstance(this).sendBroadcast(responseIntent);
                    }

                    break;
                case UserCollectionService.ACTION_MARK_SEASON_AS_UNSEEN:
                    season = (Season) intent.getSerializableExtra(UserCollectionService.KEY_DATA);
                    markSeasonAsUnseen(season);

                    break;
                case UserCollectionService.ACTION_MARK_SEASON_AS_SEEN:
                    List<Episode> episodes = (List<Episode>) intent.getSerializableExtra(UserCollectionService.KEY_DATA);
                    markSeasonAsSeen(episodes);

                    break;
                case UserCollectionService.ACTION_DB_EXPORT:
                    exportDBToJSON((Uri) intent.getParcelableExtra(UserCollectionService.KEY_DATA));

                    break;
                case UserCollectionService.ACTION_DB_IMPORT:
                    importDBFromJSON((Uri) intent.getParcelableExtra(UserCollectionService.KEY_DATA));

                    break;
                case UserCollectionService.ACTION_DB_FIRESTORE_EXPORT:
                    exportDBToFirestore(intent.getStringExtra(UserCollectionService.KEY_DATA));

                    break;
                case UserCollectionService.ACTION_DB_FIRESTORE_IMPORT:
                    importDBFromFirestore(intent.getStringExtra(UserCollectionService.KEY_DATA));

                    break;
                default:
                    break;
            }
        }
    }

    private ArrayList<TvShowPreview> getUserCollection(){
        return new ArrayList<>(AppRoomDatabase.getInstance(this).getTvShowPreviewDao().findAll());
    }

    private void saveTvShowToCollection(@NonNull TvShowPreview tvShowPreview){
        AppRoomDatabase.getInstance(this).getTvShowPreviewDao().save(tvShowPreview);
    }

    private void deleteTvShowFromCollection(@NonNull final TvShowPreview tvShowPreview){
        AppRoomDatabase.getInstance(this).runInTransaction(new Runnable() {
            @Override
            public void run() {
                AppRoomDatabase.getInstance(getApplicationContext()).getEpisodeDao().deleteByTvShowID(tvShowPreview.getTv_show_id());
                AppRoomDatabase.getInstance(getApplicationContext()).getTvShowPreviewDao().deleteByTvShowID(tvShowPreview.getTv_show_id());
            }
        });
    }

    private void saveEpisodeToCollection(@NonNull Episode episode){
        AppRoomDatabase.getInstance(this).getEpisodeDao().save(episode);
    }

    private void deleteEpisodeFromCollection(@NonNull Episode episode){
        AppRoomDatabase.getInstance(this).getEpisodeDao().deleteByEpisodeID(episode.getEpisode_id());
    }

    private ArrayList<Episode> getEpisodesBySeason(@NonNull Season season){
        return new ArrayList<>(AppRoomDatabase.getInstance(this).getEpisodeDao().findBySeasonId(season.getSeason_id()));
    }

    private boolean isTvShowInCollection(@NonNull long tv_show_id){
        return (AppRoomDatabase.getInstance(this).getTvShowPreviewDao().findByTvShowId(tv_show_id) != null);
    }

    private void markSeasonAsUnseen(@NonNull Season season){
        AppRoomDatabase.getInstance(this).getEpisodeDao().deleteBySeasonID(season.getSeason_id());
    }

    private void markSeasonAsSeen(@NonNull List<Episode> episodes){
        List<Episode> episodesAlreadySaved = AppRoomDatabase.getInstance(this).getEpisodeDao().findBySeasonId(episodes.get(0).getSeason_id());
        List<Episode> episodesNotSavedYet = new ArrayList<>();

        for(Episode e : episodes){
            if(!episodesAlreadySaved.contains(e)){
                episodesNotSavedYet.add(e);
            }
        }

        AppRoomDatabase.getInstance(this).getEpisodeDao().save(episodesNotSavedYet);
    }

    private void exportDBToJSON(@NonNull Uri filePath){
        List<TvShowPreview> tvShowPreviews = AppRoomDatabase.getInstance(this).getTvShowPreviewDao().findAll();
        List<Episode> episodes = AppRoomDatabase.getInstance(this).getEpisodeDao().findAll();

        DataContainerObject dataContainer = new DataContainerObject(tvShowPreviews, episodes);
        String jsonContainer = JSONDealer.dataContainerObjectToJSON(dataContainer);

        FileHandler.write(this, filePath, jsonContainer);
    }
    
    private void importDBFromJSON(@NonNull Uri filePath){
        String jsonContent = FileHandler.read(this, filePath);

        final DataContainerObject dataContainer = JSONDealer.dataContainerObjectFromJSON(jsonContent);

        AppRoomDatabase.getInstance(this).runInTransaction(new Runnable() {
            @Override
            public void run() {
                AppRoomDatabase.getInstance(getApplicationContext()).getTvShowPreviewDao().deleteAll();
                AppRoomDatabase.getInstance(getApplicationContext()).getEpisodeDao().deleteAll();
                AppRoomDatabase.getInstance(getApplicationContext()).getTvShowPreviewDao().save(dataContainer.tvShowPreviews);
                AppRoomDatabase.getInstance(getApplicationContext()).getEpisodeDao().save(dataContainer.episodes);
            }
        });
    }

    private void exportDBToFirestore(@NonNull String userEmail){
        List<TvShowPreview> tvShowPreviews = AppRoomDatabase.getInstance(this).getTvShowPreviewDao().findAll();
        List<Episode> episodes = AppRoomDatabase.getInstance(this).getEpisodeDao().findAll();

        DataContainerObject dataContainer = new DataContainerObject(tvShowPreviews, episodes);
        String jsonContainer = JSONDealer.dataContainerObjectToJSON(dataContainer);

        FirestoreDB.getInstance().putData(userEmail, jsonContainer);
    }

    private void importDBFromFirestore(@NonNull String userEmail){
        FirestoreDB.getInstance().getData(userEmail).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null && task.getResult().getData() != null){
                    String jsonContent = (String) task.getResult().getData().get("data");

                    final DataContainerObject dataContainer = JSONDealer.dataContainerObjectFromJSON(jsonContent);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AppRoomDatabase.getInstance(getApplicationContext()).runInTransaction(new Runnable() {
                                @Override
                                public void run() {
                                    AppRoomDatabase.getInstance(getApplicationContext()).getTvShowPreviewDao().deleteAll();
                                    AppRoomDatabase.getInstance(getApplicationContext()).getEpisodeDao().deleteAll();
                                    AppRoomDatabase.getInstance(getApplicationContext()).getTvShowPreviewDao().save(dataContainer.tvShowPreviews);
                                    AppRoomDatabase.getInstance(getApplicationContext()).getEpisodeDao().save(dataContainer.episodes);
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }
}
