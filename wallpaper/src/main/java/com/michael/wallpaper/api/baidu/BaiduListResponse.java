package com.michael.wallpaper.api.baidu;

import android.text.TextUtils;
import com.jesson.android.internet.core.ResponseBase;
import com.jesson.android.internet.core.json.JsonProperty;
import com.michael.wallpaper.api.belle.Belle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 14-7-5.
 */
public class BaiduListResponse extends ResponseBase {

    @JsonProperty("tag1")
    public String category;

    @JsonProperty("tag2")
    public String title;

    @JsonProperty("totalNum")
    public int totalNum;

    @JsonProperty("start_index")
    public int start_index;

    @JsonProperty("return_number")
    public int return_number;

    @JsonProperty("data")
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
                ret.add(belle);
            }
        }

        return ret;
    }

}
