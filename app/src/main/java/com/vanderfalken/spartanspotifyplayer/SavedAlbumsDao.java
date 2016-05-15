package com.vanderfalken.spartanspotifyplayer;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.SavedAlbum;
import retrofit.RetrofitError;
import retrofit.client.Response;

interface SavedAlbumsDaoListener {
    void onSavedAlbumsRetrieved();
}
/**
 * Created by Josh on 29-4-2016.
 */
public class SavedAlbumsDao extends SpotifyDao<SavedAlbum, Pager<SavedAlbum>, SavedAlbumsDaoListener> {
    private int mOffset;
    private ArrayList<SavedAlbum> mSavedAlbums;
    private ArtistsDao mArtistsDao;
    private Artists mArtists;

    public SavedAlbumsDao()
    {
        mArtistsDao = new ArtistsDao();
        mOffset = 0;


    }

    @Override
    public void setAccessToken(String accessToken)
    {
        super.setAccessToken(accessToken);
        mArtistsDao.setAccessToken(accessToken);
        Log.d("accesstoken", accessToken);
    }
    @Override
    public void success(Pager<SavedAlbum> savedAlbumPager, Response response) {
        for (SavedAlbum savedAlbum : savedAlbumPager.items) {
            mSavedAlbums.add(savedAlbum);
            Log.d("succes!", savedAlbum.album.name + savedAlbum.album.artists.get(0).name);
        }
        if (savedAlbumPager.items.size() == 50)
        {
            mOffset = mOffset + 50;
            retrieveNextSavedAlbums();
        }
        else
        {
            sortAlphabetically();
            for (SavedAlbumsDaoListener hl : mListeners)
                hl.onSavedAlbumsRetrieved();
        }
    }

    public ArrayList<SavedAlbum> getSavedAlbums()
    {
        return mSavedAlbums;
    }

    private void sortAlphabetically()
    {
        Collections.sort(mSavedAlbums, new Comparator<SavedAlbum>() {
            @Override
            public int compare(SavedAlbum p1, SavedAlbum p2) {
                return p1.album.name.compareTo(p2.album.name);
            }
        });
    }

    @Override
    public void failure(RetrofitError error) {
        Log.d("error", error.getMessage());
    }

    private void retrieveNextSavedAlbums()
    {
        Map<String,Object> savedAlbumsFilters = new HashMap<>();
        savedAlbumsFilters.put("offset", mOffset);
        savedAlbumsFilters.put("limit", 50);
        this.getSpotifyService().getMySavedAlbums(savedAlbumsFilters, this);

   }

    public void retrieveSavedAlbums()
    {
        mSavedAlbums = new ArrayList<SavedAlbum>();
        mOffset = 0;

        retrieveNextSavedAlbums();
    }
}
