package com.aptina.test.unit.miscellaneous;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;

import com.aptina.camera.CameraActivity;
import com.aptina.miscellaneous.PreferencesProvider;


public class PreferencesProviderTests extends ActivityInstrumentationTestCase2<CameraActivity> {
	CameraActivity mActivity;
	Context mContext;

	public PreferencesProviderTests(String name) {
		super(CameraActivity.class);
		setName(name);
	}
	
	public PreferencesProviderTests(){
		this("PreferencesProviderTests");
	}

	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mContext = mActivity.getApplicationContext();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSetLogging() {
		assertNotNull(mActivity);
		assertNotNull(mContext);
		boolean expected = true;
		PreferencesProvider.setLogging(mContext, expected);
		
		SharedPreferences settings = mContext.getSharedPreferences(PreferencesProvider.PREFERENCES_NAME, 0);
        boolean actual = settings.getBoolean(PreferencesProvider.PREFERENCES_LOGGING_KEY, true);
        
        assertTrue(expected == actual);
        
        expected = false;
        PreferencesProvider.setLogging(mContext, expected);
        settings = mContext.getSharedPreferences(PreferencesProvider.PREFERENCES_NAME, 0);
        actual = settings.getBoolean(PreferencesProvider.PREFERENCES_LOGGING_KEY, true);
        
        assertTrue(expected == actual);
	}

	public void testIsLoggingOn() {
		boolean expected = true;
		PreferencesProvider.setLogging(mContext, expected);
		boolean actual = PreferencesProvider.isLoggingOn(mContext);
		assertTrue(expected == actual);
		 
		actual = false;
		PreferencesProvider.setLogging(mContext, expected);
		actual = PreferencesProvider.isLoggingOn(mContext);
		assertTrue(expected == actual);
	}

}
