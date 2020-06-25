package it.univaq.disim.mwt.android_native_app.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.univaq.disim.mwt.android_native_app.model.Episode;

@Dao
public interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(Episode... episodes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(List<Episode> episodes);

    @Update
    public void update(Episode... episodes);

    @Query("DELETE FROM episodes WHERE episode_id = :episode_id")
    public void deleteByEpisodeID(long episode_id);

    @Query("DELETE FROM episodes WHERE season_id = :season_id")
    public void deleteBySeasonID(long season_id);

    @Query("DELETE FROM episodes WHERE tv_show_id = :tv_show_id")
    public void deleteByTvShowID(long tv_show_id);

    @Query("DELETE FROM episodes")
    public void deleteAll();

    @Query("SELECT * FROM episodes")
    public List<Episode> findAll();

    @Query("SELECT * FROM episodes WHERE id = :id")
    public Episode findByID(long id);

    @Query("SELECT * FROM episodes WHERE episode_id = :episode_id")
    public Episode findByEpisodeId(long episode_id);

    @Query("SELECT * FROM episodes WHERE season_id = :season_id")
    public List<Episode> findBySeasonId(long season_id);

    @Query("SELECT * FROM episodes WHERE tv_show_id = :tv_show_id")
    public List<Episode> findByTvShowId(long tv_show_id);
}
