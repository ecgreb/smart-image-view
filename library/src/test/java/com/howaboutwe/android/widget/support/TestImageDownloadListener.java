package com.howaboutwe.android.widget.support;

import com.howaboutwe.android.widget.ImageDownload;

import android.graphics.Bitmap;

public class TestImageDownloadListener implements ImageDownload.ImageDownloadListener {
    private static String sLastSuccessUrl = null;
    private static String sLastErrorUrl = null;
    private static Bitmap sLastBitmapDownloaded = null;

    public TestImageDownloadListener() {
        sLastSuccessUrl = null;
        sLastErrorUrl = null;
        sLastBitmapDownloaded = null;
    }

    @Override
    public void onDownloadSuccess(String url, Bitmap bitmap) {
        sLastSuccessUrl = url;
        sLastBitmapDownloaded = bitmap;
    }

    @Override
    public void onDownloadError(String url) {
        sLastErrorUrl = url;
    }

    public static String getLastSuccessUrl() {
        return sLastSuccessUrl;
    }

    public static String getLastErrorUrl() {
        return sLastErrorUrl;
    }

    public static Bitmap getLastBitmapDownloaded() {
        return sLastBitmapDownloaded;
    }
}
