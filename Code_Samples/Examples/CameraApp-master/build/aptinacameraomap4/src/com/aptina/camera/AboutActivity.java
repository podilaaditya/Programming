package com.aptina.camera;

import com.aptina.R;
import com.aptina.camera.components.FanView;

import android.app.Activity;
import android.os.Bundle;

/**
 * Implements about screen.
 */
public class AboutActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }
}
