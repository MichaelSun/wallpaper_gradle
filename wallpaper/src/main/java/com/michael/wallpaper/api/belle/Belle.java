package com.michael.wallpaper.api.belle;

import com.jesson.android.internet.core.json.JsonProperty;

/**
 * Created by zhangdi on 14-3-7.
 */
public class Belle {

    @JsonProperty("id")
    public long id;

    @JsonProperty("time")
    public long time;

    @JsonProperty("type")
    public int type;

    @JsonProperty("desc")
    public String desc;

    @JsonProperty("url")
    public String url;

    @JsonProperty("rawUrl")
    public String rawUrl;

    @JsonProperty("thumb_large_width")
    public int thumb_large_width;

    @JsonProperty("thumb_large_height")
    public int thumb_large_height;

    public Belle(long id, long time, int type, String desc, String url, String rawUrl, int thumb_large_width, int thumb_large_height) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.desc = desc;
        this.url = url;
        this.rawUrl = rawUrl;
        this.thumb_large_width = thumb_large_width;
        this.thumb_large_height = thumb_large_height;
    }

    public Belle() {
    }

    @Override
    public String toString() {
        return "Belle{" +
                   "id=" + id +
                   ", time=" + time +
                   ", type=" + type +
                   ", desc='" + desc + '\'' +
                   ", url='" + url + '\'' +
                   ", rawUrl='" + rawUrl + '\'' +
                   ", thumb_large_width=" + thumb_large_width +
                   ", thumb_large_height=" + thumb_large_height +
                   '}';
    }
}
