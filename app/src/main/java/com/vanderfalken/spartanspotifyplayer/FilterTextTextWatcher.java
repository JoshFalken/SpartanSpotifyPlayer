package com.vanderfalken.spartanspotifyplayer;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

/**
 * Created by Josh on 8-5-2016.
 */
public class FilterTextTextWatcher implements TextWatcher, Filter.FilterListener {
    private EditText mFilterText;
    private ListView mListView;
    private SpotifyListAdapter mSpotifyListAdapter;


    public FilterTextTextWatcher(EditText filterText, ListView listView, SpotifyListAdapter spotifyListAdapter)
    {
        mFilterText = filterText;
        mListView = listView;
        mSpotifyListAdapter = spotifyListAdapter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //mPlaylistAdapter.setSelected(mListView.getSelectedItemPosition());
        //EditText filterText = (EditText) findViewById(R.id.filterText);
        String filterTextValue = mFilterText.getText().toString();
        if (TextUtils.isEmpty(filterTextValue)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(filterTextValue);
        }
        mSpotifyListAdapter.getFilter().filter(filterTextValue, this);
        mSpotifyListAdapter.notifyDataSetChanged();

    }

    @Override
    public void afterTextChanged(Editable s) {
        mSpotifyListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFilterComplete(int count) {
        final int selectedPosition = mSpotifyListAdapter.getSelectedRowPosition();
        if (selectedPosition != -1) {
            mListView.clearFocus();
            mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mListView.setSelection(selectedPosition);
        }
        mSpotifyListAdapter.notifyDataSetChanged();
    }
}
