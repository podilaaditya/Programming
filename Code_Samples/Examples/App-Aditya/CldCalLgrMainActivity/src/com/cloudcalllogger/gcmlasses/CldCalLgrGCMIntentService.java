/**
 * Application : CloudCallLogger
 * 
 * Class Name  : CldCalLgrGCMIntentService 
 * Parent For  : None
 * Extends from: GCMBaseIntentService 
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


import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

//TODO IntentService 
/**
 * TODO 1.How i will start service after i receive the Message from GCM TODO
 * 2.How the time to live will be set for the service TODO 3.How will the
 * service send the mail to the Recipient
 * 
 */

public class CldCalLgrGCMIntentService extends GCMBaseIntentService {

	private final String TAG = CldCalLgrGCMIntentService.class.getSimpleName();
	private String mStrGcmRegId;

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "onError" + "Enter");
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		String lStrMessage;
		String lStrContactNumber;
		lStrMessage = arg1.getExtras().getString("message");
		
		boolean lBoolServiceState = isMyServiceRunning();
		Log.d(TAG, "----> Message recieved from the GCM server lStrMessage = "
			+ lStrMessage);
		//Log.d(TAG, "                 Service  state                  "+lBoolServiceState);

		if (!lBoolServiceState) {
			//Log.d(TAG,"Create Intent");
			Intent lObjActionServiceIntent = new Intent(arg0.getApplicationContext(),
					com.cloudcalllogger.gcmlasses.CldCalLgrGCMAction.class);
			//Log.d(TAG,"Start  CldCalLgrGCMAction Service");
			arg0.startService(lObjActionServiceIntent);
		}

		// 1. StartTracking
		if ("StartTracking".equals(lStrMessage)) {
			// Call the service with the intent with extra options
			    Intent intent = new Intent();
				intent.setAction(CldCalLgrGCMAction.ACTION);
				intent.putExtra(CldCalLgrGCMAction.mStrIntentText, CldCalLgrGCMAction.START_TACKING_SERVICE);
				sendBroadcast(intent);
		}
		// 2. StopTracking
		else if ("StopTracking".equals(lStrMessage)) {
			// Call the service with the intent with extra options
				//contact
			    Intent intent = new Intent();
				intent.setAction(CldCalLgrGCMAction.ACTION);
				intent.putExtra(CldCalLgrGCMAction.mStrIntentText, CldCalLgrGCMAction.STOP_TACKING_SERVICE);
				//mStrIntentTextCntact
				
				sendBroadcast(intent);
		}
		// 3. SendSMS
		else if ("SendSMS".equals(lStrMessage)) {
			// Call the service with the intent with extra options
				lStrContactNumber = arg1.getExtras().getString("contact");
				//Log.d(TAG,"lStrContactNumber == "+lStrContactNumber);

			    Intent intent = new Intent();
				intent.setAction(CldCalLgrGCMAction.ACTION);
				intent.putExtra(CldCalLgrGCMAction.mStrIntentText, CldCalLgrGCMAction.SEND_SMS_SERVICE);
				intent.putExtra(CldCalLgrGCMAction.mStrIntentTextCntact, lStrContactNumber);
				sendBroadcast(intent);
		}
		// 4. NoAction
		else if ("NoAction".equals(lStrMessage)) {
			// Call the service with the intent with extra options
			    Intent intent = new Intent();
				intent.setAction(CldCalLgrGCMAction.ACTION);
				intent.putExtra(CldCalLgrGCMAction.mStrIntentText, CldCalLgrGCMAction.SEND_NO_ACTION);
				sendBroadcast(intent);
		}

	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "onRegistered" + "Enter");
		// Will now do the job of registering with the Web server here
		// Try to register again, but not in the UI thread.
		// It's also necessary to cancel the thread onDestroy(),
		// hence the use of AsyncTask instead of a raw thread.
		//Log.d(TAG, "----> Try to register again, but not in the UI thread.");
		final Context context = arg0;
		mStrGcmRegId = GCMRegistrar.getRegistrationId(this);
		boolean registered = CldCalLgrGCMServerUtilities.register(context,
				mStrGcmRegId);
		// At this point all attempts to register with the app
		// server failed, so we need to unregister the device
		// from GCM - the app will try to register again when
		// it is restarted. Note that GCM will send an
		// unregistered callback upon completion, but
		// GCMIntentService.onUnregistered() will ignore it.
		if (!registered) {
			//Log.d(TAG, "----> Try to register again, but not Registerd");

			GCMRegistrar.unregister(context);
		}
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "onUnregistered" + "Enter");
	}

	/**
	 * Inner class which would be able to complete the tasks for us to send
	 * email [get call log and sms listing]
	 * 
	 */
	// private TimerTask updateTask = new TimerTask() {
	// @Override
	// public void run() {
	// Log.d(TAG, "Timer task doing work");
	// try {
	//
	// // check if time to send a mail
	// // i would send only one mail in 30 min initially
	// // Since we would not need to spam the mail box with these mails
	//
	// } catch (Throwable t) { /* you should always ultimately catch
	// all exceptions in timer tasks, or
	// they will be sunk */
	// Log.e(TAG, "Failed to retrieve the twitter results", t);
	// }
	// }
	// };

	// private class CldCalLgrGCMIntentServiceTimerTask extends TimerTask {
	//
	// // would n
	// //long im
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// }

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (CldCalLgrGCMAction.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
