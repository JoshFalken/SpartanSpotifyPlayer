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

import kaaes.spotify.webapi.android.models.PlaylistSimple;

/*

    public PlaylistListAdapter(Activity context, ArrayList<PlaylistSimple> playlistsSimples) {
        super(context, R.layout.mylist);

        mOriginalPlaylistSimples = playlistsSimples;
        mContext = context;
        mSelectedUri = "";

        mPlaylistAdapterFilter = new PlaylistListAdapterFilter(this, playlistsSimples);
    }


    public void setSelectedUriPosition(int position)
    {
        mPlaylistAdapterFilter.setSelectedUriPosition(position);
    }

    public int getPositionOfSelectedUri() {
        return this.mPlaylistAdapterFilter.getPositionOfSelectedUri();
    }


    public Filter getFilter()
    {
        return mPlaylistAdapterFilter;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPlaylistAdapterFilter.getFilteredPlaylistSimples().size();
    }

    @Override
    public String getItem(int position) {
        return mPlaylistAdapterFilter.getFilteredPlaylistSimples().get(position).name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<PlaylistSimple> getFilteredPlaylistSimples() {
        return mPlaylistAdapterFilter.getFilteredPlaylistSimples();
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        //TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(mPlaylistAdapterFilter.getFilteredPlaylistSimples().get(position).name);
        //imageView.setImageResource(R.drawable.playlist);
        //extratxt.setText(mPlaylistSimpleArrayList.get(position).tracks.total);
        //extratxt.setText("");
        return rowView;
    };


 */
/**
 * Created by Josh on 7-5-2016.
 */
public abstract class SpotifyListAdapter<T> extends ArrayAdapter<String> implements Filterable {
    private ArrayList<T> mBaseList;
    protected Context mContext;
    protected String mSelectedItemIdentifier;
    private SpotifyListAdapterFilter<T> mSpotifyListAdapterFilter;
    private int mResource;
    private int mSelectedPosition;

    abstract String getName();
    abstract String getUri();

    public SpotifyListAdapter(Context context, ArrayList<T> baseList, SpotifyListAdapterFilter<T> spotifyListAdapterFilter, int resource)
    {
        super(context, resource);
        mBaseList = baseList;
        mContext = context;
        mSpotifyListAdapterFilter = spotifyListAdapterFilter;
        mResource = resource;
    }

    public SpotifyListAdapterFilter<T> getFilter()
    {
        return mSpotifyListAdapterFilter;
    }

    protected void setSelectedItemIdentifier()
    {


    }

    protected void setSelectedPosition(int position)
    {
        mSpotifyListAdapterFilter.setSelectedPosition(position);
    }

    public ArrayList<T> getFilteredItems()
    {
        return mSpotifyListAdapterFilter.getFilteredItems();
    }


    public void setSelectedItemIdentifier(String selectedItemIdentifier)
    {
        mSelectedItemIdentifier = selectedItemIdentifier;
    }

    public int getSelectedRowPosition()
    {
        return mSpotifyListAdapterFilter.getSelectedPosition();
    }

    @Override
    public int getCount() {
        return mSpotifyListAdapterFilter.getFilteredItems().size();
    }

    abstract public String getItem(int position);

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /*
    public View getView(int position, View view, ViewGroup parent) {
        //LayoutInflater inflater = mContext.getLayoutInflater();
        LayoutInflater inflater= LayoutInflater.from(mContext);
        View rowView=inflater.inflate(mResource, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        //TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        //txtTitle.setText(mPlaylistAdapterFilter.getFilteredPlaylistSimples().get(position).name);
        //imageView.setImageResource(R.drawable.playlist);
        //extratxt.setText(mPlaylistSimpleArrayList.get(position).tracks.total);
        //extratxt.setText("");
        return rowView;
    };
    */
}
