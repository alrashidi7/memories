package com.alrshididev.memories;

public class Memories {
    private String title;
    private String description;
    private String uriImage;

    public Memories() {
    }

    public Memories(String title, String description, String uriImage) {
        this.title = title;
        this.description = description;
        this.uriImage = uriImage;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUriImage() {
        return uriImage;
    }
}
