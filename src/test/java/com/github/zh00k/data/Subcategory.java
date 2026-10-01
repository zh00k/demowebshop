package com.github.zh00k.data;

public enum Subcategory {
    DESKTOPS("/desktops", "Desktops");

    private final String path;
    private final String title;

    Subcategory(String path, String title) {
        this.path = path;
        this.title = title;
    }

    public String path() {
        return path;
    }

    public String title() {
        return title;
    }
}
