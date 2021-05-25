package com.cb.seventhwave.topalbumviewer;

import android.content.Context;
import android.util.Log;

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

    ViewModel(Context context)
    {
        if(exampleList == null)
        {
            exampleList = new ArrayList<Album>();
        }
        requestCount = 25;
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

    public void parseJSON(final CustomListener<String> listener)
    {
        //String realUrl = "https://rss.itunes.apple.com/api/v1/us/apple-music/top-albums/all/25/explicit.json/";
        String realUrl = "https://rss.itunes.apple.com/api/v1/us/apple-music/top-albums/all/"+requestCount+"/explicit.json/";

        exampleList = new ArrayList<Album>();

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
                                {}

                                String artistURL = hit.getString("artistUrl");

                                Log.i(TAG, "Adding element to list");
                                exampleList.add(new Album(imageUrl, creatorName, releaseDate, albumName, explicit, artistURL, false));
                            }
                            listener.getResult("done");
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

        //Log.i(TAG, "About to make volley request");
        requestQueue.add(realRequest);
        //Log.i(TAG, "Finished processing list, size: "+exampleList.size());

    }

}
