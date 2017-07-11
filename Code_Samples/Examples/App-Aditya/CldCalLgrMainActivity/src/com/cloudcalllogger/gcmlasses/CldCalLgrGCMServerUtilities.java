/**
 * Application : CloudCallLogger
 * 
 * Class Name  : CldCalLgrGCMServerUtilities 
 * Parent For  : None
 * Extends from: None 
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
 * Date : 1 January 2013
 * 
 * @author souris
 * 
 */



package com.cloudcalllogger.gcmlasses;


// GCM Class which handles the Registration
import static com.cloudcalllogger.gcmlasses.CldCalLgrGCMCommonUtilities.SERVER_URL;
//import static com.cloudcalllogger.gcmlasses.CldCalLgrGCMCommonUtilities.TAG;
import static com.cloudcalllogger.gcmlasses.CldCalLgrGCMCommonUtilities.displayMessage;

import com.google.android.gcm.GCMRegistrar;
import com.cloudcalllogger.gcmlasses.CldCalLgrGCMCommonUtilities;
import com.cloudcalllogger.R;

import android.accounts.Account;
import android.accounts.AccountManager;
//Context and Log classes
import android.content.Context;
import android.util.Log;

//Java Classes for Server code
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


public final class CldCalLgrGCMServerUtilities {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
	private static final String TAG = "CldCalLgrGCMServerUtilities";
    private static String mUserName =  null;
    
    /**
     * Register this account/device pair within the server.
     *
     * @return whether the registration succeeded or not.
     */
    public static boolean register(final Context context, final String regId) {
        Log.i(TAG, "registering device (regId = " + regId + ")");
        String serverUrl = SERVER_URL + "/register";
        Log.i(TAG, "registering device on URL = " +SERVER_URL + "/register");
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        // get the user name and assign it to the variable 
        mUserName = getAccountDetailsFromDevice(context);
        params.put("user", mUserName);
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register it in the
        // demo server. As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
           // Log.d(TAG, "Attempt #" + i + " to register");
            try {
                displayMessage(context, context.getString(
                        R.string.server_registering, i, MAX_ATTEMPTS));
                post(serverUrl, params);
                GCMRegistrar.setRegisteredOnServer(context, true);
                String message = context.getString(R.string.server_registered);
                CldCalLgrGCMCommonUtilities.displayMessage(context, message);
                return true;
            } catch (IOException e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(TAG, "Failed to register on attempt " + i, e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    //Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    //Log.d(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return false;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        CldCalLgrGCMCommonUtilities.displayMessage(context, message);
        return false;
    }

    /**
     * Unregister this account/device pair within the server.
     */
    static void unregister(final Context context, final String regId) {
        Log.i(TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        params.put("user", mUserName);
        try {
            post(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            CldCalLgrGCMCommonUtilities.displayMessage(context, message);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            CldCalLgrGCMCommonUtilities.displayMessage(context, message);
        }
    }

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
    private static void post(String endpoint, Map<String, String> params)
            throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        //Log.v(TAG, "------ > Posting '" +"  body == " +body + "' to Url = " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            //Log.d(TAG, "-------------------FINAL TESTS----- Response from the post :: "+status);
            if (status != 200) {
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
    
    /**
     * @author souris
     * getAccountDetailsFromDevice 
     * 
     *
     * get Account information from the device
     * 
     */
    public static String getAccountDetailsFromDevice(final Context contxt){
    	
    	//1. Would need the account get permission to the Android Manifest File
    	//2. use Accounts class to get all the information for the google accounts in the device
    	AccountManager manager = (AccountManager) contxt.getSystemService(contxt.ACCOUNT_SERVICE);   
    			//getSystemService(ACCOUNT_SERVICE);
    	Account[] list = manager.getAccountsByType("com.google");
    	//Log.d(TAG,"------------>Account list count ::"+list.length);
    	String lStrUser = null;

    	for(Account account: list) {
    	    if(account.type.equalsIgnoreCase("com.google")) {
    	    	//Log.d(TAG, "Account type com.google");
    	    	lStrUser = account.name.substring(0, account.name.indexOf("@"));
    	    	//Log.d(TAG, "Account name ="+lStrUser);
    	    	break;
    	    }    		//Log.d(TAG,"account::"+ account.type.);
    	}    	
    	
    	return lStrUser;
    }
    
    
    public String getUsername(final Context contxt){
        AccountManager manager = (AccountManager) contxt.getSystemService(contxt.ACCOUNT_SERVICE); 
        Account[] accounts = manager.getAccountsByType("com.google"); 
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
          // TODO: Check possibleEmail against an email regex or treat
          // account.name as an email address only for certain account.type values.
          possibleEmails.add(account.name);
        }

        if(!possibleEmails.isEmpty() && possibleEmails.get(0) != null){
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");
            if(parts.length > 0 && parts[0] != null)
                return parts[0];
            else
                return null;
        }else
            return null;
    }
}
