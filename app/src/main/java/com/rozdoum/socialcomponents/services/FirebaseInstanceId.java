package com.rozdoum.socialcomponents.services;

public class FirebaseInstanceId {
    private static FirebaseInstanceId instance;

    public static FirebaseInstanceId getInstance() {
        if (instance == null) {
            instance = new FirebaseInstanceId();
        }
        return instance;
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    //            getInstance
}
