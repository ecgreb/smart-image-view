package com.howaboutwe.android.demo.smartimageview;

import com.howaboutwe.android.widget.SmartImageView;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * {@link SmartImageActivity} test suite.
 */
@RunWith(RobolectricTestRunner.class)
public class SmartImageActivityTest {

    private SmartImageActivity mActivity;

    @Before
    public void setUp() throws Exception {
        mActivity = new SmartImageActivity();
        mActivity.onCreate(null);
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertThat(mActivity, notNullValue());
    }

    @Test
    public void shouldLoadRemoteImage() throws Exception {
        SmartImageView smartImageView = (SmartImageView) mActivity.findViewById(R.id.smart_image);
        assertThat(smartImageView.getImageUrl(), equalTo(SmartImageActivity.DEMO_IMAGE_URL));
    }
}
