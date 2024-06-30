package org.example.lectorbots.subscribes.entries;

import java.util.UUID;

public class Subscribe {
    private int subscribe_id;
    private String subscribe_type;
    private String descriptor;



    public Subscribe(String subscribe_type, String descriptor) {
        this.subscribe_type = subscribe_type;
        this.descriptor = descriptor;
        this.subscribe_id = UUID.randomUUID().variant();
    }


    public int getSubscribeId() {
        return subscribe_id;
    }

    public void setSubscribeId(int subscribe_id) {
        this.subscribe_id = subscribe_id;
    }

    public void setSubscribeType(String subscribe_type) {
        this.subscribe_type = subscribe_type;
    }

    public String getSubscribeType() {
        return subscribe_type;
    }

    public int getSubscribe_id() {
        return subscribe_id;
    }

    public String getSubscribe_type() {
        return subscribe_type;
    }

    public String getDescription() {
        return descriptor;
    }


}
