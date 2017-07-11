/**
 * 
 */
package com.aptina.camera.eventlisteners;

import com.aptina.camera.interfaces.GestureInterface;

import android.content.Context;

/**
 * @author stoyan
 *
 */
public class VideoCompositeListener extends GestureCompositeListener {
	public interface VideoGestureInterface extends GestureInterface {
		
	}
	/**
	 * 
	 */
	public VideoCompositeListener(Context context) {
		super(context);
		addSlideListener(SlideGestureListener.SLIDE_UP);
		addSlideListener(SlideGestureListener.SLIDE_RIGHT);
		addSlideListener(SlideGestureListener.SLIDE_DOWN);
		addSlideListener(SlideGestureListener.SLIDE_LEFT);
		
		//Add double tap listener for the video snapshot
		addDoubleTapListener();
	}

}
