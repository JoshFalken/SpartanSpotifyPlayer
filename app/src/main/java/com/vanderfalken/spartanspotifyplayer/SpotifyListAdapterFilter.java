package com.vanderfalken.spartanspotifyplayer;

import android.util.Log;
import android.widget.Filter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.PlaylistSimple;

/**
 * Created by Josh on 7-5-2016.
 */
public abstract class SpotifyListAdapterFilter<T> extends Filter {
    protected ArrayList<T> mFilteredItems;
    protected ArrayList<T> mOriginalItems;
    protected SpotifyListAdapter<T> mSpotifyListAdapter;
    protected int mSelectedPosition;
    protected String mSelectedIdentifier;

    abstract void setSelectedPositionToIdentifier();
    abstract protected boolean includeItem(CharSequence constraint, T item);
    abstract protected int getSelectedPosition();

    public SpotifyListAdapterFilter(ArrayList<T> originalItems)
    {
        mFilteredItems = originalItems;
        mOriginalItems = originalItems;
        mSpotifyListAdapter = null;
        mSelectedPosition = -1;
    }



    public void setListAdapter(SpotifyListAdapter<T> spotifyListAdapter)
    {
        mSpotifyListAdapter = spotifyListAdapter;
    }


    // Following must be implemented
    protected void setSelectedPosition(int position)
    {
        mSelectedPosition = position;
    }



    public ArrayList<T> getFilteredItems()
    {
        return mFilteredItems;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        final FilterResults oReturn = new FilterResults();
        mFilteredItems = new ArrayList<T>();
        if (constraint != null) {
            if (mOriginalItems != null && mOriginalItems.size() > 0) {
                for (final T t : mOriginalItems) {
                    if (includeItem(constraint, t)) {
                        mFilteredItems.add(t);
                        //mFilteredPlaylistSimples.add(playlistSimple);
                    }
                }
            }
            oReturn.values = mFilteredItems;
        }
        setSelectedPositionToIdentifier();
        return oReturn;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint,
                                  FilterResults results) {
        mFilteredItems = (ArrayList<T>) results.values;
        if (mSpotifyListAdapter != null)
            mSpotifyListAdapter.notifyDataSetChanged();
    }


}
