package com.aptina.camera.eventlisteners;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Path;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.aptina.camera.interfaces.CircleInterface;
import com.aptina.camera.interfaces.DoubleTapInterface;
import com.aptina.camera.interfaces.GestureInterface;
import com.aptina.camera.interfaces.SlideInterface;
import com.aptina.camera.interfaces.SmileInterface;
import com.aptina.camera.interfaces.TouchGestureInterface;
import com.aptina.camera.interfaces.ZoomInterface;
/**
 * @author stoyan
 *
 */
public abstract class GestureCompositeListener implements OnTouchListener{
	/**
	 * String to describe function to logcat
	 */
	private static final String TAG = "GestureCompositeListener";
	
	/**
	 * List of listeners we will combine to have our gesture interface for the VideoActivity
	 */
	private List<OnTouchListener> registeredListeners = new ArrayList<OnTouchListener>();
	
	/**
	 * Toggle to turn on/off logging
	 */
	private static final boolean LOG_ON = true;
	
	/**
	 * Hold the context of the activity
	 */
	private Context mContext;
	/**
     * VideoGestureInterface selection callback target.
     */
    protected GestureInterface mCallbackTarget = null;

    public void setCallback(GestureInterface callback){
    	mCallbackTarget = callback;
    }
	/**
	 * Constructor of class which should set all of the listeners for the gesture interface
	 */
	public GestureCompositeListener(Context context){
		mContext = context;
	}
	
	protected void addTouchListener(){
		TouchGestureListener touchListener = new TouchGestureListener();
		TouchGestureInterface touchInterface = new TouchGestureInterface(){

			@Override
			public void onDownTouch(MotionEvent event) {
				mCallbackTarget.OnDownTouch(event);
				
			}

			@Override
			public void onMoveTouch(MotionEvent event) {
				mCallbackTarget.OnMoveTouch(event);
				
			}

			@Override
			public void onUpTouch(MotionEvent event) {
				mCallbackTarget.OnUpTouch(event);
				
			}
			
			@Override
			public void onPointerDown(MotionEvent event) {
				mCallbackTarget.OnPointerDown(event);
				
			}


			@Override
			public void onPointerUp(MotionEvent event) {
				mCallbackTarget.OnPointerUp(event);
				
			}
		};
		
		touchListener.setCallback(touchInterface);
		this.registerListener(touchListener);
	}
	
	protected void addSlideListener(int slide_direction){
		SlideInterface slideInterface = new SlideInterface(){

			@Override
			public void onSlideEvent(int slide_dir) {
				Log.i(TAG, "Logging from slideInterface SLIDE : " + slide_dir);
				mCallbackTarget.OnSlideGesture(slide_dir);

			}
			
		};
		SlideGestureListener slideGesure = new SlideGestureListener(slide_direction);
		slideGesure.setCallback(slideInterface);
		this.registerListener(slideGesure);
	}
	
	protected void addDoubleTapListener(){
		DoubleTapGestureListener tapListener = new DoubleTapGestureListener();
		DoubleTapInterface tapInterface = new DoubleTapInterface(){

			@Override
			public void onDoubleTap(MotionEvent event) {
				Log.d(TAG, "onDoubleTap");
				mCallbackTarget.OnDoubleTapGesture(event);
				
			}
			
		};
		tapListener.setCallback(tapInterface);
		this.registerListener(tapListener);
	}
	
	protected void addCircleGestureListener(){
		CircleGestureListener circleListener = new CircleGestureListener(mContext);
		CircleInterface circleInterface= new CircleInterface(){

			@Override
			public void onThetaEvent(double current_theta, double start_theta) {
				Log.d(TAG, "onThetaEvent");
				mCallbackTarget.OnThetaEvent(current_theta, start_theta);
			}

			@Override
			public void onCircleEvent(Path.Direction rotation) {
				Log.d(TAG, "onCircleEvent");
				mCallbackTarget.OnCircleEvent(rotation);
			}
			
		};
		circleListener.setCallback(circleInterface);
		this.registerListener(circleListener);
	}
	protected void addSmileGestureListener(){
		SmileGestureListener smileListener = new SmileGestureListener();
		SmileInterface smileInterface = new SmileInterface(){

			@Override
			public void OnSmileDetected() {
				Log.d(TAG, "OnSmileDetected");
				mCallbackTarget.OnSmileGesture();
			}

			@Override
			public void OnFrownDetected() {
				Log.d(TAG, "OnFrownDetected");
				mCallbackTarget.OnFrownDetected();
				
			}
		};
		smileListener.setCallback(smileInterface);
		this.registerListener(smileListener);
	}
	
	protected void addZoomListener(){
		ZoomGestureListener mZoomGestureListener = new ZoomGestureListener();
		ZoomInterface mZoomInterface = new ZoomInterface(){

			@Override
			public Camera getCamera() {
				return mCallbackTarget.getCamera();
			}


			@Override
			public void OnZoomChange(float zoom_scale) {
				mCallbackTarget.OnZoomGesture(zoom_scale);
			}

		};

		mZoomGestureListener.setCallback(mZoomInterface);
		this.registerListener(mZoomGestureListener);
	}
	
	/**
	 * 
	 */
	/**
	 * Register listeners to be passed touch events from the view this 
	 * is set to
	 * @param listener The listener to add to our registered listeners list
	 */
	public void registerListener (OnTouchListener listener) {
	      registeredListeners.add(listener);
	}
	
	/**
	 * Testing method for getting the SlideGestureListeners
	 */
	public SlideGestureListener getSlideListener(int idx){
		return (SlideGestureListener)registeredListeners.get(idx);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		passEvent(v,event);
		//Indicate event was consumed so that the view does not consume this event itself and block ACTION_MOVE, etc..
		return true;
	}
	
	private void passEvent(View v, MotionEvent event){
		for(OnTouchListener listener : registeredListeners) {
	         listener.onTouch(v,event);
	         
	    }
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
