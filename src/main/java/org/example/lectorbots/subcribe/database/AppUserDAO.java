package org.example.lectorbots.subcribe.database;

import org.example.lectorbots.subscribes.entries.AppUser;
import org.example.lectorbots.subscribes.entries.Subscribe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AppUserDAO implements IAppUserDAO {


    @Override
    public ArrayList<AppUser> getAllSubscribers() {

        ArrayList<AppUser> users = new ArrayList<>();
        try {

            var connection = DataManager.getConnection();
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM user_table");
                while (rs.next()) {
                    AppUser user = new AppUser();
                    user.setId(rs.getLong("id"));
                    user.setTelegramUserId(rs.getLong("telegram_user_id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setUsername(rs.getString("user_name"));
                    users.add(user);
                }
                rs.close();
                for (AppUser user: users) {
                    rs = stmt.executeQuery("SELECT * FROM subscribe WHERE user_id = " + user.getTelegramUserId());
                    ArrayList<Subscribe> subs = new ArrayList<>();
                    while (rs.next()) {
                        Subscribe subscribe = new Subscribe("new", "");
                        subscribe.setSubscribeType(rs.getString("type"));
                        subs.add(subscribe);
                    }
                    user.setSubscribe(subs.toArray(new Subscribe[0]));
                    rs.close();
                }
                stmt.close();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public ArrayList<Long> getAllSubscribersIdByType(String type) {
        ArrayList<Long> usersId = new ArrayList<>();
        try {
            var connection = DataManager.getConnection();
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM user_table LEFT JOIN subscribe ON subscribe.user_id = user_table.telegram_user_id WHERE type = '" + type + "'");
                while (rs.next()) {
                    usersId.add(rs.getLong("telegram_user_id"));
                }
                rs.close();
                stmt.close();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersId;
    }

    @Override
    public void deleteUser(Long id) {
        try {
            var connection = DataManager.getConnection();
            try {
                PreparedStatement st = connection.prepareStatement("DELETE FROM user_table WHERE telegram_user_id = ?");
                st.setLong(1, id);
                st.executeUpdate();
                st = connection.prepareStatement("DELETE FROM user_subscribe WHERE user_id = ?");
                st.setLong(1, id);
                st.executeUpdate();
                st = connection.prepareStatement("DELETE FROM subscribe WHERE user_id = ?");
                st.setLong(1, id);
                st.executeUpdate();
                st.close();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(AppUser user) {
        try {
            var connection = DataManager.getConnection();
            try {
                PreparedStatement st = connection.prepareStatement("UPDATE user_table SET first_name = '" + user.getFirstName() + "', last_name = '" + user.getLastName() + "' WHERE telegram_user_id = " + user.getTelegramUserId());
                st.executeUpdate();
                st = connection.prepareStatement("DELETE FROM subscribe WHERE user_id = " + user.getTelegramUserId());
                st.executeUpdate();
                st = connection.prepareStatement("DELETE FROM user_subscribe WHERE user_id = " + user.getTelegramUserId());
                st.executeUpdate();
                if (user.getSubscribe() != null) {
                    for (Subscribe sub: user.getSubscribe()) {
                        st = connection.prepareStatement("INSERT INTO subscribe (type, user_id) VALUES (?, ?)");
                        st.setString(1, sub.getSubscribeType());
                        st.setLong(2, user.getTelegramUserId());
                        st.executeUpdate();
                    }
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe WHERE user_id = " + user.getTelegramUserId());
                    ArrayList<Integer> idSubs = new ArrayList<>();
                    while (rs.next()) {
                        idSubs.add(rs.getInt("id"));
                    }
                    stmt.close();
                    for (int id: idSubs) {
                        st = connection.prepareStatement("INSERT INTO user_subscribe (subscribe_id, user_id) VALUES (?, ?)");
                        st.setInt(1,  id);
                        st.setLong(2, user.getTelegramUserId());
                        st.executeUpdate();
                    }
                }
                st.close();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
