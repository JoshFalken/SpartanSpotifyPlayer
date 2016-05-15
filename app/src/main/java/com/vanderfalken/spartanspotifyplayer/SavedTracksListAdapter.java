package com.vanderfalken.spartanspotifyplayer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.SavedAlbum;
import kaaes.spotify.webapi.android.models.SavedTrack;

/**
 * Created by Josh on 11-4-2016.
 */
public class SavedTracksListAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<SavedTrack> mSavedTracksArrayList;

    private Activity mContext;
    private ArrayList<SavedTrack> mOrig;

    public SavedTracksListAdapter(Activity context, ArrayList<SavedTrack> savedTrackArrayList) {
        super(context, R.layout.mylist);

        mSavedTracksArrayList = savedTrackArrayList;
        mContext = context;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<SavedTrack> results = new ArrayList<SavedTrack>();
                if (mOrig == null)
                    mOrig = mSavedTracksArrayList;
                if (constraint != null) {
                    if (mOrig != null && mOrig.size() > 0) {
                        for (final SavedTrack savedTrack : mOrig) {
                            if (savedTrack.track.name.toLowerCase().contains(constraint.toString()))
                                results.add(savedTrack);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                mSavedTracksArrayList = (ArrayList<SavedTrack>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSavedTracksArrayList.size();
    }

    @Override
    public String getItem(int position) {
        return mSavedTracksArrayList.get(position).track.name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        //TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(mSavedTracksArrayList.get(position).track.name);
        //imageView.setImageResource(R.drawable.playlist);
        //extratxt.setText(mPlaylistSimpleArrayList.get(position).tracks.total);
        //extratxt.setText("");
        return rowView;
    };
}
