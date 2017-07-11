package com.aptina.test.functional;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Fragment;
import android.app.Instrumentation;
import android.graphics.Path;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aptina.R;
import com.aptina.adapter.HistogramPageAdapter;
import com.aptina.camera.CameraActivity;
import com.aptina.camera.CameraInfo;
import com.aptina.camera.ImageDetailActivity;
import com.aptina.camera.components.OptionsMenu;
import com.aptina.camera.components.ThumbnailControl;
import com.aptina.camera.components.ZoomControl;
import com.aptina.camera.fragments.HistogramFrag;
import com.aptina.camera.interfaces.MenuLayoutInterface;
import com.aptina.data.HistogramRGBData;
import com.jayway.android.robotium.solo.Solo;

/**
 * @author stoyan
 *
 */
public class CameraActivityFunctionalTests extends
		ActivityInstrumentationTestCase2<CameraActivity> {
	private static final String TAG = "CameraActivityFunctionalTests";
	private CameraActivity mActivity;
	private Camera mCamera;
	private String[] focus_options;
	private Solo solo;
	
	/**
	 * Histogram
	 */
    private ViewPager mViewPager;
    private HistogramPageAdapter mHistPageAdapter;
    private LinearLayout mHistogramMenuList;
    /**
     * Options view.
     */
    private View mOptionsButton;
    private OptionsMenu mOptionsMenu;
    private LinearLayout mOptionsMenuView;
    private MenuLayoutInterface mMenuLayoutInterface;
	
	/**
	 * Time to wait before timing out image capture, in seconds
	 */
	private static final int CAPTURE_TIMEOUT_TIME = 10;
	/**
	 * Time to wait before timing out histogram update, in seconds
	 */
	private static final int HISTOGRAM_TIMEOUT_TIME = 10;
	
	public CameraActivityFunctionalTests(String name) {
		super(CameraActivity.class);
		setName(name);
	}
	public CameraActivityFunctionalTests() {
		this("CameraActivityFunctionalTests");
	}
	
	public  String TEST_FILE_DIR = Environment.getExternalStorageDirectory() + "/CameraAppTest/";
	
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		solo = new Solo(getInstrumentation(),getActivity());
		
        mOptionsButton = (View) mActivity.findViewById(com.aptina.R.id.options_button);
        mOptionsMenu = mActivity.getOptionsMenu();
        mOptionsMenuView = mActivity.getFocusMenuView();
        mMenuLayoutInterface = mActivity.getOptionsMenuInterface();
        mViewPager = (ViewPager) mActivity.findViewById(R.id.histogram_viewpager);
        mHistPageAdapter = mActivity.getHistogramPageAdapter();
        mHistogramMenuList = (LinearLayout) mActivity.findViewById(R.id.ll_menu_histogram);
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public final void testPreconditions(){
		assertNotNull(mActivity);
	}
	
    
    public final void testHistogramThreads(){
    	//Test the existence of the options elements
    	OptionsPreconditions();
    	//Test the existence of the histogram elements
    	HistogramPreconditions();
    	
    	//Check to see if the preview is running
		assertTrue(mActivity.isPreviewing());
		
		//Set the histogram update to false
//		mHistPageAdapter.setUpdateLatch(false);
//		//Test if the updateLatch was set
//		assertFalse(mHistPageAdapter.getUpdateLatch());
		
		//Test that the histogram is not visible at start
		assertEquals(mViewPager.getVisibility(),View.INVISIBLE);
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					//Make the histogram visible
					mActivity.histogramVisible(true);
					
				}
			});
	
		} catch (Throwable e) {
			fail("Setting histogram to visible failed");
			e.printStackTrace();
		}finally{
			
		}
		//Wait for UI
		getInstrumentation().waitForIdleSync();
		
		//Test that the histogram was made visible
		assertEquals(mViewPager.getVisibility(),View.VISIBLE);
		
//		//Set the histogram to start updating
//		mHistPageAdapter.setUpdateLatch(true);
//		//Test that the histogram has been set to update
//		assertTrue(mHistPageAdapter.getUpdateLatch());
//		
//		//Make Test wait for the onPreExecute() to complete of the AsynTask
//		waitForHistogramAdapter(1);
//		
//		//Test that the histogram update mUpdateHist has been set to false
//		assertFalse(mHistPageAdapter.getUpdateLatch());
//		
//		//Release the AsynTask thread to continue to doInBackground
//		mHistPageAdapter.incrementHistLatch();
		

		
		
		
//		mHistPageAdapter.incrementHistLatch();
		

		
//		waitForHistogramAdapter(1);
		
