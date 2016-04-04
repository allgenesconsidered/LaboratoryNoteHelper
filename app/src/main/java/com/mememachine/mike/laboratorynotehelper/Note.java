package com.mememachine.mike.laboratorynotehelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/*
 * Note.class for generating and accessing notes, as well as
 * note metadata (Date, is the plate finished).
 */
public class Note {

    private UUID mID;
    private String mTitle;
    private Date mDate;
    private String mCellType;
    private String mBody;

    public Note(){
        this(UUID.randomUUID());
    }

    public Note(UUID id){
        mID = id;
        mDate = new Date();
    }

    public UUID getID() {
        return mID;
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

    public void setDate(Date date) { mDate = date;}

    public String getCellType() {
        return mCellType;
    }

    public void setCellType(String type) {
        mCellType = type;
    }

    public String getBody() {return mBody;}

    public void setBody(String body) {mBody = body;}

    public String getPhotoFilename() {
        return "IMG_" + getID().toString() + ".jpg";
    }
}
