package com.abohomol.gifky.repository;

public class GifItem {

    private final String description;
    private final String url;

    public GifItem(String description, String url) {
        this.description = description;
        this.url = url;
    }

    public String description() {
        return description;
    }

    public String uri() {
        return url;
    }
}
