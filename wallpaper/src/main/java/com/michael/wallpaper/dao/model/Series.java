package com.michael.wallpaper.dao.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table SERIES.
 */
public class Series implements java.io.Serializable {

    private int type;
    /** Not-null value. */
    private String title;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Series() {
    }

    public Series(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /** Not-null value. */
    public String getTitle() {
        return title;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTitle(String title) {
        this.title = title;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
