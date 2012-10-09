package com.howaboutwe.android.widget.support;

import com.howaboutwe.android.widget.ImageDownload;
import com.howaboutwe.android.widget.SmartImageView;

import android.content.Context;

/**
 * Alternate version of {@link SmartImageView} that replaces the real {@link ImageDownload} with a
 * {@link TestImageDownload} in the test harness.
 */
public class TestSmartImageView extends SmartImageView {
    public TestSmartImageView(Context context) {
        super(context);
    }

    @Override
    protected ImageDownload createImageDownload() {
        return new TestImageDownload(getImageUrl(), this);
    }
}
