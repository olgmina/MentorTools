package org.example.lectorbots.subcribe.database;

import org.example.lectorbots.subscribes.entries.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SubscribeDAO implements IDAO<Subscribe> {
     Logger logger= LoggerFactory.getLogger(SubscribeDAO.class);


    public Subscribe[] getAllSubscribeKey() {
        ArrayList<Subscribe> keys = new ArrayList<Subscribe>();

            try {
                Statement stmt = DataManager.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe");
                while (rs.next()) {
                    Subscribe sub = new Subscribe("new", "");
                    String key = rs.getString("key");
                    String type = rs.getString("type");
                    if (key != null) {

                        sub.setSubscribeType(type);
                        keys.add(sub);
                    };
                }
                rs.close();
                stmt.close();

        } catch (Exception e) {
            logger.error("БД Подписки: ошибка при создании" );
            e.printStackTrace();
        }
        return keys.toArray(new Subscribe[0]);
    }


    @Override
    public Collection getAll() {
        ArrayList<Subscribe> subscribe = new ArrayList<>();
        try {
            var connection = DataManager.getConnection();

            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Subscribe");
                while (rs.next()) {
                    Subscribe type = new Subscribe(rs.getString("subscribe_type"), rs.getString("descriptor"));
                    subscribe.add(type);
                }
                rs.close();
                stmt.close();
            } finally {
               // connection.close();
            }
        } catch (Exception e) {
            logger.error("БД Подписки: ошибка при создании" );
            e.printStackTrace();
        }
        return subscribe;
    }

    @Override
    public Subscribe get(int id) {
        Subscribe sub = new Subscribe("type", "subscribe");
        try {
            var connection = DataManager.getConnection();

            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Subscribe WHERE id = " + id);
                while (rs.next()) {
                    sub.setSubscribeId(rs.getInt("id"));

                    sub.setSubscribeType(rs.getString("type"));
                }
                rs.close();
                stmt.close();
            } finally {
               // connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sub;
    }

    @Override
    public void delete(int id) {
        try {
            var connection = DataManager.getConnection();

            try {
                PreparedStatement st = connection.prepareStatement("DELETE FROM Subscribe WHERE id = " + id);
                st.executeUpdate();
                st = connection.prepareStatement("DELETE FROM user_subscribe WHERE Subscribe_id = " + id);
                st.executeUpdate();
                st.close();
            } finally {
              //  connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Subscribe subscribe) {
        try {
            var connection = DataManager.getConnection();
            try {
                PreparedStatement st = connection.prepareStatement("INSERT INTO Subscribe (type, descr) VALUES (?, ?)");
                st.setString(1, subscribe.getSubscribeType());
                st.setString(2, subscribe.getDescription());
                st.executeUpdate();
                st.close();
            } finally {
               // connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Subscribe subscribe) {
        try {
            var connection = DataManager.getConnection();
            try {
                PreparedStatement st = connection.prepareStatement("UPDATE subscribe SET type = ?, descr = ? where id = ?");
                st.setString(1, subscribe.getSubscribeType());
                st.setString(2, subscribe.getDescription());
                st.setInt(3, subscribe.getSubscribeId());
                st.executeUpdate();
                st.close();
            } finally {
              //  connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
