package org.example.lectorbots.subcribe.database;

import java.util.Collection;

public interface IDAO <T> {
    Collection<T> getAll();
    T get(int id);
    void delete(int id);
    void create(T subscribe);
    void update(T subscribe);
}
