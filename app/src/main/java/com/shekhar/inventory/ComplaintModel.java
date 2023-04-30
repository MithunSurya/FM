package com.shekhar.inventory;

import com.google.firebase.Timestamp;

public class ComplaintModel {

    private String name, description, image, section;
    private Timestamp timestamp;

    public ComplaintModel() {}

    public ComplaintModel(String name, String description, String image, String section, Timestamp timestamp) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.section = section;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
