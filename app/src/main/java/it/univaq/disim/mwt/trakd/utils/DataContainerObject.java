package it.univaq.disim.mwt.trakd.utils;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

import it.univaq.disim.mwt.trakd.model.Episode;
import it.univaq.disim.mwt.trakd.model.TvShowPreview;

public class DataContainerObject implements Serializable {
    @Expose
    public List<TvShowPreview> tvShowPreviews;
    @Expose
    public List<Episode> episodes;

    public DataContainerObject(){
    }

    public DataContainerObject(List<TvShowPreview> tvShowPreviews, List<Episode> episodes){
        this.tvShowPreviews = tvShowPreviews;
        this.episodes = episodes;
    }
}
