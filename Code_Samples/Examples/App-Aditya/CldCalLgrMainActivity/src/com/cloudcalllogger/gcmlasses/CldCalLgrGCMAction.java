package com.cloudcalllogger.gcmlasses;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cloudcalllogger.utills.CldCalLgrCallLogUtills;
import com.cloudcalllogger.utills.CldCalLgrContentProviderURIS;
import com.cloudcalllogger.utills.CldCalLgrSmsDataStoreSchema;
//Imports for the Email and SMS Objects
import com.cloudcalllogger.utills.CldCalLgrSmsUtills;
import com.cloudcalllogger.utills.CldCalLgrEmailUtills;

public class CldCalLgrGCMAction extends Service {

	public final static String ACTION = "GCMIntentServiceAction";
	private static final String TAG = "CldCalLgrGCMAction";
	public final static int START_TACKING_SERVICE = 1;
	public final static int STOP_TACKING_SERVICE = 2;
	public final static int SEND_SMS_SERVICE = 3;
	public final static int SEND_NO_ACTION = 4;
	public final static String mStrIntentText = "PERFORM";
	public final static String mStrIntentTextCntact = "CONTACT";
	public final long mHoursInMilliSec = 3600000;
	// This would be accessed in a synchronized code block
	public boolean mboolThreadRun = true;
	public int mIActioncode = 0;
	public long mLngPrevMailTime = 0;
	public long mLngNextMailTime = 0;
	public long mNumberOfMailsInAHr = 60;
	public long mMailTimeAddition = 0;
	public boolean mboolFirstMailSent = false;
	public String mStrSmsNumber = null;
	public String mStrSmsFileName = null;
	public String mStrCallLogFileName = null;
	public String mStrTimeString = null;
	public String mStrFilePathCallLog = null;
	public String mStrFilePathSms = null;
	public String [] mStrSmsRec = null;
	public String [] mstrCallRec =  null;
	// Email and Sms Util Objects
	public CldCalLgrSmsUtills mObjSmsUtill = new CldCalLgrSmsUtills();
	public CldCalLgrEmailUtills mObjEmailUtill = new CldCalLgrEmailUtills(
			"cloudcalltrackerapp@gmail.com", "Arunachalasouris");
	public CldCalLgrCallLogUtills mObjCallLogUtill = new CldCalLgrCallLogUtills();
	NotifyServiceReceiver notifyServiceReceiver;
	

	Object mObjMutex = new Object();
	// int mActCode;
	//EmailThread mObjEmailSenderThread = new EmailThread();

	 private class LongOperation extends AsyncTask<String, Void, String> {
         @Override
         protected String doInBackground(String... params) {
		    try {
		        //Log.d("--->CldCalLgrGCMAction<----","Task Sending Email " +  "Email Before sending");
		        sendEmail();
		        //Log.d("--->CldCalLgrGCMAction<----","Task Sending Email " + " Email after sending");
	        } 
	        catch (Exception e2) {
	            // TODO Auto-generated catch block
	            e2.printStackTrace();
	        }
           return "Executed";
         }      

         @Override
         protected void onPostExecute(String result) {
         }

         @Override
         protected void onPreExecute() {
         }

         @Override
         protected void onProgressUpdate(Void... values) {
         }
   }   
	
	
//	public class EmailThread extends Thread {
//		Object mObjSyncro = new Object();
//
//		@Override
//		public void run() {
//			//Log.d(TAG, "-- running --");
//			try {
//				// Loop until socketThread is assigned something else
//				// (a new Thread instance or simply null)
//				synchronized (mObjSyncro) {
//					while (mboolThreadRun) {
//						if (getLatestAction() == 1) {
//							boolean ltruthval = getTimeToMail();
//							if (ltruthval) {
//								Log.d(TAG,"Would send Mail ");
//								sendEmail();
//							}
//							mboolThreadRun = true;
//							// mIActioncode = 0;
//						} 
//						else if (getLatestAction() == 2) {
//							mboolThreadRun = true;
//						} 
//						else 
//						{
//							mboolThreadRun = true;
//						}
//					}
//				}
//
//			} catch (Exception exp) {
//				// Log.d(TAG, "Exception thrown " + exp.getMessage());
//			}
//
//		}
//	}

