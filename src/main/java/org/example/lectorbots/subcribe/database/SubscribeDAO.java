package org.example.lectorbots.subcribe.database;

import org.example.lectorbots.subscribes.entries.Subscribe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SubscribeDAO implements ISubscribeDAO {


    @Override
    public Subscribe[] getAllSubscribeKey() {
        ArrayList<Subscribe> keys = new ArrayList<Subscribe>();
        try {
            var connection = DataManager.createDataSource().getConnection();
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe");
                while (rs.next()) {
                    Subscribe sub = new Subscribe("new", "");
                    String key = rs.getString("key");
                    String type = rs.getString("type");
                    if (key != null) {
                        sub.setSubscribeKey(key);
                        sub.setSubscribeType(type);
                        keys.add(sub);
                    };
                }
                rs.close();
                stmt.close();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys.toArray(new Subscribe[0]);
    }

    @Override
    public ArrayList<Subscribe> getAllSubscribe() {
        ArrayList<Subscribe> subscribe = new ArrayList<>();
        try {
            var connection = DataManager.createDataSource().getConnection();

            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT DISTINCT type FROM subscribe");
                while (rs.next()) {
                    Subscribe type = new Subscribe(rs.getString("subscribe_type"), rs.getString("descriptor"));
                    subscribe.add(type);
                }
                rs.close();
                stmt.close();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subscribe;
    }

    @Override
    public void setSubscribeKey(Subscribe subscribe) {
        try {
            var connection = DataManager.createDataSource().getConnection();
            try {
                PreparedStatement st = connection.prepareStatement("INSERT INTO subscribe (key, type) VALUES (?, ?)");
                st.setString(1, subscribe.getSubscribeKey());
                st.setString(2, subscribe.getSubscribeType());
                st.executeUpdate();
                st.close();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
