package com.howaboutwe.android.demo.smartimageview;

import com.howaboutwe.android.widget.SmartImageView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.net.MalformedURLException;

/**
 * {@link SmartImageView} demo activity.
 */
public class SmartImageActivity extends Activity {

    public static final String DEMO_IMAGE_URL =
            "http://developer.android.com/images/home/android-jellybean.png";

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final SmartImageView smartImageView = (SmartImageView) findViewById(R.id.smart_image);
        try {
            smartImageView.setImageUrl(DEMO_IMAGE_URL);
        } catch (MalformedURLException e) {
            Toast.makeText(this, "Invalid image url", Toast.LENGTH_SHORT).show();
        }
    }
}
