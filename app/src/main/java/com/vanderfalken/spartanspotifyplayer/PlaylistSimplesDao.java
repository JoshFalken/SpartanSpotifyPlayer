package com.vanderfalken.spartanspotifyplayer;

import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.SavedAlbum;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


interface PlaylistSimplesDaoListener {
    void onPlaylistSimplesRetrieved();
}

/**
 * Created by Josh on 4-4-2016.
 */
public class PlaylistSimplesDao extends SpotifyDao<PlaylistSimple, Pager<PlaylistSimple>, PlaylistSimplesDaoListener> {
    private int mOffset;
    private ArrayList<PlaylistSimple> mPlaylistSimples;

    public  PlaylistSimplesDao() {
        mOffset = 0;
    }

    @Override
    public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
        Log.d("playlist retrieval", "number of playlists = " + playlistSimplePager.items.size());
        for (PlaylistSimple playlistSimple : playlistSimplePager.items) {
            mPlaylistSimples.add(playlistSimple);
        }
        if (playlistSimplePager.items.size() == 50)
        {
            mOffset = mOffset + 50;
            retrieveNextPlaylistSimples();
        }
        else
        {
            sortAlphabetically();
            for (PlaylistSimplesDaoListener hl : mListeners)
                hl.onPlaylistSimplesRetrieved();
        }
        PlaylistSimple playlistSimple;
    }

    private void sortAlphabetically()
    {
        Collections.sort(mPlaylistSimples, new Comparator<PlaylistSimple>() {
            @Override
            public int compare(PlaylistSimple p1, PlaylistSimple p2) {
                return p1.name.toLowerCase().compareTo(p2.name.toLowerCase());
            }
        });
    }


    @Override
    public void failure(RetrofitError error) {
        Log.d("playlist retrieval", error.getMessage());
    }

    private void retrieveNextPlaylistSimples()
    {

        Map<String,Object> playlistFilters = new HashMap<>();
        playlistFilters.put("offset", mOffset);
        playlistFilters.put("limit", 50);

        this.getSpotifyService().getMyPlaylists(playlistFilters, this);
    }


    public void retrievePlaylistSimples()
    {
        mOffset = 0;
        mPlaylistSimples = new ArrayList<PlaylistSimple>();
        retrieveNextPlaylistSimples();

        //mPlaylistSimples = playlistsSimples;
    }

    public ArrayList<PlaylistSimple> getPlaylistSimples()
    {
        return mPlaylistSimples;
    }

}
