/**
 * 
 */
package com.aptina.test.functional;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import android.app.Instrumentation;
import android.app.Activity;
import android.os.Environment;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import android.hardware.Camera.Size;
import android.media.ExifInterface;

import com.aptina.R;
import com.aptina.camera.CameraInfo;
import com.aptina.camera.VideoActivity;
import com.aptina.camera.eventlisteners.SlideGestureListener;
import com.aptina.camera.components.FanView;
import com.jayway.android.robotium.solo.Solo;





public class VideoActivityFunctionalTests extends ActivityInstrumentationTestCase2<VideoActivity> {
	private VideoActivity mActivity;
	

	/**
	 * String for logcat
	 */
	private static final String TAG = "VideoActivityFunctionalTests";
	
	private Solo solo;
	private FanView mFanView;
	/**
	 * @param name
	 */
	public VideoActivityFunctionalTests(String name) {
		super(VideoActivity.class);
		setName(name);
	}
	public VideoActivityFunctionalTests() {
		this("VideoActivityFunctionalTests");
	}
	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		solo = new Solo(getInstrumentation(),getActivity());
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	

	/**
	 * Test for Snapshot Overvideo
	 * Verify a video recording has started by clicking the record button
	 * Simulate double tap on screen for 3 times
	 * Verify photo is saved in each snapshot.
	 * Stop the video recording and verify camera is in preview
	 */
	public final void testSnapshotOverVideo(){
		if (CameraInfo.sIsDeviceOMAP4Blaze == true)
			return;
		
		mActivity.runningTest = true;
		try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    mActivity.getShutterButton().requestFocus();
                    mActivity.getShutterButton().performClick();
                }
            });
            
            
        } catch (Throwable e) {
            fail("Running click on Record video button failed");
            e.printStackTrace();
        }finally{
            
        }
        getInstrumentation().waitForIdleSync();
        assertTrue(mActivity.getVideoRecordingInProgress());
        
        mActivity.setCountDownLatch(1);
		try {
			mActivity.signal.await(5, TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		RelativeLayout main = (RelativeLayout) mActivity.findViewById(R.id.main_video_frame);
        float eachstepwidth = (float) (main.getWidth()/4);
        float eachstepheight = (float) (main.getHeight()/4);
        long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		Instrumentation inst = getInstrumentation();
		//Start tapping from the Left top
		float xcord = eachstepwidth;
		float ycord = eachstepheight;
		
		//Double Tap screen for 3 times from left top to right bottom to take snapshot
		for (int i=0; i<3; i++) {
			MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xcord, ycord, 0);
			inst.sendPointerSync(event);
			eventTime = SystemClock.uptimeMillis();
			event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xcord, ycord, 0);
			inst.sendPointerSync(event);
			eventTime = SystemClock.uptimeMillis()+200;
			event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xcord, ycord, 0);
			inst.sendPointerSync(event);
			eventTime = SystemClock.uptimeMillis();
			event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xcord, ycord, 0);
			inst.sendPointerSync(event);
			 getInstrumentation().waitForIdleSync();
			 mActivity.setCountDownLatch(2);
			 try {
					mActivity.signal.await(5, TimeUnit.SECONDS);
	
			} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			 String fname = "SnapShotOverVideoTest" + i + ".jpg";
			 copysavedfileTo(fname);
			 
			 xcord = xcord+eachstepwidth;
			 ycord = ycord+eachstepheight;
		}
		try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    mActivity.getShutterButton().requestFocus();
                    mActivity.getShutterButton().performClick();
                }
            });
            
            
        } catch (Throwable e) {
            fail("Running click on Record video button failed");
            e.printStackTrace();
        }finally{
            
        }
        getInstrumentation().waitForIdleSync();
        
        mActivity.setCountDownLatch(1);
		try {
			mActivity.signal.await(5, TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(mActivity.getVideoRecordingInProgress());
       
	}
	/**
     * Test for Reset button
     * Set the video resolution other than the default.
     * Record a 5 sec. video and press reset.
     * Record a 5 sec. video and verify the current video resolution reset to default.
     * User also need to verify the video resolution by watching the record videos after the tests
     */
	public final void testResetButtonVideo(){		
		mActivity.runningTest = true;
		final Size[] VideoResolutions = mActivity.getVideoSizes();
		
		final Size savedVideoSize = mActivity.getCurrentVideoResolution();
		
		   
		try {
            runTestOnUiThread(new Runnable() {
                public void run() {
            		mActivity.VideoResSelected(VideoResolutions[2], 2);
            		assertEquals(VideoResolutions[2].width,mActivity.getCurrentVideoResolution().width);
                    assertEquals(VideoResolutions[2].height,mActivity.getCurrentVideoResolution().height);
            		
            		mActivity.getShutterButton().requestFocus();
                    mActivity.getShutterButton().performClick();               
                }
            });     
            
        } catch (Throwable e) {
            fail("Changing Video resolution failed");
            e.printStackTrace();
        }finally{
            
        }
        getInstrumentation().waitForIdleSync();
        mActivity.setCountDownLatch(1);
		try {
			mActivity.signal.await(5, TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                	mActivity.getShutterButton().requestFocus();
                    mActivity.getShutterButton().performClick();
            		}
            });
        
        } catch (Throwable e) {
            fail("Running click on Record button failed");
            e.printStackTrace();
        }finally{
            
        }
        getInstrumentation().waitForIdleSync();
        mActivity.setCountDownLatch(1);
		try {
			mActivity.signal.await(2, TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                	mActivity.getResetButton().requestFocus();
                    mActivity.getResetButton().performClick();
                    
            		assertEquals(savedVideoSize.width,mActivity.getCurrentVideoResolution().width);
                    assertEquals(savedVideoSize.height,mActivity.getCurrentVideoResolution().height);
            		}
            });
        
        } catch (Throwable e) {
            fail("Running click on Reset button failed");
            e.printStackTrace();
        }finally{
            
        }
        getInstrumentation().waitForIdleSync();
        try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    mActivity.getShutterButton().requestFocus();
                    mActivity.getShutterButton().performClick();
                }
            });
            
            
        } catch (Throwable e) {
            fail("Running click on Record video button failed");
            e.printStackTrace();
        }finally{
            
        }
        getInstrumentation().waitForIdleSync();
        
        mActivity.setCountDownLatch(1);
		try {
			mActivity.signal.await(5, TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    mActivity.getShutterButton().requestFocus();
                    mActivity.getShutterButton().performClick();
                }
            });
            
            
        } catch (Throwable e) {
            fail("Running click on Record video button failed");
            e.printStackTrace();
        }finally{
            
        }
        getInstrumentation().waitForIdleSync();
        
        mActivity.setCountDownLatch(1);
		try {
			mActivity.signal.await(2, TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
	/**
     * Test for Video resolution change
     * Set the video resolution to each of the available size.
     * Record a 2 sec. video and verify the current video resolution.
     * User will need to verify the play back of each video record has the correct resolution after test.
     */
	public final void testVideoResolutionsChange(){		
		mActivity.runningTest = true;
		final Size[] VideoResolutions = mActivity.getVideoSizes();
		
		assertEquals(VideoResolutions[0].height, mActivity.getCurrentVideoResolution().height);
		assertEquals(VideoResolutions[0].width, mActivity.getCurrentVideoResolution().width);

		for(int i=0; i<VideoResolutions.length;i++){
			mActivity.VideoResSelected(VideoResolutions[i], i);
			
			assertEquals(VideoResolutions[i].height, mActivity.getCurrentVideoResolution().height);
			assertEquals(VideoResolutions[i].width, mActivity.getCurrentVideoResolution().width);
			
			try {
				runTestOnUiThread(new Runnable() {
					public void run() {
						mActivity.getShutterButton().requestFocus();
						mActivity.getShutterButton().performClick();						
					}
				});
							
			} catch (Throwable e) {
				fail("Running click on Record video button failed");
				e.printStackTrace();
			}finally{
				
			}
			getInstrumentation().waitForIdleSync();
			
			mActivity.setCountDownLatch(1);
			try {
				mActivity.signal.await(2, TimeUnit.SECONDS);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			assertEquals(VideoResolutions[i].height, mActivity.getCurrentVideoResolution().height);
			assertEquals(VideoResolutions[i].width, mActivity.getCurrentVideoResolution().width);
			
			try {
				runTestOnUiThread(new Runnable() {
					public void run() {
						mActivity.getShutterButton().requestFocus();
						mActivity.getShutterButton().performClick();						
					}
				});
							
			} catch (Throwable e) {
				fail("Running click on Record video button failed");
				e.printStackTrace();
			}finally{
				
			}
			getInstrumentation().waitForIdleSync();
			
			mActivity.setCountDownLatch(1);
			try {
				mActivity.signal.await(3, TimeUnit.SECONDS);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	/**
     * Test verify DVS on/off by a sliding right and sliding down gesture.
     * Take a 3 seconds video during DVS on/off and verify after the test.
     */
	public final void testDVSMode(){
		mActivity.runningTest = true;

		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mActivity.setVideoGestureInterface().OnSlideGesture(SlideGestureListener.SLIDE_RIGHT);
					assertTrue(mActivity.getDVSMode().equals(CameraInfo.VIDEO_MODE_DVS_ON));
					mActivity.getShutterButton().requestFocus();
					mActivity.getShutterButton().performClick();
			
				}
			});
						
		} catch (Throwable e) {
			fail("Running click on Record video button failed");
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
		
		mActivity.setCountDownLatch(1);
		try {
			mActivity.signal.await(3, TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mActivity.getShutterButton().requestFocus();
					mActivity.getShutterButton().performClick();
			
				}
			});
						
		} catch (Throwable e) {
			fail("Running click on Record video button failed");
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
		
		mActivity.setCountDownLatch(1);
		try {
			mActivity.signal.await(3, TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mActivity.setVideoGestureInterface().OnSlideGesture(SlideGestureListener.SLIDE_LEFT);
					assertTrue(mActivity.getDVSMode().equals(CameraInfo.VIDEO_MODE_DVS_OFF));
					mActivity.getShutterButton().requestFocus();
					mActivity.getShutterButton().performClick();
			
				}
			});
						
		} catch (Throwable e) {
			fail("Running click on Record video button failed");
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
		
		mActivity.setCountDownLatch(1);
		try {
			mActivity.signal.await(3, TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mActivity.getShutterButton().requestFocus();
					mActivity.getShutterButton().performClick();
			
				}
			});
						
		} catch (Throwable e) {
			fail("Running click on Record video button failed");
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
		
		mActivity.setCountDownLatch(1);
		try {
			mActivity.signal.await(3, TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public final void testHDROnOff(){

		solo.scrollToSide(solo.LEFT);
		solo.clickOnButton("HDR");
		solo.takeScreenshot("HDR On in Video Mode");
//		TODO Not available yet
//		Verify HDR mode is on
		solo.clickOnButton("HDR");
		solo.sleep(1000);
		solo.takeScreenshot("HDR Off in Video Mode");

	}
	
	/*Test verify the FanView for Exposure and HDR options can be open and closed as expected
	 * 
	 */
	public final void testFanViewOnOff(){
		mActivity.runningTest = true;
		mFanView = (FanView) mActivity.findViewById(R.id.fan_view);
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
				mActivity.setVideoGestureInterface().OnSlideGesture(SlideGestureListener.SLIDE_UP);
				}
			});
						
		} catch (Throwable e) {
			fail("Slide to the top of screen failed");
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
		solo.sleep(2000);
		solo.takeScreenshot("FanView Opened");
		boolean viewable = mFanView.isOpen();
		assertTrue( "FanVies fail to open", viewable);
		
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
				mActivity.setVideoGestureInterface().OnSlideGesture(SlideGestureListener.SLIDE_DOWN);
				}
			});
						
		} catch (Throwable e) {
			fail("Slide to the top of screen failed");
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
		solo.sleep(2000);
		solo.takeScreenshot("FanView Closed");
		viewable = mFanView.isOpen();
		assertFalse( "FanVies fail to close", viewable);

	}
	
	/**
     * Getting Inform from EXIF
     * Return width and height of image file
     * @return
     */
    private int[] ExifInfo(){
        
        String PhotoFileName = mActivity.getSavedPhotoFileName();
        assertNotNull(PhotoFileName);
        
        int size[] = new int[2];
        
        try {
            ExifInterface exif = new ExifInterface(PhotoFileName);
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
    public  String TEST_FILE_DIR = Environment.getExternalStorageDirectory() + "/CameraAppTest/";
    /* Copy the saved image file to the Test Result Folder 
     * and name it according to the test.
     */
    private void copysavedfileTo(String dtFile){

    	File outputdir = new File(TEST_FILE_DIR);
        if (!outputdir.exists()) {
       	 if (!(outputdir.mkdirs())) {
       		 throw new RuntimeException("Error creating directory " + TEST_FILE_DIR);
       	 }
        }
        
    	try{
    		String savedFileName = mActivity.getSavedPhotoFileName();
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
