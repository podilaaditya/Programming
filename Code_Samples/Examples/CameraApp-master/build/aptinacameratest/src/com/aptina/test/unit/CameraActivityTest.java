package com.aptina.test.unit;


import android.test.ActivityInstrumentationTestCase2;

import com.aptina.camera.CameraActivity;




public class CameraActivityTest  extends ActivityInstrumentationTestCase2<CameraActivity>{


	// The Application object for the application under test
    private CameraActivity mActivity;
	public CameraActivityTest(Class<CameraActivity> activityClass) {
		super(activityClass);
		
	}
	
	public CameraActivityTest(){
		super(CameraActivity.class);
	}
	   /*
     * Sets up the test environment before each test.
     * @see android.test.ActivityInstrumentationTestCase2#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        /*
         * Call the super constructor (required by JUnit)
         */

        super.setUp();

        /*
         * prepare to send key events to the app under test by turning off touch mode.
         * Must be done before the first call to getActivity()
         */

        setActivityInitialTouchMode(false);

        /*
         * Start the app under test by starting its main activity. The test runner already knows
         * which activity this is from the call to the super constructor, as mentioned
         * previously. The tests can now use instrumentation to directly access the main
         * activity through mActivity.
         */
        mActivity = getActivity();

        /*
         * Get references to objects in the application under test. These are
         * tested to ensure that the app under test has initialized correctly.
         */


    }


}
