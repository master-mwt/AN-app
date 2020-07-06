package it.univaq.disim.mwt.trakd.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TvShowPreviewWithEpisodes {
    @Embedded
    public TvShowPreview tvShowPreview;
    @Relation(parentColumn = "tv_show_id", entityColumn = "tv_show_id")
    public List<Episode> episodes;
}
