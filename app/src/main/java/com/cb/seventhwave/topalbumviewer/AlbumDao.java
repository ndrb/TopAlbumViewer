package com.cb.seventhwave.topalbumviewer;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AlbumDao {

    @Insert
    public void addData(Album album);

    @Query("select * from album ORDER BY `order` ASC ")
    public List<Album> getAllData();

    /*
    The implementation of the method will update its parameters in the
    database if they already exists (checked by primary keys).
    If they don't already exists, this option will not change the database.
     */
    @Update
    public void updateAlbum(Album album);

    @Query("DELETE FROM album")
    public void nukeTable();

    @Delete
    public void delete(Album album);

}
