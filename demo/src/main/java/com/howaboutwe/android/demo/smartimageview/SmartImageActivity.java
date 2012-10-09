package com.howaboutwe.android.demo.smartimageview;

import com.howaboutwe.android.widget.ImageCache;
import com.howaboutwe.android.widget.SmartImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.net.MalformedURLException;

/**
 * {@link SmartImageView} demo activity.
 */
public class SmartImageActivity extends Activity {

    public static final String DEMO_IMAGE_URL =
            "http://developer.android.com/images/brand/Android_Robot_200.png";

    private SmartImageView mSmartImageView = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mSmartImageView = (SmartImageView) findViewById(R.id.smart_image);
        loadImage();

        final Button refreshButton = (Button) findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageCache.getInstance().evictAll();
                mSmartImageView.clearImage();
                loadImage();
            }
        });

        final Switch indicatorSwitch = (Switch) findViewById(R.id.use_indicator);
        indicatorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSmartImageView.setLoadingIndicatorEnabled(isChecked);
            }
        });
    }

    private void loadImage() {
        try {
            mSmartImageView.setImageUrl(DEMO_IMAGE_URL);
        } catch (MalformedURLException e) {
            Toast.makeText(this, "Invalid image url", Toast.LENGTH_SHORT).show();
        }
    }
}
