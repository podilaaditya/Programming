package com.aptina.camera.interfaces;

import android.hardware.Camera;

public interface ZoomInterface {
	/**
     * Function to return the current camera 
     */
	public Camera getCamera();
    /**
     * Notify that the distance between the two points has changed
     * @zoom_scale The normalized difference between the last two points
     */
    public void OnZoomChange(float zoom_scale);

}