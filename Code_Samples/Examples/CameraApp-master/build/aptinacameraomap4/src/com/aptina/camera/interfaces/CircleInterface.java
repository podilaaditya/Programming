/**
 * 
 */
package com.aptina.camera.interfaces;

import android.graphics.Path;

/**
 * @author stoyan
 *
 */
public interface CircleInterface {

	/**
	 * Return the degree that the finger has traveled since touch down.
	 * 
	 * @param current_theta Angle, 0-360, in degrees between {@link #mInitialTheta}  and the current finger position
	 * @param start_theta Angle of when the circle gesture began
	 */
	public void onThetaEvent(double current_theta, double start_theta);
	
	/**
	 * Return when the angle theta has reached 360, indicating
	 * that a closed circular gesture has been produced
	 * 
	 * @param rotation Clockwise and Counter-Clockwise, Path.Direction.CCW && Path.Direction.CW
	 */
	public void onCircleEvent(Path.Direction rotation);
	

}
