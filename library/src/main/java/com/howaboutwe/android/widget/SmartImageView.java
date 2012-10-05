package com.howaboutwe.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.net.MalformedURLException;

/**
 * Remote image view with built-in HTTP download and caching.
 */
public class SmartImageView extends ImageView implements ImageDownload.ImageDownloadListener {

    private String mImageUrl = null;
    private Drawable mDefaultDrawable = null;
    private ImageDownload mImageDownload = null;

    final private ImageCache mImageCache = ImageCache.getInstance();

    /**
     * {@inheritDoc}
     */
    public SmartImageView(Context context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    public SmartImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * {@inheritDoc}
     */
    public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseCustomAttributes(context, attrs);
    }

    /**
     * Parses custom xml attributes.
     *
     * @param context the context with which this component was created.
     * @param attrs the layout attributes declared in xml.
     */
    private void parseCustomAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SmartImageView);
        mDefaultDrawable = array.getDrawable(R.styleable.SmartImageView_default_drawable);
        array.recycle();
        showDefaultDrawable();
    }

    public void setImageUrl(String url) throws MalformedURLException {
        if (TextUtils.isEmpty(url)) {
            throw new MalformedURLException("Invalid URL: " + url);
        }

        if (url.equals(this.mImageUrl)) {
            return;
        }

        mImageUrl = url;

        Bitmap cachedBitmap = mImageCache.get(url);
        if (cachedBitmap != null) {
            setImageBitmap(cachedBitmap);
        } else {
            downloadImage();
        }
    }

    private void downloadImage() {
        if (mImageDownload != null && mImageDownload.isAlive()) {
            mImageDownload.cancel();
        }

        mImageDownload = new ImageDownload(mImageUrl, this);
        mImageDownload.start();
    }

    public void showDefaultDrawable() {
        setImageDrawable(mDefaultDrawable);
    }

    @Override
    public void onDownloadSuccess(String url, Bitmap bitmap) {
        if (url != null && bitmap != null) {
            if (this.mImageUrl.equals(url)) {
                loadImage(bitmap);
            }
            mImageCache.put(url, bitmap);
        }
    }

    @Override
    public void onDownloadError(String url) {
    }

    private void loadImage(final Bitmap bitmap) {
        post(new Runnable() {
            @Override
            public void run() {
                setImageBitmap(bitmap);
            }
        });
    }

    // Setters and Getters

    public void setDefaultDrawable(Drawable defaultDrawable) {
        mDefaultDrawable = defaultDrawable;
    }

    public Drawable getDefaultDrawable() {
        return mDefaultDrawable;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
