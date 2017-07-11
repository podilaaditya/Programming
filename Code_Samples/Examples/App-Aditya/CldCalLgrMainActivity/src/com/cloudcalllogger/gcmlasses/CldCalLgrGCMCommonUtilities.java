/**
 * Application : CloudCallLogger
 * 
 * Class Name  : CldCakLgrMainActivity 
 * Parent For  : None
 * Extends from: Activity 
 * 
 * 
 * Description :
 * 
 * 
 * Public Methods :
 * 
 * 
 * 
 * 
 * 
 * Public Wrappers: 
 * 
 * 
 * 
 * 
 * 
 * Date : 31 December 2012
 * 
 * @author souris
 * 
 */


package com.cloudcalllogger.gcmlasses;

import android.content.Context;
import android.content.Intent;

public class CldCalLgrGCMCommonUtilities {
	/**
	 * Helper class providing methods and constants common to other classes in the
	 * app.
	 */
	
	
    /**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */
    public static final String SERVER_URL = "http://cloudcalltracker.appspot.com";
	

    /**
     * Google API project id registered to use GCM.
     */
    public static final String SENDER_ID = "334500177888";
    
    /**
     * Tag used on log messages.
     */
    static final String TAG = "CallLgrGCMCode";
    
    /**
     * Intent used to display a message in the screen.
     */
    static final String DISPLAY_MESSAGE_ACTION =
            "com.google.android.gcm.demo.app.DISPLAY_MESSAGE";

    /**
     * Intent's extra that contains the message to be displayed.
     */
    static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
    
    
}
