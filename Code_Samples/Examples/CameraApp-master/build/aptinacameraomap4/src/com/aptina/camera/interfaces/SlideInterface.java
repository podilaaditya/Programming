package com.aptina.camera.interfaces;

public interface SlideInterface {
	/**
	 * Send slide event to gesture composite listener
	 * in order to pass action to VideoActivity
	 */
	public void onSlideEvent(int slide_dir);
}
