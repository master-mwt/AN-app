package it.univaq.disim.mwt.trakd.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import it.univaq.disim.mwt.trakd.R;
import it.univaq.disim.mwt.trakd.model.Episode;
import it.univaq.disim.mwt.trakd.model.TvShowPreview;

@Database(entities = {TvShowPreview.class, Episode.class}, version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {
    private static AppRoomDatabase instance = null;

    public static synchronized AppRoomDatabase getInstance(Context context){
        return (instance == null) ?
                instance = Room.databaseBuilder(context, AppRoomDatabase.class, context.getString(R.string.room_database_name)).build() :
                instance;
    }

    // DAOs methods
    public abstract TvShowPreviewDao getTvShowPreviewDao();
    public abstract EpisodeDao getEpisodeDao();
}
