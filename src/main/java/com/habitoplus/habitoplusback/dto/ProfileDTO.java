package com.habitoplus.habitoplusback.dto;

import com.habitoplus.habitoplusback.model.Photo;

public class ProfileDTO {
    private Integer idProfile;

    private Photo photo;

    private String username;

    private String name;

    private String lastName;

    private String numberPhone;

    private Boolean status;

    private String lastConnection;

    public ProfileDTO() {
    }
    
    public ProfileDTO(Integer idProfile, String lastConnection, String lastName, String name, String numberPhone, Photo photo, Boolean status, String username) {
        this.idProfile = idProfile;
        this.lastConnection = lastConnection;
        this.lastName = lastName;
        this.name = name;
        this.numberPhone = numberPhone;
        this.photo = photo;
        this.status = status;
        this.username = username;
    }

    public Integer getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(Integer idProfile) {
        this.idProfile = idProfile;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(String lastConnection) {
        this.lastConnection = lastConnection;
    }



}
