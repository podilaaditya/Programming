package com.aptina.test.ui;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.RelativeLayout;

import com.aptina.R;
import com.aptina.camera.CameraActivity;
import com.aptina.camera.components.ZoomControl;

public class ZoomControlUITests extends ActivityInstrumentationTestCase2<CameraActivity>  {
	public static final String TAG = "ZoomControlUITests";
	private CameraActivity mActivity;
	public ZoomControlUITests(String name) {
		super(CameraActivity.class);
		setName(name);
	}
	public ZoomControlUITests(){
		this("ZoomControlUITests");
	}

	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public final void testSliderLimits(){
		assertNotNull(mActivity);
		
		RelativeLayout main = (RelativeLayout) mActivity.findViewById(R.id.main_camera_frame);
		ZoomControl zc = (ZoomControl) mActivity.findViewById(R.id.zoom_layout);
		Log.i(TAG, "zoom control left : " + zc.getLeft());
		Log.i(TAG, "main width : "+ main.getWidth());
		
	}

}
