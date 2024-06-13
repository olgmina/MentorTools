package org.example.lectorbots.subcribe.database;

import org.example.lectorbots.subscribes.entries.AppUser;

import java.util.ArrayList;

public interface IAppUserDAO {
    ArrayList<AppUser> getAllSubscribers();
    ArrayList<Long> getAllSubscribersIdByType(String type);
    void deleteUser(Long id);
    void updateUser(AppUser user);
}
