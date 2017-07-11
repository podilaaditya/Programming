package com.aptina.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aptina.R;
import com.aptina.adapter.HistogramPageAdapter;
import com.aptina.camera.components.FanView;
import com.aptina.camera.components.OptionSet;
import com.aptina.camera.components.ShutterButton;
import com.aptina.camera.components.ThumbnailControl;
import com.aptina.camera.eventlisteners.ModeSelectionListener;
import com.aptina.camera.eventlisteners.SlideGestureListener;
import com.aptina.camera.eventlisteners.VideoCompositeListener;
import com.aptina.camera.eventlisteners.VideoCompositeListener.VideoGestureInterface;
import com.aptina.camera.fragments.CaptureFragment;
import com.aptina.camera.fragments.HistogramFrag;
import com.aptina.camera.fragments.OptionsFragment;
import com.aptina.camera.fragments.PanelFragment;
import com.aptina.camera.fragments.ThumbFragment;
import com.aptina.camera.interfaces.CaptureFragmentInterface;
import com.aptina.camera.interfaces.OptionsFragInterface;
import com.aptina.camera.interfaces.PanelFragInterface;
import com.aptina.camera.interfaces.ResolutionChangeInterface;
import com.aptina.data.HistogramRGBData;
import com.aptina.logger.Logger;
import com.aptina.miscellaneous.DateTimeUtils;
import com.aptina.miscellaneous.FileUtils;
import com.aptina.miscellaneous.PreferencesProvider;


/**
 * Implements main camera preview activity.
 */
@SuppressLint("NewApi")
public class VideoActivity extends Activity{

	/**
	 * TAG of this activity for logcat
	 */
	private static final String TAG = " VideoActivity";

    /**
     * Message type: update record time.
     */
    private static final int UPDATE_RECORD_TIME = 58643;
    
    /**
     * Message type: update video snapshot thumbnail
     */
    private static final int UPDATE_SNAPSHOT_THUMBNAIL = 68644;

    /**
     * Minimal available size on SD card. 20Mb in bytes. 
     */
    private static final long MINIMAL_AVAILABLE_CARD_SIZE_TO_SHOW_MESSAGE = 20*1024*1024L;

    /**
     * Minimal available size on SD card to start recording of new video. (1.5 mb).
     */
    private static final long MINIMAL_AVAILABLE_CARD_SIZE_TO_START_RECORD = 3*512*1024L;

    /**
     * Current date holder for KPI.
     */
    private Date mCurrentDate;

    /**
     * Previous date holder for KPI.
     */
    private Date mPreviousDate;

    /**
     * The main view that gold the {@link #mPreview} as well as the panels
     */
    private FanView mFanView;
    /**
     * Camera preview view.
     */
    private SurfaceView mPreview = null;

    /**
     * Preview holder.
     */
    private SurfaceHolder mPreviewHolder = null;
    
    /**
     * Global variable for the parent of all the video.xml other views
     */
	private RelativeLayout mainVideoFrame;
	
	/**
	 * The surface of the preview
	 */
	private SurfaceView previewSurface;
	
	/**
	 * The time it takes for the preview screen to expand to 
	 * full screen after DVS has been turned on, in ms.
	 */
	private final int DVS_ANIMATION_TIME = 1000;
    /**
     * Variable to hold the current width(x) and height(y) of the android display
     */
    private Point dispDim = new Point();

    /**
     * Camera instance.
     */
    private Camera mCamera = null;

    /**
     * Media recorder instance.
     */
    private MediaRecorder mMediaRecorder;

    /**
     * Camcoder profile.
     */
    private CamcorderProfile[] mCamcorderProfiles;

    /**
     * In preview flag.
     */
    private boolean mPreviewing = false;

    /**
     * SD card availability flag.
     */
    private boolean mIsSdCardAvailable = false;

    /**
     * Video is in progress flag.
     */
    private static boolean mInProgress = false;

    /**
     * Thumbnail control fragment.
     */
    private ThumbFragment mThumbFragment;
    
    /**
     * Capture control fragment, includes: s
     * hutter button, dvs icon, camera switcher, video recording text
     */
    private CaptureFragment mCaptureFragment;

    /**
     * Callback for the {@link #mCaptureFragment}
     */
    private CaptureFragmentInterface mCaptureFragInterface;
    
    /**
     * Callback for the {@link #mPanelFragment}
     */
    private PanelFragInterface mPanelFragInterface;
    
    /**
     * Panel fragment, contains all of the panel buttons
     */
    private PanelFragment mPanelFragment;
   
    /**
     * Options fragment, contains options for focus and histogram toggle
     */
    private OptionsFragment mOptionsFragment;
    
    /**
     * Interface for {@link #mOptionsFragment}
     */
    private OptionsFragInterface mOptionsFragInterface;
    /**
     * Mode selector view.
     */
    private View mModeSelectionButton = null;
    
    /**
     * Mode selector image view of icon
     */
    private ImageView mModeSelectionPanelImage = null;
    
    /**
     * Mode selection listener.
     */
    private ModeSelectionListener mModeSelectionListener = null;

    /**
     * Toggle of whether HDR mode is on/off, is toggled by {@link #mModeSelectionButton}
     */
    private boolean mHdrOn = false;
    /**
     * The text of the HDR mode button, we want to toggle its color during {@link #toggleHDRMode()}
     */
    private TextView mModeTextView = null;
   
	/**
	 * The current focus mode
	 */
	private String mFocusMode = CameraInfo.FOCUS_CONTINUOUS_VIDEO;
	
    /**
     * The adapter that holds our {@link HistogramFrag} fragments
     * Needs to be updated every time new frames come in
     */
    private HistogramPageAdapter mHistPageAdapter = null;
    /**
     * The histogram viewpager that we set {@link #mHistPageAdapter}
     */
    private ViewPager mViewPager = null;
   



    /**
     * Still/video Switch.
     */
    private View mSwitchButton;


    /**
     * Remember if the camera has been initialized
 	 */
    private boolean mCamInit = false;

    /**
     * Surface holder callback.
     */
    private CustomSurfaceHolderCallback mPreviewHolderCallback = null;

    /**
     * Current camera index.
     */
    private int mCurrentCameraIndex = -1;

    /**
     * Array of camera parameters.
     */
    private Camera.Parameters[] mCurrentCameraParameters = null;

    /**
     * Array of default camera parameters.
     */
    private Camera.Parameters[] mDefaultCameraParameters = null;

    /**
     * Reset button view.
     */
    private View mResetButton; 

    /**
     * Text message.
     */
    private TextView mTextMessage;

    /**
     * Time when video recording was started.
     */
    private static long mRecordStartTime;

    /**
     * Last video file name.
     */
    private String currentFileName;

    /**
     * Handles messages inside the application.
     */
    private static VideoHandler mHandler;

    /**
     * Camera active mode.
     */
    private String mActiveMode;

    /**
     * Indicates that preview surface view was destroyed.
     */
    private boolean mSurfaceWasDestroyed = true;

    
    private boolean mSnapshotInProgress = false;
    
    private boolean mVideoRecordingInProgress = false;
    
    /**
     * Still Camera Activity flag.
     */
    private boolean mInCameraActivity = false;
    
    /**
     * Check to see that another thread, #mVideoGestureInterface, has not
     * locked the camera, which would cause a cam access error crash
     */
    private boolean mCamLocked = true;
    
    /**
     * Views for Preview 
     */
    RelativeLayout mVideoFrame = null;
    SurfaceView mPreviewSurface = null;
	
    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
		
		

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if(mVideoRecordingInProgress){
				mSnapshotInProgress = false;
				new SavePhotoAction().execute(data);
			}

