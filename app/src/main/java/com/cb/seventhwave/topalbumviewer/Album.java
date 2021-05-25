package com.cb.seventhwave.topalbumviewer;

import static java.io.FileDescriptor.out;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName="album")
public class Album implements Serializable, Comparable
{
    @PrimaryKey
    private int albumId;

    @ColumnInfo(name = "imageUrl")
    private String imageUrl;

    @ColumnInfo(name = "artist")
    private String artist;

    @ColumnInfo(name = "releaseDate")
    private String releaseDate;

    @ColumnInfo(name = "albumName")
    private String albumName;

    @ColumnInfo(name = "explicit")
    private boolean explicit;

    @ColumnInfo(name = "artistURL")
    private String artistURL;

    @ColumnInfo(name = "favorite")
    private boolean favorite;

    @ColumnInfo(name = "order")
    private int order;

    public Album(int albumId, String imageUrl, String artist, String releaseDate, String albumName,
                 boolean explicit, String artistURL, boolean favorite, int order)
    {
        this.albumId = albumId;
        this.imageUrl = imageUrl;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.albumName = albumName;
        this.explicit = explicit;
        this.artistURL = artistURL;
        this.favorite = favorite;
        this.order = order;
    }

    public int getAlbumId() { return albumId; }

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

    public int getOrder() { return order; }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Album))
        {
            return false;
        }

        Album album = (Album) obj;
        return Objects.equals(album.albumId, this.albumId);
    }

    @Override
    public int compareTo(Object obj) {
        Album album = (Album) obj;
        return Integer.compare(albumId, album.getAlbumId());
    }
}