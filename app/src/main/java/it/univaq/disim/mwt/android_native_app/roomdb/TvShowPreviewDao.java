package it.univaq.disim.mwt.android_native_app.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;

@Dao
public interface TvShowPreviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(TvShowPreview... tvShowPreviews);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(List<TvShowPreview> tvShowPreviews);

    @Update
    public void update(TvShowPreview... tvShowPreviews);

    @Delete
    public void delete(TvShowPreview tvShowPreview);

    @Query("DELETE FROM tv_show_collection")
    public void deleteAll();

    @Query("SELECT * from tv_show_collection ORDER BY name ASC")
    public List<TvShowPreview> findAll();

    @Query("SELECT * FROM tv_show_collection WHERE _id = :id")
    public TvShowPreview findByID(long id);

    @Query("SELECT * FROM tv_show_collection WHERE tv_show_id = :tv_show_id")
    public TvShowPreview findByTvShowId(long tv_show_id);
}
