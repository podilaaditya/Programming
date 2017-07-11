/**
 * Application : CloudCallLogger
 * 
 * Class Name  : CldCalLgrContentProviderURIS 
 * Parent For  : None
 * Extends from: extends Object 
 * 
 * 
 * Description : Does job of providing the URL
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
 * Date : 20 Januray 2013
 * 
 * @author souris
 * 
 */


package com.cloudcalllogger.utills;


import android.net.Uri;


/**
 * This class would provide the Uri for the different content providers
 * 
 * @author aditya
 *
 */


public class CldCalLgrContentProviderURIS {
	
	// static method to provide the providing the Content URL 

	private static Uri uriCallLogsContentProvider 	= android.provider.CallLog.CONTENT_URI;
	private static Uri uriSms 						= Uri.parse("content://sms/");
	private static Uri uriSmsInbox 					= Uri.parse("content://sms/inbox");
	private static Uri uriContactContract 			= android.provider.ContactsContract.AUTHORITY_URI;
	private static Uri uriContactCallLog			= android.provider.CallLog.Calls.CONTENT_URI;
	
	
	/**
	 * Function would provide the URI for Call Logs content provider
	 * @return
	 */
	public static Uri getCallLogProviderURI(){
		
		return uriContactCallLog;
	}
	
	/**
	 * Function would provide the URI for  Sms Inbox content provider
	 * @return
	 */
	public static Uri getSmsInboxProviderURI(){
		
		return uriSmsInbox;
	}

	/**
	 * Function would provide the URI for  Sms content provider
	 * @return
	 */
	public static Uri getSmsProviderURI(){
		
		return uriSms;
	}
	
	
	/**
	 * Function would provide the URI for Contact content provider
	 * @return
	 */
	public static Uri getContactProviderURI(){
		
		//alContextObj.
		return uriContactContract;
	}
	
	
}
