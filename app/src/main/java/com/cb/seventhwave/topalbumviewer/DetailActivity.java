package com.cb.seventhwave.topalbumviewer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import static com.cb.seventhwave.topalbumviewer.ViewModel.IMAGEURL;
import static com.cb.seventhwave.topalbumviewer.ViewModel.ALBUMNAME;
import static com.cb.seventhwave.topalbumviewer.ViewModel.ARTIST;
import static com.cb.seventhwave.topalbumviewer.ViewModel.POSITION;
import static com.cb.seventhwave.topalbumviewer.ViewModel.RELEASEDATE;
import static com.cb.seventhwave.topalbumviewer.ViewModel.ARTISTURL;
import static com.cb.seventhwave.topalbumviewer.ViewModel.EXPLICIT;

import java.net.URL;


public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DETAIL ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        int position = intent.getIntExtra(POSITION,-1);
        if(position != -1)
        {

        }

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

        SpannableString content = new SpannableString(artistURL);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textViewArtistUrl.setText(content);
        
        textViewArtistUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(artistURL));
                startActivity(intent);
            }
        });

        if(!explicit)
            textViewExplicit.setVisibility(TextView.GONE); //View goes away and does not take space for layout
    }
}
