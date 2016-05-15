package com.vanderfalken.spartanspotifyplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.SavedAlbum;
import kaaes.spotify.webapi.android.models.SavedTrack;
import retrofit.RetrofitError;
import retrofit.client.Response;

interface SavedTracksDaoListener {
    void onSavedTracksRetrieved();
}
/**
/**
 * Created by Josh on 29-4-2016.
 */
public class SavedTracksDao  extends SpotifyDao<SavedTrack, Pager<SavedTrack>, SavedTracksDaoListener> {
    private int mOffset;
    private ArrayList<SavedTrack> mSavedTracks;

    @Override
    public void success(Pager<SavedTrack> savedTrackPager, Response response) {
        for (SavedTrack savedTrack : savedTrackPager.items) {
            mSavedTracks.add(savedTrack);
        }
        if (savedTrackPager.items.size() == 50)
        {
            mOffset = mOffset + 50;
            retrieveNextSavedAlbums();
        }
        else
        {
            sortAlphabetically();
            for (SavedTracksDaoListener hl : mListeners)
                hl.onSavedTracksRetrieved();
        }
    }

    private ArrayList<SavedTrack> getSavedTracks()
    {
        return mSavedTracks;
    }

    private void sortAlphabetically()
    {
        Collections.sort(mSavedTracks, new Comparator<SavedTrack>() {
            @Override
            public int compare(SavedTrack p1, SavedTrack p2) {
                return p1.track.name.compareTo(p2.track.name);
            }
        });
    }

    @Override
    public void failure(RetrofitError error) {

    }

    private void retrieveNextSavedAlbums()
    {
        Map<String,Object> savedAlbumsFilters = new HashMap<>();
        savedAlbumsFilters.put("offset", mOffset);
        savedAlbumsFilters.put("limit", 50);
        this.getSpotifyService().getMySavedTracks(savedAlbumsFilters, this);

    }

    public void retrieveSavedAlbums()
    {
        mSavedTracks = new ArrayList<SavedTrack>();
        mOffset = 0;

        retrieveNextSavedAlbums();
    }
}
