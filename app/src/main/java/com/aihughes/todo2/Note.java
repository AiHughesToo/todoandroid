package com.aihughes.todo2;



import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

/**
 * Created by alexhughes on 10/23/16.
 */

public class Note implements Comparable<Note>{
    @SerializedName("title")
    private String title;
    @SerializedName("text")
    private String text;
    @SerializedName("date")
    private Date date;
    @SerializedName("category")
    private String category;
    @SerializedName("image_bg_src")
    private String image_bg_src;
    @SerializedName("dueDate")
    private String dueDate;
    @SerializedName("catID")
    private String catID;


    // this is a constructor for the note
    public Note (String title, String text, Date date, String category, String image_bg, String dueDate, String catID){
        this.title = title;
        this.text = text;
        this.date = date;
        this.category = category;
        this.image_bg_src = image_bg;
        this.dueDate = dueDate;
        this.catID = catID;
    }
    //these are short methods for the notes
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory(){ return category;}

    public void setCategory(String category) {this.category = category;}

    public String getImage_bg_src() {return image_bg_src;}

    public void setImage_bg_src(String image_bg_src) {this.image_bg_src = image_bg_src;}

    public void setDueDate(String dueDate) {this.dueDate = dueDate;}

    public String getDueDate(){return dueDate;}

    public String getCatID() {return catID;}

    public void setCatID(String catID) {this.catID = catID;}


    @Override
    public int compareTo(Note another) {

         //return another.getDate().compareTo(getDate());
         return getDate().compareTo(another.getDate());
    }

}
