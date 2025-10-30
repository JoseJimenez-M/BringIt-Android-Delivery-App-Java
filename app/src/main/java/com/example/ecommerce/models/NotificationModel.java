package com.example.ecommerce.models;

public class NotificationModel {
    private int id;
    private String founding;
    private String last4;

    public NotificationModel(int id, String founding, String last4) {
        this.id = id;
        this.founding = founding;
        this.last4 = last4;
    }

    public int getId() { return id; }
    public String getFounding() { return founding; }
    public String getLast4() { return last4; }
}