//		try {
//			final boolean didNotTimedOut = mActivity.signal.await(HISTOGRAM_TIMEOUT_TIME, TimeUnit.SECONDS);
//			assertTrue(didNotTimedOut);
//
//		} catch (InterruptedException e) {
//			fail("Interrupted Execption in AutoPicture Capture test");
//			e.printStackTrace();
//		}
		
		
    }
    
    private final void waitForHistogramAdapter(int countdowns){
    	//Set the histogram count down latch to 1 
    	mHistPageAdapter.setHistCountDownLatch(countdowns);
    			
    	//Test that this test case waits for 
    	assertTrue(mHistPageAdapter.latchWait());
    }
    
    private final void OptionsPreconditions(){
    	assertNotNull(mOptionsButton);
    	assertNotNull(mOptionsMenu);
    	assertNotNull(mOptionsMenuView);
    	assertNotNull(mMenuLayoutInterface);

    }
    
    private final void HistogramPreconditions(){
    	assertNotNull(mViewPager);
    	assertNotNull(mHistPageAdapter);
    	assertNotNull(mHistogramMenuList);
    }
	/**
	 * Test should check that camera captures raw, jpeg, and postview images
	 * when the shutter button is triggered. Continuous-picture should be the focus.
	 * Need additional tests to check for other focus modes and resolutions in auto mode
	 */
	public final void testAutoPictureCapture(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
		assertTrue(CameraInfo.CAMERA_MODE_AUTO.equalsIgnoreCase(mActivity.getCurrentActiveMode()));
		assertNull(mActivity.getAutofocusCallback());
		//http://stackoverflow.com/questions/2321829/android-asynctask-testing-problem-with-android-test-framework
		//http://mobilengineering.blogspot.com/2012/05/tdd-testing-asynctasks-on-android.html

		
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mActivity.mShutterButton.requestFocus();
					mActivity.getShutterButton().performClick();
					
				}
			});
	
		} catch (Throwable e) {
			fail("Running click on shutter button failed in AutoPicture Capture test");
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
		
		assertFalse(mActivity.isPreviewing());
		assertTrue(mActivity.isSDCardAvailable());
		assertFalse(mActivity.getShutterButton().isClickable());
		
		assertNotNull(mActivity.getAutofocusCallback());
		
		assertNotNull(mActivity.getShutterCallback());
		assertNotNull(mActivity.getRawPictureCallback());
		assertNotNull(mActivity.getPostViewCallback());
		assertNotNull(mActivity.getJPEGPictureCallback());
		

		mActivity.setCountDownLatch(4);
		try {
			final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
			assertTrue(didNotTimedOut);

		} catch (InterruptedException e) {
			fail("Interrupted Execption in AutoPicture Capture test");
			e.printStackTrace();
		}
		
		assertTrue(mActivity.isRawDataNull());
		assertFalse(mActivity.isJPEGDataNull());
		
		assertTrue(mActivity.isPreviewing());
		assertTrue(mActivity.isSDCardAvailable());
		assertNull(mActivity.getAutofocusCallback());
		
		copyfile("testAutoPicture.jpg");
		
		//Test to make sure that all the files were saved
		//and saved file size has the default resolution
		Size[] availableSizes = mActivity.getAvailableSizes();
		int[] resolution =  ExifInfo();
		assertEquals(availableSizes[0].width, resolution[0]);
		assertEquals(availableSizes[0].height, resolution[1]);
		
	}
	
	/**
	 * Test to verify all the available resolution can be set and capture.
	 * Snapshot from each resolution is verify by the size information from exif.
	 */
	public final void testResolutionChange(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
		assertTrue(CameraInfo.CAMERA_MODE_AUTO.equalsIgnoreCase(mActivity.getCurrentActiveMode()));
		assertNull(mActivity.getAutofocusCallback());
	
		Size[] availableSizes = mActivity.getAvailableSizes();
		assertEquals(availableSizes[0].height, mActivity.getCurrentResolution().height);
		assertEquals(availableSizes[0].width, mActivity.getCurrentResolution().width);

		
		for(Size s : availableSizes){
			Log.i(TAG,"testing resolution : ("+ s.width + ", "+ s.height+")");
			mActivity.onResolutionSelected(s, 0);
			
			assertEquals(s.height, mActivity.getCurrentResolution().height);
			assertEquals(s.width, mActivity.getCurrentResolution().width);
			
			try {
				runTestOnUiThread(new Runnable() {
					public void run() {
						mActivity.mShutterButton.requestFocus();
						mActivity.getShutterButton().performClick();						
					}
				});
							
			} catch (Throwable e) {
				fail("Click Shutter Button failed in resolution change test");
				e.printStackTrace();
			}finally{
				
			}
			getInstrumentation().waitForIdleSync();
			
			mActivity.setCountDownLatch(4);
			try {
				final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
				assertTrue(didNotTimedOut);

			} catch (InterruptedException e) {
				fail("Interrupted Exception in resolution change test");
				e.printStackTrace();
			}
			copyfile("testRes_"+ s.width +"x" + s.height + ".jpg");
			int[] resolution =  ExifInfo();
			assertEquals(s.width, resolution[0]);
			assertEquals(s.height, resolution[1]);
			
			assertEquals(s.height, mActivity.getCurrentResolution().height);
			assertEquals(s.width, mActivity.getCurrentResolution().width);
			
		}

	}

	/**
	 * Test to see to what level of zoom a preview resolution can handle before it crashes the 
	 * tablet. Modify the preview resolution in {@link com.aptina.camera.CameraInfo.MIN_PREVIEW_RESOLUTIONS}
	 */
	public final void testZoomCapture(){
		mActivity.runningTest = true;
		
		final ZoomControl zc = (ZoomControl) mActivity.findViewById(R.id.zoom_layout);	
		Camera mCamera = mActivity.getCamera();
		assertTrue(mActivity.runningTest);
		assertTrue(CameraInfo.CAMERA_MODE_AUTO.equalsIgnoreCase(mActivity.getCurrentActiveMode()));
		assertNull(mActivity.getAutofocusCallback());
		
		Size[] availableSizes = mActivity.getAvailableSizes();
		assertEquals(availableSizes[0].height, mActivity.getCurrentResolution().height);
		assertEquals(availableSizes[0].width, mActivity.getCurrentResolution().width);
	
		int max_zoom = mCamera.getParameters().getMaxZoom();
		Log.i(TAG,  "max_zoom : " + max_zoom);
		
		
		
		int curZoom = mCamera.getParameters().getZoom();
		assertEquals(0,curZoom);
	
		for(int i = 0; i < max_zoom; i += 5){
			Log.i(TAG,"testing with resolution : ("+ availableSizes[0].width + ", "+ availableSizes[0].height+")");
			Log.i(TAG, "testing with zoom : " + i);
			final int idx = i;
		
			try {
				runTestOnUiThread(new Runnable() {
					public void run() {
						zc.setZoom(idx);
						mActivity.mShutterButton.requestFocus();
						mActivity.getShutterButton().performClick();						
					}
				});
							
			} catch (Throwable e) {
				fail("Click Shutter Button failed in resolution change test");
				e.printStackTrace();
			}finally{
				
			}
			getInstrumentation().waitForIdleSync();
			
			mActivity.setCountDownLatch(4);
			try {
				final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
				assertTrue(didNotTimedOut);

			} catch (InterruptedException e) {
				fail("Interrupted Exception in resolution change test");
				e.printStackTrace();
			}
		}

	}
	/**
	 * Test to verify application can switch to front face camera and back to back camera.
	 * Resolution is verify in each case. Snapshot in each case is also verify by the
	 * size information in exif.
	 */
	public final void testSwitchingfrontbackcamera(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
		assertTrue(CameraInfo.CAMERA_MODE_AUTO.equalsIgnoreCase(mActivity.getCurrentActiveMode()));
		assertNull(mActivity.getAutofocusCallback());
		
		int backCameraHeight = mActivity.getCurrentResolution().height;
		int backCameraWidth = mActivity.getCurrentResolution().width;		
		
		//Switch to front camera and take a snapshot
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mActivity.getCameraChanger().requestFocus();
					mActivity.getCameraChanger().performClick();
					mActivity.mShutterButton.requestFocus();
					mActivity.getShutterButton().performClick();
				}
			});
			
			
		} catch (Throwable e) {
			fail("Running click on UI failed in Switch Camera test");
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
		
		mActivity.setCountDownLatch(4);
		try {
			final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
			assertTrue(didNotTimedOut);

		} catch (InterruptedException e) {
			fail("Interrupted Exception in Switch Camera test");
			e.printStackTrace();
		}
		
		copyfile("testFrontCamera.jpg");
		
		Size[] availableSizes = mActivity.getAvailableSizes();
		//Front Camera has default to lowest resolution
		int currentSizeIndex = availableSizes.length-1;

		assertEquals(availableSizes[currentSizeIndex].height, mActivity.getCurrentResolution().height);
		assertEquals(availableSizes[currentSizeIndex].width, mActivity.getCurrentResolution().width);
				
		int[] resolution =  ExifInfo();
		assertEquals(mActivity.getCurrentResolution().width, resolution[0]);
		assertEquals(mActivity.getCurrentResolution().height, resolution[1]);
		
		//Switch to back camera and take a snapshot
		try {
			runTestOnUiThread(new Runnable() {
			public void run() {
					mActivity.getCameraChanger().requestFocus();
					mActivity.getCameraChanger().performClick();
					mActivity.mShutterButton.requestFocus();
					mActivity.getShutterButton().performClick();											
						}
					});				
					
			} catch (Throwable e) {
				fail("Running click on UI failed in Switch Camera test");
			e.printStackTrace();
			}finally{
					
			}
		getInstrumentation().waitForIdleSync();
		
		mActivity.setCountDownLatch(4);
		try {
			final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
			assertTrue(didNotTimedOut);

		} catch (InterruptedException e) {
			fail("Interrupted Exception in Switch Camera test");
			e.printStackTrace();
		}
		
		copyfile("testBackCamera.jpg");
		
		assertEquals(backCameraHeight, mActivity.getCurrentResolution().height);
		assertEquals(backCameraWidth, mActivity.getCurrentResolution().width);
		
		int[] resolution1 =  ExifInfo();
		assertEquals(mActivity.getCurrentResolution().width, resolution1[0]);
		assertEquals(mActivity.getCurrentResolution().height, resolution1[1]);
		
	}

	/**
	 * Test to verify AUTO mode is set correctly when selected.
	 * Take a snapshot and verify a image file of default size is saved.
	 * Snapshot file should also be verify by the user.
	 */
	public final void testAutoMode(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
		
		solo.scrollToSide(solo.LEFT);
		solo.clickOnText("Auto");
		solo.clickOnText("Auto");
		assertTrue(CameraInfo.CAMERA_MODE_AUTO.equalsIgnoreCase(mActivity.getCurrentActiveMode()));
		
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mActivity.mShutterButton.requestFocus();
					mActivity.getShutterButton().performClick();					
				}
			});		
			
		} catch (Throwable e) {
			fail("Running click on Shutter button failed in Auto Mode test");
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
		
		assertFalse(mActivity.isPreviewing());
		assertTrue(mActivity.isSDCardAvailable());
		assertFalse(mActivity.getShutterButton().isClickable());
		
		assertNotNull(mActivity.getAutofocusCallback());
		
		assertNotNull(mActivity.getShutterCallback());
		mActivity.setCountDownLatch(3);
		try {
			final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
			assertTrue(didNotTimedOut);

		} catch (InterruptedException e) {
			fail("Interrupted Exception in Auto mode test");
			e.printStackTrace();
		}
		
		copyfile("testAutoMode.jpg");
		
		Size[] availableSizes = mActivity.getAvailableSizes();
		int[] resolution =  ExifInfo();

//		Log.i(TAG, "exif (w,h ) : " + resolution[1] + "x" + resolution[0]);
		assertEquals(availableSizes[0].width, resolution[0]);
		assertEquals(availableSizes[0].height, resolution[1]);
	}

	/**
	 * Reference the code in FocusMenuFuctionalTests
	 * Test to verify Focus modes are set correctly from the focus options available.
	 * Take a snapshot and verify a image file of default size is saved.
	 * Snapshot files should also be verify by the user for the correct focus.
	 */
	public final void testFocusOptions(){
		//Tell the activity that countdown latches will be used
		mActivity.runningTest = true;
		//Test that the activity is in test mode
		assertTrue(mActivity.runningTest);
		//Check that the focus is at the default when starting
		assertTrue(mActivity.getDefaultFocusMode().equalsIgnoreCase(mActivity.getCamera().getParameters().getFocusMode()));
		
		//Get and test camera
		mCamera = mActivity.getCamera();
		assertNotNull(mCamera);
		
		
		focus_options = getFocusOptionsButton();
		for(int k = 0; k < focus_options.length; k++){
			Log.w(TAG, "focus : " + focus_options[k]);
		}
		//Variable for counting
		int i = 0;
		for(String mode : focus_options){
			mOptionsButton = mActivity.getOptionsButton();
			//click on the focus menu
			final int ii=i;
			
			try {//Open the menu, select the focus, close the menu
				runTestOnUiThread(new Runnable() {
				public void run() {
					//open
					mOptionsButton.requestFocus();
					mOptionsButton.performClick();
					
					//select
					mActivity.getOptionsMenu().setFocus(ii);
					
					//close
					mOptionsButton.requestFocus();
					mOptionsButton.performClick();
				}
			});
			
			
			} catch (Throwable e) {
				fail("Running click on UI in Focus Options test failed");
				e.printStackTrace();
			}finally{
			
			}
			
			//wait for UI
			getInstrumentation().waitForIdleSync();
			//Verify the correct focus mode was set
			assertEquals(mode, mCamera.getParameters().getFocusMode());
			try {//Take snapshot
				runTestOnUiThread(new Runnable() {
					public void run() {
						mActivity.getShutterButton().requestFocus();
						mActivity.getShutterButton().performClick();
					}
				});			
			} catch (Throwable e) {
				fail("Running click on UI in Focus Options test failed");
				e.printStackTrace();
			}finally{}
			//wait
			getInstrumentation().waitForIdleSync();
			
			//set latches for AsyncTask SavePhotoAction
			mActivity.setCountDownLatch(4);
			try {
				//Test
				final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
				assertTrue(didNotTimedOut);

			} catch (InterruptedException e) {
				fail("Interrupted Exception in Focus Options test");
				e.printStackTrace();
			}
			copyfile("testFocusMode_" + mode + ".jpg");
			i++;
	
		}
	}
	/**
	 * For focus options test
	 * Click the option button 
	 * @return
	 */
	private final String[] getFocusOptionsButton(){
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mOptionsButton.requestFocus();
					mOptionsButton.performClick();//open options
//					mOptionsButton.performClick();//close options
				}
			});
			
			
		} catch (Throwable e) {
			fail("Running click on Focus Options button failed");
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
		String[] modes = mActivity.getFocusOptionsArray();
		assertNotNull(modes);
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mOptionsButton.requestFocus();
					mOptionsButton.performClick();//close options
				}
			});
			
			
		} catch (Throwable e) {
			fail("Running click on Focus Options button failed");
			e.printStackTrace();
		}finally{
			
		}
		return modes;
	}
	
	public final void testResetCamera(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
		assertTrue(CameraInfo.FOCUS_CONTINUOUS_PICTURE.equalsIgnoreCase(mActivity.getCamera().getParameters().getFocusMode()));
		
		mCamera = mActivity.getCamera();
		mOptionsButton = mActivity.getOptionsButton();
		solo.scrollToSide(solo.LEFT);
		solo.clickOnButton("Options");
		//Click the second focus mode, we know this is not the default
		solo.clickOnRadioButton(1);
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mOptionsButton.requestFocus();
					mOptionsButton.performClick();			
				}
			});
			
			
		} catch (Throwable e) {
			fail("Running click on Focus Options button failed");
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
	
		
		assertFalse(CameraInfo.FOCUS_CONTINUOUS_PICTURE.equalsIgnoreCase(mActivity.getCamera().getParameters().getFocusMode()));
		
		final Size[] availableSizes = mActivity.getAvailableSizes();
	
		try {
			runTestOnUiThread(new Runnable() {
			public void run() {
				//Set to resolution other than availabeSizes[0]
				mActivity.onResolutionSelected(availableSizes[1], 0);
				mActivity.mShutterButton.requestFocus();
				mActivity.getShutterButton().performClick();
				
			}
		});
		
		
		} catch (Throwable e) {
			fail("Running click on Reset or Shutter button failed in Reset Camera test");
			e.printStackTrace();
		}finally{
		
		}
		getInstrumentation().waitForIdleSync();
		mActivity.setCountDownLatch(4);
		try {
			final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
			assertTrue(didNotTimedOut);

		} catch (InterruptedException e) {
			fail("Interrupted Exception in Reset Camera test");
			e.printStackTrace();
		}
		
		//Verify file was saved
		copyfile("testBeforeReset.jpg");
		int[] resolution =  ExifInfo();
		assertEquals(availableSizes[1].width, resolution[0]);
		assertEquals(availableSizes[1].height, resolution[1]);
		
		//Now press reset button and take a snapshot
		try {
			runTestOnUiThread(new Runnable() {
			public void run() {
				mActivity.getResetButton().requestFocus();
				mActivity.getResetButton().performClick();
				mActivity.mShutterButton.requestFocus();
				mActivity.getShutterButton().performClick();
				
			}
		});
		
		
		} catch (Throwable e) {
			fail("Running click on Reset or Shutter button failed in Reset Camera test");
			e.printStackTrace();
		}finally{
		
		}
		getInstrumentation().waitForIdleSync();
		mActivity.setCountDownLatch(4);
		try {
			final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
			assertTrue(didNotTimedOut);

		} catch (InterruptedException e) {
			fail("Interrupted Exception in Reset Camera test");
			e.printStackTrace();
		}
		
		copyfile("testAfterReset.jpg");
		int[] resolution1 =  ExifInfo();
		assertEquals(availableSizes[0].width, resolution1[0]);
		assertEquals(availableSizes[0].height, resolution1[1]);
		
		assertTrue(CameraInfo.FOCUS_CONTINUOUS_PICTURE.equalsIgnoreCase(mActivity.getCamera().getParameters().getFocusMode()));
	}
	
	/**
	 * Test to verify still camera switch to video successfully.
	 * Verify the video activity flag when in video mode.
	 */
	public final void testSwitchToVideo(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
		assertTrue(CameraInfo.FOCUS_CONTINUOUS_PICTURE.equalsIgnoreCase(mActivity.getCamera().getParameters().getFocusMode()));
		
		
		 try {
	            runTestOnUiThread(new Runnable() {
	                public void run() {
	                	mActivity.getSwitchButton().requestFocus();
	                    mActivity.getSwitchButton().performClick();    
	                        
	                }
	            });
	            
	            
	        } catch (Throwable e) {
	            fail("Running click on Switch button failed");
	            e.printStackTrace();
	        }finally{
	            
	        }
		 	
	        getInstrumentation().waitForIdleSync();
			assertTrue(mActivity.inVideoActivity());
			
			
	}
	
	/**
	 * Test to verify ZSL mode is set correctly when selected.
	 * Take a snapshot and verify a image file of default size is saved.
	 * Verify Camera is set to Infinity focus mode.
	 * Snapshot file should also be verify by the user.
	 */
	public final void testZSLMode(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
		
		if (CameraInfo.sIsDeviceOMAP4Blaze == false)
			return;
		
		
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mActivity.onModeSelected(CameraInfo.CAMERA_MODE_ZSL);
					mActivity.mShutterButton.requestFocus();
					mActivity.getShutterButton().performClick();
					
				}
			});		
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
		mActivity.setCountDownLatch(4);
		try {
			final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
			assertTrue(didNotTimedOut);

		} catch (InterruptedException e) {
			fail("Interrupted Exception in ZSL Mode test");
			e.printStackTrace();
		}
		
		copyfile("testZSLMode.jpg");
		
		Size[] availableSizes = mActivity.getAvailableSizes();
		int[] resolution =  ExifInfo();

