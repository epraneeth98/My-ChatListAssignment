package com.example.chatlistassignment.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.chatlistassignment.utils.ListConverter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "ContactDB")
public class Contact implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    @TypeConverters(ListConverter.class)
    private List<String> number;

    public Contact(@NonNull String id, String name, List<String> number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNumber() {
        return number;
    }

    public void setNumber(List<String> number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return id.equals(contact.id) &&
                Objects.equals(name, contact.name) &&
                Objects.equals(number, contact.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, number);
    }
}
