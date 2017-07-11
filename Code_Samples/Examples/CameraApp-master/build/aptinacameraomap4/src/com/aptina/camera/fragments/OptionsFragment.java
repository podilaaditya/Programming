/**
 * 
 */
package com.aptina.camera.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aptina.R;
import com.aptina.camera.CameraInfo;
import com.aptina.camera.components.OptionSet;
import com.aptina.camera.components.OptionsMenu;
import com.aptina.camera.interfaces.MenuLayoutInterface;
import com.aptina.camera.interfaces.OptionsFragInterface;

/**
 * @author stoyan
 *
 */
public class OptionsFragment extends Fragment{
	/**
	 * Logging tag
	 */
	private static final String TAG = "OptionsFragment";
	
	 /**
     * Options view.
     */
    private View mOptionsButton;
    
    /**
     * Variable to hold our focus mode dialog
     */
    private OptionsMenu mOptionsMenu;
    
    /**
     * Interface for handling menu options
     */
    private MenuLayoutInterface mMenuLayoutInterface;
    
    /**
     * Click listener for toggling the {@link #mOptionsMenuView} after
     * clicking the {@link #mOptionsButton}
     */
    private OnClickListener optionsDialogListener;
    
    /**
     * Focus menu view, needs to be populated by OptionsMenu class
     */
    private LinearLayout mOptionsMenuView = null;
    
    /**
     * The list of focuses available, fits inside {@link #mOptionsMenuView}
     */
    private LinearLayout mFocusMenuList = null;
    
    /**
     * The toggle of histogram visible/invisible, fits inside {@link #mOptionsMenuView}
     */
    private LinearLayout mHistogramMenuList = null;

    /**
     * The list of HDR modes, fits inside {@link #mOptionsMenuView}
     */
    private LinearLayout mHDRMenuList = null;
    
    /** 
     * Camera Focus Settings Arrays 
     */
	private String[] mFocusOptions;
	
	/**
     * The interface that we use to interface with the logic in video activity 
     * and the option button in panel fragment
     */
    private OptionsFragInterface mCallback;
    
