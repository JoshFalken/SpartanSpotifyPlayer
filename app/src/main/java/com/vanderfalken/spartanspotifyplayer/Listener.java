package com.vanderfalken.spartanspotifyplayer;

import java.util.ArrayList;
import java.util.List;


abstract public class Listener<T> {
    protected List<T> mListeners = new ArrayList<T>();

    public void addListener(T t)
    {
        mListeners.add(t);
    }

    abstract public void notifyListeners();
}
