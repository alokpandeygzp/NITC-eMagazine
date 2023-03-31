package com.example.nitcemag;

public class ModelFollow {
    String email,key;
    public  ModelFollow()
    {

    }

    public ModelFollow(String email,String key) {
        this.email = email;
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
