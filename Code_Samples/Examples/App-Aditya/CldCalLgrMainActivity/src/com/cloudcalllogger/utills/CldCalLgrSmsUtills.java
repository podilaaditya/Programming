package com.cloudcalllogger.utills;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

//import static com.cloudcalllogger.utills.CldCalLgrContentProviderURIS.getSmsInboxProviderURI;
//import static com.cloudcalllogger.utills.

public class CldCalLgrSmsUtills {

	private static final String TAG = "CldCalLgrSmsUtills";
	// Has columns which would be used for the selection of the rows in the Call
	// Log DB
	private static Uri 			mSmsInboxProvider = CldCalLgrContentProviderURIS
			.getSmsInboxProviderURI();
	private static ArrayList<String> mLstSmsProjection = new ArrayList<String>();
	private static String[] 	mStrSmsProjection;
	private boolean 			mBolSmsProjectionDone = false;
	private static String 		mStrSmsSelection = null;
	private static String 		mStrOrder = CldCalLgrSmsDataStoreSchema.mSrtClmMessageDateRecieved
			+ " DESC";


/**
 * Has the where clause related string, Which is in our case the Date and
 * the time all the call logs for the day i receive the GCM message
 * mStrSelection = Date[today] in time and hrs and in descending order,
 * where in we expect the latest call to be on top.
 * android.provider.CallLog.Calls.DATE + " DESC "
 */

/**
 * Constructor
 */
	public CldCalLgrSmsUtills() {
		// gets called when Object is create

		setSmsProjection();
		getSmsProjection();
		// TODO: this needs to be set based on locale
		// Getting the time zone of the calendar object and the setting it to
	}

/**Methods*/
/**
 * This function is being written to set the column selection for the SMS
 * Reading Would needs to be called only once when the App Gets initialized
 * TODO: Please see if this can written into a common utility for DB
 * 
 * @return void
 */
	private void setSmsProjection() {
		// { "address", "person", "date", "body","type" };
		// new String[] { "_id", "thread_id","address", "person","date","body",
		// "type" },
		mLstSmsProjection.add(CldCalLgrSmsDataStoreSchema.mSrtClmMessageId);
		// Thread identity number
		mLstSmsProjection.add(CldCalLgrSmsDataStoreSchema.mStrClmThreadId);
		//
		mLstSmsProjection.add(CldCalLgrSmsDataStoreSchema.mSrtClmSenderAddress);
		//
		mLstSmsProjection.add(CldCalLgrSmsDataStoreSchema.mstrClmPerson);
		//
		mLstSmsProjection
				.add(CldCalLgrSmsDataStoreSchema.mSrtClmMessageDateRecieved);
		//
		mLstSmsProjection.add(CldCalLgrSmsDataStoreSchema.mStrClmBody);
		//
		mLstSmsProjection.add(CldCalLgrSmsDataStoreSchema.mSrtClmMessageType);
		mLstSmsProjection.add(CldCalLgrSmsDataStoreSchema.mStrClmSubject);
		// TODO presently returing null to comply witlh the code
		mBolSmsProjectionDone = true;
	}

/**
 * This would be called to get the columns to be selected for SMS
 * 
 * @return
 */
	public String[] getSmsProjection() {
		int liLstCnt;
		
		if (mBolSmsProjectionDone == false) {
			setSmsProjection();
		}
		mStrSmsProjection = new String[mLstSmsProjection.size()];

		liLstCnt = mLstSmsProjection.size();
		if (!mLstSmsProjection.isEmpty()) {
			for (int cntr = 0; cntr < liLstCnt; cntr++) {
				mStrSmsProjection[cntr] = mLstSmsProjection.get(cntr);
			}
		}
		return mStrSmsProjection;
	}

/**
 * 	
 * @param year
 * @param month
 * @param day
 * @return
 */
	
	public static Long queryDateAndTime(int year, int month, int day) {
		GregorianCalendar lGCal = new GregorianCalendar(year, month, day);	
		lGCal.set(year, month, day);
//		Log.d(TAG,
//				"-----------------------------Start----------------------------------------------");
//		Log.d(TAG, "year =" + year + " month =" + month + "day =" + day);
//		Log.d(TAG, "calendar ::" + lGCal.getTime());
//		Log.d(TAG,
//				"------------------------------End-----------------------------------------------");
		return lGCal.getTimeInMillis();
	}
	
/**
 * 
 * @return
 */
	public static String queryWhereClause(int year, int month, int day) {
		mStrSmsSelection =null;
		mStrSmsSelection = com.cloudcalllogger.utills.CldCalLgrSmsDataStoreSchema.mSrtClmMessageDateRecieved
				+ " = "
				+ queryDateAndTime(year, month, day) 
				+ " OR " 
				+ com.cloudcalllogger.utills.CldCalLgrSmsDataStoreSchema.mSrtClmMessageDateRecieved
				+ " > " 
				+ queryDateAndTime(year, month, day);
		//Log.d(TAG, "where clause ::" + mStrSmsSelection);
		return mStrSmsSelection;
	}

/**
 * Public SMS Reader Function which would read Header/ For starting the
 * Service This would be an enhancement for future use just code[if possible
 * in time available] and keep in the Comments
 * @param aStrDateRangeStart
 * @param aStrDateRangeEnd
 * @param aObjCntx
 */
	public Cursor getSmsList(String aStrDateRangeStart, String aStrDateRangeEnd,
			Context aObjCntx) {
		Cursor lObjCursor = aObjCntx.getContentResolver().query(mSmsInboxProvider,
				mStrSmsProjection, mStrSmsSelection, null, mStrOrder);
		if (lObjCursor != null  ) {
			return lObjCursor;
		}
		else {
			return null;
		}
	}

/**
 * 
 * @param context
 * @return
 */	
	private long getThreadId(Context context) {
		long threadId = 0;
		String SMS_READ_COLUMN = "read";
		String WHERE_CONDITION = SMS_READ_COLUMN + " = 0";
		String SORT_ORDER = "date DESC";
		int count = 0;
		Cursor cursor = context.getContentResolver().query(
				mSmsInboxProvider,
				new String[] { "_id", "thread_id", "address", "person", "date",
						"body" }, WHERE_CONDITION, null, SORT_ORDER);
		if (cursor != null) {
			try {
				count = cursor.getCount();
				if (count > 0) {
					cursor.moveToFirst();
					threadId = cursor.getLong(1);
				}
			} finally {
				cursor.close();
			}
		}
		return threadId;
	}

/**
 * 
 * @param aintNumber
 * @param aStrMessageBody
 * @param aStrMessageHeader
 * @return
 */
	public static boolean  sendSms(String strNumber, String aStrMessageBody,
			String aStrMessageHeader) {
		if (strNumber != null && !aStrMessageBody.isEmpty()) {
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(strNumber, null,
					aStrMessageBody, null, null);
			return true;
		} else {
			return false;
		}
	}

}
