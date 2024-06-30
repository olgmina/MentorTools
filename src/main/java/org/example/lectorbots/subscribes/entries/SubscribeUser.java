package org.example.lectorbots.subscribes.entries;

public class SubscribeUser {
    private Long id;
    private Long userId;
    private Long subscribeId;

    public Long getuserId() {
        return userId;
    }
    public Long getId() {
        return id;
    }

    public Long getsubscribeId() {
        return subscribeId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setuserId(Long USER_id) {
        this.userId = USER_id;
    }

    public void setsubscribeId(Long SUBSCRIBE_id) {
        this.subscribeId = SUBSCRIBE_id;
    }
}
