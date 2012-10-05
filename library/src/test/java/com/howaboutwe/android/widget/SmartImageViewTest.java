package com.howaboutwe.android.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowImageView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * {@link SmartImageView} test suite.
 */
@RunWith(RobolectricTestRunner.class)
public class SmartImageViewTest {

    private SmartImageView mSmartImageView;

    @Before
    public void setUp() throws Exception {
        mSmartImageView = new SmartImageView(null);
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
    public void shouldLoadBitmapFromCache() throws Exception {
        ImageCache imageCache = ImageCache.getInstance();
        String url = "http://www.example.com/some/image.jpg";
        Bitmap bitmap = BitmapFactory.decodeFile("");
        imageCache.put(url, bitmap);

        mSmartImageView.setImageUrl(url);
        ShadowImageView shadowImageView = Robolectric.shadowOf(mSmartImageView);
        assertThat(shadowImageView.getImageBitmap(), notNullValue());
    }
}
