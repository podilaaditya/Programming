package com.aptina.camera.interfaces;

import android.view.MotionEvent;

/**
 * Interface for the listener
 */
public interface TouchGestureInterface{
	/**
	 * Pass the touch down event
	 */
	public void onDownTouch(MotionEvent event);
	
	/**
	 * Pass the movement of the event
	 */
	public void onMoveTouch(MotionEvent event);
	
	/**
	 * Pass the touch up event
	 */
	public void onUpTouch(MotionEvent event);
	
    /**
     * Notify that a second finger has touched the screen
     * 
     * @param event The event of the pointer down
     */
	public void onPointerDown(MotionEvent event);
	
	/**
	 * Notify that a pointer has left the screen
	 * 
	 * @param event
	 */
	public void onPointerUp(MotionEvent event);
	
}