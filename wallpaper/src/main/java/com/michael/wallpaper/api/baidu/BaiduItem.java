package com.michael.wallpaper.api.baidu;

import com.jesson.android.internet.core.json.JsonProperty;

/**
 * Created by michael on 14-7-5.
 */
public class BaiduItem {

    @JsonProperty("id")
    public String id;

    @JsonProperty("desc")
    public String desc;

    @JsonProperty("image_url")
    public String image_url;

    @JsonProperty("thumbnail_url")
    public String thumbnail_url;

    @JsonProperty("thumb_large_url")
    public String thumb_large_url;

    @JsonProperty("isAdapted")
    public int isAdapted;

    @Override
    public String toString() {
        return "BaiduItem{" +
                   "id='" + id + '\'' +
                   ", desc='" + desc + '\'' +
                   ", image_url='" + image_url + '\'' +
                   ", thumbnail_url='" + thumbnail_url + '\'' +
                   ", thumb_large_url='" + thumb_large_url + '\'' +
                   ", isAdapted=" + isAdapted +
                   '}';
    }
}
