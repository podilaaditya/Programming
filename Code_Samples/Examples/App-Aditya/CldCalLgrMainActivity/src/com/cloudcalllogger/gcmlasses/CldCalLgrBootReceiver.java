package com.cloudcalllogger.gcmlasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CldCalLgrBootReceiver extends BroadcastReceiver {
	private static final String TAG = "CldCalLgrBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context,CldCalLgrGCMAction.class);
        context.startService(service);
        Log.d(TAG,"StartService Called from BootReciever");
    }
}
