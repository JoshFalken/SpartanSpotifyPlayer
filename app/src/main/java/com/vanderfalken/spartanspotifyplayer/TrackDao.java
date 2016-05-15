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

/**
 * Created by Josh on 16-4-2016.
 */

interface TrackDaoListener {
    public void onTrackRetrieved();
}

public class TrackDao extends  SpotifyDao<Track, Track, TrackDaoListener>  implements ArtistsDaoListener {
    private int mOffset;
    private Track mTrack;
    private ArtistsDao mArtistsDao;
    private Artists mArtists;

    public  TrackDao() {
        mOffset = 0;
        mArtistsDao = new ArtistsDao();
    }

    @Override
    public void setAccessToken(String accessToken)
    {
        super.setAccessToken(accessToken);
        mArtistsDao.setAccessToken(accessToken);
    }

    public void retrieveTrack(String uri)
    {
        this.getSpotifyService().getTrack(uri, this);
    }

    public Track getTrack()
    {
        return mTrack;
    }

    public ArrayList<ArtistSimple> getArtistSimples()
    {
        return mArtistsDao.getArtistSimples();
    }



    @Override
    public void success(Track track, Response response) {
        mTrack = track;
        mArtistsDao = new ArtistsDao();
        Log.d("TrackDao", "Going to retrieve Artists");
        mArtistsDao.retrieveArtists(track.artists);
        mArtistsDao.addListener(this);
    }

    @Override
    public void failure(RetrofitError error) {

    }

    @Override
    public void onArtistsRetrieved() {
        Log.d("TrackDao.java", "onartistLoadingComplete");
        for (TrackDaoListener h1 : mListeners) {
            h1.onTrackRetrieved();
        }
    }
}
