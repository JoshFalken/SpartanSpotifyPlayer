package com.vanderfalken.spartanspotifyplayer;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.Callback;

interface ArtistsDaoListener {
    public void onArtistsRetrieved();
}

/**
 * Created by Josh on 16-4-2016.
 */
public class ArtistsDao extends SpotifyDao<ArtistSimple, Pager<ArtistSimple>, ArtistsDaoListener> implements ArtistDaoListener {
    private int mOffset;

    private List<ArtistSimple> mBaseArtists;
    private ArtistDao mArtistDao;
    private ArrayList<ArtistSimple> mRetrievedArtists;

    public ArtistsDao()
    {
        mArtistDao = new ArtistDao();
        mRetrievedArtists = new ArrayList<ArtistSimple>();
    }

    @Override
    public void setAccessToken(String accessToken)
    {
        super.setAccessToken(accessToken);
        mArtistDao.setAccessToken(accessToken);
    }

    public void retrieveArtists(List<ArtistSimple> artistSimples)
    {
        Log.d("ArtistsDao", "Going to retrieve Artists");
        mBaseArtists = artistSimples;
        mArtistDao.addListener(this);

        if (artistSimples.size() > 0) {
            mArtistDao.retrieveArtist(artistSimples.get(0).uri.replace("spotify:artist:", ""));
        }
        else
        {
            for (ArtistsDaoListener  h1: mListeners) {
                h1.onArtistsRetrieved();
            }

        }
    }


    public ArrayList<ArtistSimple> getArtistSimples()
    {
        return mRetrievedArtists;
    }


    @Override
    public void onArtistSimpleRetrieved() {
        mRetrievedArtists.add(mArtistDao.getArtistSimple());
        mOffset = mOffset + 1;
        if (mOffset >= mBaseArtists.size())
        {
            for (ArtistsDaoListener  h1: mListeners) {
                h1.onArtistsRetrieved();
            }
        }
        else {
            mArtistDao.retrieveArtist(mBaseArtists.get(mOffset).uri.replace("spotify:artist:", ""));
        }
    }
}
