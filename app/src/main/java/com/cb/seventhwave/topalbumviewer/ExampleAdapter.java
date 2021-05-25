package com.cb.seventhwave.topalbumviewer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private Context context;
    private ArrayList<Album> albumsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ExampleAdapter(Context context, ArrayList<Album> exampleList) {
        this.context = context;
        albumsList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.album_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position)
    {
        Album currentItem = albumsList.get(position);

        String imageUrl = currentItem.getImageUrl();
        String creatorName = currentItem.getArtist();
        String albumName = currentItem.getAlbumName();

        holder.textViewArtist.setText(creatorName);
        holder.textViewAlbumName.setText(albumName);
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.imageView);

        if(currentItem.isFavorite())
        {
            holder.favoriteView.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else
        {
            holder.favoriteView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        holder.favoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //If it's already been favourited then we want to un-favorite

                //Ideally I would put one single db access object in ViewModel and fetch it from there
                if(currentItem.isFavorite())
                {
                    currentItem.setFavorite(false);
                    holder.favoriteView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    MainActivity.albumDb.albumDao().updateAlbum(currentItem);

                }
                else {
                    currentItem.setFavorite(true);
                    holder.favoriteView.setImageResource(R.drawable.ic_baseline_favorite_24);
                    MainActivity.albumDb.albumDao().updateAlbum(currentItem);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return albumsList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;
        public TextView textViewArtist;
        public TextView textViewAlbumName;
        public ImageView favoriteView;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textViewArtist = itemView.findViewById(R.id.text_view_artist);
            textViewAlbumName = itemView.findViewById(R.id.text_view_albumName);
            favoriteView = itemView.findViewById(R.id.favorite_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}