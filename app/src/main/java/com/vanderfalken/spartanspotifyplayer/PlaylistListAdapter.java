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

import kaaes.spotify.webapi.android.models.PlaylistSimple;

/**
 * Created by Josh on 11-4-2016.
 */
public class PlaylistListAdapter extends SpotifyListAdapter<PlaylistSimple> {
    private String mSelectedUri;
    private ArrayList<PlaylistSimple> mOriginalPlaylistSimples;
    //private PlaylistListAdapterFilter mPlaylistAdapterFilter;
    SpotifyListAdapterFilter<PlaylistSimple> mPlaylistAdapterFilter;

    public PlaylistListAdapter(Context context, ArrayList<PlaylistSimple> baseList, SpotifyListAdapterFilter<PlaylistSimple> playlistAdapterFilter, int resource)
    {
        super(context, baseList, playlistAdapterFilter, resource);
        mPlaylistAdapterFilter = playlistAdapterFilter;
    }

    @Override
    public String getItem(int position) {
        return mPlaylistAdapterFilter.getFilteredItems().get(position).name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<PlaylistSimple> getFilteredItems() {
        return mPlaylistAdapterFilter.getFilteredItems();
    }

    @Override
    public String getName()
    {
        int selectedPosition = mPlaylistAdapterFilter.getSelectedPosition();
        String name = mPlaylistAdapterFilter.getFilteredItems().get(selectedPosition).name;
        return name;
    }

    @Override
    public String getUri()
    {
        int selectedPosition = mPlaylistAdapterFilter.getSelectedPosition();
        if (selectedPosition == -1) {
            return "";
        }
        else {
            return mPlaylistAdapterFilter.getFilteredItems().get(selectedPosition).uri;
        }
    }


    @Override
    public View getView(int position,View view,ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(mContext); //mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);
        if (position == mPlaylistAdapterFilter.getSelectedPosition())
        {
            //TODO: set the proper selection color here:
            rowView.setBackgroundResource(android.R.color.holo_blue_dark);
        }
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(mPlaylistAdapterFilter.getFilteredItems().get(position).name);
        return rowView;
    }
}
