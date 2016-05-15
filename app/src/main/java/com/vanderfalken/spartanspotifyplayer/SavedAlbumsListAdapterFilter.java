package com.vanderfalken.spartanspotifyplayer;

import android.util.Log;
import android.widget.Filter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.SavedAlbum;

/**
 * Created by Josh on 7-5-2016.
 */
public class SavedAlbumsListAdapterFilter extends SpotifyListAdapterFilter<SavedAlbum>  {
    private String mSelectedUri;
    private int mSelectedPosition;

    public SavedAlbumsListAdapterFilter(ArrayList<SavedAlbum> originalItems)
    {
        super(originalItems);
        mSelectedUri = "";
        mSelectedPosition = -1;
    }

    @Override
    public void setSelectedPositionToIdentifier()
    {
        int index;
        for (index = 0;index < mFilteredItems.size();index++)
        {
            if (mFilteredItems.get(index).album.uri.compareTo(mSelectedUri) == 0) {
                mSelectedPosition = index;
                return;
            }
        }
    }

    @Override
    public void setSelectedPosition(int selectedPosition)
    {
        mSelectedUri = mFilteredItems.get(selectedPosition).album.uri;
        mSelectedPosition = selectedPosition;
    }

    @Override
    public int getSelectedPosition()
    {
        return mSelectedPosition;
    }

    @Override
    protected boolean includeItem(CharSequence constraint, SavedAlbum item)
    {
        if (item.album.name.toLowerCase().contains(constraint.toString().toLowerCase()))
            return true;

        return false;
    }

}
