package it.univaq.disim.mwt.android_native_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TvShowCharacter implements Parcelable {
    private long id;
    private String character;
    private String name;
    private String profile_path;

    public static final Creator<TvShowCharacter> CREATOR = new Creator<TvShowCharacter>() {
        @Override
        public TvShowCharacter createFromParcel(Parcel source) {
            return new TvShowCharacter(source);
        }

        @Override
        public TvShowCharacter[] newArray(int size) {
            return new TvShowCharacter[size];
        }
    };

    public TvShowCharacter() {
    }

    public TvShowCharacter(long id, String character, String name, String profile_path) {
        this.id = id;
        this.character = character;
        this.name = name;
        this.profile_path = profile_path;
    }

    public TvShowCharacter(Parcel source){
        id = source.readLong();
        character = source.readString();
        name = source.readString();
        profile_path = source.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(character);
        dest.writeString(name);
        dest.writeString(profile_path);
    }
}
