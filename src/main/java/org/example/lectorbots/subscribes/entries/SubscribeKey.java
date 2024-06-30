package org.example.lectorbots.subscribes.entries;

public class SubscribeKey {
    private int subscribeId;
    private String key;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSubscribeId(int id) {
        this.subscribeId = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSubscribeId() {
        return subscribeId;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key + " : " + type;
    }
}
