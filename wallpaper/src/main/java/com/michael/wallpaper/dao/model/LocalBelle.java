package com.michael.wallpaper.dao.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table LOCAL_BELLE.
 */
public class LocalBelle {

    private long id;
    private long time;
    private int type;
    private String desc;
    /** Not-null value. */
    private String url;
    private String rawUrl;
    private Integer width;
    private Integer height;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public LocalBelle() {
    }

    public LocalBelle(long id, long time, int type, String desc, String url, String rawUrl, Integer width, Integer height) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.desc = desc;
        this.url = url;
        this.rawUrl = rawUrl;
        this.width = width;
        this.height = height;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /** Not-null value. */
    public String getUrl() {
        return url;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUrl(String url) {
        this.url = url;
    }

    public String getRawUrl() {
        return rawUrl;
    }

    public void setRawUrl(String rawUrl) {
        this.rawUrl = rawUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
