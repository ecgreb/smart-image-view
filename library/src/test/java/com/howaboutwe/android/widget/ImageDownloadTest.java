package com.howaboutwe.android.widget;

import com.howaboutwe.android.widget.support.TestHttpURLConnection;
import com.howaboutwe.android.widget.support.TestImageDownloadListener;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * {@link ImageDownload} test suite.
 */
@RunWith(RobolectricTestRunner.class)
public class ImageDownloadTest {

    private static final String TEST_URL = "http://www.example.com/some/image.jpg";

    private ImageDownload mImageDownload;
    private ImageDownload.ImageDownloadListener mListener;

    @Before
    public void setUp() throws Exception {
        mListener = new TestImageDownloadListener();
        mImageDownload = new TestImageDownload(TEST_URL, mListener);
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertThat(mImageDownload, notNullValue());
    }

    @Test
    public void shouldNotifyListenerOnDownloadSuccess() throws Exception {
        TestHttpURLConnection.setTestResponseCode(HttpURLConnection.HTTP_OK);
        mImageDownload.run();
        assertThat(TestImageDownloadListener.getLastSuccessUrl(), equalTo(TEST_URL));
        assertThat(TestImageDownloadListener.getLastBitmapDownloaded(), notNullValue());
    }

    @Test
    public void shouldNotifyListenerOnDownloadError() throws Exception {
        TestHttpURLConnection.setTestResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
        mImageDownload.run();
        assertThat(TestImageDownloadListener.getLastErrorUrl(), equalTo(TEST_URL));
        assertThat(TestImageDownloadListener.getLastBitmapDownloaded(), nullValue());
    }

    @Test
    public void shouldCancelRequest() throws Exception {
        mImageDownload.cancel();
        mImageDownload.run();
        assertThat(TestImageDownloadListener.getLastSuccessUrl(), nullValue());
        assertThat(TestImageDownloadListener.getLastErrorUrl(), nullValue());
    }

    private class TestImageDownload extends ImageDownload {
        public TestImageDownload(String url, ImageDownloadListener listener) {
            super(url, listener);
        }

        @Override
        protected HttpURLConnection createConnection() throws IOException {
            return new TestHttpURLConnection(new URL(getUrl()));
        }
    }
}
