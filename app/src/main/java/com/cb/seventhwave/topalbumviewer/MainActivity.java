package com.cb.seventhwave.topalbumviewer;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import static com.cb.seventhwave.topalbumviewer.ViewModel.FETCHCOUNT;
import static com.cb.seventhwave.topalbumviewer.ViewModel.IMAGEURL;
import static com.cb.seventhwave.topalbumviewer.ViewModel.ALBUMNAME;
import static com.cb.seventhwave.topalbumviewer.ViewModel.ARTIST;
import static com.cb.seventhwave.topalbumviewer.ViewModel.RELEASEDATE;
import static com.cb.seventhwave.topalbumviewer.ViewModel.ARTISTURL;
import static com.cb.seventhwave.topalbumviewer.ViewModel.EXPLICIT;
import static com.cb.seventhwave.topalbumviewer.ViewModel.POSITION;
import static com.cb.seventhwave.topalbumviewer.ViewModel.STATELIST;


public class MainActivity extends AppCompatActivity implements ExampleAdapter.OnItemClickListener
{
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private ExampleAdapter exampleAdapter;
    private TextView emptyErrorView;
    private EditText fetchCountView;
    private Button buttonFetch;
    public ViewModel vm;
    public static AlbumDatabase albumDb; //Only use this for updating favorite

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        albumDb = Room.databaseBuilder(getApplicationContext(), AlbumDatabase.class, "album").allowMainThreadQueries().build();

        loadUiElements();

        vm = new ViewModel(this);


        //We could put this code in onRestoreInstanceState and we would not need null checking
        // If we have a saved state then we can restore it now
        if (savedInstanceState != null)
        {
            try
            {
                ArrayList<Album> albums = (ArrayList<Album>) savedInstanceState.getSerializable(STATELIST);
                int fetchCount = savedInstanceState.getInt(FETCHCOUNT);
                vm.setExampleList(albums);
                vm.setRequestCount(fetchCount);
                configRecycler();
            }
            catch(ClassCastException cce)
            {
                Log.i(TAG, "Could not fetch data from saved instance");
            }
        }

        else
        {
            fetchCountView.setText(vm.getRequestCount()+""); //By default 25
            updateList();
        }
    }

    @Override
    public void onItemClick(int position)
    {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Album clickedItem = vm.getExampleList().get(position);

        detailIntent.putExtra(POSITION, position);
        detailIntent.putExtra(IMAGEURL, clickedItem.getImageUrl());
        detailIntent.putExtra(ARTIST, clickedItem.getArtist());
        detailIntent.putExtra(RELEASEDATE, clickedItem.getReleaseDate());
        detailIntent.putExtra(ALBUMNAME, clickedItem.getAlbumName());
        detailIntent.putExtra(EXPLICIT, clickedItem.isExplicit());
        detailIntent.putExtra(ARTISTURL, clickedItem.getArtistURL());

        startActivity(detailIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATELIST, vm.getExampleList());
        outState.putInt(FETCHCOUNT, vm.getRequestCount());
    }

    private void loadUiElements()
    {
        emptyErrorView = (TextView) findViewById(R.id.empty_error_view);
        fetchCountView = (EditText) findViewById(R.id.fetch_count_text_input);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configRecycler()
    {
        exampleAdapter = new ExampleAdapter(MainActivity.this, vm.getExampleList());
        recyclerView.setAdapter(exampleAdapter);
        exampleAdapter.setOnItemClickListener(MainActivity.this);
    }

    public void myAlbumFetchClickHandler(View target)
    {
        String fetchCountString = fetchCountView.getText().toString();
        if(fetchCountString != null && !fetchCountString.isEmpty())
        {
            int fetchCountInt = Integer.parseInt(fetchCountString);
            if(fetchCountInt != 0)// && fetchCountInt!= vm.getRequestCount())
            {
                vm.setRequestCount(fetchCountInt);
                vm.refreshData( new CustomListener<String>()
                {
                    @Override
                    public void getResult(String result)
                    {
                        if (result.equals("200"))
                        {
                            emptyErrorView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            configRecycler();
                        }
                        else
                        {
                            recyclerView.setVisibility(View.GONE);
                            emptyErrorView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }
    }

    private void updateList()
    {
        vm.getData( new CustomListener<String>()
        {
            @Override
            public void getResult(String result)
            {
                if (result.equals("200"))
                {
                    emptyErrorView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    configRecycler();
                }
                else
                {
                    recyclerView.setVisibility(View.GONE);
                    emptyErrorView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}