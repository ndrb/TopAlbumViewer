package com.cb.seventhwave.topalbumviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import static com.cb.seventhwave.topalbumviewer.MainActivity.IMAGEURL;
import static com.cb.seventhwave.topalbumviewer.MainActivity.ALBUMNAME;
import static com.cb.seventhwave.topalbumviewer.MainActivity.ARTIST;
import static com.cb.seventhwave.topalbumviewer.MainActivity.RELEASEDATE;
import static com.cb.seventhwave.topalbumviewer.MainActivity.ARTISTURL;
import static com.cb.seventhwave.topalbumviewer.MainActivity.EXPLICIT;

import java.net.URL;


public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DETAIL ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(IMAGEURL);
        String artist = intent.getStringExtra(ARTIST);
        String releaseDate = intent.getStringExtra(RELEASEDATE);
        String albumName = intent.getStringExtra(ALBUMNAME);
        boolean explicit = intent.getBooleanExtra(EXPLICIT, false);
        String artistURL = intent.getStringExtra(ARTISTURL);


        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewAlbumName = findViewById(R.id.text_view_albumName_detail);
        TextView textViewArtist = findViewById(R.id.text_view_artist_detail);
        TextView textViewReleaseDate = findViewById(R.id.text_view_release_date_detail);
        TextView textViewArtistUrl = findViewById(R.id.text_view_artist_url_detail);
        TextView textViewExplicit = findViewById(R.id.text_view_explicit_detail);


        Picasso.get().load(imageUrl).fit().centerInside().into(imageView);
        textViewAlbumName.setText(albumName);
        textViewArtist.setText(artist);
        textViewReleaseDate.setText(releaseDate);
        textViewArtistUrl.setText(artistURL);

        if(!explicit)
            textViewExplicit.setVisibility(TextView.GONE); //View goes away and does not take space for layout
    }
}