			mHandler.sendEmptyMessage(UPDATE_SNAPSHOT_THUMBNAIL);
			getSynchronizedCamera().startPreview();
			setSnapProgress(false);
			if(runningTest && signal != null){
            	signal.countDown();
            }
	
		}
	};

	private VideoCompositeListener videoGestureListener = new VideoCompositeListener(this);
	public VideoCompositeListener getVideoGestureListener(){
		return videoGestureListener;
	}
	
	public VideoGestureInterface setVideoGestureInterface(){
		return mVideoGestureInterface;
	}
	
	public String getDVSMode(){
		return mDVSMode;
		
	}
	
	//DVS mode of the camera on activity onCreate
	private String mDVSMode = CameraInfo.VIDEO_MODE_DVS_OFF;
	private VideoGestureInterface mVideoGestureInterface = new VideoGestureInterface(){
		

		@Override
		public void OnSlideGesture(int slide_dir) {
			
			switch(slide_dir){
			case SlideGestureListener.SLIDE_RIGHT://turn dvs on
				if (mVideoRecordingInProgress){
					Toast.makeText(getApplicationContext(), "DVS settings cannot be changed during Video Recording", Toast.LENGTH_SHORT).show();
				}
//				else if(mCamcorderProfiles[mCurrentCameraIndex].videoFrameWidth != CameraInfo.VGA_WIDTH ||
//						mCamcorderProfiles[mCurrentCameraIndex].videoFrameHeight != CameraInfo.VGA_HEIGHT){
//					Log.w(TAG, "DVS for Camcoder resolution : (" + mCamcorderProfiles[mCurrentCameraIndex].videoFrameWidth + ", "
//							+ mCamcorderProfiles[mCurrentCameraIndex].videoFrameHeight + ") " + "is not supported");
//					String txt = "Only DVS for Camcoder resolution : (" + CameraInfo.VGA_WIDTH + ", " + CameraInfo.VGA_HEIGHT + ") is supported";
//					Log.w(TAG, txt);
//					Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
//					
//				}
				else {
					if(!mDVSMode.equalsIgnoreCase(CameraInfo.VIDEO_MODE_DVS_ON)){
						
						toggleVideoSnapshot(switchToDVSMode(CameraInfo.VIDEO_MODE_DVS_ON));
						mCaptureFragment.setDVSIndicator(true);
						Toast.makeText(getApplicationContext(), "Setting DVS to On", Toast.LENGTH_SHORT).show();
						mDVSMode = CameraInfo.VIDEO_MODE_DVS_ON;
					}else Toast.makeText(getApplicationContext(), "DVS already On", Toast.LENGTH_SHORT).show();
				}
					
				break;
			case SlideGestureListener.SLIDE_LEFT://turn dvs off
				if (mVideoRecordingInProgress){
					Toast.makeText(getApplicationContext(), "DVS settings cannot be changed during Video Recording", Toast.LENGTH_SHORT).show();
				} else {
					if(!mDVSMode.equalsIgnoreCase(CameraInfo.VIDEO_MODE_DVS_OFF)){
						toggleVideoSnapshot(switchToDVSMode(CameraInfo.VIDEO_MODE_DVS_OFF));
						mCaptureFragment.setDVSIndicator(false);
						Toast.makeText(getApplicationContext(), "Setting DVS to Off", Toast.LENGTH_SHORT).show();
						mDVSMode = CameraInfo.VIDEO_MODE_DVS_OFF;
					} else Toast.makeText(getApplicationContext(), "DVS already Off", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case SlideGestureListener.SLIDE_DOWN://toggle menu
			case SlideGestureListener.SLIDE_UP://toggle menu
				mFanView.showMenu();
				break;
			}
			
		}

		@Override
		public void OnDoubleTapGesture(MotionEvent event) {
			Log.i(TAG,"onVideoSnapshotGesture()");
			if (mSnapshotInProgress || (!mVideoRecordingInProgress) || !snapshot_supported_at_resolution) {  //BKTODO disable during video stabilization
				Log.w(TAG, "Cannot take video snapshot");
				return;
			}
			if (getSynchronizedCamera() != null  && mVideoRecordingInProgress && mJpegCallback != null) {
				Log.i(TAG, "Taking video snapshot");
				takeVideoSnapshot();
				
			}else{
				Log.i(TAG, "Not recording, no picture taking");
			}
			
		}

		@Override
		public void OnSmileGesture() {
			
			
		}
		
		@Override
		public void OnFrownDetected() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void OnZoomGesture(float zoom_scale) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Camera getCamera() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void OnSingleTapGesture(MotionEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void OnDownTouch(MotionEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void OnMoveTouch(MotionEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void OnUpTouch(MotionEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void OnThetaEvent(double current_theta, double start_theta) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void OnCircleEvent(Path.Direction rotation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void OnPointerDown(MotionEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void OnPointerUp(MotionEvent event) {
			// TODO Auto-generated method stub
			
		}


		
	};
	
	private boolean snapInProgress = false;
	private boolean isSnapInProgress(){
		return snapInProgress;
	}
	private synchronized void setSnapProgress(boolean val){
		snapInProgress = val;
	}
	/**
	 * Take video snapshot and check whether a snapshot is still in progress
	 */
	private void takeVideoSnapshot(){
		if(!isSnapInProgress()){
			setSnapProgress(true);
			getSynchronizedCamera().takePicture(null, null, null, mJpegCallback);
		}else{
			Log.w(TAG, "Snapshot is already in progress, skipping new snapshot");
		}
		
	}
	 /**
     * Thread safe access to the camera object, necessary for the 
     * AsyncTasks
     * 
     * @return Synchronized camera object
     */
    private  Camera getSynchronizedCamera(){
    	synchronized(mCamSyncObj){
    		return mCamera;
    	}
    }
    /**
     * Thread safe access to the camera object, necessary for the 
     * AsyncTasks
     */
    private void setSynchronizedCamera(Camera cam){
    	synchronized(mCamSyncObj){
    		mCamera = cam;
    	}
    }
    private Object mCamSyncObj = new Object();
	private Camera.Parameters switchToDVSMode(String dvsMode){
    	RelativeLayout mSv = (RelativeLayout) findViewById(R.id.main_video_frame);

    	ViewGroup.LayoutParams sv_p = mSv.getLayoutParams();
    	SurfaceView mPreviewSurface = (SurfaceView) findViewById(R.id.preview);
       	ViewGroup.LayoutParams preview_p = mPreviewSurface.getLayoutParams();
       	Camera cam;
       	if((cam = getSynchronizedCamera()) != null && mCamLocked) {
       		Camera.Parameters param = cam.getParameters();
       		if (param.isVideoStabilizationSupported() == true) {
           		
           		
           		
           		if (dvsMode.equalsIgnoreCase(CameraInfo.VIDEO_MODE_DVS_ON)) {
           			getSynchronizedCamera().stopPreview();
           			param.setRecordingHint(false);
           			param.set(CameraInfo.VIDEO_SNAPSHOT_MODE, CameraInfo.CAMERA_MODE_DISABLED_DEFAULT_VALUE);
           			param.setVideoStabilization(true);
           			mDVSMode = dvsMode;
           			getSynchronizedCamera().setParameters(param);
               		
               		getSynchronizedCamera().startPreview();
               	    Log.i(TAG,"------------------Preview Size "+ "  w / H  " + param.getPreviewSize().width + "/" + param.getPreviewSize().height);
           			// moved from below
           			preview_p.width = (int)(sv_p.width * 0.75);
           			preview_p.height = (int)(sv_p.height * 0.75);

           			mPreviewSurface.setLayoutParams(preview_p);
           			animatePreviewExpanansion(0.75f,1f);
           			// end move
           		}
           		else {
           			getSynchronizedCamera().stopPreview();
           			param.setVideoStabilization(false);
           			mDVSMode = dvsMode;
           			getSynchronizedCamera().setParameters(param);
               		
               		getSynchronizedCamera().startPreview();
               		
               		
           			// moved from below
           			preview_p.width = (int)(sv_p.width * 0.75);
           			preview_p.height = (int)(sv_p.height * 0.75);

           			mPreviewSurface.setLayoutParams(preview_p);
           			animatePreviewExpanansion(0.75f, 1f);
           			// end move
           			
           		}
           	} else{
           		Log.i(TAG, "VideoStabilization not supported");
           	}
       		return param;
       	}
       	
       	
       	
		
       	return null;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Reset the Histogram
         */
        HistogramRGBData.reset();
//        setContentView(R.layout.video);
        setContentView(R.layout.video);
        //Set the minimal resolution sizes as points to use in CameraInfo
        CameraInfo.initMinResolutionSizes();
        
        //Initiate the preview screen with buttons
        initPreview();
        
        
    }
    /* For Functional test*/
    public ShutterButton getShutterButton() {
		return mCaptureFragment.getShutterButton();
	}
    public boolean getVideoRecordingInProgress(){
    	return mVideoRecordingInProgress;
    }
    public View getSwitchButton() {
		return mSwitchButton;
	}
    public boolean getInCameraActivity(){
    	return mInCameraActivity;
    }
    public View getResetButton(){
    	return mResetButton;
    }
    public View getVideoResolutionButton(){
//    	return mVideoResolutionButton; TODO pass video res button to tests
    	return null;
    }
    
    public boolean runningTest = false;
    public CountDownLatch signal;
    public void setCountDownLatch(int n){
    	signal = new CountDownLatch(n);
    }
    public CountDownLatch getCountDownLatch(){
    	return signal;
    }
    public void incrementLatch(){
    	signal.countDown();
    }
    private String PhotoFileName;
    public String getSavedPhotoFileName(){
    	return PhotoFileName;
    }
//    private ModeSelector mModeSelector = null;
    private void initPreview(){
    	mFanView = (FanView) findViewById(R.id.fan_view);
        mFanView.setViews(R.layout.video_main, R.layout.video_fan_menu);
    	mPreview = (SurfaceView)findViewById(R.id.preview);
        mPreview.setKeepScreenOn(true);

        mVideoRecordingInProgress = false;
        mSnapshotInProgress = false;
        
        

        
        FragmentManager fragmentManager = this.getFragmentManager();
        
        mThumbFragment = (ThumbFragment) fragmentManager.findFragmentById(R.id.gallery_thumbnail_fragment);
        mCaptureFragment = (CaptureFragment) fragmentManager.findFragmentById(R.id.video_capture_fragment);
        mPanelFragment = (PanelFragment) fragmentManager.findFragmentById(R.id.video_panel_fragment);
        mOptionsFragment = (OptionsFragment) fragmentManager.findFragmentById(R.id.options_menu_fragment);
        
        
        mModeSelectionButton = findViewById(R.id.mode_button);
        mModeTextView = (TextView) findViewById(R.id.mode_label);
        mModeSelectionPanelImage = (ImageView) findViewById(R.id.mode_image_holder);

        mHistPageAdapter = new HistogramPageAdapter(getFragmentManager());
    	mViewPager = (ViewPager)findViewById(R.id.histogram_viewpager);
        mViewPager.setAdapter(mHistPageAdapter);
        
        mResetButton = findViewById(R.id.reset_button);
        


        mTextMessage = (TextView) findViewById(R.id.txt_message);

        mSwitchButton = findViewById(R.id.switch_button);
        
        mHandler = new VideoHandler();
        
        mVideoFrame = (RelativeLayout) findViewById(R.id.main_video_frame);
    	mPreviewSurface = (SurfaceView) findViewById(R.id.preview);
    }
    /**
     * Initialize all of the touch listeners after the camera has been started
     * 
     * Note: Camera has to be initialized first to prevent null pointer exceptions from occurring
     * on touch events
     */
    private void initTouchListeners(){
    	videoGestureListener.setCallback(mVideoGestureInterface);
        mPreview.setOnTouchListener(videoGestureListener);
        mThumbFragment.setGalleryListener(true);
        mCaptureFragInterface = new CaptureFragmentInterface(){

			@Override
			public void onShutterClick() {
				Log.i(TAG,  "shutter button clicked");
				if (mPreviewing && mIsSdCardAvailable) {
                    // KPI logging.
                    updateKpiTime();
                    Logger.logMessage("ShutterButton.onClick() called at " + DateTimeUtils.formatDate(mCurrentDate));

                    if (mInProgress) {
                    	Log.i(TAG,  "stopVideoRecording()");
                        stopVideoRecording();
                    } else {
                        if (FileUtils.getAvailableBytes() > MINIMAL_AVAILABLE_CARD_SIZE_TO_START_RECORD) {
                        	Log.i(TAG,  "startVideoRecording()");
                            startVideoRecording();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), R.string.sdcard_not_enough_space, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
				
			}

			@Override
			public void onCameraChangerClick() {
				switchCamera();
			}
        	
        };
        mCaptureFragment.setInterface(mCaptureFragInterface);
        
        mPanelFragInterface = new PanelFragInterface(){

			@Override
			public void onSnapshot() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onHDR() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onOptions() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onReset() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSwitch() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Camera getCamera() {
				return getSynchronizedCamera();
			}

			@Override
			public ResolutionChangeInterface getVideoResolutionCallback() {
				return videoResolutionCallback;
			}

			@Override
			public ResolutionChangeInterface getSnapshotResolutionCallback() {
				return snapshotResolutionCallback;
			}
        };
        mPanelFragment.setInterface(mPanelFragInterface);
        
        mOptionsFragInterface = new OptionsFragInterface(){

			@Override
			public Camera getCamera() {
				return getSynchronizedCamera();
			}

			@Override
			public void setFocusOptions(OptionSet focusSet) {
				mCurrentCameraParameters[mCurrentCameraIndex].setFocusMode(focusSet.getMode());
				mFocusMode = focusSet.getMode();
				getSynchronizedCamera().setParameters(mCurrentCameraParameters[mCurrentCameraIndex]);
				Log.i(TAG, "Focus mode selected : " + focusSet.getMode());
			}
			
			@Override
			public void setHistogramVisibility(int vis){
				mViewPager.setVisibility(vis);
			}

			@Override
			public int getHistogramVisibility() {
				return mViewPager.getVisibility();
			}

			@Override
			public void hideModeSelector() {
				mPanelFragment.hideModeSelector();
			}
        	
        };
        mOptionsFragment.setInterface(mOptionsFragInterface);

        mModeSelectionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleHDRMode();
			}
		});
        mResetButton.setOnClickListener(resetButtonListener);
        mSwitchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(cameraIntent);
                mInCameraActivity = true;
                finish();
            }
        });
        mCamInit = true;
    }
    
    private void removeTouchListeners(){
    	videoGestureListener.setCallback(null);
        mPreview.setOnTouchListener(null);
        mThumbFragment.setGalleryListener(false);
        mCaptureFragment.setInterface(null);
        mOptionsFragment.setInterface(null);
        mResetButton.setOnClickListener(null);
        mSwitchButton.setOnClickListener(null);
        
        getSynchronizedCamera().setPreviewCallback(null);
        
        mCamInit = false;
    }
    
    
    
 	
 	
    @Override
    public void onResume() {
        super.onResume();        
        loadPreferences();
        mainVideoFrame = (RelativeLayout) findViewById(R.id.main_video_frame);
        previewSurface = (SurfaceView) findViewById(R.id.preview);
        
        new Thread(){
        	@Override
        	public void run(){
        		startCamera();
                
                mCamInit = true;
        	}
        	
        }.run();
        
        if (!mSurfaceWasDestroyed) {
            startCameraPreview(mPreview.getWidth(), mPreview.getHeight());
        }
        mVideoRecordingInProgress = false;
        mSnapshotInProgress = false;
        checkSdCard();
        addSdCardIntentFilters();
  
    }

    @Override
    public void onPause() {
        if (mInProgress) {
            stopVideoRecording();
        }
        stopCamera();
        unregisterReceiver(mReceiver);
        super.onPause();
    }
    
    private OnClickListener resetButtonListener = new OnClickListener(){
    	@Override
        public void onClick(View v) {
            getSynchronizedCamera().stopPreview();                
            Camera.Parameters params = mDefaultCameraParameters[mCurrentCameraIndex];
            mDVSMode = CameraInfo.VIDEO_MODE_DVS_OFF;
        	params.setVideoStabilization(false);
        	
            getSynchronizedCamera().setParameters(params);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                if (mCamcorderProfiles == null) {
                    mCamcorderProfiles = new CamcorderProfile[Camera.getNumberOfCameras()];
                }

                mCamcorderProfiles[mCurrentCameraIndex] = CamcorderProfile.get(mCurrentCameraIndex, CamcorderProfile.QUALITY_HIGH);
            } else {
                if (mCamcorderProfiles == null) {
                    mCamcorderProfiles = new CamcorderProfile[1];
                }

                mCamcorderProfiles[mCurrentCameraIndex] = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            }
            
            Size[] vsizes = getVideoSizes();
            Size vsize = vsizes[0]; 
            
            Size[] ssizes = getImageSizes();
            Size ssize = ssizes[0]; 

            mCamcorderProfiles[mCurrentCameraIndex]= adaptCamcorderForNewResolution(mCamcorderProfiles[mCurrentCameraIndex], vsize.width, vsize.height);

            // Update UI
            mActiveMode = CameraInfo.CAMERA_MODE_AUTO;
            mCaptureFragment.setDVSIndicator(false);
            setHDRMode(false);
            mModeSelector.setVisibility(View.GONE);
            
            mVideoResolutionChangeListener.setSizes( vsizes);
            mVideoResolutionValue.assignSize(getSynchronizedCamera().new Size(mCamcorderProfiles[mCurrentCameraIndex].videoFrameWidth, mCamcorderProfiles[mCurrentCameraIndex].videoFrameHeight)); 
            
            mSnapResolutionChangeListener.setSizes( ssizes);
            mSnapResolutionValue.assignSize(ssize); 
            params.setPictureSize(ssize.width, ssize.height);
            getSynchronizedCamera().setParameters(params);


            startPreviewWithOptimalParameters(mPreview.getWidth(), mPreview.getHeight());
        }
    	
    };
    
    /**
     * Toggle the hdr mode
     * 
     * @param toggle True turns on hdr, false turns it off
     */
	private void setHDRMode(boolean toggle) {
		Resources res = this.getResources();
		if(toggle){
			mHdrOn = true;
			mModeSelectionPanelImage.setImageDrawable(res.getDrawable(R.drawable.icon_active_hdr));
			mModeTextView.setTextColor(res.getColor(R.color.active_icon_color));
			mCaptureFragment.setHDRVisibility(View.VISIBLE);
		}else{
			mHdrOn = false;
			mModeSelectionPanelImage.setImageDrawable(res.getDrawable(R.drawable.icon_inactive_hdr));
			mModeTextView.setTextColor(res.getColor(R.color.inactive_icon_color));
			mCaptureFragment.setHDRVisibility(View.INVISIBLE);
		}
	}
	/**
	 * Calls {@link #setHDRMode(boolean)}, but uses !{@link #mHdrOn} as argument
	 */
	private void toggleHDRMode(){
		setHDRMode(!mHdrOn);
	}
	private void animateVideoSnap(Bitmap bit) {


		final ImageView image = (ImageView) findViewById(R.id.video_snap_anim);

        image.setImageBitmap(bit);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale_gone);
       
        a.setAnimationListener(new AnimationListener() {

            public void onAnimationStart(Animation animation) {
            	image.setVisibility(View.VISIBLE);

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
            	image.setVisibility(View.GONE);   	
            	image.setImageBitmap(null);
            }
        });
        
        image.startAnimation(a);
	}

	private void animatePreviewExpanansion(float fromVal, float toVal){
        ValueAnimator va = ValueAnimator.ofFloat(fromVal, toVal);
        va.setDuration(DVS_ANIMATION_TIME);
       
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        	public void onAnimationUpdate(ValueAnimator animation) {
        		
        		Float value = (Float) animation.getAnimatedValue();
        		Log.d(TAG, "animation value float : " + value.floatValue());

            	ViewGroup.LayoutParams sv_p = mainVideoFrame.getLayoutParams();
            	
               	ViewGroup.LayoutParams preview_p = previewSurface.getLayoutParams();
               	preview_p.width = (int)(sv_p.width * value.floatValue());
            	preview_p.height = (int)(sv_p.height * value.floatValue());
            	previewSurface.setLayoutParams(preview_p);

        	}
        });

        
        va.start();
//        mainVideoFrame.setBackgroundDrawable(null);
	}

    /**
     * Async task to save photo on SD card.
     */
    private class SavePhotoAction extends AsyncTask<byte[], Object, Integer> {

		@Override
		protected Integer doInBackground(final byte[]... jpeg) {
			 try {

	                String state = android.os.Environment.getExternalStorageState();

	                if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
	                	Log.e(TAG, "media not mounted");
	                    throw new IOException("Couldn't find SD card.");
	                }

	                if ((jpeg[0] == null) || (jpeg[0].length == 0)) {
	                	Log.e(TAG, "data == " + jpeg[0]);
	                    Logger.logMessage("SavePhotoAction.doInBackground(): no data received");
	                    return 0;
	                }

	                if (FileUtils.getAvailableBytes() < jpeg[0].length) {
	                    runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	                            Toast.makeText(getApplicationContext(), R.string.sdcard_not_enough_space, Toast.LENGTH_LONG).show();
	                        }
	                    });
	                    return 0;
	                }

	                // Update thumbnail.
	                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                BitmapFactory.Options options = new BitmapFactory.Options();                            
                                if (mCurrentCameraParameters[mCurrentCameraIndex].getPictureSize().height > CameraInfo.THUMBNAIL_IMAGE_HEIGHT) {
                                    options.inSampleSize = Math.round(mCurrentCameraParameters[mCurrentCameraIndex].getPictureSize().height / CameraInfo.THUMBNAIL_IMAGE_HEIGHT);
                                } else {
                                    options.inSampleSize = 1;
                                }
                                Bitmap b = BitmapFactory.decodeByteArray(jpeg[0], 0, jpeg[0].length, options);
                                Log.w(TAG, "Bitmap for mGallery == null ? : " + mThumbFragment.isGalleryNull());
                                mThumbFragment.setGalleryThumbnail(b, null);
