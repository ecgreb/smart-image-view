package com.howaboutwe.android.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Remote image download via background {@link Thread}.
 */
public class ImageDownload extends Thread {
    private static final int CONNECT_TIMEOUT_IN_MILLIS = 30 * 1000;

    protected String mUrl;
    protected ImageDownloadListener mListener;
    protected boolean mIsCanceled = false;

    /**
     * Constructs a new {@code ImageDownload} with the given url and listener.
     */
    public ImageDownload(String url, ImageDownloadListener listener) {
        mUrl = url;
        mListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        runInternal();
    }

    /**
     * Attempts to execute the HTTP request unless this download has been canceled.
     */
    protected void runInternal() {
        if (!mIsCanceled) {
            try {
                downloadImage();
            } catch (IOException e) {
                mListener.onDownloadError(mUrl);
            }
        }
    }

    /**
     * Executes the image download HTTP request.
     *
     * @throws IOException if the request fails.
     */
    protected void downloadImage() throws IOException {
        final HttpURLConnection connection = createConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT_IN_MILLIS);

        final int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            final InputStream in = connection.getInputStream();
            onHttpSuccess(in);
        } else {
            onHttpError();
        }
    }

    /**
     * Opens a new {@link HttpURLConnection} for the given url.
     *
     * @return the newly created {@link HttpURLConnection}.
     * @throws IOException if unable to open the connection.
     */
    protected HttpURLConnection createConnection() throws IOException {
        return (HttpURLConnection) new URL(mUrl).openConnection();
    }

    /**
     * Handles successful download.
     *
     * @param in the HTTP response stream.
     */
    private void onHttpSuccess(InputStream in) {
        final Bitmap bitmap = decodeBitmap(in);
        if (bitmap != null) {
            mListener.onDownloadSuccess(mUrl, bitmap);
        }
    }

    /**
     * Handles download error.
     */
    private void onHttpError() {
        mListener.onDownloadError(mUrl);
    }

    /**
     * Decodes an {@link InputStream} into a {@link Bitmap}.
     *
     * @param in byte stream for the remote image to be loaded.
     */
    private Bitmap decodeBitmap(InputStream in) {
        if (mIsCanceled) {
            return null;
        }

        return BitmapFactory.decodeStream(in);
    }

    /**
     * Cancels an image download currently in progress.
     */
    public void cancel() {
        mIsCanceled = true;
        interrupt();
    }

    /**
     * Callback interface for {@link ImageDownload} updates.
     */
    public interface ImageDownloadListener {
        /**
         * Success callback.
         *
         * @param url remote image url.
         * @param bitmap decoded bitmap.
         */
        public void onDownloadSuccess(String url, Bitmap bitmap);

        /**
         * Error callback.
         *
         * @param url remote image url.
         */
        public void onDownloadError(String url);
    }
}
