/**
 * 
 */
package com.aptina.test.unit.miscellaneous;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.aptina.camera.CameraActivity;
import com.aptina.miscellaneous.FileUtils;

import junit.framework.TestCase;

/**
 * @author stoyan
 *
 */
public class FileUtilsTests extends ActivityInstrumentationTestCase2<CameraActivity> {
	CameraActivity mActivity;
	Context mContext;
	/**
	 * @param name
	 */
	public FileUtilsTests(String name) {
		super(CameraActivity.class);
		setName(name);
	}
	public FileUtilsTests() {
		this("FileUtilsTests");
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mContext = mActivity.getApplicationContext();
		
//		PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
//        WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
//        wakeLock.acquire();
//        KeyguardManager keyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE); 
//        KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
//        keyguardLock.disableKeyguard();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.aptina.miscellaneous.FileUtils#isSdCardMounted()}.
	 */
	public void testIsSdCardMounted() {
		assertTrue(FileUtils.isSdCardMounted());
	}

	/**
	 * Test method for {@link com.aptina.miscellaneous.FileUtils#isFileExist(java.lang.String)}.
	 * @throws IOException 
	 * 	// this will create app_tmp1  directory under data/data/com.example.test/,
		// in another word, use test app's internal storage.
		this.getInstrumentation().getContext().getDir("tmp1", 0);
		//this will create app_tmp2 directory under data/data/com.example/,
		// in another word use app's internal storage.
		this.getInstrumentation().getTargetContext().getDir("tmp2", 0);
	 */
	
	public void testIsFileExist() throws IOException {
		assertNotNull(mActivity);
		assertNotNull(mContext);
		
		final String testFileName = "file_test.txt";
		

		try {
			
			FileOutputStream os = this.getInstrumentation().getTargetContext().openFileOutput(testFileName, Context.MODE_PRIVATE);
			os.close();
			final File testFile = this.getInstrumentation().getTargetContext().getFileStreamPath(testFileName);
			assertTrue(FileUtils.isFileExist(testFile.getAbsolutePath()));
			
			testFile.delete();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Test method for {@link com.aptina.miscellaneous.FileUtils#getAvailableBytes()}.
	 */
	public void testGetAvailableBytes() {
		assertTrue(FileUtils.getAvailableBytes() > 0);
	}

}
