package com.aptina.camera.interfaces;

/**
 * Interface for the listener
 */
public interface SmileInterface{
	/**
	 * Pass the smile detected event
	 */
	public void OnSmileDetected();
	
	/**
	 * Pass the frown detected event
	 */
	public void OnFrownDetected();
	
}