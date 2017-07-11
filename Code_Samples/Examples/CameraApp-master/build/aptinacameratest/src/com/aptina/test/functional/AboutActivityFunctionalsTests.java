package com.aptina.test.functional;

import com.aptina.camera.AboutActivity;


import android.test.ActivityInstrumentationTestCase2;

public class AboutActivityFunctionalsTests extends
		ActivityInstrumentationTestCase2<AboutActivity> {

	public AboutActivityFunctionalsTests(String name) {
		super(AboutActivity.class);
		setName(name);
	}
	public AboutActivityFunctionalsTests() {
		this("AboutActivityFunctionalsTests");
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
