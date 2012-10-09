package com.howaboutwe.android.widget;

import com.howaboutwe.android.widget.support.TestHttpURLConnection;
import com.howaboutwe.android.widget.support.TestSmartImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowImageView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;

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
        mSmartImageView = new TestSmartImageView(null);
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
        ShadowImageView shadowImageView = Robolectric.shadowOf(mSmartImageView);
        assertThat(shadowImageView.getImageBitmap(), notNullValue());
    }

    @Test
    public void shouldDownloadImageIfNotCached() throws Exception {
        TestHttpURLConnection.setTestResponseCode(HttpURLConnection.HTTP_OK);
        mSmartImageView.setImageUrl(TEST_URL);
        ShadowImageView shadowImageView = Robolectric.shadowOf(mSmartImageView);
        assertThat(shadowImageView.getImageBitmap(), notNullValue());
    }
}
