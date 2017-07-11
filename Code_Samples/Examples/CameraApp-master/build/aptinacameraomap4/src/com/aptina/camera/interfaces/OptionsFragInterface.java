/**
 * 
 */
package com.aptina.camera.interfaces;

import com.aptina.camera.components.OptionSet;

import android.hardware.Camera;

/**
 * @author stoyan
 *
 */
public interface OptionsFragInterface {
	/**
	 * Called in fragment to get the current state of the camera object
	 * 
	 * Note: method is thread safe
	 */
	public Camera getCamera();
	
	
   /**
	* Call to change the focus mode of the camera
	* @param focusSet The focus {@link OptionSet} from {@link #mOptionsMenu}
	*/
	public void setFocusOptions(OptionSet focusSet);
	
	public void setHistogramVisibility(int vis);
	
	public int getHistogramVisibility();
	
	public void hideModeSelector();
}
