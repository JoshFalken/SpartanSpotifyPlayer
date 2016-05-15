package com.vanderfalken.spartanspotifyplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.SavedAlbum;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Josh on 29-4-2016.
 */
interface SpotifyDaoListener {
    void onLoadingComplete();
}

public class SpotifyDao<DataModel, CallbackModel, ListenerModel> extends Listener<ListenerModel> implements Callback<CallbackModel> {
    private String mAccessToken;
    private int mOffset;

    public SpotifyDao() {
        mOffset = 0;
    }

    public void setAccessToken(String accessToken)
    {
        mAccessToken = accessToken;
    }

    public String getAccessToken()
    {
        return mAccessToken;
    }

    protected SpotifyService getSpotifyService()
    {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(mAccessToken);
        SpotifyService spotifyService = api.getService();

        return spotifyService;
    }

    public void retrieve(DataModel model)
    {
        Map<String,Object> filters = new HashMap<>();
        filters.put("offset", mOffset);
        filters.put("limit", 50);
        //spotify.getMySavedAlbums(savedAlbumsFilters, this);

    }

    @Override
    public void success(CallbackModel callbackModel, Response response) {

    }

    @Override
    public void failure(RetrofitError error) {

    }

    @Override
    public void notifyListeners() {

    }


}
