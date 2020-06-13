package it.univaq.disim.mwt.android_native_app.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import it.univaq.disim.mwt.android_native_app.model.Season;
import it.univaq.disim.mwt.android_native_app.model.SeasonWithEpisodes;

@Dao
public interface SeasonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(Season... seasons);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(List<Season> seasons);

    @Update
    public void update(Season... seasons);

    @Delete
    public void delete(Season season);

    @Query("DELETE FROM seasons")
    public void deleteAll();

    @Query("SELECT * FROM seasons")
    public List<Season> findAll();

    @Query("SELECT * FROM seasons WHERE id = :id")
    public Season findByID(long id);

    @Query("SELECT * FROM seasons WHERE season_id = :season_id")
    public Season findBySeasonId(long season_id);

    @Transaction
    @Query("SELECT * FROM seasons")
    public List<SeasonWithEpisodes> getSeasonWithEpisodes();
}