    public void setInterface(OptionsFragInterface callback){
    	mCallback = callback;
    }
	/**
	 * Instantiate the fragment with the {@link #setArguments(Bundle)} called 
	 */
	public static final OptionsFragment newInstance()
	{
		OptionsFragment frag = new OptionsFragment();
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
		final View fragV = inflater.inflate(R.layout.options_fragment, container, false);
        //TODO turn into its own fragment
        mOptionsButton = fragV.findViewById(R.id.options_button);
        mOptionsMenuView = (LinearLayout) fragV.findViewById(R.id.options_menu);
        mFocusMenuList = (LinearLayout) fragV.findViewById(R.id.ll_menu_focuses);
        mHistogramMenuList = (LinearLayout) fragV.findViewById(R.id.ll_menu_histogram);
        mHDRMenuList = (LinearLayout) fragV.findViewById(R.id.ll_menu_hdr);
		
		
	        
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
    
    private void initMenuListeners(){
    	if(optionsDialogListener == null){
    		optionsDialogListener = new OnClickListener() {
        		@Override
        		public void onClick(View argv) {
        			mCallback.hideModeSelector();
        			if(mOptionsMenuView.getVisibility() == View.GONE){
        				initOptions();
        			}else{
        				setOptions();
        				removeOptions();
        			}

        		}

            };
    	}
        
        mOptionsButton.setOnClickListener(optionsDialogListener);
        if(mMenuLayoutInterface == null){
    		mMenuLayoutInterface = new MenuLayoutInterface() {
    	    	/**
    			 * Add each focus option view to the focus menu section
    			 * of camera.xml
    			 * @param view The focus menu option
    			 */
    			@Override
    			public void addFocusViewToLayout(View view) {
    				mFocusMenuList.addView(view);
    			}
    			
    			/**
    			 * Add the histogram controls to the menu
    			 * @param view
    			 */
				@Override
				public void addHistogramViewToLayout(View view) {
					mHistogramMenuList.addView(view);
					
				}
				/**
				 * Add the HDR mode controls to the menu
				 * @param view
				 */
				@Override
				public void addHDRViewToLayout(View view) {
					mHDRMenuList.addView(view);
					
				}
    			/**
    			 * Toggle the focus menu section of camera.xml visible
    			 * once all of the options items have been created and added
    			 * to the main view. Menu panel button should be used to toggle
    			 *
    			 * @param visible Toggle boolean
    			 */
    			@Override
    			public void setVisible(boolean visible) {
    				mOptionsMenuView.setVisibility(visible ? View.VISIBLE : View.GONE);
    				
    				//Hide the histogram so it does not overlap menu
    				mCallback.setHistogramVisibility(visible ? View.GONE : View.VISIBLE);
    			}
    		};	
    	}
    	
    	if(mOptionsMenu == null){
    		mOptionsMenu = new OptionsMenu(this.getActivity(), mMenuLayoutInterface);
    	}
    }    
    
    /**
	  * Initiates all of the options in the options view,
	  * calls {@link #initFocusOptions()}, {@link #initHistogramOptions()},
	  * and {@link #initHdrOptions()}
	  */
	 private void initOptions(){
		initFocusOptions();
		initHistogramOptions();
		initHdrOptions();
		mOptionsMenu.setVisible(true);
	 }
	 
	 /**
	  * Get the focus modes and populate the custom dialog class.
	  * Set click listeners for the different focus options views
	  */
	 private void initFocusOptions(){
		 mFocusMenuList.removeAllViews();
		 List<String> focusModes = null;
		 if (mCallback.getCamera() != null && (focusModes = mCallback.getCamera().getParameters().getSupportedFocusModes()) != null && !focusModes.isEmpty()) {
					List<String> list = new ArrayList<String>();
					for(String s : focusModes) {
		        		if(s.equals(CameraInfo.FOCUS_CONTINUOUS_VIDEO)) {//TODO add focus support
		        			list.add(s);
		        		}
		        	}
					focusModes = list;

					mFocusOptions = focusModes.toArray(new String[focusModes.size()]);
					
					String focusMode= mCallback.getCamera().getParameters().getFocusMode();

					mOptionsMenu.setFocusModesArray(mFocusOptions, focusMode);
				}
	 }
	 	 
	 private void initHistogramOptions(){
	 	mHistogramMenuList.removeAllViews();
	 	String[] histOpt = getResources().getStringArray(R.array.HistogramOptions);
	 	String current = mCallback.getHistogramVisibility() == View.VISIBLE ? histOpt[0] : histOpt[1];
		mOptionsMenu.setHistogramArray(histOpt, current);
	 }
	 	 
	 private void initHdrOptions(){
	 	mHDRMenuList.removeAllViews();
	 	String[] hdrOpt = getResources().getStringArray(R.array.HDROptions);
	 	mOptionsMenu.setHDRArray(hdrOpt, null);
	 }
	 private void setOptions(){
	 	setFocusOptions(mOptionsMenu.getFocusSet());
	 	setHistOptions(mOptionsMenu.getHistogramSet());
	 	setHDROptions(mOptionsMenu.getHDRSet());
	 }
	 	
	 /**
	  * Call to change the focus mode of the camera
	  * @param focusSet The focus {@link OptionSet} from {@link #mOptionsMenu}
	  */
	 private void setFocusOptions(OptionSet focusSet){
	 	mCallback.setFocusOptions(focusSet);
	 }
	 /**
	  * Call to change the histogram mode of the camera
	  * 
	  * @param histSet The histogram {@link OptionSet} from {@link #mOptionsMenu}
	  */
	 private void setHistOptions(OptionSet histSet){
	 	int vis = histSet.getMode().equals(histSet.getAllModes()[0])  ? View.VISIBLE : View.INVISIBLE;
	 	mCallback.setHistogramVisibility(vis);
	 }
	 	
	 /**
	  * Call to change the histogram mode of the camera
	  * 
	  * @param hdrSet The HDR {@link OptionSet} from {@link #mOptionsMenu}
	  */
	 private void setHDROptions(OptionSet hdrSet){
	 }
	 	
	 /**
	  * Call to change the histogram mode of the camera
	  */
	 private void removeOptions(){
	 	mHistogramMenuList.removeAllViews();
	 	mHDRMenuList.removeAllViews();
	 	mOptionsMenuView.setVisibility(View.GONE);
	 }	
	 
	 /**
	  * Hides the overlays of the {@link mOptionsMenuView} it was showing
	  * @return True if the overlays where showing
	  */
	 public boolean hideOverlays(){
		if(mOptionsMenuView.getVisibility() == View.VISIBLE){
	        mMenuLayoutInterface.setVisible(false);
	        return true;
		}
		return false;
	}
}
