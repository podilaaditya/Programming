/**
 * 
 */
package com.aptina.camera.components;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aptina.R;
import com.aptina.camera.interfaces.OptionSetInterface;

/**
 * @author stoyan
 *
 */
public class OptionSet {
	private static final String TAG = "OptionSet";
	/**
	 * The application context
	 */
	private Context mContext = null;
	 /**
	  * Index of the radio button touched on MotionEven.ActionDown
	  */
	 private int mDown = -1;
	 /**
	  * Array of views, each containing on item in {@link #mButtons}
	  */
	 private View[] mViews;
	 /**
	  * Array of radio buttons to choose mode.
	  */
	 private RadioButton[] mButtons;
	 /**
	  * Hold the current camera  when this dialog is shown
	  */
	 private String mCurrent;
	 /**
	  * List of supported  modes
	  */
	 private String[] mModes; 
	 
	 /**
	  * Interface to set the particular view to its container
	  */
	 private OptionSetInterface mCallbackTarget = null;
	 
	 public OptionSet(Context ctx, OptionSetInterface callback){
		 this.mContext = ctx;
		 this.mCallbackTarget = callback;
	 }
	 
	 /**
	  * Get the selected mode amongst the {@link #mModes} that this 
	  * object was initialized with
	  * 
	  * @return the currently selected mode
	  */
	 public String getMode(){
		 return mCurrent;
	 }
	 
	 /**
	  * Get all the available modes
	  * 
	  * @return The available modes for this set
	  */
	 public String[] getAllModes(){
		 return mModes;
	 }
	 
	 /**
	  * Get the mode at the index
	  * @param index Index of the mode to get
	  * @return The string value of the mode
	  */
	 public String getModeAt(int index){
		 return mModes[index];
	 }
	 /**
	  * Set the {@link #mButtons} and {@link #mViews} along with the onTouchListeners
	  * 
	  * @param opts Array of all the possible options
	  * @param currentOpt The currently selected option when this object is created
	  */
	 public void setModeArray(String[] opts, String currentOpt) {
		 	mModes = opts;
			mCurrent = currentOpt;
			//Set the views array
			mViews = createRadioViews(opts, currentOpt);
			//Create the buttons array
			mButtons = new RadioButton[mViews.length];
			
			Log.i(TAG, "mHistViews lenght : " + mViews.length);
			for(int i = 0; i < mViews.length; i++){
				//Add each button to the buttons array
				mButtons[i] = (RadioButton) mViews[i].findViewById(R.id.radioButton);
				if (currentOpt != null && currentOpt.equalsIgnoreCase(opts[i])) {
					mButtons[i].setChecked(true);
				}
				final int index = i;
				//Set each buttons click listener to display the correct radio button selected
				mButtons[i].setOnTouchListener(new View.OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch(event.getAction()){
						case MotionEvent.ACTION_DOWN:
							mDown = index;
							break;
						case MotionEvent.ACTION_UP:
							if(mDown == index && mDown != -1){
								for(int j = 0; j < mButtons.length; j++){
									((RadioButton) mButtons[j]).setChecked(j == index);
									mCurrent = mModes[index];
								}
							}
							break;
						case MotionEvent.ACTION_MOVE:
							break;
						}
						return true;
					}
				});
				
				mCallbackTarget.setViewToLayout(mViews[i]);
			}
	 }
	 
	 /**
	  * Makes the radio button checked. Makes all other buttons in {@link #mButtons} unchecked
	  * @param index The index of the checked radio button
	  */
	 public void setChecked(int index){
		 for(int j = 0; j < mButtons.length; j++){
			 ((RadioButton) mButtons[j]).setChecked(j == index);
				mCurrent = mModes[index];
		 }
	 }
	 
	 /**
	  * Creates the views for all the modes in {@link #mModes}, but does not create
	  * the radio buttons or their listeners that go inside these views. See {@link #setModeArray(String[], String)}
	  * 
	  * @param opts Array of all the possible options
	  * @param currentOpt The currently selected option when this object is created
	  * @return An array of the views
	  */
	 private View[] createRadioViews(String[] opts, String currentOpt){
			View[] views = new View[opts.length];
			for(int i = 0; i < opts.length; i++){
				views[i] = View.inflate(mContext, R.layout.options_item, null);
				TextView text = (TextView) views[i].findViewById(R.id.text);
				text.setText(opts[i]);
			}
			return views;
	 }
}
