package com.howaboutwe.android.widget.support;

import com.howaboutwe.android.widget.ImageDownload;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Alternate version of {@link ImageDownload} that uses a fake network connection and runs
 * synchronously in the test harness.
 */
public class TestImageDownload extends ImageDownload {

    private static boolean sIsTestDownloadEnabled = true;

    public TestImageDownload(String url, ImageDownloadListener listener) {
        super(url, listener);
    }

    @Override
    public synchronized void start() {
        if (sIsTestDownloadEnabled) {
            runInternal();
        }
    }

    @Override
    protected HttpURLConnection createConnection() throws IOException {
        return new TestHttpURLConnection(new URL(mUrl));
    }

    /**
     * Sets whether or not the image download should complete in the test environment. Useful for
     * testing the download in-progress state.
     *
     * @param isEnabled true if test downloads should complete, false if test downloads should not
     * complete.
     */
    public static void setTestDownloadEnabled(boolean isEnabled) {
        sIsTestDownloadEnabled = isEnabled;
    }
}
