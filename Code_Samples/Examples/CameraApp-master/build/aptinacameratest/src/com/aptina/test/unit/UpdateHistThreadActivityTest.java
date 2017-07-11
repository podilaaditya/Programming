/**
 * 
 */
package com.aptina.test.unit;

import android.test.ActivityInstrumentationTestCase2;

import com.aptina.camera.CameraActivity;

/**
 * @author stoyan
 *
 */
public class UpdateHistThreadActivityTest extends ActivityInstrumentationTestCase2<CameraActivity> {
	// The Application object for the application under test
    private CameraActivity mActivity;
	public UpdateHistThreadActivityTest(Class<CameraActivity> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}
	
	public UpdateHistThreadActivityTest() {
		super(CameraActivity.class);
		// TODO Auto-generated constructor stub
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public final void testPreconditions(){
		assertNotNull(mActivity);
	}

}
