/**
 * 
 */
package com.aptina.camera.interfaces;

import android.view.View;

/**
 * @author stoyan
 *
 */
public interface MenuLayoutInterface{
	/**
	 * Add each focus option view to the focus menu section
	 * of camera.xml
	 * @param view The focus menu option
	 */
	public void addFocusViewToLayout(View view);
	
	/**
	 * Add the histogram controls to the menu
	 * @param view
	 */
	public void addHistogramViewToLayout(View view);
	
	/**
	 * Add the HDR mode controls to the menu
	 * @param view
	 */
	public void addHDRViewToLayout(View view);
	
	/**
	 * Toggle the focus menu section of camera.xml visible
	 * once all of the options items have been created and added
	 * to the main view. Menu panel button should be used to toggle
	 * 
	 * @param visible Toggle boolean
	 */
	public void setVisible(boolean visible);
}
