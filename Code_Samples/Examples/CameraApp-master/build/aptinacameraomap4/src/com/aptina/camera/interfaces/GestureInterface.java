package com.aptina.camera.interfaces;

import android.graphics.Path;
import android.hardware.Camera;
import android.view.MotionEvent;

public interface GestureInterface {
	
	/**
	 * Return the coordinates of where a tap event occurs
	 */
	public void OnSingleTapGesture(MotionEvent event);
	/**
     * Function to return the current camera 
     */
	public Camera getCamera();

    /**
     * Set the DVS mode of the camera and animate switching
     */
    public void OnSlideGesture(int slide_dir);
    
    /**
     * Pass the onDoubleTap event
     */
    public void OnDoubleTapGesture(MotionEvent event);
    
    /**
     * Pass the OnSmileDetected callback to the camera
     */
    public void OnSmileGesture();
    /**
	 * Pass the OnFrownDetected callback to the camera
	 */
	public void OnFrownDetected();
    /**
     * Pass the zoom change from the zoom listener
     */
    public void OnZoomGesture(float zoom_scale);
    
    /**
     * Pass the pointer down event from the zoom listener
     */
    public void OnPointerDown(MotionEvent event);
    
	/**
	 * Pass that a pointer has left the screen
	 * 
	 * @param event
	 */
	public void OnPointerUp(MotionEvent event);
    
	/**
	 * Pass the touch down event, TouchGestureInterface
	 */
	public void OnDownTouch(MotionEvent event);
	
	/**
	 * Pass the movement of the event, TouchGestureInterface
	 */
	public void OnMoveTouch(MotionEvent event);
	
	/**
	 * Pass the touch up event, TouchGestureInterface
	 */
	public void OnUpTouch(MotionEvent event);
	
	/**
	 * Return the degree that the finger has traveled since touch down.
	 * 
	 * @param current_theta Angle, 0-360, in degrees between Touch Down and the current finger position
	 * @param start_theta Angle of when the circle gesture begans
	 */
	public void OnThetaEvent(double current_theta, double start_theta);
	
	/**
	 * Return when the angle theta has reached 360, indicating
	 * that a closed circular gesture has been produced
	 */
	public void OnCircleEvent(Path.Direction rotation);
	

}