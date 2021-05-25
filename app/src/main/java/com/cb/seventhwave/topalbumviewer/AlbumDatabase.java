package com.cb.seventhwave.topalbumviewer;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities={Album.class},version = 1)
public abstract class AlbumDatabase extends RoomDatabase{

    public abstract AlbumDao albumDao();


}
