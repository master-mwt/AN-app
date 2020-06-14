package it.univaq.disim.mwt.android_native_app.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import it.univaq.disim.mwt.android_native_app.model.Episode;
import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;

@Database(entities = {TvShowPreview.class, Episode.class}, version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {
    private static AppRoomDatabase instance = null;

    public static synchronized AppRoomDatabase getInstance(Context context){
        return (instance == null) ?
                instance = Room.databaseBuilder(context, AppRoomDatabase.class, "AppDB.db").build() :
                instance;
    }

    // DAOs methods
    public abstract TvShowPreviewDao getTvShowPreviewDao();
    public abstract EpisodeDao getEpisodeDao();
}
