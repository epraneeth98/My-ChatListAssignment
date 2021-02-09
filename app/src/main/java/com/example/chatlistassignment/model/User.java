package com.example.chatlistassignment.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "UserDB")
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String name;
    @NonNull
    private String contactNumber;
    private String profilePic;
    private String dateOfBirth;

    public User(String name, String contactNumber, String profilePic, String dateOfBirth) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.profilePic = profilePic;
        this.dateOfBirth = dateOfBirth;
    }

    public User(int uid, String name, String contactNumber, String profilePic, String dateOfBirth) {
        this.uid = uid;
        this.name = name;
        this.contactNumber = contactNumber;
        this.profilePic = profilePic;
        this.dateOfBirth = dateOfBirth;
    }

    public User() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uid == user.uid &&
                name.equals(user.name) &&
                Objects.equals(contactNumber, user.contactNumber) &&
                Objects.equals(profilePic, user.profilePic) &&
                Objects.equals(dateOfBirth, user.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, contactNumber, profilePic, dateOfBirth);
    }
}
