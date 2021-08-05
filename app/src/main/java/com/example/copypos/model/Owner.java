package com.example.copypos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("imageName")
    @Expose
    private String imageName;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success2) {
        this.success = success2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public Owner(int id2, String name2, String phone2, String email2, String password2) {
        this.name = name2;
        this.phone = phone2;
        this.email = email2;
        this.password = password2;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String imageName2) {
        this.imageName = imageName2;
    }
}
