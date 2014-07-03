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

    @JsonProperty("url")
    public String url;

    @JsonProperty("rawUrl")
    public String rawUrl;

    @Override
    public String toString() {
        return "Belle{" +
                   "id=" + id +
                   ", time=" + time +
                   ", type=" + type +
                   ", url='" + url + '\'' +
                   ", rawUrl='" + rawUrl + '\'' +
                   '}';
    }

    public Belle() {

    }

    public Belle(long id, long time, int type, String url, String rawUrl) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.url = url;
        this.rawUrl = rawUrl;
    }
}
