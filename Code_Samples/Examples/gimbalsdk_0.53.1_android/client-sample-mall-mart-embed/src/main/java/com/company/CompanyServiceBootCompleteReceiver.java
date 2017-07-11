package com.innominds.company;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CompanyServiceBootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent companyServiceIntent = new Intent(context, CompanyService.class);
        String packageName = context.getPackageName();
        companyServiceIntent.setAction(packageName + ".COMPANY_SERVICE");

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            context.startService(companyServiceIntent);
        }
        else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
            context.stopService(companyServiceIntent);
        }
    }

}
