/**
 * Application : CloudCallLogger
 * 
 * Class Name  : CldCalLgrGCMReciever 
 * Parent For  : None
 * Extends from: GCMBroadcastReceiver 
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
import android.util.Log;

import com.google.android.gcm.GCMBroadcastReceiver;


public class CldCalLgrGCMReciever extends GCMBroadcastReceiver {
	
	private final String TAG = GCMBroadcastReceiver.class.getSimpleName(); 
/*
 * 	(non-Javadoc)
 * @see com.google.android.gcm.GCMBroadcastReceiver#getGCMIntentServiceClassName(android.content.Context)
 */
	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		//Log.d(TAG,"Intent Service Name ::"+getIntentServiceClassName(context));
		return getIntentServiceClassName(context);
	}
	
/*TODO CldCalLgrGCMReciever
 *TODO 1.Unit Test the Intent Service Name sent from here 
 * 
 * 
 */
    static final String getIntentServiceClassName(Context context) {
        String className = context.getPackageName()+".gcmlasses" +"."+CldCalLgrGCMIntentService.class.getSimpleName();
        return className;
    }
	//

}
