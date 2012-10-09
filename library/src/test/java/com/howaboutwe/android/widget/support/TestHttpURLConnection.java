package com.howaboutwe.android.widget.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Test implementation of {@link HttpURLConnection} that simulates all network requests.
 */
public class TestHttpURLConnection extends HttpURLConnection {
    private static int sTestResponseCode = -1;

    public TestHttpURLConnection(URL url) {
        super(url);
    }

    @Override
    public int getResponseCode() throws IOException {
        return sTestResponseCode;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        File file = new File("src/test/java/com/howaboutwe/android/widget/support/test_image.png");
        return new FileInputStream(file);
    }

    @Override
    public void disconnect() {
    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    @Override
    public void connect() throws IOException {
    }

    public static void setTestResponseCode(int responseCode) {
        sTestResponseCode = responseCode;
    }
}