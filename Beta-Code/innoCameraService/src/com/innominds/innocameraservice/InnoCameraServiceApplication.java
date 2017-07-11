/*
*   **This is the Application which is the entrey point to the App and Applciation manager loads the custom
*   Application class
*/

package com.innominds.innocameraservice;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.innominds.innocameraservice.Observer;
import com.innominds.innocameraservice.Observable;
import com.innominds.innocameraservice.CommonAppConstants;


//What more to be done 
//1. Design to be made so that the notifications for start/stop/pause of the preview can 
//be sent accross to all the clients
//2. Control stub should be made to controll and operate the remote camera
//3. Taking picture and file sharing also needs to supported



public class InnoCameraServiceApplication extends Application implements Observable {

    private static final String TAG = "camera.InnoCameraServiceApplication";
    public static String PACKAGE_NAME;
	private Module mModule = Module.NONE;
	private String mErrorString =  null;
	public  ComponentName mRunningService = null;

	//public AllJoynService.BusAttachmentState mBusAttachmentState = AllJoynService.BusAttachmentState.DISCONNECTED;
	private List<Observer> mObservers = new ArrayList<Observer>();

	/**
	 * Enumeration of the high-level moudules in the system.  There is one
	 * value per module.
	 */
	public static enum Module {
		NONE,
		GENERAL,
		USE,
		HOST
	}

	//@Overide
	public void OnCreate() {
		
		Log.i(TAG, "onCreate()");
        PACKAGE_NAME = getApplicationContext().getPackageName();
        Intent intent = new Intent(this, InnoAllJoynCameraService.class);
        mRunningService = startService(intent);
        if (mRunningService == null) {
            Log.i(TAG, "onCreate(): failed to startService()");
        }
	}
    /**
     * Application components call this method to indicate that they are alive
     * and may have need of the AllJoyn Service.  This is required because the
     * Android Application class doesn't have an end to its lifecycle other
     * than through "kill -9".  See quit().
     */
    public void checkin() {
        Log.i(TAG, "checkin()");
    	if (mRunningService == null) {
            Log.i(TAG, "checkin():  Starting the AllJoynService");
            Intent intent = new Intent(this, InnoAllJoynCameraService.class);
            mRunningService = startService(intent);
            if (mRunningService == null) {
                Log.i(TAG, "checkin(): failed to startService()");
            }    		
    	}
    }
	//@Overide 
	public  void onLowMemory() {

	}
	
	//@Overide 
	public  void onTerminate(){

	}

	//@Overide 
	public  void onTrimMemory(){

	}

	
	/**
	 * This object is really the model of a model-view-controller architecture.
	 * The observer/observed design pattern is used to notify view-controller
	 * objects when the model has changed.  The observed object is this object,
	 * the model.  Observers correspond to the view-controllers which in this
	 * case are the Android Activities (corresponding to the use tab and the
	 * hsot tab) and the Android Service that does all of the AllJoyn work.
	 * When an observer wants to register for change notifications, it calls
	 * here. 
	 */
	public synchronized void addObserver(Observer obs) {
        Log.i(TAG, "addObserver(" + obs + ")");
		if (mObservers.indexOf(obs) < 0) {
			mObservers.add(obs);
		}
	}
	
	/**
	 * When an observer wants to unregister to stop receiving change 
	 * notifications, it calls here. 
	 */
	public synchronized void deleteObserver(Observer obs) {
        Log.i(TAG, "deleteObserver(" + obs + ")");
		mObservers.remove(obs);
	}
	
	/**
	 * This object is really the model of a model-view-controller architecture.
	 * The observer/observed design pattern is used to notify view-controller
	 * objects when the model has changed.  The observed object is this object,
	 * the model.  Observers correspond to the view-controllers which in this
	 * case are the Android Activities (corresponding to the use tab and the
	 * hsot tab) and the Android Service that does all of the AllJoyn work.
	 * When the model (this object) wants to notify its observers that some
	 * interesting event has happened, it calles here and provides an object
	 * that identifies what has happened.  To keep things obvious, we pass a
	 * descriptive string which is then sent to all observers.  They can decide
	 * to act or not based on the content of the string. 
	 */
	private void notifyObservers(Object arg, byte[] newPreviewFrame) {
        Log.i(TAG, "notifyObservers(" + arg + ")");
        for (Observer obs : mObservers) {
            Log.i(TAG, "notify observer = " + obs);
            if(newPreviewFrame != null)
            obs.update(this, arg,newPreviewFrame);
        }
	}



	// /**
	//  * This is the method that the "host" Activity calls when the 
	//  * Camera Preview is initiated.
	//  */
	// public synchronized void hostHasInitCamera() {
	// 	notifyObservers(CommonAppConstants.HOST_CAMERA_CHANNEL_STATE_CHANGED_EVENT);
	// 	notifyObservers(CommonAppConstants.HOST_INIT_CAMERA_CHANNEL_EVENT);
	// }
	
	// /**
	//  * This is the method that the "host" Activity calls when the 
	//  * Camera Preview is ready to sending preview call back.
	//  */
	// public synchronized void hostHasStartedCamera() {
	// 	notifyObservers(CommonAppConstants.HOST_CAMERA_CHANNEL_STATE_CHANGED_EVENT);
	// 	notifyObservers(CommonAppConstants.HOST_START_CAMERA_CHANNEL_EVENT);
	// }
	
	// /**
	//  * This is the method that the "host" Activity calls when the 
	//  * Camera Preview is stoped.
	//  */		
	// public synchronized void hostHasStopedCameraChannel() {
	// 	notifyObservers(CommonAppConstants.HOST_CAMERA_CHANNEL_STATE_CHANGED_EVENT);
	// 	notifyObservers(CommonAppConstants.HOST_STOP_CAMERA_CHANNEL_EVENT);
	// }

/**
* Activity sends the preview data to be sent accross to clients or process in bus 
*/
	public synchronized void newPreviewFrameMessage(byte[] aPreviewFrame) {
		notifyObservers(CommonAppConstants.HOST_NEW_PREVIEW_FRAME_READY_EVENT, aPreviewFrame);
	}


	/**
	 * This is the method that AllJoyn Service calls to tell us that an error
	 * has happened.  We are provided a module, which corresponds to the high-
	 * level "hunk" of code where the error happened, and a descriptive string
	 * that we do not interpret.
	 * 
	 * We expect the user interface code to sort out the best activity to tell
	 * the user about the error (by calling getErrorModule) and then to call in
	 * to get the string. 
	 */
	public synchronized void alljoynError(Module m, String s) {
		mModule = m;
		mErrorString = s;
		notifyObservers(CommonAppConstants.ALLJOYN_ERROR_EVENT,null);
	}

	/**
	 * Return the high-level module that caught the last AllJoyn error.
	 */
	public Module getErrorModule() {
		return mModule;
	}
}