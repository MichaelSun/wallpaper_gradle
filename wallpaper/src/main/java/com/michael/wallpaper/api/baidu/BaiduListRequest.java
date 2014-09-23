package com.michael.wallpaper.api.baidu;

import com.jesson.android.internet.core.annotations.HttpMethod;
import com.jesson.android.internet.core.annotations.RequiredParam;
import com.jesson.android.internet.core.annotations.RestMethodUrl;
import com.michael.wallpaper.api.BelleRequestBase;

/**
 * Created by michael on 14-7-5.
 */

@RestMethodUrl("http://image.baidu.com/data/imgs")
@HttpMethod("GET")
//col=搞笑&tag=碉堡&sort=0&tag3=&pn=0&rn=60&p=channel&from=1
public class BaiduListRequest extends BelleRequestBase<BaiduListResponse> {

    @RequiredParam("pn")
    private int pageName;

    @RequiredParam("rn")
    private int pageSize;

    //分类，美女，壁纸等
    @RequiredParam("col")
    private String col;

    //子类，高清美女，碉堡等
    @RequiredParam("tag")
    private String tag;

    @RequiredParam("sort")
    private int sort;

    @RequiredParam("p")
    private String p;

    @RequiredParam("from")
    private int from;

    public BaiduListRequest(int pageName, int pageSize, String col, String tag, int sort, String p, int from) {
        this.pageName = pageName;
        this.pageSize = pageSize;
        this.col = col;
        this.tag = tag;
        this.sort = sort;
        this.p = p;
        this.from = from;
    }
}
