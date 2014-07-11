package com.michael.wallpaper.api.baidu;

import com.jesson.android.internet.core.annotations.HttpMethod;
import com.jesson.android.internet.core.annotations.RequiredParam;
import com.jesson.android.internet.core.annotations.RestMethodUrl;
import com.michael.wallpaper.api.BelleRequestBase;

/**
 * Created by michael on 14-7-10.
 */
@RestMethodUrl("http://image.baidu.com/channel/listjson")
@HttpMethod("GET")
public class BaiduListRequest1 extends BelleRequestBase<BaiduListResponse> {

    @RequiredParam("pn")
    private int pageName;

    @RequiredParam("rn")
    private int pageSize;

    //分类，美女，壁纸等
    @RequiredParam("tag1")
    private String category;

    @RequiredParam("tag2")
    private String title;

    @RequiredParam("tag3")
    private String tag3;

    public BaiduListRequest1(int pageName, int pageSize, String tag1, String tag2, String tag3) {
        this.pageName = pageName;
        this.pageSize = pageSize;
        this.category = tag1;
        this.title = tag2;
        this.tag3 = tag3;
    }
}
