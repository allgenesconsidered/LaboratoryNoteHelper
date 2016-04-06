package com.mememachine.mike.laboratorynotehelper;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Notebook {
    private UUID mID;
    private String mTitle;
    private Date mDate;
    private String mColor;

    public Notebook(){
        this(UUID.randomUUID());
    } //Runs if Notebook is created.

    public Notebook(UUID id){ //Previous notebook.
        mID = id;
        mDate = new Date();
        mColor = "blue";
    }

    public UUID getID() {
        return mID;
    }

    public void setID(UUID ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public String getStringDate() {
        SimpleDateFormat format = new SimpleDateFormat("E, MM/dd hh:mm aa");
        String string =  format.format(this.getDate());
        return string;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }
}
