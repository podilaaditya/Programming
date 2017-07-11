/**
 * 
 */
package com.aptina.camera.eventlisteners;

import android.content.Context;


/**
 * @author stoyan
 *
 */
public class CameraCompositeListener extends GestureCompositeListener {
	
//	public interface CameraGestureInterface extends GestureInterface {
////		public void 
//	}

	/**
	 * 
	 */
	public CameraCompositeListener(Context context) {
		super(context);
		
		//Add double tap listener for the ROI focus
		addDoubleTapListener();
		
		//Add the zoom listener for camera zoom
		addZoomListener();
		
		//Add the smile detection listener for smile capture
		addSmileGestureListener();
		
		//Add the touch detection listener for the gestureview
		addTouchListener();
		
		//Add the circle listener for the gestureview
		addCircleGestureListener();
	}
	


}
