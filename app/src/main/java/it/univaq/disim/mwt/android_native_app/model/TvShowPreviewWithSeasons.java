package it.univaq.disim.mwt.android_native_app.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TvShowPreviewWithSeasons {
    @Embedded
    public TvShowPreview tvShowPreview;
    @Relation(parentColumn = "tv_show_id", entityColumn = "tv_show_id")
    public List<Season> seasons;
}
