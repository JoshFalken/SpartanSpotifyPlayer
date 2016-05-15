package com.vanderfalken.spartanspotifyplayer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {


    public CustomListAdapter(Activity context, ArrayList<String> itemName, ArrayList<Integer> imgId, ArrayList<String> id) {
        super(context, R.layout.mylist, itemName);
        // TODO Auto-generated constructor stub

        /*this.mContext = context;

        this.mItemName = itemName;
        this.mImgId =  imgId;
        this.mId = id; */
    }

    private String getTitle(int position)
    {
        return "";
    }

    public Integer getImageResource(int position)
    {
        return 0;
    }

    public String getExtraText(int position)
    {
        return "";
    }

    public View getView(int position,View view,ViewGroup parent) {
    /*    LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(getTitle(position));
        imageView.setImageResource(getImageResource(position));
        extratxt.setText("Description " + getExtraText(position));
        return rowView;
    };*/
        return null;
    }
}