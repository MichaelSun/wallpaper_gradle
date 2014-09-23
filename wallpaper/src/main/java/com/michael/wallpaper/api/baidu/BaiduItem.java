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

    @JsonProperty("imageUrl")
    public String image_url;

    @JsonProperty("thumbnailUrl")
    public String thumbnail_url;

    @JsonProperty("thumbLargeUrl")
    public String thumb_large_url;

    @JsonProperty("isAdapted")
    public int isAdapted;

    @JsonProperty("imageWidth")
    public int image_width;

    @JsonProperty("imageHeight")
    public int image_height;

    @JsonProperty("thumbLargeWidth")
    public int thumb_large_width;

    @JsonProperty("thumbLargeHeight")
    public int thumb_large_height;

    @Override
    public String toString() {
        return "BaiduItem{" +
                   "id='" + id + '\'' +
                   ", desc='" + desc + '\'' +
                   ", image_url='" + image_url + '\'' +
                   ", thumbnail_url='" + thumbnail_url + '\'' +
                   ", thumb_large_url='" + thumb_large_url + '\'' +
                   ", isAdapted=" + isAdapted +
                   ", image_width=" + image_width +
                   ", image_height=" + image_height +
                   ", thumb_large_width=" + thumb_large_width +
                   ", thumb_large_height=" + thumb_large_height +
                   '}';
    }
}