	@Override
	public void onCreate() {
		super.onCreate();
		// TODO Auto-generated method stub
		notifyServiceReceiver = new NotifyServiceReceiver();
		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		//StrictMode.setThreadPolicy(policy); 
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION);
		registerReceiver(notifyServiceReceiver, intentFilter);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(notifyServiceReceiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	//#################################################    SMS Related Code   ###########################################	

	/**
	 * Send sms for the action state 3
	 */
	public synchronized void sendSmsAction() {
		String lStrSMSString = null;
		//0.checkIfSMSCanBeSent
		if(checkIfSMSCanBeSent()){
			//1. Get the SMS string
			lStrSMSString = getMessageToBeSent();
			//Log.d(TAG,"lStrSMSString == "+lStrSMSString);
			if(!lStrSMSString.equals(null)) {
				//2. Get the Alternate Number
				CldCalLgrSmsUtills.sendSms(mStrSmsNumber, lStrSMSString, null); 
			}
		}
		else {
			Log.d(TAG,"Can Not Send SMS");
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean checkIfSMSCanBeSent() {
		boolean lSendSms = false;
		SharedPreferences lObjPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		lSendSms = lObjPrefs.getBoolean("send_message_checkbox", true);
		return lSendSms;
	}

	/**
	 * 
	 */
	public String getAlternateNumber() {
		String lStrPhoneNumber = null;
		SharedPreferences lObjPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		lStrPhoneNumber = lObjPrefs.getString("alternate_number_text", "");
		return lStrPhoneNumber;
	}

	/**
	 * 
	 * @return
	 */
	public String getMessageToBeSent() {
		String lStrMessage, lStrMsg;
		SharedPreferences lObjPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		lStrMsg = lObjPrefs.getString("message_text", "Call me on");
		lStrMessage = lStrMsg + " " + getAlternateNumber();
		return lStrMessage;
	}

	//#################################################    Email Related Code   ###########################################
	
	/**
	 * Get the email id for which mail would be sent the notification mail
	 */
	public synchronized String getEmailIdFromPreferences() {
		SharedPreferences lObjPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String lStrEmail;
		String lStrActiveGoogleAccountGmailId = printGoogleMailIDs(); //"podila.aditya@gmail.com"; 
		if (!lStrActiveGoogleAccountGmailId.equals(null))
			lStrEmail = lObjPrefs.getString("email_text",
					lStrActiveGoogleAccountGmailId);
		else
			lStrEmail = lObjPrefs.getString("email_text", "null");

		//Log.d(TAG,"lStrEmail" +lStrEmail );
		return lStrEmail;
	}

	/**
	 * Get the email frequency from the settings
	 * @return
	 */
//	public synchronized void getNumberOfEmailsPerHr() {
//		SharedPreferences lObjPrefs = PreferenceManager
//				.getDefaultSharedPreferences(this);
//		String lIEmailCnt = "1";
//		lIEmailCnt = lObjPrefs.getString("email_frequency_list", "1");
//		mNumberOfMailsInAHr = Integer.parseInt(lIEmailCnt);
//		// return lIEmailCnt;
//	}
	
	/**
	 * @return
	 */
	public String printGoogleMailIDs() {
		AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
		Account[] list = manager.getAccounts();
		String gmail = null;

		for (Account account : list) {
			if (account.type.equalsIgnoreCase("com.google")) {
				gmail = account.name;
				Log.d(TAG,"--> Email id gmail="+gmail);
				break;
			}
		}
		return gmail;
	}

	/**
	 * 
	 * @return
	 */
//	public synchronized boolean getTimeToMail() {
//		
//		if (!mboolFirstMailSent) {
//			mboolFirstMailSent = true;
//			//Log.d(TAG, "getTimeToMail First Mail enter return true");
//			return true;
//		} 
//		else {
//			mLngNextMailTime = mLngPrevMailTime
//					+ (mNumberOfMailsInAHr + mMailTimeAddition);
//			long lTime = System.currentTimeMillis();
//			if ( lTime > mLngNextMailTime) {
//				Log.d(TAG,"Time To Send Mail");
//				return true;
//				
//			} 
//			else {
//				return false;
//			}
//		}
//	}

	/**
	 * 
	 */
	public synchronized void setDateCutOffForSMSAndCallLog() {
		// GregorianCalendar lGCal = new GregorianCalendar();
		Calendar lGCal = Calendar.getInstance();
		CldCalLgrCallLogUtills.queryWhereClause(
				(lGCal.getTime().getYear() + 1900), lGCal.getTime().getMonth(),
				lGCal.getTime().getDate());
		CldCalLgrSmsUtills.queryWhereClause((lGCal.getTime().getYear() + 1900),
				lGCal.getTime().getMonth(), lGCal.getTime().getDate());
	}

	/**
	 * 
	 */
	public synchronized void createSMSFile() {
		int liColCnt = 0;
		int liNumberColumn = 0;
		int liDateColumn = 0 ;
		int liMessageColumn = 0;
		int liStrRowCnt = 0;
		Calendar lGCal = Calendar.getInstance();
		
		SimpleDateFormat lObjSdf = new SimpleDateFormat("dd-MMMM-yyyy");
		mStrSmsRec = new  String[0];
		
		//TODO Stuff the date needs to be set for the call log and sms 
		// since we could be needing it for where clause
		// setDateCutOffForSMSAndCallLog();
		// 2. Get the file Name ;;
		mStrSmsFileName =  null;
		mStrSmsFileName = "Sms"+mStrTimeString+".txt";
		
		String [] StrList = mObjSmsUtill.getSmsProjection();
		String StrWhere = CldCalLgrSmsUtills.queryWhereClause((lGCal.getTime().getYear() + 1900),
				lGCal.getTime().getMonth(), lGCal.getTime().getDate());
		String 		mStrOrder = CldCalLgrSmsDataStoreSchema.mSrtClmMessageDateRecieved
				+ " DESC";
		
		// 1. Get the SMS Cursor
		Cursor lObjCursor = this.getContentResolver().query(CldCalLgrContentProviderURIS
				.getSmsInboxProviderURI(),
				StrList, StrWhere, null, mStrOrder);
		if (lObjCursor != null  ) {
			//Log.d(TAG, "lObjCursor Cursor Count =" + lObjCursor.getCount());
			//return lObjCursor;
		}
		
		if(lObjCursor != null) {
			if(	lObjCursor.getCount() > 0)
				 {
					mStrSmsRec = new  String[lObjCursor.getCount()];
					liColCnt = lObjCursor.getColumnCount();
					liNumberColumn = lObjCursor
							.getColumnIndex(CldCalLgrSmsDataStoreSchema.mSrtClmSenderAddress);
					liDateColumn = lObjCursor
							.getColumnIndex(CldCalLgrSmsDataStoreSchema.mSrtClmMessageDateRecieved);
					// type can be: Incoming, Outgoing or Missed
					liMessageColumn = lObjCursor
							.getColumnIndex(CldCalLgrSmsDataStoreSchema.mStrClmBody);
					lObjCursor.moveToFirst();
					do {
						String lStrRowString = null; 
						String lStrSmsSenderPhoneNumber = lObjCursor
								.getString(liNumberColumn);
						long lliMessageREcievedDate = lObjCursor.getLong(liDateColumn);
						String lStrSmsMessage = lObjCursor.getString(liMessageColumn);
						Date lObjDate = new Date(lliMessageREcievedDate);
						String lStrResultdate = lObjSdf.format(lObjDate);
						// Drawable currentIcon = null;
						lStrRowString = "Messgae From :: "+ lStrSmsSenderPhoneNumber +
									    "                Recieved on  :: "+ lStrResultdate +
									    "                Message is   :: "+ lStrSmsMessage;
						mStrSmsRec[liStrRowCnt] = lStrRowString;
					  liStrRowCnt++;
					} while (lObjCursor.moveToNext());

				}
			else {
				mStrSmsRec =  new  String[1];
				mStrSmsRec[0] = "     			      ### No Message(s) received ###                      ";
			}
			
		}
		
		lObjCursor.close();
	}

	/**
	 * 
	 */
	public synchronized void createCallLogfile() {
		
		//Local Variables 
		int liColCnt = 0;
		int liNumberColumn = 0;
		int liDateColumn = 0 ;
		int liCallTypeColumn = 0;
		int liStrRowCnt = 0;
		mstrCallRec =  new  String[0];
		Calendar lGCal = Calendar.getInstance();
		SimpleDateFormat lObjSdf = new SimpleDateFormat("dd-MMMM-yyyy");

		String [] StrList = mObjCallLogUtill.getCallLogProjection();
		String StrWhere = CldCalLgrCallLogUtills.queryWhereClause((lGCal.getTime().getYear() + 1900),
				lGCal.getTime().getMonth(), lGCal.getTime().getDate());
		String 		mStrOrder = android.provider.CallLog.Calls.DATE + " DESC";
		// 1. Get the SMS Cursor
		Cursor lObjCursor = this.getContentResolver().query(CldCalLgrContentProviderURIS
				.getCallLogProviderURI(),
				StrList, StrWhere, null, mStrOrder);
		/**
		 * Create the String Array with the Records 
		 */
		if(lObjCursor != null) {
			liColCnt = lObjCursor.getColumnCount();
			liNumberColumn = lObjCursor
					.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
			liDateColumn = lObjCursor
					.getColumnIndex(android.provider.CallLog.Calls.DATE);
			// type can be: Incoming, Outgoing or Missed
			liCallTypeColumn = lObjCursor
					.getColumnIndex(android.provider.CallLog.Calls.TYPE);

			if(	lObjCursor.getCount() > 0) {
				
				mstrCallRec =  new  String[lObjCursor.getCount()];
				lObjCursor.moveToFirst();
				do {
					String lStrRowString = null; 
					
					String lStrCallerPhoneNumber = lObjCursor
							.getString(liNumberColumn);
					long lliCallDate = lObjCursor.getLong(liDateColumn);
					int liCallType = lObjCursor.getInt(liCallTypeColumn);
					Date lObjDate = new Date(lliCallDate);
					String lStrResultdate = lObjSdf.format(lObjDate);
					// Drawable currentIcon = null;
					switch (liCallType) {
						case android.provider.CallLog.Calls.INCOMING_TYPE:
							lStrRowString = "Incomming Call ::"+ lStrCallerPhoneNumber + "      Call Time  ::"+lStrResultdate;
							break;
						case android.provider.CallLog.Calls.MISSED_TYPE:
							lStrRowString = "Missed Call    ::"+ lStrCallerPhoneNumber + "      Call Time        ::"+lStrResultdate;
							break;
						case android.provider.CallLog.Calls.OUTGOING_TYPE:
							lStrRowString = "Outgoing Call  ::"+ lStrCallerPhoneNumber + "      Call Time        ::"+lStrResultdate;
							break;
						}
					  mstrCallRec[liStrRowCnt] = lStrRowString;
					  liStrRowCnt++;
				} while (lObjCursor.moveToNext());
			}	
			else {
				mstrCallRec =  new  String[1];
				mstrCallRec[0] = "                   ## No  Incoming/Outgoing/Missed Calls  ##                   ";
			}
		}
		
		lObjCursor.close();
	}

	
	public synchronized void createFile( String[] aRecordRows, String aFileName,
			int aRowCount) {
		FileOutputStream lObjfileOutputStream = null;
		OutputStreamWriter lObjOutputSteamWr = null;
		int liRowCntr = 0;
		try{
			lObjfileOutputStream = openFileOutput(aFileName, Context.MODE_PRIVATE);
			lObjOutputSteamWr = new OutputStreamWriter(lObjfileOutputStream);
			if(aRowCount == 0){
//				Log.d(TAG,"createFile --> Recor Print --> \n"+aRecordRows[0]);
				lObjOutputSteamWr.write(aRecordRows[0]);
				lObjOutputSteamWr.write("\n");
			}
			else {
				for (liRowCntr= 0; liRowCntr < aRowCount ;aRowCount++ ) {
//					Log.d(TAG,"createFile --> Recor Print --> \n"+aRecordRows[liRowCntr]);
					lObjOutputSteamWr.write(aRecordRows[liRowCntr]);
					lObjOutputSteamWr.write("\n");
				}
			}

			lObjOutputSteamWr.close();
			lObjfileOutputStream.close();
		}catch(Exception e){
			e.printStackTrace(System.err);

		}

	}
	
	/**
	 * 1. Set Query Date for cursors in sms and call logs 1. Get SMS List 2. Get
	 * Call Log List 2. prepare the text files 3. Create email with attachments
	 * and send mail 2. Set the mLngPrevMailTime = System.currentTimeMillis();
	 * 
	 */
	public synchronized void sendEmail() {

		String lStrToEmail = getEmailIdFromPreferences();
		String[] toArr = { lStrToEmail };

		mObjEmailUtill.setTo(toArr);
		mObjEmailUtill.setFrom("podila.panduraga@gmail.com");
		mObjEmailUtill
				.setSubject("Auto Mail sent from Cloud Call Tracker");
		
		createCallLogfile();
		createSMSFile();
		mObjEmailUtill.setSmsRec(mStrSmsRec);
		mObjEmailUtill.setCalRec(mstrCallRec);
		mLngPrevMailTime = System.currentTimeMillis();
		try {
			mObjEmailUtill.send();
		} 
		catch (Exception exp) {
			Log.d(TAG, "Sending Mail Eception " + exp.getMessage());
			exp.printStackTrace();
		}
	}
// ####################  Common Usable Code ################### 
	public synchronized int getLatestAction() {
		return mIActioncode;
	}

	/**
	 * 
	 * @author souris
	 * 
	 */
	public class NotifyServiceReceiver extends BroadcastReceiver {

		// mObjEmailSenderThread.
		Object mObjSyncro = new Object();
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			// TODO 1. StartTracking Sms/Call log
			// TODO 2. StopTracking Sms/Call log
			// TODO 3. Send SMS
			synchronized (mObjSyncro) {
				mIActioncode = arg1.getIntExtra(mStrIntentText, 0);
				//Log.d(TAG, "---------->  Action to perform are ---> " + mIActioncode);
				if (mIActioncode == START_TACKING_SERVICE) {
					LongOperation mObjTask =  new LongOperation();
					mObjTask.execute("test task");
					//sendEmail();
					//mObjEmailSenderThread.
				} 
				else if (mIActioncode == SEND_SMS_SERVICE) {
					//mISmsNumber = arg1.getIntExtra(mStrIntentTextCntact, 0);
					mStrSmsNumber = arg1.getStringExtra(mStrIntentTextCntact);  
					
					//Log.d(TAG,"Trying to get the SMS to Number mISmsNumber=== "+mStrSmsNumber);
					sendSmsAction();
				} 
				else if (mIActioncode == STOP_TACKING_SERVICE) {
					mboolFirstMailSent = false;
				} 
				else {
					mboolFirstMailSent = false;
				}
			}
		}

		/**
		 * Get the Call text File
		 */
	}// end of innner class
}
