package com.vanderfalken.spartanspotifyplayer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.SavedAlbum;

/**
 * Created by Josh on 11-4-2016.
 */
public class SavedAlbumsListAdapter extends SpotifyListAdapter<SavedAlbum> {
    private String mSelectedUri;
    private ArrayList<SavedAlbum> mOriginalSavedAlbums;
    //private savedAlbumListAdapterFilter msavedAlbumAdapterFilter;
    SpotifyListAdapterFilter<SavedAlbum> mSavedAlbumAdapterFilter;

    public SavedAlbumsListAdapter(Context context, ArrayList<SavedAlbum> baseList, SpotifyListAdapterFilter<SavedAlbum> savedAlbumAdapterFilter, int resource)
    {
        super(context, baseList, savedAlbumAdapterFilter, resource);
        mSavedAlbumAdapterFilter = savedAlbumAdapterFilter;
    }

    @Override
    public String getItem(int position) {
        return mSavedAlbumAdapterFilter.getFilteredItems().get(position).album.name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<SavedAlbum> getFilteredItems() {
        return mSavedAlbumAdapterFilter.getFilteredItems();
    }

    @Override
    public String getName()
    {
        int selectedPosition = mSavedAlbumAdapterFilter.getSelectedPosition();
        String name = mSavedAlbumAdapterFilter.getFilteredItems().get(selectedPosition).album.name;
        return name;
    }

    @Override
    public String getUri()
    {
        int selectedPosition = mSavedAlbumAdapterFilter.getSelectedPosition();
        if (selectedPosition == -1) {
            return "";
        }
        else {
            return mSavedAlbumAdapterFilter.getFilteredItems().get(selectedPosition).album.uri;
        }
    }

    public SavedAlbum getSavedAlbum()
    {
        int selectedPosition = mSavedAlbumAdapterFilter.getSelectedPosition();
        if (selectedPosition == -1) {
            return null;
        }
        else {
            return mSavedAlbumAdapterFilter.getFilteredItems().get(selectedPosition);
        }
    }


    @Override
    public View getView(int position,View view,ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(mContext); //mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);
        if (position == mSavedAlbumAdapterFilter.getSelectedPosition())
        {
            rowView.setBackgroundResource(android.R.color.holo_blue_dark);
        }
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        SavedAlbum savedAlbum = mSavedAlbumAdapterFilter.getFilteredItems().get(position);
        txtTitle.setText(savedAlbum.album.name + " - " + savedAlbum.album.artists.get(0).name);
        return rowView;
    }
}
