package com.michael.wallpaper.api.baidu;

import android.text.TextUtils;
import com.jesson.android.internet.core.ResponseBase;
import com.jesson.android.internet.core.json.JsonProperty;
import com.michael.wallpaper.AppConfig;
import com.michael.wallpaper.api.belle.Belle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 14-7-5.
 */
public class BaiduListResponse extends ResponseBase {

    @JsonProperty("col")
    public String category;

    @JsonProperty("tag")
    public String title;

    @JsonProperty("totalNum")
    public int totalNum;

    @JsonProperty("startIndex")
    public int start_index;

    @JsonProperty("returnNumber")
    public int return_number;

    @JsonProperty("imgs")
    public List<BaiduItem> baiduItems;

    @Override
    public String toString() {
        return "BaiduListResponse{" +
                   "category='" + category + '\'' +
                   ", title='" + title + '\'' +
                   ", totalNum=" + totalNum +
                   ", start_index=" + start_index +
                   ", return_number=" + return_number +
                   ", baiduItems=" + baiduItems +
                   '}';
    }

    public static List<Belle> makeBellesFromBaiduItem(List<BaiduItem> datas) {
        List<Belle> ret = new ArrayList<Belle>();
        if (datas == null) return ret;
        for (BaiduItem item : datas) {
            if (!TextUtils.isEmpty(item.image_url)
                && !TextUtils.isEmpty(item.thumb_large_url)) {
                Belle belle = new Belle();
                belle.id = -1;
                belle.time = System.currentTimeMillis();
                belle.desc = item.desc;
                belle.url = item.thumb_large_url;
                belle.rawUrl = item.image_url;
                belle.thumb_large_width = item.thumb_large_width;
                belle.thumb_large_height = item.thumb_large_height;

                if ((item.image_height * item.image_width * 4) / 1024 < AppConfig.MAX_IMAGE_MEMORY) {
                    belle.url = item.image_url;
                }

                if ((item.thumb_large_width * item.thumb_large_height * 4) / 1024 < AppConfig.MAX_IMAGE_MEMORY) {
                    ret.add(belle);
                }
            }
        }

        return ret;
    }

}
