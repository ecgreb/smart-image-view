package com.howaboutwe.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.net.MalformedURLException;

/**
 * Remote image view with built-in HTTP download and caching.
 */
public class SmartImageView extends RelativeLayout implements ImageDownload.ImageDownloadListener {
    private static final String TAG = SmartImageView.class.getSimpleName();

    final protected ImageCache mImageCache = ImageCache.getInstance();

    protected String mImageUrl = null;
    protected Drawable mDefaultDrawable = null;
    protected ImageDownload mImageDownload = null;
    protected ImageView mImageView = null;
    protected View mLoadingIndicator = null;

    /**
     * {@inheritDoc}
     */
    public SmartImageView(Context context) {
        this(context, null);
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
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.smart_image_view, this);
        mImageView = (ImageView) findViewById(R.id.image);
        mLoadingIndicator = findViewById(R.id.loading_indicator);

        if (attrs != null) {
            parseCustomAttributes(context, attrs);
        }
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

    /**
     * Sets the image url. Loads the bitmap from cache if available. Otherwise initiates a new
     * {@link ImageDownload} request.
     *
     * @param url the url of the image.
     * @throws MalformedURLException if the url is not valid.
     */
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
            mImageView.setImageBitmap(cachedBitmap);
        } else {
            showLoadingIndicator();
            downloadImage();
        }
    }

    /**
     * Returns the url of the image currently being displayed or downloaded if a download is in
     * progress.
     *
     * @return the current image url.
     */
    public String getImageUrl() {
        return mImageUrl;
    }

    /**
     * Clears the current image url and resets the view to default.
     */
    public void clearImage() {
        mImageUrl = null;
        showDefaultDrawable();
    }

    /**
     * Initiates a new image download request. Cancels any download currently in progress.
     */
    private void downloadImage() {
        if (mImageDownload != null && mImageDownload.isAlive()) {
            mImageDownload.cancel();
        }

        mImageDownload = createImageDownload();
        mImageDownload.start();
    }

    /**
     * {@link ImageDownload} factory method.
     *
     * @return a new {@link ImageDownload}.
     */
    protected ImageDownload createImageDownload() {
        return new ImageDownload(mImageUrl, this);
    }

    /**
     * Reset the current display to the default drawable.
     */
    public void showDefaultDrawable() {
        mImageView.setImageDrawable(mDefaultDrawable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDownloadSuccess(final String url, final Bitmap bitmap) {
        post(new Runnable() {
            @Override
            public void run() {
                hideLoadingIndicator();
                if (url != null && bitmap != null) {
                    if (mImageUrl.equals(url)) {
                        mImageView.setImageBitmap(bitmap);
                    }
                    mImageCache.put(url, bitmap);
                }

            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDownloadError(String url) {
        Log.e(TAG, "Error downloading image: " + url);
        post(new Runnable() {
            @Override
            public void run() {
                hideLoadingIndicator();
            }
        });
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    public void setDefaultDrawable(Drawable defaultDrawable) {
        mDefaultDrawable = defaultDrawable;
    }

    public Drawable getDefaultDrawable() {
        return mDefaultDrawable;
    }
}
