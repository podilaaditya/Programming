/**
 * 
 */
package com.aptina.test.ui;

import android.test.ActivityInstrumentationTestCase2;

import com.aptina.camera.CameraActivity;



/**
 * @author stoyan
 *
 */
public class CameraActivityUITests extends ActivityInstrumentationTestCase2<CameraActivity> {
	private CameraActivity mActivity;
	/**
	 * @param name
	 */
	public CameraActivityUITests(String name) {
		super(CameraActivity.class);
		setName(name);
	}
	public CameraActivityUITests() {
		this("CameraActivityUITests");
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
