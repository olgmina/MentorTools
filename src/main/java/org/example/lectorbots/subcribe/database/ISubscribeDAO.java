package org.example.lectorbots.subcribe.database;

import org.example.lectorbots.subscribes.entries.Subscribe;

import java.util.ArrayList;

public interface ISubscribeDAO {
    Subscribe[] getAllSubscribeKey();
    ArrayList<Subscribe> getAllSubscribe();
    void setSubscribeKey(Subscribe subKey);
}
