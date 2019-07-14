package com.apps.photoapp.api.users.ui.model;

public class AlbumResponse {

    private String albumId;
    private String userId;
    private String name;
    private String description;

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(final String albumId) {
        this.albumId = albumId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
