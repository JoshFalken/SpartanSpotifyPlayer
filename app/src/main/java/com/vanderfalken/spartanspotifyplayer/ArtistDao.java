package com.vanderfalken.spartanspotifyplayer;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Artists;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Josh on 16-4-2016.
 */

interface ArtistDaoListener {
    public void onArtistSimpleRetrieved();
}

public class ArtistDao extends SpotifyDao<Artist, Artist, ArtistDaoListener> {
    private int mOffset;
    private ArtistSimple mArtistSimple;

    public void retrieveArtist(String uri)
    {
        Log.d("ArtistDao", "Uri = " + uri);
        this.getSpotifyService().getArtist(uri, this);

        mArtistSimple = new ArtistSimple();
    }


    public ArtistSimple getArtistSimple()
    {
        return mArtistSimple;
    }

    @Override
    public void success(Artist artist, Response response) {
        mArtistSimple.name = artist.name;
        Log.d("artistDao", "found = " + artist.name);
        for (ArtistDaoListener h1 : mListeners) {
            h1.onArtistSimpleRetrieved();
        }
    }

    @Override
    public void failure(RetrofitError error) {

    }
}
