/**
 * 
 */
package com.aptina.camera.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aptina.R;
import com.aptina.camera.CameraInfo;
import com.aptina.camera.components.ModeSelector;
import com.aptina.camera.components.OptionSet;
import com.aptina.camera.components.OptionsMenu;
import com.aptina.camera.components.SizeHolder;
import com.aptina.camera.eventlisteners.PanelSlideListener;
import com.aptina.camera.eventlisteners.ResolutionChangeListener;
import com.aptina.camera.interfaces.MenuLayoutInterface;
import com.aptina.camera.interfaces.PanelFragInterface;

/**
 * @author stoyan
 *
 */
public class PanelFragment extends Fragment {
	/**
	 * Logging tag
	 */
	private static final String TAG = "PanelFragment";
	
    /**
     * Left settings panel.
     */
    private RelativeLayout mPanel = null;
    
    /**
     * Left panel image.
     */
    private ImageView mSliderHandle = null;
    
    /**
     * Mode selector view.
     */
    private ModeSelector mModeSelector = null;
    
    /**
     * Resolution buttons.
     */
    private View mVideoResolutionButton = null;
    private View mSnapResolutionButton = null;
    
    /**
     * Chosen resolution drawer.
     */
    private SizeHolder mVideoResolutionValue = null;
    private SizeHolder mSnapResolutionValue = null;
    
    /**
     * Resolution panel buttons listeners.
     */
    private ResolutionChangeListener mVideoResolutionChangeListener = null;
    private ResolutionChangeListener mSnapResolutionChangeListener = null;
    
    /**
     * The interface that we use to interface with the logic in videoactivity
     */
    private PanelFragInterface mCallback;
    
    public void setInterface(PanelFragInterface callback){
    	mCallback = callback;
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
		final View fragV = inflater.inflate(R.layout.video_panel, container, false);
		mPanel = (RelativeLayout) fragV.findViewById(R.id.panel_layout);
		mSliderHandle = (ImageView) fragV.findViewById(R.id.slider_handle);
		mModeSelector = (ModeSelector) fragV.findViewById(R.id.mode_selector);
		
		mVideoResolutionButton = fragV.findViewById(R.id.video_resolution_button);		
        mVideoResolutionValue = (SizeHolder) fragV.findViewById(R.id.video_resolution_value);
        
        mSnapResolutionButton = fragV.findViewById(R.id.snapshot_resolution_button);
        mSnapResolutionValue = (SizeHolder) fragV.findViewById(R.id.snapshot_resolution_value);
        
        
		
		
	        
        return fragV;
    }
	
	@Override
    public void onResume() {
        super.onResume();
        initListeners();
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
    
    private void initListeners(){
    	PanelSlideListener listener = new PanelSlideListener(mPanel, this.getActivity());
	    mPanel.setOnTouchListener(listener);        
	    mSliderHandle.setOnTouchListener(listener);
    }
    

	public void setResolutionListeners(Size[] videoSizes, Size[] imageSizes) {
		// Inits resolution changers.
        if (mVideoResolutionChangeListener == null || mSnapResolutionChangeListener == null) {
        	mVideoResolutionChangeListener = new ResolutionChangeListener(this.getActivity(), videoSizes, mModeSelector, mOptionsMenuView, true);
        	mSnapResolutionChangeListener = new ResolutionChangeListener(this.getActivity(), imageSizes, mModeSelector, mOptionsMenuView, false);
        	
        	mVideoResolutionButton.setOnClickListener(mVideoResolutionChangeListener);
        	mSnapResolutionButton.setOnClickListener(mSnapResolutionChangeListener);
        	
            mVideoResolutionChangeListener.setCallbackTarget(mCallback.getVideoResolutionCallback());
            mSnapResolutionChangeListener.setCallbackTarget(mCallback.getSnapshotResolutionCallback());

        }
        
        mVideoResolutionChangeListener.setSizes(videoSizes);
        mSnapResolutionChangeListener.setSizes(imageSizes);
        
        mVideoResolutionValue.assignSize(videoSizes[0]);
        mSnapResolutionValue.assignSize(imageSizes[0]);
		
	}
	
    /**
     * Sets panel buttons clickable/disabled.
     * 
     * @param enabled <b>true</b> to enable panel buttons, <b>false</b> to disable.
     */
    public void setPanelEnabled(boolean enabled) {
    	mVideoResolutionButton.setClickable(enabled);
    	mSnapResolutionButton.setClickable(enabled);
        mResetButton.setClickable(enabled);
        mSwitchButton.setClickable(enabled);
    }
	public void showModeSelector(boolean visible){
		if(visible){
			mModeSelector.setVisibility(View.VISIBLE);
		}else{
			mModeSelector.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Hides the overlays of the {@link #mModeSelector} if it was showing
	 * @return True if the overlays where showing
	 */
	public boolean hideOverlays(){
		if(mModeSelector.getVisibility() == View.VISIBLE){
			mModeSelector.setVisibility(View.GONE);
        	return true;
		}
		return false;
	}
	public void hideModeSelector() {
		mModeSelector.setVisibility(View.GONE);
	}
	
	
	
	
	
	
	
	
	
	
    
    
    
	 
	 	
    
    
    
}
