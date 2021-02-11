package com.example.chatlistassignment.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.chatlistassignment.utils.MyTypeConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

@Entity(tableName = "UserDB")
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String name;
    @NonNull
    @TypeConverters(MyTypeConverter.class)
    private ArrayList<String> contactNumbers;
    private String profilePic;
    private long dateOfBirth;
    private long createdAt;
    private long lastUpdatedAt;

    public User( @NonNull int uid, String name, @NonNull ArrayList<String> contactNumbers, String profilePic, long dateOfBirth, long lastUpdatedAt) {
        this.uid = uid;
        this.name = name;
        this.contactNumbers = contactNumbers;
        this.profilePic = profilePic;
        this.dateOfBirth = dateOfBirth;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @Ignore
    public User(String name, @NonNull ArrayList<String> contactNumbers, String profilePic, long dateOfBirth,@NonNull long createdAt,@NonNull long lastUpdatedAt) {
        this.name = name;
        this.contactNumbers = contactNumbers;
        this.profilePic = profilePic;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(long lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @Ignore
    public User(int uid, String name, @NonNull ArrayList<String> contactNumbers, String profilePic, long dateOfBirth) {
        this.uid = uid;
        this.name = name;
        this.contactNumbers = contactNumbers;
        this.profilePic = profilePic;
        this.dateOfBirth = dateOfBirth;
    }

    @Ignore
    public User(String name, @NonNull ArrayList<String> contactNumbers, String profilePic, long dateOfBirth) {
        this.name = name;
        this.contactNumbers = contactNumbers;
        this.profilePic = profilePic;
        this.dateOfBirth = dateOfBirth;
    }

    @NonNull
    public ArrayList<String> getContactNumbers() {
        return contactNumbers;
    }

    public void setContactNumbers(@NonNull ArrayList<String> contactNumbers) {
        this.contactNumbers = contactNumbers;
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


    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uid == user.uid &&
                Objects.equals(name, user.name) &&
                contactNumbers.equals(user.contactNumbers) &&
                Objects.equals(profilePic, user.profilePic) &&
                Objects.equals(dateOfBirth, user.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, contactNumbers, profilePic, dateOfBirth);
    }


}
