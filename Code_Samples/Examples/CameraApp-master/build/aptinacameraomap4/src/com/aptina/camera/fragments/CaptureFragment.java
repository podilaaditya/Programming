/**
 * 
 */
package com.aptina.camera.fragments;

import android.app.Fragment;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aptina.R;
import com.aptina.camera.components.ShutterButton;
import com.aptina.camera.interfaces.CaptureFragmentInterface;

/**
 * @author stoyan
 *
 */
public class CaptureFragment extends Fragment {
	/**
	 * Logging tag
	 */
	private static final String TAG = "CaptureFragment";
	
    /**
     * Button to make photos.
     */
    private ShutterButton mShutterButton;
    
    /**
     * Switches between front/back cameras.
     */
    private ImageView mCameraChanger;
    
    /**
     * The imageview for the HDR on icon overlay in the top right corner
     */
    private ImageView mHDRIconOverlay = null;
    
    /**
     * Icons for DVS on/off
     */
    private ImageView mDvsOn = null;
    
    /**
     * Record indicator.
     */
    private LinearLayout recordIndicator;
    
    /**
     * Textview to display record time.
     */
    private static TextView recordingText;
    
    /**
     * Callback for the {@link #mShutterButton} and {@link #mCameraChanger}
     */
    private CaptureFragmentInterface mCallback;
	
    public void setInterface(CaptureFragmentInterface callback){
    	mCallback = callback;
    	if(mCallback != null){
    		enableShutter(true);
    		enableCamChanger(true);
    		
    	}else{
    		mShutterButton.setOnClickListener(null);
    		mCameraChanger.setOnClickListener(null);
    	}
    }
    
	/**
	 * Instantiate the fragment with the {@link #setArguments(Bundle)} called 
	 */
	public static final CaptureFragment newInstance()
	{
		CaptureFragment frag = new CaptureFragment();
	    return frag;
	}
	/**
	 * Set all the arguments for the fragment and initialize the graph
	 */
	@Override
	public void setArguments(Bundle bundle) {
		if(bundle != null){
			
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		final View fragV = inflater.inflate(R.layout.video_capture_control_fragment, container, false);
		mShutterButton = (ShutterButton) fragV.findViewById(R.id.btn_shutter);
		mCameraChanger = (ImageView) fragV.findViewById(R.id.img_camera_change);
		mHDRIconOverlay = (ImageView) fragV.findViewById(R.id.hdr_overlay_indicator);
		recordIndicator = (LinearLayout) fragV.findViewById(R.id.video_recording_indicator);
		recordingText = (TextView) fragV.findViewById(R.id.record_time);
		mDvsOn = (ImageView) fragV.findViewById (R.id.dvs_on);
        return fragV;
    }
	@Override
    public void onResume() {
        super.onResume();
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}
	
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    /**
     * Function used to toggle the state of the {@link #mCameraChanger}
     * @param enable
     */
    public void enableCamChanger(boolean enable){
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            int numberOfCameras = Camera.getNumberOfCameras(); 

            if (numberOfCameras > 1) {
            	mCameraChanger.setVisibility(View.VISIBLE);
            	if(enable){
                    mCameraChanger.setOnClickListener(new OnClickListener(){

    					@Override
    					public void onClick(View v) {
    						mCallback.onCameraChangerClick();
    					}
                    	
                    });
            	}else{
            		mCameraChanger.setOnClickListener(null);
            	}
                
            }
        }
    	
    }
    
    /**
     * Function used to toggle the state of the {@link #mShutterButton}
     * @param enable
     */
    public void enableShutter(boolean enable){
    	if(enable){
    		mShutterButton.setOnClickListener(new OnClickListener(){

    			@Override
    			public void onClick(View v) {
    				mCallback.onShutterClick();
    				
    			}
    			
    		});
    	}else{
    		mShutterButton.setOnClickListener(null);
    	}
    	
    }
    
    public void setDVSIndicator(boolean toggle) {
    	if (toggle) { //on   		
    		mDvsOn.setVisibility(View.VISIBLE);
    	} else { //off
    		mDvsOn.setVisibility(View.GONE);
    	}
    	
    	mDvsOn.postInvalidate();
    	
	}
    
    /**
     * Return the {@link #mShutterButton}
     * @return
     */
    public ShutterButton getShutterButton(){
    	return mShutterButton;
    }

	public void setHDRVisibility(int visibility) {
		mHDRIconOverlay.setVisibility(visibility);
	}

	public void setRecordIndicatorVisibility(int visibility) {
		recordIndicator.setVisibility(visibility);
	}

	public static void setRecordingText(String recordTimeString) {
		recordingText.setText(recordTimeString);
	}
}
