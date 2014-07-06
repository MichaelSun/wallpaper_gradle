package com.michael.wallpaper.api.baidu;

import com.jesson.android.internet.core.annotations.HttpMethod;
import com.jesson.android.internet.core.annotations.RequiredParam;
import com.jesson.android.internet.core.annotations.RestMethodUrl;
import com.michael.wallpaper.api.BelleRequestBase;

/**
 * Created by michael on 14-7-5.
 */

@RestMethodUrl("http://image.baidu.com/channel/listjson")
@HttpMethod("GET")
public class BaiduListRequest extends BelleRequestBase<BaiduListResponse> {

    @RequiredParam("pn")
    private int pageName;

    @RequiredParam("rn")
    private int pageSize;

    //分类，美女，壁纸等
    @RequiredParam("tag1")
    private String category;

    @RequiredParam("tag2")
    private String title;

    public BaiduListRequest(int pageName, int pageSize, String category, String title) {
        this.pageName = pageName;
        this.pageSize = pageSize;
        this.category = category;
        this.title = title;
    }
}
