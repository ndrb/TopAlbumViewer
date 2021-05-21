package com.cb.seventhwave.topalbumviewer;

import static java.io.FileDescriptor.out;

import android.os.Parcel;
import android.os.Parcelable;

public class ExampleItem {
    private String imageUrl;
    private String artist;
    private String releaseDate;
    private String albumName;
    private boolean explicit;
    private String artistURL;

    public ExampleItem(String imageUrl, String artist, String releaseDate, String albumName,
                       boolean explicit, String artistURL) {
        this.imageUrl = imageUrl;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.albumName = albumName;
        this.explicit = explicit;
        this.artistURL = artistURL;
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
}