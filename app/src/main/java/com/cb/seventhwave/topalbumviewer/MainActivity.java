package com.cb.seventhwave.topalbumviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

public class MainActivity extends AppCompatActivity implements ExampleAdapter.OnItemClickListener {
    public static final String IMAGEURL = "imageUrl";
    public static final String ARTIST = "artist";
    public static final String RELEASEDATE = "releaseDate";
    public static final String ALBUMNAME = "albumName";
    public static final String EXPLICIT = "explicit";
    public static final String ARTISTURL = "artistURL";

    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mExampleList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();
    }

    private void parseJSON()
    {
        String realUrl = "https://rss.itunes.apple.com/api/v1/us/apple-music/top-albums/all/25/explicit.json/";

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


                                mExampleList.add(new ExampleItem(imageUrl, creatorName, releaseDate, albumName, explicit, artistURL));
                            }

                            mExampleAdapter = new ExampleAdapter(MainActivity.this, mExampleList);
                            mRecyclerView.setAdapter(mExampleAdapter);
                            mExampleAdapter.setOnItemClickListener(MainActivity.this);

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

        mRequestQueue.add(realRequest);

    }

    @Override
    public void onItemClick(int position)
    {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        ExampleItem clickedItem = mExampleList.get(position);

        detailIntent.putExtra(IMAGEURL, clickedItem.getImageUrl());
        detailIntent.putExtra(ARTIST, clickedItem.getArtist());
        detailIntent.putExtra(RELEASEDATE, clickedItem.getReleaseDate());
        detailIntent.putExtra(ALBUMNAME, clickedItem.getAlbumName());
        detailIntent.putExtra(EXPLICIT, clickedItem.isExplicit());
        detailIntent.putExtra(ARTISTURL, clickedItem.getArtistURL());


        startActivity(detailIntent);
    }
}