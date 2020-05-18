package com.example.testretrofit;

public class Golos {
    private String image_id;
    private String sub_id;
    private int value;

    public Golos(String image_id, String sub_id, int value){
        this.image_id=image_id;
        this.sub_id=sub_id;
        this.value=value;
    }

    public int getValue() {
        return value;
    }

    public String getImage_id() {
        return image_id;
    }

    public String getSub_id() {
        return sub_id;
    }
}
