package com.tz.utils;

/**
 * 视频发布状态
 */
public enum VideoStatus {

//    发布成功
    SUCCESS(1),
//    禁止播放
    FORBID(2);
    private final int value;

    VideoStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
