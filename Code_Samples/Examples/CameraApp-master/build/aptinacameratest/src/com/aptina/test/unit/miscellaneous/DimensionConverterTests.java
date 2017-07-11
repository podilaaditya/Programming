/**
 * 
 */
package com.aptina.test.unit.miscellaneous;


import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.aptina.camera.CameraActivity;
import com.aptina.miscellaneous.DimensionConverter;





/**
 * @author stoyan
 *
 */
public class DimensionConverterTests extends
		ActivityInstrumentationTestCase2<CameraActivity> {

	CameraActivity mActivity;
	Context mContext;
	public DimensionConverterTests() {
		this("DimensionConverterTests");
	}
	
	public DimensionConverterTests(String name) {
		super(CameraActivity.class);
		setName(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mContext = mActivity.getApplicationContext();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.aptina.miscellaneous.DimensionConverter#dipToPixels(android.content.Context, float)}.
	 */
	public void testDipToPixels() {
		assertNotNull(mActivity);
		assertNotNull(mContext);
		final float scale = mContext.getResources().getDisplayMetrics().density;
		for( int i = 0; i < 4; i++){
			final float dip = i * 3; 
			final float expected = scale * dip;
			final float actual = DimensionConverter.dipToPixels(mContext, dip);
			final String msg = "expected : " + expected + ", but returned : " + actual;
			assertEquals(msg, expected, actual);
		}
		


	}

}
