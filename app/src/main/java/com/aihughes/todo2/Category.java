package com.aihughes.todo2;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Category {
    @SerializedName("name")
    private String name;

    @SerializedName("notes")
    public ArrayList<Note> notes;

    public Category(String name, ArrayList<Note> notes) {
        this.name = name;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}