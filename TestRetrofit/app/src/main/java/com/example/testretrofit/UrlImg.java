package com.example.testretrofit;

public class UrlImg {
    private String url;
    private String id;

    public UrlImg (String url, String id){
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }
}