//		Log.i(TAG, "exif (w,h ) : " + resolution[1] + "x" + resolution[0]);
		assertEquals(availableSizes[0].width, resolution[0]);
		assertEquals(availableSizes[0].height, resolution[1]);
		assertTrue(CameraInfo.CAMERA_MODE_ZSL.equalsIgnoreCase(mActivity.getCurrentActiveMode()));
		assertTrue(CameraInfo.FOCUS_INFINITY.equalsIgnoreCase(mActivity.getCamera().getParameters().getFocusMode()));
		
	}
	
	/**
	 * Test to verify Zoom Slider operation modes are setting correctly when selected.
	 * Simulate touch and move slider events from no zoom to maximum zoom
	 * Verify the zoom value has increased for every step size of 1/4 of maximum zoom.
	 */
	public final void testZoomSliderModes(){
	
		ZoomControl zc = (ZoomControl) mActivity.findViewById(R.id.zoom_layout);	
		Camera mCamera = mActivity.getCamera();
	
		float totalzoompoints = zc.getTotalZoomPoints();
		float widthbeforeslider = (float)(zc.getWidth()*zc.getWidthBeforeFirstPointPercent());
	    float eachpointwidth = (float)(zc.getWidth()*zc.getZoomSliderPointWidthPercent());
	    float ZoomSliderXStart = (float) (zc.getLeft() + widthbeforeslider);
		
		//start the touch on the middle position of the first point of the slider
		float xstart = (float) (ZoomSliderXStart + eachpointwidth/2);
		float ystart = zc.getBottom() - zc.getHeight()/3;
		
		int curZoom = mCamera.getParameters().getZoom();
		assertEquals(0,curZoom);
		
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		Instrumentation inst = getInstrumentation();
		
		MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xstart, ystart, 0);
		inst.sendPointerSync(event);

		float quartersteppoints = totalzoompoints/4;
		int prevZoomvalue = mCamera.getParameters().getZoom();
		
		float xcord = xstart + eachpointwidth*(quartersteppoints-1);
		float ycord = ystart;
		
		//Verify every quarter step of zoom
		for (float i=0;i<totalzoompoints; i=i+quartersteppoints) {
			
			eventTime = SystemClock.uptimeMillis()+3000;
			event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xcord, ycord, 0);
			inst.sendPointerSync(event);
			
			eventTime = SystemClock.uptimeMillis();
			event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xcord, ycord, 0);
			inst.sendPointerSync(event);
			
			assertTrue(prevZoomvalue<mCamera.getParameters().getZoom());
			prevZoomvalue = mCamera.getParameters().getZoom();			
			xcord = xcord + eachpointwidth*quartersteppoints;
			if (xcord > ((totalzoompoints*eachpointwidth) + ZoomSliderXStart)){
				break;
			}
			eventTime = SystemClock.uptimeMillis();
			event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xcord, ycord, 0);
			inst.sendPointerSync(event);
			
		}
		
		xcord = xcord - eachpointwidth*quartersteppoints;
		
		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xcord, ycord, 0);
		inst.sendPointerSync(event);
		
		xcord = xcord - eachpointwidth*quartersteppoints*2;
		eventTime = SystemClock.uptimeMillis()+3000;
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xcord, ycord, 0);
		inst.sendPointerSync(event);
		
		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xcord, ycord, 0);
		inst.sendPointerSync(event);
		
		//Verify the zoom value has decreased when zoom slider set backward to the left
		assertTrue(mCamera.getParameters().getZoom()<prevZoomvalue);
		
	}
	/**
	 * Test to verify Camera Activity pause and resume normally.
	 * Set the resolution of snapshot to a size other than default
	 * Perform a pause and resume.
	 * Verify the camera can resume normally with the snapshot resolution unchanged.
	 */
	public final void testPaueApp(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
		
		//mActivity.onPause();
		final Size[] availableSizes = mActivity.getAvailableSizes();
		try {
			runTestOnUiThread(new Runnable() {
			public void run() {
				//Set to resolution other than availabeSizes[0](default size).
				mActivity.onResolutionSelected(availableSizes[1], 0);
				mActivity.mShutterButton.requestFocus();
				mActivity.getShutterButton().performClick();				
			}
		});
		
		} catch (Throwable e) {
			fail("Running click on shutter button in pause app test failed");
			e.printStackTrace();
		}finally{
		
		}
		getInstrumentation().waitForIdleSync();
		mActivity.setCountDownLatch(4);
		try {
			final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
			assertTrue(didNotTimedOut);

		} catch (InterruptedException e) {
			fail("Interrupted Exception in Pause Application test");
			e.printStackTrace();
		}
		
		copyfile("testPauseCamera.jpg");
		
		mActivity.onPause();
		mActivity.onResume();
		assertEquals(availableSizes[1].width, mActivity.getCurrentResolution().width);
		assertEquals(availableSizes[1].height,mActivity.getCurrentResolution().height);

	}
	/**
	 * Test to verify Camera can set to ROI focus correctly.
	 * ROI focus mode is currently not list in the focus option
	 * So user has to verify the focus is correct by verify the snapshots.
	 * Place objects in right and left of image with different distance from the camera.
	 * Double tap left screen and right screen, take a snapshot in each case.
	 */
	public final void testROIFocus(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
		assertTrue(CameraInfo.FOCUS_CONTINUOUS_PICTURE.equalsIgnoreCase(mActivity.getCamera().getParameters().getFocusMode()));
		
		RelativeLayout main = (RelativeLayout) mActivity.findViewById(R.id.main_camera_frame);
		float quarterwidth = (float) (main.getWidth()/4);
		float midheight = (float) (main.getHeight()/2);
		
		Point lefttouchcords = new Point();
		lefttouchcords.x = (int) quarterwidth;
		lefttouchcords.y = (int) midheight;
		
		doubletap(lefttouchcords);
		clickShutterButton();
			 mActivity.setCountDownLatch(2);
			 try {
					mActivity.signal.await(5, TimeUnit.SECONDS);
	
			} catch (InterruptedException e) {
				fail("Interrupted Exception in ROI Focus test");
					e.printStackTrace();
				}
			 
		copyfile("testROIFoucs_lefttouch.jpg");
			 
		 Point righttouchcords = new Point();
		 righttouchcords.x = (int) quarterwidth*3;
		 righttouchcords.y = (int) midheight;
		
		 doubletap(righttouchcords);
		 clickShutterButton();
		 mActivity.setCountDownLatch(2);
		 try {
			 final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
			 assertTrue(didNotTimedOut);	
		 } catch (InterruptedException e) {
			 fail("Interrupted Exception in ROI Focus test");
			 e.printStackTrace();
		 }
			 
		 copyfile("testROIFoucs_righttouch.jpg");

	}
	
	/**
	 * Test the gallery thumbnail icon. Should launch the {@link ImageDetailActivity} 
	 * with the correct jpeg selected
	 */
	public final void testGalleryThumb(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
//		assertTrue(CameraInfo.FOCUS_CONTINUOUS_PICTURE.equalsIgnoreCase(mActivity.getCamera().getParameters().getFocusMode())
//				|| CameraInfo.FOCUS_AUTO.equalsIgnoreCase(mActivity.getCamera().getParameters().getFocusMode()));
		clickGalleryViewGroup();

	}
	/**
	 * Test to verify Burst Capture.
	 * Test is selecting Burst Capture mode and perform a capture
	 * Verify no. of burst snapshot is equal to default (6 of now, may change later).
	 * Set to Auto mode after burst capture.
	 * Verify camera is back to auto mode and can do snapshot normally.
	 * User also need to verify the burst captured images that saved.
	 */
