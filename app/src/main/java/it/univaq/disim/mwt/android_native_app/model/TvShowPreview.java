package it.univaq.disim.mwt.android_native_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TvShowPreview implements Parcelable {
    private long _id;
    private long tv_show_id;
    private String name;
    private String poster_path;

    public static final Creator<TvShowPreview> CREATOR = new Creator<TvShowPreview>() {
        @Override
        public TvShowPreview createFromParcel(Parcel source) {
            return new TvShowPreview(source);
        }

        @Override
        public TvShowPreview[] newArray(int size) {
            return new TvShowPreview[size];
        }
    };

    public TvShowPreview() {
    }

    public TvShowPreview(long tv_show_id, String name, String poster_path) {
        this.tv_show_id = tv_show_id;
        this.name = name;
        this.poster_path = poster_path;
    }

    public TvShowPreview(Parcel source) {
        _id = source.readLong();
        tv_show_id = source.readLong();
        name = source.readString();
        poster_path = source.readString();
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getTv_show_id() {
        return tv_show_id;
    }

    public void setTv_show_id(long tv_show_id) {
        this.tv_show_id = tv_show_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeLong(tv_show_id);
        dest.writeString(name);
        dest.writeString(poster_path);
    }
}
