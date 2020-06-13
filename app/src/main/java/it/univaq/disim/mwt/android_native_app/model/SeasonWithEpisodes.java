package it.univaq.disim.mwt.android_native_app.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SeasonWithEpisodes {
    @Embedded
    public Season season;
    @Relation(parentColumn = "season_id", entityColumn = "season_id")
    public List<Episode> episodes;
}
