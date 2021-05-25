package com.cb.seventhwave.topalbumviewer;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewModel
{
    public static final String IMAGEURL = "imageUrl";
    public static final String ARTIST = "artist";
    public static final String RELEASEDATE = "releaseDate";
    public static final String ALBUMNAME = "albumName";
    public static final String EXPLICIT = "explicit";
    public static final String ARTISTURL = "artistURL";

    public static final String POSITION = "position";

    public static final String STATELIST = "state_list";
    public static final String FETCHCOUNT = "fetch_count";

    private static final String TAG = "ViewModel";
    private ArrayList<Album> exampleList;
    private RequestQueue requestQueue;
    private int requestCount;
    public static AlbumDatabase albumDb;

    private AlbumDatabase db;

    ViewModel(Context context)
    {
        //Declare initial data
        requestCount = 25;
        if(exampleList == null)
        {
            exampleList = new ArrayList<Album>();
        }

        albumDb = Room.databaseBuilder(context, AlbumDatabase.class, "album").allowMainThreadQueries().build();

        //If there is data saved in the room then load it into list
        ArrayList<Album> holderArrayList = parseRoom();
        if(holderArrayList.size() != 0)
        {
            exampleList = holderArrayList;
            requestCount = holderArrayList.size();
        }

        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public ArrayList<Album> getExampleList() {
        return exampleList;
    }

    public void setExampleList(ArrayList<Album> mExampleList) {
        this.exampleList = mExampleList;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public void getData(final CustomListener<String> listener)
    {
        //If there is data saved in the room then load it into list
        ArrayList<Album> holderArrayList = parseRoom();
        if (holderArrayList.size() != 0) {
            exampleList = holderArrayList;
            requestCount = holderArrayList.size();
            listener.getResult("200");
        }
        else
        {
            parseJSON( new CustomListener<String>()
            {
                @Override
                public void getResult(String result)
                {
                    if (result.equals("200"))
                    {
                        listener.getResult("200");
                    }
                    else
                    {
                        listener.getResult("300");
                    }
                }
            });
        }
    }


    public void refreshData(final CustomListener<String> listener)
    {
        parseJSON( new CustomListener<String>()
        {
            @Override
            public void getResult(String result)
            {
                if (result.equals("200"))
                {
                    listener.getResult("200");
                }
                else
                {
                    listener.getResult("300");
                }
            }
        });
    }

    private ArrayList<Album> parseRoom()
    {
        List<Album> listOfAlbums = albumDb.albumDao().getAllData();
        ArrayList<Album> arrayListAlbums = new ArrayList<Album>();

        if(listOfAlbums.size() != 0)
        {
            arrayListAlbums.addAll(listOfAlbums);
        }
        return arrayListAlbums;
    }

    private void parseJSON(final CustomListener<String> listener)
    {
        String realUrl = "https://rss.itunes.apple.com/api/v1/us/apple-music/top-albums/all/"+requestCount+"/explicit.json/";

        ArrayList<Album> holderArrayList = new ArrayList<Album>();
        //exampleList = new ArrayList<Album>();
        albumDb.albumDao().nukeTable();//Reset database

        JsonObjectRequest realRequest = new JsonObjectRequest(Request.Method.GET, realUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            /*
                            Main Components of a JSON file:
                            1	Array([)
                            In a JSON file , square bracket ([) represents a JSON array

                            2   Objects({)
                            In a JSON file, curly bracket ({) represents a JSON object

                            3	Key
                            A JSON object contains a key that is just a string. Pairs of key/value make up a JSON object

                            4	Value
                            Each key has a value that could be string , integer or double e.t.c
                             */

                            JSONObject feed = response.getJSONObject("feed");
                            JSONArray results = feed.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++)
                            {
                                JSONObject hit = results.getJSONObject(i);

                                int albumId = hit.getInt("id");
                                String creatorName = hit.getString("artistName");
                                String imageUrl = hit.getString("artworkUrl100");
                                String releaseDate = hit.getString("releaseDate");
                                String albumName = hit.getString("name");
                                boolean explicit = false;
                                try{
                                    if(hit.getString("contentAdvisoryRating").equals("Explicit"))
                                    {
                                        explicit = true;
                                    }
                                }
                                catch(JSONException e)
                                {
                                    listener.getResult("300");
                                }

                                String artistURL = hit.getString("artistUrl");

                                Log.i(TAG, "Adding element to list");

                                Album album = new Album(albumId, imageUrl, creatorName, releaseDate, albumName, explicit, artistURL, false, i);
                                holderArrayList.add(album);
                            }

                            //Now that our new fetched list is complete, do a merge
                            mergeFavoriteAttribute(exampleList, holderArrayList);

                            exampleList = holderArrayList;

                            for(Album alb : exampleList)
                            {
                                albumDb.albumDao().addData(alb);
                            }

                            listener.getResult("200");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(realRequest);
    }

    private void mergeFavoriteAttribute(ArrayList<Album> originalArrayList, ArrayList<Album> newArrayList)
    {
        //Iterate through new list
        for(Album album : newArrayList)
        {
            //If the album has been fetched in the past, there might be a Favorite attribute set
            if(originalArrayList.contains(album))
            {
                //Log.i(TAG, "AFFECT");
                //Find the favorite attribute of the album
                int position = originalArrayList.indexOf(album);
                boolean fav = originalArrayList.get(position).isFavorite();

                int newPosition = newArrayList.indexOf(album);
                newArrayList.get(newPosition).setFavorite(fav);
            }
        }
    }

}
