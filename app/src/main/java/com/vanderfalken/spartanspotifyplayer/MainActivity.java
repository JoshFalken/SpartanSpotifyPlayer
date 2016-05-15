// TutorialApp
// Created by Spotify on 25/02/14.
// Copyright (c) 2014 Spotify. All rights reserved.
package com.vanderfalken.spartanspotifyplayer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;

import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.SavedAlbum;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;

public class MainActivity extends Activity implements SpotifyControllerListener {

    private static final String CLIENT_ID = "9ba1951ffd33416fa321699c3bc77585";
    private static final String REDIRECT_URI = "spartanplayerlogin://callback";

    // Request code that will be passed together with authentication result to the onAuthenticationResult callback
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;
    private ListView mListView;
    private PlaylistListAdapterFilter mPlaylistListAdapterFilter;
    private PlaylistListAdapter mPlaylistAdapter;

    private SavedAlbumsListAdapter mSavedAlbumsListAdapter;
    private SavedAlbumsListAdapterFilter mSavedAlbumsListAdapterFilter;

    private SpotifyListAdapter mCurrentSpotifyListAdapter;
    private SpotifyController mSpotifyController;
    private PlayerStatus mPlayerStatus;
    private PlayerModus mPlayerModus;

    private enum PlayerStatus {
        Idle, Playing, Pausing
    }

    private enum PlayerModus {
        Linear, Shuffling
    }

    private enum UIButtons {
        All, Shuffle, PlayStop, Backward, Forward, PowerOff
    }

    private enum ActiveView {
        UserPlaylists, UserSavedAlbums
    }


    ListView list;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    public MainActivity() {
        mSpotifyController = new SpotifyController();
        mSpotifyController.addListener(this);
        mPlayerStatus = PlayerStatus.Idle;
        mPlayerModus = PlayerModus.Linear;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayMainScreen();
    }

