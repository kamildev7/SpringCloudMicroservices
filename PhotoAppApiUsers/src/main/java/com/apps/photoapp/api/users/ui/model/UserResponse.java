package com.apps.photoapp.api.users.ui.model;

import java.util.List;

public class UserResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private List<AlbumResponse> albums;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public List<AlbumResponse> getAlbums() {
        return albums;
    }

    public void setAlbums(final List<AlbumResponse> albums) {
        this.albums = albums;
    }
}
