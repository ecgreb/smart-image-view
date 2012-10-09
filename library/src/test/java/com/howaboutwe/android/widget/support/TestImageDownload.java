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
    public TestImageDownload(String url, ImageDownloadListener listener) {
        super(url, listener);
    }

    @Override
    public synchronized void start() {
        runInternal();
    }

    @Override
    protected HttpURLConnection createConnection() throws IOException {
        return new TestHttpURLConnection(new URL(mUrl));
    }
}
