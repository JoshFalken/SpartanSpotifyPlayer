package com.vanderfalken.spartanspotifyplayer;

import android.util.Log;
import android.widget.Filter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.PlaylistSimple;

/**
 * Created by Josh on 7-5-2016.
 */
public class PlaylistListAdapterFilter extends SpotifyListAdapterFilter<PlaylistSimple>  {
    private String mSelectedUri;
    private int mSelectedPosition;

    public PlaylistListAdapterFilter(ArrayList<PlaylistSimple> originalItems)
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
            if (mFilteredItems.get(index).uri.compareTo(mSelectedUri) == 0) {
                mSelectedPosition = index;
                return;
            }
        }
    }

    @Override
    public void setSelectedPosition(int selectedPosition)
    {
        mSelectedUri = mFilteredItems.get(selectedPosition).uri;
        mSelectedPosition = selectedPosition;
    }

    @Override
    public int getSelectedPosition()
    {
        return mSelectedPosition;
    }

    @Override
    protected boolean includeItem(CharSequence constraint, PlaylistSimple item)
    {
        if (item.name.toLowerCase().contains(constraint.toString().toLowerCase()))
            return true;

        return false;
    }

}
