package com.michael.wallpaper.event;

import com.michael.wallpaper.api.belle.Belle;

import java.util.List;

/**
 * Created by zhangdi on 14-3-7.
 */
public class GetBelleListEvent {

    public static final int TYPE_LOCAL = 1;
    public static final int TYPE_SERVER = 2;
    public static final int TYPE_SERVER_MORE = 3;
    public static final int TYPE_SERVER_RANDOM = 4;

    public int type = TYPE_LOCAL;

    public boolean hasMore;

    public int startIndex;

    public int pageCount;

    public int totalNum;

    public int contentType;

    public List<Belle> belles;

    @Override
    public String toString() {
        return "GetBelleListEvent{" +
                   "type=" + type +
                   ", hasMore=" + hasMore +
                   ", startIndex=" + startIndex +
                   ", pageCount=" + pageCount +
                   ", totalNum=" + totalNum +
                   ", contentType=" + contentType +
                   ", belles=" + belles +
                   '}';
    }
}
