package com.vanderfalken.spartanspotifyplayer;

import android.util.Log;
import android.widget.Toast;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.SavedAlbum;
import kaaes.spotify.webapi.android.models.Track;

interface SpotifyControllerListener {
    public void onTrackChanged(Track newTrack, List<ArtistSimple> artistSimples);
    public void onPlaylistSimplesRetrieved();
    public void onSavedAlbumsRetrieved();
    public void onPlay();
    public void onPausePlay();
    public void onLoggedOut();
}


/**
 * Created by Josh on 25-4-2016.
 */
public class SpotifyController extends Listener<SpotifyControllerListener> implements TrackDaoListener, PlayerNotificationCallback, ConnectionStateCallback, PlaylistSimplesDaoListener, SavedTracksDaoListener, SavedAlbumsDaoListener {
    private String mAccessToken;
    private PlaylistSimplesDao mPlaylistSimplesDao;
    private SavedTracksDao mSavedTracksDao;
    private SavedAlbumsDao mSavedAlbumsDao;
    private TrackDao mTrackDao;

    public SpotifyController()
    {
        mPlaylistSimplesDao = new PlaylistSimplesDao();
        mPlaylistSimplesDao.addListener(this);

        mSavedTracksDao = new SavedTracksDao();
        mSavedTracksDao.addListener(this);

        mSavedAlbumsDao = new SavedAlbumsDao();
        mSavedAlbumsDao.addListener(this);


        mTrackDao = new TrackDao();
        mTrackDao.addListener(this);
    }

    public void retrieveSavedAlbums()
    {
        mSavedAlbumsDao.retrieveSavedAlbums();
    }

    public void retrievePlaylistSimples()
    {
        mPlaylistSimplesDao.retrievePlaylistSimples();
    }

    @Override
    public void onTrackRetrieved() {
        Log.d("SpotifyController", "loading complete");
        for (SpotifyControllerListener hl : mListeners)
            hl.onTrackChanged(mTrackDao.getTrack(), mTrackDao.getArtistSimples());
    }


    public ArrayList<PlaylistSimple> getPlaylistSimples()
    {
        return mPlaylistSimplesDao.getPlaylistSimples();
    }

    public ArrayList<SavedAlbum> getSavedAlbums()
    {
        return mSavedAlbumsDao.getSavedAlbums();
    }


    @Override
    public void onPlaylistSimplesRetrieved() {
        for (SpotifyControllerListener hl : mListeners)
            hl.onPlaylistSimplesRetrieved();
    }

    @Override
    public void onSavedAlbumsRetrieved() {
        for (SpotifyControllerListener hl : mListeners)
            hl.onSavedAlbumsRetrieved();
    }


    public void setAccessToken(String accessToken)
    {
        Log.d("controller", "setting access token!");
        mPlaylistSimplesDao.setAccessToken(accessToken);
        mTrackDao.setAccessToken(accessToken);
        mSavedTracksDao.setAccessToken(accessToken);
        mSavedAlbumsDao.setAccessToken(accessToken);

        mAccessToken = accessToken;
    }

    public String getAccessToken()
    {
        return mAccessToken;
    }


    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
        ArrayList<PlaylistSimple> playlistSimples = new ArrayList<PlaylistSimple>();

        setAccessToken(mAccessToken);

        retrievePlaylistSimples();
        /*mPlaylistSimplesDao.setAccessToken(mAccessToken);
        mSavedAlbumsDao.setAccessToken(mAccessToken); */
    }

    @Override
    public void onLoggedOut() {
        for (SpotifyControllerListener hl : mListeners)
            hl.onLoggedOut();
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }



    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {

        Log.d("MainActivity", "Playback event received: " + eventType.name());
        switch (eventType) {
            // Handle event type as necessary
            case PLAY:
                Log.d("onPlaybackEvent", "Play");
                for (SpotifyControllerListener hl : mListeners)
                    hl.onPlay();
                break;
            case PAUSE:
                Log.d("onPlaybackEvent", "Pause");
                for (SpotifyControllerListener hl : mListeners)
                    hl.onPausePlay();
                break;
            case TRACK_CHANGED:
                Track track = new Track();
                mTrackDao.retrieveTrack(playerState.trackUri.replace("spotify:track:", ""));
                break;
            case SKIP_NEXT:
                break;
            case SKIP_PREV:
                break;
            case SHUFFLE_ENABLED:
                break;
            case SHUFFLE_DISABLED:
                break;
            case REPEAT_ENABLED:
                break;
            case REPEAT_DISABLED:
                break;
            case BECAME_ACTIVE:
                break;
            case BECAME_INACTIVE:
                break;
            case LOST_PERMISSION:
                break;
            case AUDIO_FLUSH:
                break;
            case END_OF_CONTEXT:
                break;
            case TRACK_START:
                break;
            case TRACK_END:
                break;
            case EVENT_UNKNOWN:
                break;
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
        switch (errorType) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onSavedTracksRetrieved() {

    }

    @Override
    public void notifyListeners() {

    }
}
