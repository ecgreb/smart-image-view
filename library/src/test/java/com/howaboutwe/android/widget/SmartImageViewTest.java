package com.howaboutwe.android.widget;

import com.howaboutwe.android.widget.support.TestHttpURLConnection;
import com.howaboutwe.android.widget.support.TestImageDownload;
import com.howaboutwe.android.widget.support.TestSmartImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowImageView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * {@link SmartImageView} test suite.
 */
@RunWith(RobolectricTestRunner.class)
public class SmartImageViewTest {

    private static final String TEST_URL = "http://www.example.com/some/image.jpg";

    private SmartImageView mSmartImageView;

    @Before
    public void setUp() throws Exception {
        mSmartImageView = new TestSmartImageView(new Activity());
        ImageCache.getInstance().evictAll();
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertThat(mSmartImageView, notNullValue());
    }

    @Test(expected = MalformedURLException.class)
    public void shouldRejectNullUrl() throws Exception {
        mSmartImageView.setImageUrl(null);
    }

    @Test(expected = MalformedURLException.class)
    public void shouldRejectEmptyUrl() throws Exception {
        mSmartImageView.setImageUrl("");
    }

    @Test
    public void shouldLoadBitmapFromCacheIfAvailable() throws Exception {
        ImageCache imageCache = ImageCache.getInstance();
        Bitmap bitmap = BitmapFactory.decodeFile("");
        imageCache.put(TEST_URL, bitmap);

        mSmartImageView.setImageUrl(TEST_URL);
        ShadowImageView shadowImageView =
                (ShadowImageView) Robolectric.shadowOf(mSmartImageView.findViewById(R.id.image));
        assertThat(shadowImageView.getImageBitmap(), notNullValue());
    }

    @Test
    public void shouldDownloadImageIfNotCached() throws Exception {
        TestHttpURLConnection.setTestResponseCode(HttpURLConnection.HTTP_OK);
        mSmartImageView.setImageUrl(TEST_URL);
        ShadowImageView shadowImageView =
                (ShadowImageView) Robolectric.shadowOf(mSmartImageView.findViewById(R.id.image));
        assertThat(shadowImageView.getImageBitmap(), notNullValue());
    }

    @Test
    public void shouldShowLoadingIndicatorWhileImageDownloadInProgress() throws Exception {
        TestImageDownload.setTestDownloadEnabled(false);
        TestHttpURLConnection.setTestResponseCode(HttpURLConnection.HTTP_OK);
        mSmartImageView.setImageUrl(TEST_URL);
        View loadingIndicator = mSmartImageView.findViewById(R.id.loading_indicator);
        assertThat(loadingIndicator.getVisibility(), equalTo(View.VISIBLE));
    }

    @Test
    public void shouldHideLoadingIndicatorOnImageDownloadSuccess() throws Exception {
        TestHttpURLConnection.setTestResponseCode(HttpURLConnection.HTTP_OK);
        mSmartImageView.setImageUrl(TEST_URL);
        mSmartImageView.onDownloadSuccess(TEST_URL, BitmapFactory.decodeFile(""));
        View loadingIndicator = mSmartImageView.findViewById(R.id.loading_indicator);
        assertThat(loadingIndicator.getVisibility(), equalTo(View.GONE));
    }

    @Test
    public void shouldHideLoadingIndicatorOnImageDownloadError() throws Exception {
        TestHttpURLConnection.setTestResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
        mSmartImageView.setImageUrl(TEST_URL);
        mSmartImageView.onDownloadSuccess(TEST_URL, BitmapFactory.decodeFile(""));
        View loadingIndicator = mSmartImageView.findViewById(R.id.loading_indicator);
        assertThat(loadingIndicator.getVisibility(), equalTo(View.GONE));
    }

    @Test
    public void shouldNotShowLoadingIndicatorIfDisabled() throws Exception {
        TestImageDownload.setTestDownloadEnabled(false);
        TestHttpURLConnection.setTestResponseCode(HttpURLConnection.HTTP_OK);
        mSmartImageView.setLoadingIndicatorEnabled(false);
        mSmartImageView.setImageUrl(TEST_URL);
        View loadingIndicator = mSmartImageView.findViewById(R.id.loading_indicator);
        assertThat(loadingIndicator.getVisibility(), equalTo(View.GONE));
    }
}
