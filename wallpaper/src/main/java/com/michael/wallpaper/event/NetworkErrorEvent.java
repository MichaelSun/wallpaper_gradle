package com.michael.wallpaper.event;

/**
 * Created by zhangdi on 14-3-7.
 */
public class NetworkErrorEvent {

    public Exception e;

    public int contentType;

    public NetworkErrorEvent() {
    }

    public NetworkErrorEvent(Exception e, int contentType) {
        this.e = e;
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "NetworkErrorEvent{" +
                   "e=" + e +
                   ", contentType=" + contentType +
                   '}';
    }
}
