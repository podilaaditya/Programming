package com.aptina.camera.components;


import android.content.Context;
import android.util.Log;
import android.view.View;

import com.aptina.camera.CameraInfo;
import com.aptina.camera.interfaces.MenuLayoutInterface;
import com.aptina.camera.interfaces.OptionSetInterface;

/**
 * A list of options for particular settings, manages the available settings
 * given to it and provides an interface where radio buttons are used to exclusively
 * select which mode should be set. On menu close, the calling activity should get {@link #mFocusOP},
 * {@link #mHistOP}, and {@link #mHDROP} and figure which mode is set in each; then set the activity
 * based on that information
 * 
 * @author stoyan
 *
 */
public class OptionsMenu {
	private static final String TAG = "OptionsMenu";

	/**
	 * Variable to toggle logging across the whole class
	 */
	private static final boolean LOGGING_ON = false;
	
	/**
	 * The option set for the focus
	 */
	private OptionSet mFocusOP;
	
	/**
	 * The option set for the histogram
	 */
	private OptionSet mHistOP; 
	
	/**
	 * The option set for the HDR
	 */
	private OptionSet mHDROP;
	
	 /**
	  * Hold the context of the CameraActivity for view inflation
	  */
	 private Context mContext;

	 /**
	  * Interface to handle layout changes
	  */
	 private MenuLayoutInterface mCallbackTarget = null;
	 
	 /**
	  * Constructor of the options menu
	  * @param context The context of the calling activity
	  * @param callback A callback to control the menu views
	  */
	 public OptionsMenu(Context context, MenuLayoutInterface callback) {
		 mContext = context;
		 mCallbackTarget = callback;
	 }
	 
	 /**
	  * Set the callback to control the menu views
	  * @param callback
	  */
	 public void setCallbackTarget(MenuLayoutInterface callback){
			mCallbackTarget = callback;
	 }
	 

	 /**
	  * Set the focus modes available in the activity
	  * @param focusOpt A array of the focus options available
	  * @param currentOpt The current setting of the focus option
	  */
	 public void setFocusModesArray(String[] focusOpt, String currentOpt){
		 if (currentOpt != null && currentOpt.length() > 0) {
			 LOGI("currentFocus : " + currentOpt);
		 } else {
			 LOGI("no current focus : setting focus to auto");
			 currentOpt = CameraInfo.FOCUS_CONTINUOUS_PICTURE;
		 }
		 mFocusOP = new OptionSet(mContext, new OptionSetInterface(){

				@Override
				public void setViewToLayout(View view) {
					mCallbackTarget.addFocusViewToLayout(view);
				}
		 });
		 mFocusOP.setModeArray(focusOpt, currentOpt);
	 }
	 /**
	  * Set the histogram modes available in the activity
	  * @param histOpts A array of the histogram options available
	  * @param currentOpt The current setting of the histogram option
	  */
	 public void setHistogramArray(String[] histOpts, String currentOpt) {
		 mHistOP = new OptionSet(mContext, new OptionSetInterface(){

			 @Override
			 public void setViewToLayout(View view) {
				 mCallbackTarget.addHistogramViewToLayout(view);
			 }
		 });
		 mHistOP.setModeArray(histOpts, currentOpt);
	 }
	 /**
	  * Set the HDR modes available in the activity
	  * @param hdrOpts A array of the HDR options available
	  * @param currentOpt The current setting of the HDR option
	  */
	 public void setHDRArray(String[] hdrOpts, String currentOpt) {
		 mHDROP = new OptionSet(mContext, new OptionSetInterface(){

			 @Override
			 public void setViewToLayout(View view) {
				 mCallbackTarget.addHDRViewToLayout(view);
			 }
		 });
		 mHDROP.setModeArray(hdrOpts, currentOpt);
	 }
	
	 /**
	  * Changes the radio button that is checked for the focus array
	  * @param index index of the radio button
	  */
	 public void setFocus(int index){
		 Log.i(TAG, "setFocus index : " + index);
		 if(mFocusOP != null){
			 mFocusOP.setChecked(index);
		 }else{
			 Log.w(TAG, "mFocusOP == null, not setting focus");
		 }
		 
	 }
	 
	 /**
	  * Change the visibility of the menu view
	  * @param visible True is visible
	  */
	 public void setVisible(boolean visible){
		 mCallbackTarget.setVisible(visible);
	 }
	
	 /**
	  * Get the focus mode set
	  * @return focus set
	  */
	 public OptionSet getFocusSet(){
		 return mFocusOP;
	 }
	 /**
	  * Get the histogram mode set
	  * @return histogram set
	  */
	 public OptionSet getHistogramSet(){
		 return mHistOP;
	 }
	 /**
	  * Get the HDR set
	  * @return HDR set
	  */
	 public OptionSet getHDRSet(){
		 return mHDROP;
	 }
	 
	 /**
	  * Private class for logging that we can toggle for quick
	  * logging
	  * @param msg The string to log
	  */
	 private void LOGI(String msg){
		 if(LOGGING_ON){
			 Log.i(TAG, msg);
		 }
	 }
}
