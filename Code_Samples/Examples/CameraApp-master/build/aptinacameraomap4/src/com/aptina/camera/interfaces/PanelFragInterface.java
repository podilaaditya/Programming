/**
 * 
 */
package com.aptina.camera.interfaces;

import android.hardware.Camera;
import android.hardware.Camera.Size;

/**
 * @author stoyan
 *
 */
public interface PanelFragInterface {

	/**
	 * Called in fragment to get the current state of the camera object
	 * 
	 * Note: method is thread safe
	 */
	public Camera getCamera();
	
	/**
	 * Called when the resolution button is clicked
	 */
	public ResolutionChangeInterface getVideoResolutionCallback();
	
	/**
	 * Called when the resolution button is clicked
	 */
	public ResolutionChangeInterface getSnapshotResolutionCallback();
	
	/**
	 * Called when the snapshot button is clicked
	 */
	public void onSnapshot();

	/**
	 * Called when the HDR button is clicked
	 */
	public void onHDR();

	/**
	 * Called when the options button is clicked
	 */
	public void onOptions();

	/**
	 * Called when the reset button is clicked
	 */
	public void onReset();

	/**
	 * Called when the camera switch button is clicked
	 */
	public void onSwitch();
}
