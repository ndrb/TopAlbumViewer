package com.cb.seventhwave.topalbumviewer;

import static java.io.FileDescriptor.out;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Album implements Serializable {
    private String imageUrl;
    private String artist;
    private String releaseDate;
    private String albumName;
    private boolean explicit;
    private String artistURL;
    private boolean favorite;

    public Album(String imageUrl, String artist, String releaseDate, String albumName,
                 boolean explicit, String artistURL, boolean favorite) {
        this.imageUrl = imageUrl;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.albumName = albumName;
        this.explicit = explicit;
        this.artistURL = artistURL;
        this.favorite = favorite;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getArtist() {
        return artist;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getAlbumName() {
        return albumName;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public String getArtistURL() {
        return artistURL;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}