//                                mGallery.setThumbnail(BitmapFactory.decodeByteArray(jpeg[0], 0, jpeg[0].length, options), null);
                                mThumbFragment.showGallery();
                                
                            } catch (Exception ex) {
                                Logger.logApplicationException(ex, "SavePhotoAction.doInBackground(): failed to update thumbnail");
                            }
                        }

                    });
	                runOnUiThread(new Runnable() {
		                @Override
		                public void run() {
		                    try {
		                    	//Decrease image size so as to avoid out of memory errors when image capture dimensions are large
		                    	BitmapFactory.Options options = new BitmapFactory.Options();                            
                                if (mCurrentCameraParameters[mCurrentCameraIndex].getPictureSize().height > CameraInfo.THUMBNAIL_IMAGE_HEIGHT) {
                                    options.inSampleSize = Math.round(mCurrentCameraParameters[mCurrentCameraIndex].getPictureSize().height / (CameraInfo.THUMBNAIL_IMAGE_HEIGHT * 3));
                                } else {
                                    options.inSampleSize = 1;
                                }
		                    	//Show snapshot animation
		    	                animateVideoSnap(BitmapFactory.decodeByteArray(jpeg[0], 0, jpeg[0].length, options));
		                        
		                    } catch (Exception ex) {
		                        Logger.logApplicationException(ex, "animateVideoSnap();");
		                    }
		                }

		            });
	                
	
	                PhotoFileName = getPhotoPath(null);

	                File directory = new File(PhotoFileName).getParentFile();
	                if (!directory.exists() && !directory.mkdirs()) {
	                	throw new IOException("Couldn't create path to file.");
	                }
	   
	                try {
	                    FileOutputStream fileOutputStream = new FileOutputStream(PhotoFileName);
	                    fileOutputStream.write(jpeg[0]);
	                    fileOutputStream.close();

	                    // KPI Logging.
	                    updateTime();
	                    Logger.logMessage("Saved photo in " + PhotoFileName + " at " + DateTimeUtils.formatDate(mCurrentDate) + ", time diff: " + getTimeDiff());
	                }
	                catch (java.io.IOException e) {
	                    Logger.logApplicationException(e, "VideoActivity.SavePhotoAction() failed. Couldn't save the file");
	                }

	                String name = PhotoFileName.substring(PhotoFileName.lastIndexOf("/") + 1);

	                ContentValues values = new ContentValues();
	                values.put(Images.Media.TITLE, name);
	                values.put(Images.Media.DISPLAY_NAME, name);
	                values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
	                values.put(Images.Media.MIME_TYPE, "image/jpeg");
	                values.put(Images.Media.DATA, PhotoFileName);
	                values.put(Images.Media.SIZE, jpeg[0].length);

	                final Uri uri = getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
	
	                // KPI logging.
	                updateTime();
	                Logger.logMessage("Thumbnail loaded at " + DateTimeUtils.formatDate(mCurrentDate) + ", time diff: " + getTimeDiff());

	                runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	mThumbFragment.setGalleryUri(uri);                            
	                    	mThumbFragment.showGallery();
	
	                    }
	                });

	                System.gc();
	                return 0;
	                
			 }catch(final Exception e) {
	                Logger.logApplicationException(e, "VideoActivity.SavePhotoAction.doInBackground() failed.");
	         }
			 if(runningTest && signal != null){
	            	signal.countDown();
			 }
			return null;
			
		}
		
		private void updateTime() {
			mPreviousDate = mCurrentDate;
        	mCurrentDate = new Date();
			
		}

		/**
         * Retrieves name for photo (without extension).
         * 
         * @return photo name.
         */
        private String getPhotoName() {
            return "IMG-" + DateTimeUtils.getPhotoFormatDate(new Date());
        }
        
        /**
         * Retrieves path for photo or null if failed.
         * 
         * @param index Index of burst/hdr photo.
         * @param mode current mode.
         * @param givenName already given name that should be used for result path. Can be null.
         * 
         * @return path for photo. 
         */
        private String getPhotoPath(String givenName) {
            try {

                String fileName = givenName == null ? getPhotoName() : givenName;

                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + fileName + ".jpg";;

                int num = 1;
                while ((new File(path)).exists()) {
                    path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + fileName + "-" + num + ".jpg";
                    num++;
                }
                File directory = new File(path).getParentFile();
                if (!directory.exists() && !directory.mkdirs()) {
                    throw new IOException("Couldn't create path to file: " + path);
                }
                new File(path).createNewFile();

                return path;
            }
            catch(final Exception e) {
                Logger.logApplicationException(e, "VideoActivity.SavePhotoAction.getPhotoPath() failed.");
            }
            return null;
        }
    	
    }

    /**
     * Registers SD card events receiver. 
     */
    private void addSdCardIntentFilters() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
        intentFilter.addDataScheme("file");
        registerReceiver(mReceiver, intentFilter);
    }

    /**
     * Intent events receiver.
     * Handles SD card events.
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkSdCard();
        }
    };

    /**
     * Checks SD card state.
     */
    private void checkSdCard() {
        if (FileUtils.isSdCardMounted()) {
            mTextMessage.setVisibility(View.GONE);
            mIsSdCardAvailable = true;
            mThumbFragment.updateGallery(getContentResolver(), ThumbnailControl.TYPE_VIDEO);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	mThumbFragment.showGallery();
                }
            });
            checkSdSize();
        } else {
            mTextMessage.setText(R.string.sdcard_unavailable);
            mTextMessage.setVisibility(View.VISIBLE);
            mIsSdCardAvailable = false;
        }
    }

    /**
     * Checks available size on SD card and displays warning message in needed.
     */
    private void checkSdSize() {
        try {
            long availableBytes = FileUtils.getAvailableBytes();
            if (availableBytes <= MINIMAL_AVAILABLE_CARD_SIZE_TO_SHOW_MESSAGE) {
                mTextMessage.setText(R.string.sdcard_small_size);
                mTextMessage.setVisibility(View.VISIBLE);
            } else {
                mTextMessage.setVisibility(View.GONE);
            }
        } catch(final Exception e) {
            Logger.logApplicationException(e, "VideoActivity.checkSdSize(): Failed.");
        }
    }

    /**
     * Loads application preferences.
     */
    private void loadPreferences() {        
        Logger.setLogging(PreferencesProvider.isLoggingOn(getApplicationContext()));
    }


    @Override
    public boolean onMenuOpened (int featureId, Menu menu) {
        MenuItem item =  menu.findItem(R.id.mi_logging);
        boolean isLoggingOn = Logger.isLoggingOn();
        if (isLoggingOn) {
            item.setTitle(R.string.disable_logging_message);
        } else {
        }

        return(super.onMenuOpened(featureId, menu));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.options, menu);

        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.mi_send_log_file:
    		sendLogFile();
    		break;
    	case R.id.mi_logging:
    		toggleLogging();
    		break;
    	case R.id.mi_about:
    		startActivity(new Intent(getApplicationContext(), AboutActivity.class));
    		break;

    	}
        return(super.onOptionsItemSelected(item));
    }
    /**
     * Send log file function for debug options
     */
    private void sendLogFile(){
    	Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.log_send_email)});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Aptina Camera Log Information");

        String path = Environment.getExternalStorageDirectory()+ "/" + "AptinaLogInfo.txt";
        if (FileUtils.isSdCardMounted() && FileUtils.isFileExist(path)) {
            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));
        } else {
            sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, Logger.getContent());
        }
        sendIntent.setType("text/html");
        startActivity(Intent.createChooser(sendIntent, "Send mail..."));
    }
    /**
     * Toggle the application logging to a file from the debug options menu
     */
    private void toggleLogging(){
    	 boolean isLoggingOn = !Logger.isLoggingOn();
         Logger.setLogging(isLoggingOn);
         if (isLoggingOn) {
             Toast.makeText(getApplicationContext(), R.string.logging_is_enabled_message, Toast.LENGTH_SHORT).show();
         } else {
             Toast.makeText(getApplicationContext(), R.string.logging_is_disabled_message, Toast.LENGTH_SHORT).show();
         }

         PreferencesProvider.setLogging(getApplicationContext(), isLoggingOn);
    }

    /**
     * Starts camera.
     */
    private void startCamera() {
        try {
            mPreviewHolderCallback = new CustomSurfaceHolderCallback();

            mPreviewHolder = mPreview.getHolder();
            mPreviewHolder.addCallback(mPreviewHolderCallback);
            mPreviewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                int numberOfCameras = Camera.getNumberOfCameras();
                if (mCurrentCameraIndex < 0) {
                    mCurrentCameraIndex = getBackCameraIndex();
                } else { 
                    mCurrentCameraIndex = mCurrentCameraIndex % numberOfCameras;
                }
                setSynchronizedCamera(Camera.open(mCurrentCameraIndex));
                
                if(getSynchronizedCamera() == null){
                	setSynchronizedCamera(Camera.open(mCurrentCameraIndex));
                }
                if (mCurrentCameraParameters == null) {
                    mCurrentCameraParameters = new Camera.Parameters[numberOfCameras];
                }

                if (mDefaultCameraParameters == null) {
                    mDefaultCameraParameters = new Camera.Parameters[numberOfCameras];
                }
            }
            
            if (getSynchronizedCamera() == null) {
            	setSynchronizedCamera(Camera.open());
                mCurrentCameraIndex = 0;
            }
        } catch(final Exception e) {
            Logger.logApplicationException(e, "VideoActivity.startCamera() failed during camera initialization.");
        }

        if(!mCamInit){
        	/**
             * Initialize all of the touch listeners after the camera has been started
             * 
             * Note: Camera has to be initialized first to prevent null pointer exceptions from occurring
             * on touch events
             */
            initTouchListeners();
        }
        
        try {
            if (mDefaultCameraParameters == null) {
                mDefaultCameraParameters = new Camera.Parameters[1];
            }

            if (mDefaultCameraParameters[mCurrentCameraIndex] == null) {
                mDefaultCameraParameters[mCurrentCameraIndex] = getSynchronizedCamera().getParameters();
            }

            if (mCurrentCameraParameters == null) {
                mCurrentCameraParameters = new Camera.Parameters[1];
                mCurrentCameraParameters[0] = getSynchronizedCamera().getParameters();
            } else if (mCurrentCameraParameters[mCurrentCameraIndex] == null) {
                mCurrentCameraParameters[mCurrentCameraIndex] = getSynchronizedCamera().getParameters();
            } else {

                getSynchronizedCamera().setParameters(mCurrentCameraParameters[mCurrentCameraIndex]);
                
            }
        } catch(final Exception e) {
            Logger.logApplicationException(e, "VideoActivity.startCamera() failed during setting camera parameters");
        }

        try {


            // Hide mode dialog.
        	mPanelFragment.showModeSelector(false);

            if (mCamcorderProfiles == null || mCamcorderProfiles[mCurrentCameraIndex] == null) {
                initCamcorders();
            }
            
            mPanelFragment.setResolutionListeners(getVideoSizes(), getImageSizes());

        } catch(final Exception e) {
            Logger.logApplicationException(e, "VideoActivity.startCamera() failed during components initializing");
        }
    }

    /**
     * Stops preview and releases camera.
     */
    private void stopCamera() {
        try {
            mCurrentCameraParameters[mCurrentCameraIndex] = getSynchronizedCamera().getParameters();

            if (mPreviewing) {
                getSynchronizedCamera().stopPreview();
            }
            mPreviewing = false;
            removeTouchListeners();
            getSynchronizedCamera().release();
            setSynchronizedCamera(null);
            
        } catch(final Exception e) {
            Logger.logApplicationException(e, "VideoActivity.stopCamera() failed");
        }
    }
    
    /**
     * Initializes recorder.
     */
    private void initRecorder() {
        if (mMediaRecorder != null) return;

        try {
            if (mCamcorderProfiles == null || mCamcorderProfiles[mCurrentCameraIndex] == null) {
                initCamcorders();
            }

            mMediaRecorder = new MediaRecorder();
            
            mMediaRecorder.setCamera(getSynchronizedCamera());
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);

            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            try {
                mMediaRecorder.setProfile(mCamcorderProfiles[mCurrentCameraIndex]);
            } catch(Exception e) {
            	e.printStackTrace();
            }

            currentFileName = getVideoPath(null);
            mMediaRecorder.setOutputFile(currentFileName);
            mMediaRecorder.setPreviewDisplay(mPreviewHolder.getSurface());

            long availableBytes = FileUtils.getAvailableBytes();
            if (availableBytes > 1024*1024 && availableBytes < 1024*1024*1024) { // If we have at least 1Mb.
                availableBytes -= 1024*1024;
                mMediaRecorder.setMaxFileSize(availableBytes);
            }

      
            mMediaRecorder.prepare();

            mMediaRecorder.setOnErrorListener(new OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    stopVideoRecording();
                }
            });
     
            mMediaRecorder.setOnInfoListener(new OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED) {
                        stopVideoRecording();
                    }
                }
            });
        } catch (Exception e) {
            Logger.logApplicationException(e, "VideoActivity.initRecorder() failed.");
        }
    }

    /**
     * Starts video recording.
     * 
     * @return true if success, false otherwise.
     */
    private boolean startVideoRecording() {
        try {
            getSynchronizedCamera().stopPreview();
            mCamLocked = false;
            getSynchronizedCamera().unlock();
            initRecorder();
            mMediaRecorder.start();          
            mRecordStartTime = System.currentTimeMillis();
            mInProgress = true;
            mPanelFragment.setPanelEnabled(false);
            mThumbFragment.setGalleryListener(false);
            mCaptureFragment.enableCamChanger(false);
            
            mCaptureFragment.setRecordIndicatorVisibility(View.VISIBLE);

            updateRecordTime();
            mVideoRecordingInProgress = true;
            return true;
        } catch (final Exception e) {
            Logger.logApplicationException(e, "VideoActivity.startVideoRecording(): Failed.");
            Log.e(TAG, "Error in startVideoRecording : " + e);
        }
        if(runningTest && signal != null){
        	signal.countDown();
        }

        return false;
    }

    /**
     * Stops video recording.
     * 
     * @return true if success, false otherwise.
     */
    private boolean stopVideoRecording() {
        try {      
        	mCaptureFragment.setRecordIndicatorVisibility(View.GONE);
            try {
                mMediaRecorder.stop();
                mCamLocked = true;
                getSynchronizedCamera().lock();
            } catch(final Exception e) {
            	Log.e(TAG, "Exception : " + e.getMessage());
            }
 
            mInProgress = false;
            mPanelFragment.setPanelEnabled(true);
            mThumbFragment.setGalleryListener(true);
            mCaptureFragment.enableCamChanger(true);

            try {
                mMediaRecorder.release();
            } catch(final Exception e) {
            }

            mMediaRecorder = null;
            mVideoRecordingInProgress = false;
            getSynchronizedCamera().startPreview();
            return true;
        }
        catch (final Exception e) {
            Logger.logApplicationException(e, "VideoActivity.stopVideoRecording(): Failed.");
        } finally {

            new SaveVideoAction().execute(currentFileName);
            checkSdSize();
        }
        return false;
    }

    /**
     * Async task to save video & update thumbnail.
     */
    private class SaveVideoAction extends AsyncTask<String, Object, Object> {

        /**
         * Thumbnail bitmap.
         */
        private Bitmap mBitmap = null;

        /**
         * Video file URI.
         */
        private Uri mUri = null;

        @Override
        protected Object doInBackground(String... params) {
            try {
                String filePath = params[0];
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

                String mimeType = "video/3gpp";
                if(fileName.endsWith(".3gp")){
                    mimeType = "video/3gpp";
                } else if(fileName.endsWith(".mp4")){
                    mimeType = "video/mp4";
                }

                ContentValues values = new ContentValues();
                values.put(Video.Media.TITLE, fileName);
                values.put(Video.Media.DISPLAY_NAME, fileName);
                values.put(Video.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put(Video.Media.MIME_TYPE, mimeType);
                values.put(Video.Media.DATA, filePath);
                mUri = getContentResolver().insert(Video.Media.EXTERNAL_CONTENT_URI, values);
                String stringUri = mUri.toString(); 
                int videoId = Integer.parseInt(stringUri.substring(stringUri.lastIndexOf("/")+1));
                mBitmap = MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), videoId, MediaStore.Video.Thumbnails.MINI_KIND, null);
            } catch (final Exception e) {
                Logger.logApplicationException(e, "VideoActivity.addToGallery(): Failed.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object data) {
            if ((mBitmap != null) && (mUri != null)) {
            	mThumbFragment.setGalleryThumbnail(mBitmap, mUri);
            	mThumbFragment.showGallery();
            }
        }
    }

    /**
     * Switches to next available camera.
     * 
     * @param cameraIndex camera index.
     */
    private void switchCamera() {
        // KPI logging.
        updateKpiTime();
        Logger.logMessage("Switching cameras started at " + DateTimeUtils.formatDate(mCurrentDate));

        stopCamera();
        mCurrentCameraIndex++;
        startCamera();
        try {
        	Camera cam = getSynchronizedCamera();
        	if(cam != null){
        		cam.setPreviewDisplay(mPreviewHolder);
        	}

        } catch (IOException e) {
            Logger.logApplicationException(e, "VideoActivity.switchCamera() failed");
        }
        startPreviewWithOptimalParameters(mPreviewHolder.getSurfaceFrame().width(), mPreviewHolder.getSurfaceFrame().height());

        // KPI Logging.
        updateKpiTime();
        Logger.logMessage("Camera preview started at " + DateTimeUtils.formatDate(mCurrentDate) + ", time diff: " + getTimeDiff());
    }

    /**
     * Looking for first available back camera.
     * 
     * @return index of first available back camera.
     */
    private int getBackCameraIndex() {
        try {
            for (int i=0; i < Camera.getNumberOfCameras(); i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);

                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    return i;
                }
            }
        } catch(final Exception e) {
            Logger.logApplicationException(e, "VideoActivity.getBackCameraIndex() failed.");
        }
        return 0;
    }


    private boolean isSizeSupported(Size[] mSizeList, int width, int height) {
            int i, arraySiz;
            boolean isSupported = false;
            arraySiz = mSizeList.length;

            for(i = 0; i < arraySiz; i++){
                    if((mSizeList[i].width == width) && 
                       (mSizeList[i].height == height)){
                            isSupported = true;
                            break;
                    }
            }
            return isSupported;
    }
    /**
     * Sets optimal preview size and starts preview.
     *  
     * @param width preview view width.
     * @param height preview view height.
     */
    private void startPreviewWithOptimalParameters(int width, int height) {
    	Log.e(TAG, "optimal params : " + width + ", " + height);
        try {

            Parameters parameters = getSynchronizedCamera().getParameters();
            parameters.set("capture", "video");
//            parameters.setFocusMode(CameraInfo.CAMERA_MODE_AUTO);
         
            if (CameraInfo.VIDEO_MODE_DVS.equals(mActiveMode) && (mCamcorderProfiles[mCurrentCameraIndex].videoFrameWidth != CameraInfo.VGA_WIDTH ||
					mCamcorderProfiles[mCurrentCameraIndex].videoFrameHeight != CameraInfo.VGA_HEIGHT)) {
                parameters.set(CameraInfo.VIDEO_MODE_DVS, CameraInfo.CAMERA_MODE_ENABLED_DEFAULT_VALUE);
            } else {
                parameters.set(CameraInfo.VIDEO_MODE_DVS, CameraInfo.CAMERA_MODE_DISABLED_DEFAULT_VALUE);
            }

            

             if(isSizeSupported(getPreviewSizes(), width, height)){
            	Log.i(TAG, "getPreviewSizes()[0] returning : " + width + ", " + height);
//                preserveAspectRatio(dispDim.x, dispDim.y, width, height);
                parameters.setPreviewSize(width, height);
            } else {
                Camera.Size size = getPreviewSizes()[0];
                Log.i(TAG, "getPreviewSizes()[0] returning : " + size.width + ", " + size.height);
//                preserveAspectRatio(dispDim.x, dispDim.y, size.width, size.height);
                parameters.setPreviewSize(size.width, size.height);
            }


        	//See if Video snapshot is supported at this recording resolution
            parameters = toggleVideoSnapshot(parameters);
            if(parameters != null){
            	getSynchronizedCamera().setParameters(parameters);
            	mCurrentCameraParameters[mCurrentCameraIndex] = parameters;
            }else{
            	getSynchronizedCamera().setParameters(mCurrentCameraParameters[mCurrentCameraIndex]);
            }
            
 
            

            getSynchronizedCamera().startPreview();
            mPreviewing = true;

        }
        catch(final Exception e) {
            Logger.logApplicationException(e, "VideoActivity.startPreviewWithOptimalParameters() failed.");
        }
    }
    /**
     * Function sets the margins of the preview surface in order to preserve aspect ratio 
     * with the preview size the camera returns, instead of stretching the image to 
     * fit the screen dimensions
     */
    private void preserveAspectRatio(int surface_width, int surface_height, int preview_width, int preview_height){
    	float surface_ratio = surface_width / (float)surface_height;
    	float preview_ratio = preview_width / (float)preview_height;
    	float width_ratio = surface_width / (float)preview_width;
    	float height_ratio = surface_height / (float)preview_height;
    	Log.i(TAG, "width_ratio : " + width_ratio + ", height_ratio : " + height_ratio);
  

    	ViewGroup.LayoutParams sv_p = mainVideoFrame.getLayoutParams();
 
    	if(surface_ratio != preview_ratio){
    		if(width_ratio > height_ratio){
    			sv_p.height = surface_height;

        		double Kh = surface_height/(float)preview_height;
        		sv_p.width = (int) (preview_width * Kh);
        		Log.i(TAG, "sv_p.width : " + sv_p.width );
    		}else{
        		sv_p.width = surface_width;
        		
        		double Kw = surface_width/(float) preview_width;
        		sv_p.height = (int) (preview_height * Kw);
        		Log.i(TAG, "sv_p.height : " + sv_p.height );
    		}

    	}

    	mainVideoFrame.setLayoutParams(sv_p);
    	ViewGroup.LayoutParams  preview_p = previewSurface.getLayoutParams();
    	preview_p.width = sv_p.width;
    	preview_p.height = sv_p.height;
    	previewSurface.setLayoutParams(preview_p);


    }
    /**
     * Retrieves Array of available preview sizes
     * 
     * @return Array of available preview sizes.
     */
    private Size[] getPreviewSizes(){
    	return CameraInfo.getZSLPreviewSizes(mDefaultCameraParameters[mCurrentCameraIndex], getSynchronizedCamera(), mActiveMode, true);
    }

    /**
     * Retrieves Array of available image sizes
     * 
     * @return Array of available image sizes.
     */
    public Size[] getImageSizes(){ 
    	return CameraInfo.sortPictureSizes(mDefaultCameraParameters[mCurrentCameraIndex], getSynchronizedCamera(), mActiveMode);
    }

    /**
     * Sets preview display and starts preview.
     * 
     * @param width width of preview holder.
     * @param height height of preview holder.
     */
    public void startCameraPreview(int width, int height) {
        try {
            getSynchronizedCamera().setPreviewDisplay(mPreviewHolder);
        }
        catch (Exception e) {
            Logger.logApplicationException(e, "VideoActivity.startCameraPreview() failed");
        }

        startPreviewWithOptimalParameters(width, height);

        //Set the view gone after parent R.id.layout has been initialized so that it has a size greater than (0,0), which
        //messes up the first video snapshot animation
        ImageView image = (ImageView) findViewById(R.id.video_snap_anim);
        image.setVisibility(View.GONE);


		
        new Thread(new Runnable() {
            @Override
            public void run() {
            	mThumbFragment.updateGallery(getContentResolver(), ThumbnailControl.TYPE_VIDEO);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	mThumbFragment.showGallery();
                    }
                });
            }
        }).start();
    }
    
    /**
     * The preview callback for our histogram, have to set the {@link #mCamera} in the {@link #mPreviewHolderCallback}
     * for it to be called
     */
    private PreviewCallback mPreviewCallback = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
        	Log.d("onPreviewFrame-surfaceChanged",String.format("Got %d bytes of camera data", data.length));
        	mHistPageAdapter.update(data);
        }
     };
    /**
     * Callback from surface holder.
     */
    private class CustomSurfaceHolderCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        	getSynchronizedCamera().setPreviewCallback(mPreviewCallback);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        	Log.d(TAG, "surfaceChanged w,h : " + width + ", " + height);
        	
            mPreviewHolder = holder;
            if (!mPreviewing) {
            	dispDim.x = width;
            	dispDim.y = height;
            	
                mSurfaceWasDestroyed = false;
                startCameraPreview(width, height);
            } else {
                try {
                    getSynchronizedCamera().setPreviewDisplay(mPreviewHolder);
                } catch (IOException e) {
                    Logger.logApplicationException(e, "CustomSurfaceHolderCallback.surfaceChanged: failed to start preview");
                }
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mSurfaceWasDestroyed = true;
        }
    };
  
    /**
     * Retrieves name for photo (without extension).
     * 
     * @return photo name.
     */
    private String getVideoName() {
        return "MOV-" + DateTimeUtils.getPhotoFormatDate(new Date());
    }

    /**
     * Retrieves path for video or null if failed.
     * 
     * @param givenName already given name that should be used for result path. Can be null.
     * 
     * @return path for video. 
     */
    private String getVideoPath(String givenName) {
        try {
            String fileName = givenName == null ? getVideoName() : givenName;
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + fileName + ".3gp";

            File directory = new File(path).getParentFile();
            if (!directory.exists() && !directory.mkdirs()) {
                throw new IOException("Couldn't create path to file: " + path);
            }
            new File(path).createNewFile();
            return path;
        }
        catch(final Exception e) {
            Logger.logApplicationException(e, "VideoActivity.SavePhotoAction.getPhotoPath() failed.");
        }
        return null;
    }

    /**
     * Listener to change camera (front/back).
     */
    private class CameraChangerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switchCamera();
        }

    }



    /**
     * Updates previos & current time properties.
     */
    private void updateKpiTime() {
        mPreviousDate = mCurrentDate;
        mCurrentDate = new Date();
    }

    /**
     * Updates record time.
     */
    private static void updateRecordTime() {
        if (!mInProgress) {
            return;
        }

        long recordTime = System.currentTimeMillis() - mRecordStartTime;
        long next_update_delay = 1000 - (recordTime % 1000);
        String recordTimeString = getFormattedRecordString(recordTime);
        CaptureFragment.setRecordingText(recordTimeString);
        mHandler.sendEmptyMessageDelayed(
                UPDATE_RECORD_TIME, next_update_delay);
    }

    /**
     * Retrieves record time as string.
     * 
     * @param recordTime current record time in milliseconds.
     * 
     * @return record time as string.
     */
    private static String getFormattedRecordString(long recordTime) {
        recordTime /= 1000;
        String hours = Long.toString(recordTime / (60*60));
        if (hours.length() < 2) hours = "0" + hours;
        String mins = Long.toString((recordTime % (60*60)) / 60);
        if (mins.length() < 2) mins = "0" + mins;
        String secs = Long.toString(recordTime % 60);
        if (secs.length() < 2) secs = "0" + secs;
        return String.format("%s:%s:%s", hours, mins, secs);
    }


    /**
     * Returns difference between current and previous timestamps in milliseconds.
     * 
     * @return Difference between current & previously saved timestamps.
     */
    private long getTimeDiff() {
        return (mCurrentDate.getTime() - mPreviousDate.getTime());
    }

    /**
     * Initializes camcorders.
     */
    private void initCamcorders() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (mCamcorderProfiles == null) {
                mCamcorderProfiles = new CamcorderProfile[Camera.getNumberOfCameras()];
            }

            if (mCamcorderProfiles[mCurrentCameraIndex] == null) {
                mCamcorderProfiles[mCurrentCameraIndex] = CamcorderProfile.get(mCurrentCameraIndex, CamcorderProfile.QUALITY_HIGH);
            }
        } else {
            if (mCamcorderProfiles == null) {
                mCamcorderProfiles = new CamcorderProfile[1];
            }

            if (mCamcorderProfiles[mCurrentCameraIndex] == null) {
                mCamcorderProfiles[mCurrentCameraIndex] = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            }
        }

        Size size = getVideoSizes()[0];

        mCamcorderProfiles[mCurrentCameraIndex] = adaptCamcorderForNewResolution(mCamcorderProfiles[mCurrentCameraIndex], size.width, size.height);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
            	if(mPanelFragment.hideOverlays() || mOptionsFragment.hideOverlays()){
            		return  true;
            	}
            }
            
        }
        catch(final Exception e) {
            Logger.logApplicationException(e, "VideoActivity.onKeyDown() failed");
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Retrieves Array of available sizes.
     * 
     * @return Array of available sizes.
     */
    public Size[] getVideoSizes() { 
    	mActiveMode = CameraInfo.CAMERA_MODE_AUTO;
  
        Size[] videoSizes = CameraInfo.getVideoSizes(getSynchronizedCamera().getParameters());    

        return videoSizes;
        
    }
    
    private ResolutionChangeInterface videoResolutionCallback = new ResolutionChangeInterface(){

		@Override
		public void onResolutionSelected(Size resolution, int index) {
	    	Size[] size = getVideoSizes();
	        try {

	            if (mCamcorderProfiles == null) {
	                initCamcorders();
	            }
	
	            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
	                mCamcorderProfiles[mCurrentCameraIndex] = CamcorderProfile.get(mCurrentCameraIndex, CamcorderProfile.QUALITY_HIGH);
	            } else {
	                mCamcorderProfiles[mCurrentCameraIndex] = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
	            }
	            mCamcorderProfiles[mCurrentCameraIndex] = adaptCamcorderForNewResolution(mCamcorderProfiles[mCurrentCameraIndex], size[index].width, size[index].height);

	            getSynchronizedCamera().stopPreview();

	            startPreviewWithOptimalParameters(mCamcorderProfiles[mCurrentCameraIndex].videoFrameWidth,
	                    mCamcorderProfiles[mCurrentCameraIndex].videoFrameHeight);
	        } catch(final Exception e) {
	            Logger.logApplicationException(e, "VideoActivity.onResolutionSelected(): Failed.");
	        }
			
		}

		@Override
		public Size getCurrentResolution() {
			Camera cam = getSynchronizedCamera();
			if(cam == null){
				cam = getSynchronizedCamera();
			}
			return cam != null ? cam.new Size(mCamcorderProfiles[mCurrentCameraIndex].videoFrameWidth, 
					mCamcorderProfiles[mCurrentCameraIndex].videoFrameHeight) : null;
		}
    	
    };
    
    
    
    private ResolutionChangeInterface snapshotResolutionCallback = new ResolutionChangeInterface(){

		@Override
		public void onResolutionSelected(Size resolution, int index) {
			mCurrentCameraParameters[mCurrentCameraIndex].setPictureSize(resolution.width, resolution.height);
			getSynchronizedCamera().setParameters(mCurrentCameraParameters[mCurrentCameraIndex]);
			
		}

		@Override
		public Size getCurrentResolution() {
			Camera cam;
			if((cam = getSynchronizedCamera()) != null){
				Camera.Size pic = cam.getParameters().getPictureSize();
				return cam.new Size(pic.width, pic.height);
			}
			return null;
			
		}
    	
    };
    
    
    /**
     * Check if video snapshot is supported at the video recording
     * resolution, if not disable video snapshot parameter and change 
     * snapshot resolution selector button to NA
     * 
     * @param videoResolution Resolution to check for snapshot support
     */
    boolean snapshot_supported_at_resolution = false;
    private Camera.Parameters toggleVideoSnapshot(Camera.Parameters params){
    	if(params != null){
    		Size videoResolution = params.getPreviewSize();
        	snapshot_supported_at_resolution = CameraInfo.snapshotSupportedRes(videoResolution);
        	
        	//We can't have snapshot and DVS on at the same time
        	snapshot_supported_at_resolution = snapshot_supported_at_resolution 
        			&& (mDVSMode.equalsIgnoreCase(CameraInfo.VIDEO_MODE_DVS_OFF));
        	
        	RelativeLayout snap_button = (RelativeLayout) findViewById(R.id.snapshot_resolution_button);
        	if(snapshot_supported_at_resolution){
        		params.set("videosnapshot-mode", "enable");
        		snap_button.setVisibility(View.VISIBLE);
        		snap_button.setClickable(true);
        	}else{
        		params.set("videosnapshot-mode", "disable");
        		snap_button.setVisibility(View.GONE);
        		snap_button.setClickable(false);
        	}
        	return params;
    	}
    	return null;
    }

    /**
     * Adapts video parameters in camcorder for new resolution.
     * 
     * @param camcorderProfile Current camcorder profile.
     * @param newWidth New width for adapting.
     * @param newHeight New height for adapting.
     * 
     * @return Adapted camcorder.
     */
    private CamcorderProfile adaptCamcorderForNewResolution(CamcorderProfile camcorderProfile, int newWidth, int newHeight) {
        int width = camcorderProfile.videoFrameWidth;
        int height = camcorderProfile.videoFrameHeight;
        int bitRate = camcorderProfile.videoBitRate;
        camcorderProfile.videoFrameWidth = newWidth;
        camcorderProfile.videoFrameHeight = newHeight;
        camcorderProfile.videoBitRate = Math.round((((float)(newWidth * newHeight))/((float)width * height))*bitRate);
        return camcorderProfile;
    }

    /**
     * Custom handler that handles application messages.
     */
    private static class VideoHandler extends Handler { 
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
	            case UPDATE_RECORD_TIME:
	                updateRecordTime();
	                break;
            
            default:
                Logger.logMessage("VideoActivity.VideoHand.handleMessage() received unhandled message: " + msg.what);
                break;
            }
        }

		
    }
    
    //For functional test
    public Size getCurrentVideoResolution() {
		return getSynchronizedCamera().new Size(mCamcorderProfiles[mCurrentCameraIndex].videoFrameWidth, mCamcorderProfiles[mCurrentCameraIndex].videoFrameHeight);
	}

    public Size getCurrentSnapshotResolution() {
		Camera.Size pic = getSynchronizedCamera().getParameters().getPictureSize();
		return getSynchronizedCamera().new Size(pic.width, pic.height);
	}
    
    public void VideoResSelected(Size resolution, int index) {
    	Size[] size = getVideoSizes();
        try {
            if (mCamcorderProfiles == null) {
                initCamcorders();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                mCamcorderProfiles[mCurrentCameraIndex] = CamcorderProfile.get(mCurrentCameraIndex, CamcorderProfile.QUALITY_HIGH);
            } else {
                mCamcorderProfiles[mCurrentCameraIndex] = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            }
            mCamcorderProfiles[mCurrentCameraIndex] = adaptCamcorderForNewResolution(mCamcorderProfiles[mCurrentCameraIndex], size[index].width, size[index].height);

            getSynchronizedCamera().stopPreview();

            startPreviewWithOptimalParameters(mCamcorderProfiles[mCurrentCameraIndex].videoFrameWidth,
                    mCamcorderProfiles[mCurrentCameraIndex].videoFrameHeight);
        } catch(final Exception e) {
            Logger.logApplicationException(e, "VideoActivity.onResolutionSelected(): Failed.");
        }
    }

}