    protected void displayMainScreen()
    {
        setContentView(R.layout.activity_main);
        Resources res = this.getResources();
        String clientId = res.getString(R.string.client_id);
        String redirectUri = res.getString(R.string.redirect_uri);


        try {
            if (clientId.isEmpty() || clientId.equals(""))
                clientId = res.getString(R.string.client_id_local);

            if (redirectUri.isEmpty() || redirectUri.equals(""))
                redirectUri = res.getString(R.string.redirect_uri_local);
        }
        catch(Exception exception)
        {
            return;
        }

        // Start the authentication procedure
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN, redirectUri);
        builder.setScopes(new String[]{"user-read-private", "streaming", "playlist-read-private", "playlist-read-collaborative", "user-library-read"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        // set some basic properties
        mListView = (ListView) findViewById(R.id.list);
        FormatFilterText();
    }

    ///////////////////////////////////////////////////////////
    // Interface programmatically
    ///////////////////////////////////////////////////////////
    private void enableFilterText() {
        EditText filterText = (EditText) findViewById(R.id.filterText);
        FilterTextTextWatcher filterTextTextWatcher = new FilterTextTextWatcher(filterText, mListView, mCurrentSpotifyListAdapter);
        filterText.addTextChangedListener(filterTextTextWatcher);
    }

    private void FormatFilterText() {
        final EditText filterText = (EditText) findViewById(R.id.filterText);
        filterText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView magnifyingGlass = (TextView) findViewById(R.id.magnifyingclass);
                TextView remove = (TextView) findViewById(R.id.removeFilterText);

                if (hasFocus) {
                    magnifyingGlass.setVisibility(View.INVISIBLE);
                    remove.setVisibility(View.VISIBLE);
                    filterText.setCursorVisible(true);
                } else {
                    filterText.setCursorVisible(false);
                    if (filterText.getText().toString().compareTo("") == 0) {
                        magnifyingGlass.setVisibility(View.VISIBLE);
                        remove.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        clearFocusFilterText();
    }

    public void onRemoveFilterTextClicked(View view) {
        EditText filterText = (EditText) findViewById(R.id.filterText);
        filterText.setText("");
        TextView magnifyingGlass = (TextView) findViewById(R.id.magnifyingclass);
        TextView removeFilterText = (TextView) findViewById(R.id.removeFilterText);
        magnifyingGlass.setVisibility(View.VISIBLE);
        removeFilterText.setVisibility(View.INVISIBLE);
        clearFocusFilterText();
    }

    private void clearFocusFilterText() {
        EditText filterText = (EditText) findViewById(R.id.filterText);
        filterText.clearFocus();
    }

    ///////////////////////////////////////////////////////////
    // Playerpanel buttons
    ///////////////////////////////////////////////////////////
    //region PlayerPanelButtons
    public void onPlayStopButtonClicked(View v) {

        switch (mPlayerStatus) {
            case Pausing:
                mPlayer.resume();
                mPlayerStatus = PlayerStatus.Playing;
                updateUI(UIButtons.PlayStop);
                break;
            case Playing:
                mPlayer.pause();
                mPlayerStatus = PlayerStatus.Pausing;
                updateUI(UIButtons.PlayStop);
                break;
            default:
                break;
        }
        clearFocusFilterText();
    }

    private void updateUI(UIButtons uiButtonsToUpdate) {
        if (uiButtonsToUpdate == UIButtons.All || uiButtonsToUpdate == UIButtons.PlayStop) {
            ButtonPlus playButton;
            playButton = (ButtonPlus) findViewById(R.id.playStopButton);

            switch (mPlayerStatus) {
                case Idle:
                    playButton.setText(R.string.fa_play);
                    break;
                case Playing:
                    playButton.setText(R.string.fa_stop);
                    break;
                case Pausing:
                    playButton.setText(R.string.fa_play);
                    break;
            }
        }

        if (uiButtonsToUpdate == UIButtons.All || uiButtonsToUpdate == UIButtons.Shuffle) {
            Button shuffleButton = (Button) findViewById(R.id.shuffleButton);

            if (mPlayerModus == PlayerModus.Linear) {
                shuffleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apptheme_color));
            } else {
                shuffleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.defaulttext_color));
            }
        }
    }


    public void onShuffleButtonClicked(View v) {
        Button shuffleButton = (Button) findViewById(R.id.shuffleButton);

        if (mPlayerModus == PlayerModus.Linear) {
            mPlayerModus = PlayerModus.Shuffling;
            mPlayer.setShuffle(true);
            shuffleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apptheme_color));
        } else {
            mPlayerModus = PlayerModus.Linear;
            mPlayer.setShuffle(false);
            shuffleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.defaulttext_color));
        }
        clearFocusFilterText();
    }


    public void onBackwardButtonClicked(View v) {
        mPlayer.skipToPrevious();
        clearFocusFilterText();
    }

    public void onForwardButtonClicked(View v) {
        mPlayer.skipToNext();
        clearFocusFilterText();
    }

    public void onPowerOffButtonClicked(View v) {
        mPlayer.logout();
        Toast.makeText(this, "Hello again! (power off button)", Toast.LENGTH_LONG).show();
    }
    //endregion

    ///////////////////////////////////////////////////////////
    // Spotify callbacks
    ///////////////////////////////////////////////////////////
    //region SpotifyCallbacks
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String accesToken;
        super.onActivityResult(requestCode, resultCode, intent);
        Log.e("MainActivity", "in onActivityResult");

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                accesToken = response.getAccessToken();
                mSpotifyController.setAccessToken(accesToken);

                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {

                        mPlayer.addConnectionStateCallback(mSpotifyController);
                        mPlayer.addPlayerNotificationCallback(mSpotifyController);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void onLoggedOut()
    {
        displayMainScreen();
    }

    @Override
    public void onPlay() {
    }

    @Override
    public void onPausePlay() {
    }
    //endregion


    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        mPlayer.pause();
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onTrackChanged(Track newTrack, List<ArtistSimple> artistSimples) {
        Log.d("MainActivity.java", "new track notification");
        String text = "";
        Toast.makeText(getApplicationContext(), newTrack.artists.get(0).name + " - " + newTrack.name, Toast.LENGTH_LONG).show();
    }


    ///////////////////////////////////////////////////////////
    // ContentFiltering panel - buttons
    ///////////////////////////////////////////////////////////
    public void onSavedAlbumsButtonClicked(View v) {
        mSpotifyController.retrieveSavedAlbums();
    }

    public void onPlaylistsClicked(View v) {
        mSpotifyController.retrievePlaylistSimples();
    }

    ///////////////////////////////////////////////////////////
    // ContentFiltering panel - handling
    ///////////////////////////////////////////////////////////
    //region contentfiltering panel
    @Override
    public void onPlaylistSimplesRetrieved() {
        mPlaylistListAdapterFilter = new PlaylistListAdapterFilter(mSpotifyController.getPlaylistSimples());
        mPlaylistAdapter = new PlaylistListAdapter(this, mSpotifyController.getPlaylistSimples(), mPlaylistListAdapterFilter, R.id.list);
        mCurrentSpotifyListAdapter = mPlaylistAdapter;
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(mCurrentSpotifyListAdapter);
        list.setChoiceMode(ListView.CHOICE_MODE_NONE);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mCurrentSpotifyListAdapter.setSelectedPosition(position);
                mCurrentSpotifyListAdapter.notifyDataSetChanged();
                mPlayer.pause();
                mPlayer.play(mCurrentSpotifyListAdapter.getUri());
                mPlayerStatus = PlayerStatus.Playing;

                updateUI(UIButtons.PlayStop);
                clearFocusFilterText();
            }
        });
        enableFilterText();
    }

    @Override
    public void onSavedAlbumsRetrieved() {
        mSavedAlbumsListAdapterFilter = new SavedAlbumsListAdapterFilter(mSpotifyController.getSavedAlbums());
        mSavedAlbumsListAdapter = new SavedAlbumsListAdapter(this, mSpotifyController.getSavedAlbums(), mSavedAlbumsListAdapterFilter, R.id.list);
        mCurrentSpotifyListAdapter = mSavedAlbumsListAdapter;
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(mCurrentSpotifyListAdapter);
        list.setChoiceMode(ListView.CHOICE_MODE_NONE);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mCurrentSpotifyListAdapter.setSelectedPosition(position);
                mCurrentSpotifyListAdapter.notifyDataSetChanged();
                SavedAlbum savedAlbum = mSavedAlbumsListAdapter.getSavedAlbum();
                if (savedAlbum != null) {
                    mPlayer.clearQueue();
                    mPlayer.skipToNext();
                    for (TrackSimple trackSimple : savedAlbum.album.tracks.items) {
                        mPlayer.queue(trackSimple.uri);
                    }
                    mPlayerStatus = PlayerStatus.Playing;

                }
                clearFocusFilterText();
                Log.d("saved album playing", savedAlbum.album.uri);
                updateUI(UIButtons.PlayStop);
            }
        });
        enableFilterText();
    }
}
