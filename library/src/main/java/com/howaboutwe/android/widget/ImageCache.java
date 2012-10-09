package com.howaboutwe.android.widget;

import android.graphics.Bitmap;

/**
 * Remote bitmap cache.
 *
 * @since 1.0
 */
public class ImageCache extends LruCache<String, Bitmap> {
    private static final int CACHE_SIZE_IN_BYTES = 4 * 1024 * 1024;

    private static ImageCache instance = new ImageCache();

    public static ImageCache getInstance() {
        return instance;
    }

    private ImageCache() {
        super(CACHE_SIZE_IN_BYTES);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        oldValue.recycle();
    }
}