//	public final void testBurstCapture(){
//		mActivity.runningTest = true;
//		assertTrue(mActivity.runningTest);
//		
//		assertTrue(CameraInfo.CAMERA_MODE_AUTO.equalsIgnoreCase(mActivity.getCurrentActiveMode()));
//		final int DefaultBurstCount = 6;
//		int waittime = CAPTURE_TIMEOUT_TIME* DefaultBurstCount;
//		
//		try {
//			runTestOnUiThread(new Runnable() {
//				public void run() {
//					mActivity.onModeSelected(CameraInfo.CAMERA_MODE_BURST);
//					mActivity.mShutterButton.requestFocus();
//					mActivity.getShutterButton().performClick();
//					
//				}
//			});		
//			
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			
//		}
//		getInstrumentation().waitForIdleSync();
//		mActivity.setCountDownLatch(4);
//		try {
//			final boolean didNotTimedOut = mActivity.signal.await(waittime, TimeUnit.SECONDS);
//			assertTrue(didNotTimedOut);
//
//		} catch (InterruptedException e) {
//			fail("Interrupted Exception in Burst Capture test");
//			e.printStackTrace();
//		}
//		assertTrue(CameraInfo.CAMERA_MODE_BURST.equalsIgnoreCase(mActivity.getCurrentActiveMode()));
//		assertTrue(mActivity.TotalBurstCount()==DefaultBurstCount);
//		
//		try {
//			runTestOnUiThread(new Runnable() {
//				public void run() {
//					mActivity.onModeSelected(CameraInfo.CAMERA_MODE_AUTO);
//					mActivity.mShutterButton.requestFocus();
//					mActivity.getShutterButton().performClick();
//					
//				}
//			});		
//			
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			
//		}
//		getInstrumentation().waitForIdleSync();
//		mActivity.setCountDownLatch(4);
//		try {
//			final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
//			assertTrue(didNotTimedOut);
//
//		} catch (InterruptedException e) {
//			fail("Interrupted Exception in Burst Capture test");
//			e.printStackTrace();
//		}
//		assertTrue(CameraInfo.CAMERA_MODE_AUTO.equalsIgnoreCase(mActivity.getCurrentActiveMode()));
//		assertTrue(mActivity.TotalBurstCount()==0);
//
//		
//	}
	
	/**
	 * Test to verify Face Shutter
	 * Use the circle clockwise gesture to enable face shutter  
	 * and verify camera is in Face Shutter mode.
	 * Verify a snapshot take place when camera project to a image with a face
	 * Test is expected to be failed if there is no face in the image.
	 * User also need to verify the snapshot after the test ran.
	 */
	public final void testFaceShutter(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
		
		if (CameraInfo.sIsDeviceOMAP4Blaze == false)
			return;

		
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mActivity.getGestureInterface().OnCircleEvent(Path.Direction.CW);
					assertTrue(CameraInfo.FACE_SHUTTER.equalsIgnoreCase(mActivity.getCurrentActiveMode()));
					
				}
			});		
			
		} catch (Throwable e) {
			fail("Fails to wait for face shutter");
			e.printStackTrace();
		}finally{
			
		}
		
		 mActivity.setCountDownLatch(3);
		 try {
				final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
				assertTrue(didNotTimedOut);

			} catch (InterruptedException e) {
			fail("Interrupted Exception in Face Shutter test");
				e.printStackTrace();
			}
		
		
		copyfile("testFaceShutter.jpg");
	}
	
	/**
	 * Test to verify Face Shutter
	 * Use the circle counter clockwise gesture to enable smile shutter  
	 * the and verify camera is in Smile Shutter mode.
	 * Verify a snapshot take place when camera project to a image with a smiling face
	 * Test is expected to be failed if there is no smiling face in the image.
	 * User also need to verify the snapshot after the test ran.
	 */
	public final void testSmileShutter(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
		
		if (CameraInfo.sIsDeviceOMAP4Blaze == false)
			return;
		
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mActivity.getGestureInterface().OnCircleEvent(Path.Direction.CCW);
					assertTrue(CameraInfo.SMILE_SHUTTER.equalsIgnoreCase(mActivity.getCurrentActiveMode()));
					
				}
			});		
			
		} catch (Throwable e) {
			fail("Fails to wait for smile shutter");
			e.printStackTrace();
		}finally{
			
		}
		
		 mActivity.setCountDownLatch(3);
		 try {
				final boolean didNotTimedOut = mActivity.signal.await(CAPTURE_TIMEOUT_TIME, TimeUnit.SECONDS);
				assertTrue(didNotTimedOut);

			} catch (InterruptedException e) {
			fail("Interrupted Exception in Smile Shutter test");
				e.printStackTrace();
			}
		
		
		copyfile("testSmileShutter.jpg");
	}
	
	/*Test to verify Histogram can be enabled and disabled 
	 * 
	 */
	public final void testHistogramOnOff() {
		solo.assertCurrentActivity("Expected Still Camera Activity", "CameraActivity");
		checkHistogram("still mode");
		solo.clickOnButton("Switch");
		solo.sleep(1000);
		solo.assertCurrentActivity("Expected Video Activity", "VideoActivity");
		checkHistogram("video mode");
	}
	
	/*Verify Histogram can be scroll to the 3 different histogram
	 *and display correctly
	 */
	public final void testHistogram(){
		solo.getCurrentActivity();
		boolean found = mActivity.getFragmentManager().findFragmentById(R.id.histogram_viewpager) != null;
		assertTrue("RGB Histrogram is not found", found);
		ViewPager  histogram= (ViewPager) solo.getView(R.id.histogram_viewpager);
		
		if (histogram.getVisibility() == View.INVISIBLE) {
			solo.scrollToSide(Solo.LEFT);
			solo.clickOnText("Options");
			solo.sleep(1000);
			found = solo.searchText("Histogram");
			assertTrue(found);
			solo.clickOnRadioButton(1);
			solo.clickOnText("Options");
			solo.sleep(4000);
		}
		solo.takeScreenshot("RGBHistogram");
		
		float right_edge = histogram.getRight();
		float left_edge = histogram.getLeft();
		float top = histogram.getTop();
		float bottom = histogram.getBottom();
		float center_y = top + (bottom - top) /2;
		float width = histogram.getWidth();
		float x_start = right_edge - width/10;
		float x_end = left_edge;
		fling(x_start, x_end, center_y, center_y);
		solo.sleep(2000);
		solo.takeScreenshot("RedHistogram");
		
		List<Fragment> mHistFrags;     	
       	mHistFrags =  HistogramRGBData.getInstance().getFragList();
       	int i = histogram.getCurrentItem();
       	HistogramFrag current_frag = (HistogramFrag) (mHistFrags.get(i));
       	String title = current_frag.getHistogramTitle();
       	assertEquals("Red Histogram is not display", "Red Histogram", title);
       	
		fling(x_start, x_end, center_y, center_y);
		solo.sleep(2000);
		solo.takeScreenshot("GreenHistogram");
		i = histogram.getCurrentItem();
		current_frag = (HistogramFrag) (mHistFrags.get(i));
		title = current_frag.getHistogramTitle();
		assertEquals("Green Histogram is not display", "Green Histogram", title);  
       	
		fling(x_start, x_end, center_y, center_y);
		solo.sleep(2000);
		solo.takeScreenshot("BlueHistogram");
		i = histogram.getCurrentItem();
		current_frag = (HistogramFrag) (mHistFrags.get(i));
		title = current_frag.getHistogramTitle();
		assertEquals("Blue Histogram is not display", "Blue Histogram", title);  
 
     
	}
	
	private void checkHistogram(String mode) {
		solo.scrollToSide(solo.LEFT);
		solo.clickOnText("Options");
		solo.sleep(1000);
		
		//The options menu is covered by the histogram in smaller device
		//so there different ways to find the radio buttons.
		if (CameraInfo.sIsDeviceOMAP4Blaze){
			boolean found = solo.searchText("Histogram");
			assertEquals("Histogram Options not found in " + mode,found, true);
			if (mode== "still mode") 
				solo.clickOnRadioButton(2);
			else
				solo.searchText("Invisible");
				solo.clickOnRadioButton(0);
			solo.clickOnText("Options");
            
			ViewPager  histogram= (ViewPager) solo.getView(R.id.histogram_viewpager);
			int visibility = histogram.getVisibility();
			solo.takeScreenshot("Histogram Off in " + mode);
			assertEquals("Histrogram is not off in " + mode, visibility, View.INVISIBLE);
			solo.clickLongOnText("Options");
            
			if (mode== "video mode") {
				solo.scrollUp();
				found = solo.searchText("Histogram");
				assertEquals("Histogram Options not found in " + mode,found, true);
			}
			
			solo.clickOnRadioButton(1);

			solo.sleep(1000);
			//click on options button (it will not work with other click action except "clickOnImage" here
			solo.clickOnImage(3);
            
			histogram= (ViewPager) solo.getView(R.id.histogram_viewpager);
			visibility = histogram.getVisibility();
			solo.takeScreenshot("Histogram On in " + mode);
			assertEquals("Histrogram is not On in " + mode, visibility, View.VISIBLE);
		}
		else {
			//Check Off
			boolean found = solo.searchText("Invisible");
	
			if (mode == "still mode") {
				assertEquals("Histogram Options not found in " + mode,found, true);
				solo.clickOnRadioButton(2);
			}	
			else {
	//			need to scroll more to see the Histogram options
				found = solo.searchText("Manual");
				assertEquals("Histogram Options not found in " + mode,found, true);
				solo.clickOnRadioButton(0);
			}
			solo.clickLongOnText("Options");
			solo.sleep(1000);
			ViewPager  histogram= (ViewPager) solo.getView(R.id.histogram_viewpager);
			int visibility = histogram.getVisibility();
			solo.takeScreenshot("Histogram Off in " + mode);
			assertEquals("Histrogram is not off in " + mode, visibility, View.INVISIBLE);
			
			//Check On
			solo.clickLongOnText("Options");
			solo.sleep(1000);
			if (mode == "still mode") {
					found = solo.searchText("Histogram");
					assertEquals("Histogram Options not found in " + mode,found, true);
					solo.clickOnRadioButton(1);
				}	
			else {
		//			need to scroll more to see the Histogram options
					solo.scrollUp();
					solo.clickOnRadioButton(1);
					//skip pressing iptions in video mode for now, the button could not be found in smaller device.
					return;
			}
			solo.clickLongOnText("Options");
			solo.sleep(1000);
			visibility = histogram.getVisibility();
			solo.takeScreenshot("Histogram On in " + mode);
			assertEquals("Histrogram is not off in " + mode, visibility, View.VISIBLE);
		}
	}
	private void fling(float fromX, float toX, float fromY,
	        float toY) {
	    Instrumentation inst = getInstrumentation();

	    long downTime = SystemClock.uptimeMillis();
	    long eventTime = SystemClock.uptimeMillis();
	    MotionEvent event;
   
	 // specify the property for the touch point
	 		PointerProperties[] properties = new PointerProperties[1];
	 		PointerProperties pp1 = new PointerProperties();
	 		pp1.id = 0;
	 		pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;

	 		properties[0] = pp1;
	 		
	 		//specify the coordinations of the touch point
	 		//NOTE: you MUST set the pressure and size value, or it doesn't work
	 		PointerCoords[] pointerCoords = new PointerCoords[1];
	 		PointerCoords pc1 = new PointerCoords();
	 		pc1.x = fromX;
	 		pc1.y = fromY;
	 		pc1.pressure = 1;
	 		pc1.size = 3;

	 		pointerCoords[0] = pc1;
	    
	  //Performs a touch down, move and then up to simulate a fling on the screen. 
	  		eventTime = SystemClock.uptimeMillis();
	  		event = MotionEvent.obtain(downTime, eventTime, 
	  	            MotionEvent.ACTION_DOWN, 1, properties, 
	  	            pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );
	  		inst.sendPointerSync(event);
	  		
	  		pc1.x = toX;
	 		pc1.y = toY;
	 		pc1.pressure = 1;
	 		pc1.size = 3;

	 		pointerCoords[0] = pc1;
	  		eventTime = SystemClock.uptimeMillis();
	  		event = MotionEvent.obtain(downTime, eventTime, 
	  	            MotionEvent.ACTION_MOVE, 1, properties, 
	  	            pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );
	  		inst.sendPointerSync(event);
	  		eventTime = SystemClock.uptimeMillis();
	  		event = MotionEvent.obtain(downTime, eventTime, 
	  	            MotionEvent.ACTION_UP, 1, properties, 
	  	            pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );
	  		inst.sendPointerSync(event);
	  		
	}

	
	/**
	 * Click on the gallery view group {@link ThumbnailControl}, since
	 * the view group is a framelayout, we have to set the click listener to the
	 * imageview, and we need to perform the click on the imageview as well
	 */
	private final void clickGalleryViewGroup(){
		 try {
			 runTestOnUiThread(new Runnable() {
				 public void run() {
					 mActivity.getGalleryButton().requestFocus();
	                 boolean b = ((ThumbnailControl)mActivity.getGalleryButton()).getGalleryImg().performClick();    
//	                 Log.i(TAG, "gallery listener set: " + b);
	                 
	             }
			  });
		 } catch (Throwable e) {
			 fail("Running click on Gallery button failed");
	         e.printStackTrace();
	     }
	     getInstrumentation().waitForIdleSync();
	     
	}
	/**
     * For double tap on the screen 
     */
	private final void doubletap(Point cords){
		
		// specify the property for the touch point
		PointerProperties[] properties = new PointerProperties[1];
		PointerProperties pp1 = new PointerProperties();
		pp1.id = 0;
		pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;

		properties[0] = pp1;
		
		//specify the coordinations of the touch point
		//NOTE: you MUST set the pressure and size value, or it doesn't work
		PointerCoords[] pointerCoords = new PointerCoords[1];
		PointerCoords pc1 = new PointerCoords();
		pc1.x = cords.x;
		pc1.y = cords.y;
		pc1.pressure = 2;
		pc1.size = 2;

		pointerCoords[0] = pc1;

		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		Instrumentation inst = getInstrumentation();
		MotionEvent event;
		
		//Performs a touch down-up twice on the same point to simulate a double tap on screen. 
		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime, 
	            MotionEvent.ACTION_DOWN, 1, properties, 
	            pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );
		inst.sendPointerSync(event);
		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime, 
	            MotionEvent.ACTION_UP, 1, properties, 
	            pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );
		inst.sendPointerSync(event);
		eventTime = SystemClock.uptimeMillis()+400;
		event = MotionEvent.obtain(downTime, eventTime, 
	            MotionEvent.ACTION_DOWN, 1, properties, 
	            pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );
		inst.sendPointerSync(event);
		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime, 
	            MotionEvent.ACTION_UP, 1, properties, 
	            pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );
		inst.sendPointerSync(event);
	}
	
    /**
     * For taking a snapshot 
     * Click the Shutter button
     */
    private final void clickShutterButton(){
        try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    mActivity.mShutterButton.requestFocus();
                    mActivity.getShutterButton().performClick();            
                }
            });
            
            
        } catch (Throwable e) {
            fail("Running click on Shutter button failed");
            e.printStackTrace();
        }finally{
            
        }
        getInstrumentation().waitForIdleSync();
        
    }
    
    /**
     * Getting Inform from EXIF
     * Return width and height of image file
     * @return
     */
    private int[] ExifInfo(){
        
        String savedFileName = mActivity.getSavedFileName();
        assertNotNull(savedFileName);
        
        int size[] = new int[2];
        
        try {
            ExifInterface exif = new ExifInterface(savedFileName);
            String length = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            String width = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            size [0]= Integer.parseInt(width);
            size [1]= Integer.parseInt(length);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }    
        
        return size;
    }
    /* Copy the saved image file to the Test Result Folder 
     * and name it according to the test.
     */
    private void copyfile(String dtFile){
    	
    	File outputdir = new File(TEST_FILE_DIR);
        if (!outputdir.exists()) {
       	 if (!(outputdir.mkdirs())) {
       		 throw new RuntimeException("Error creating directory " + TEST_FILE_DIR);
       	 }
        }
        
    	try{
    		String savedFileName = mActivity.getSavedFileName();
            assertNotNull(savedFileName);
            
            File f1 = new File(savedFileName);
            String testFile = TEST_FILE_DIR + dtFile;
            File f2 = new File(testFile);
            
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2);
            
            //Add delay before getting length of file, or sometimes it will not work.
            Thread.sleep(1000);
            long longlength = f1.length();
            int length = (int) longlength;
            
            byte[] buf = new byte[length];
            int len;
            while ((len = in.read(buf)) > 0) {
            	out.write(buf,0, len);
            }
            in.close();
            out.close();
    	} catch (FileNotFoundException e) {
    		fail("Fails to get the saved output file");
			e.printStackTrace();
		} catch (IOException e) {
			fail("Fails to get the output file");
			e.printStackTrace();
		} catch (InterruptedException e) {
			fail("Fails to get the saved output file");
			e.printStackTrace();
		}finally{
            
        }
    	
    }

}
