/**
 * 
 */
package com.aptina.camera.eventlisteners;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.aptina.camera.interfaces.TouchGestureInterface;

/**
 * @author stoyan
 *
 */
public class TouchGestureListener implements OnTouchListener {
	/**
	 * String to describe function to logcat
	 */
	private static final String TAG = "TouchGestureListener";
	/**
	 * Toggle to turn on/off logging
	 */
	private static final boolean LOG_ON = false;


	/**
     * SlideInterface selection callback target.
     */
    private TouchGestureInterface mCallbackTarget = null;
	
    public void setCallback(TouchGestureInterface callback){
    	mCallbackTarget= callback;
    }
	/**
	 * Constructor of listener. This listener passes basic events
	 * such as touch down, touch up, and touch move
	 */
	public TouchGestureListener() {
	}


	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
	    case MotionEvent.ACTION_DOWN:
	    	mCallbackTarget.onDownTouch(event);
	        break;
	    case MotionEvent.ACTION_UP:
	    	mCallbackTarget.onUpTouch(event);
	    	break;
	    case MotionEvent.ACTION_MOVE:	   
	    	mCallbackTarget.onMoveTouch(event);
	        break;
	    case MotionEvent.ACTION_POINTER_DOWN:
	    	mCallbackTarget.onPointerDown(event);
	    	break;
	    case MotionEvent.ACTION_POINTER_UP:
	    	mCallbackTarget.onPointerUp(event);
	    	break;
	    }//End switch
	    //Indicate event was not handled
		return true;
	}

	 /**
     * Log to logcat with a conditional toggle
     * @param msg string to be printed to logcat
     */
    private void LOGI(String msg){
    	if(LOG_ON){
    		Log.i(TAG, msg);
    	}
    }
}
