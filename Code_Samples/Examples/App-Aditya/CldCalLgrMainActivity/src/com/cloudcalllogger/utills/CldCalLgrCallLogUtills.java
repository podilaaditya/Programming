package com.cloudcalllogger.utills;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class CldCalLgrCallLogUtills {

	//Constants 
	private static final String TAG = "CldCalLgrCallLogUtills";
	// Has columns which would be used for the selection of the rows in the Call
	// Log DB
	private static List<String> mLstStrCallLogProjection = new ArrayList<String>();
	private static String[] 	mStrCallLogProjection;
	private static boolean 		mBolCallLogProjectionDone;
	

/**
 * Has the where clause related string, Which is in our case the Date and
 * the time -- Logic
 * ------------------------------------------------------------------------
 * all the call logs for the day i receive the GCM message mStrSelection =
 * Date[today]a in time and hrs and in descending order, where in we expect
 * the latest call to be on top. android.provider.CallLog.Calls.DATE +
 * " DESC "
 */
	static private String 		mStrCallLogWhereClauseSelection;
	
	public CldCalLgrCallLogUtills(){
		
	}
	
	

/**
 * This function is being written to set the column selection for the SMS
 * Reading TODO: Please see if this can written into a common utility for DB
 * 
 * @return void
 */
	private static void setCallLogProjection() {
		mLstStrCallLogProjection
				.add(CldCalLgrCallLogDataStoreSchema.mStrContactNumber);
		mLstStrCallLogProjection
				.add(CldCalLgrCallLogDataStoreSchema.mStrContactCallDate);
		mLstStrCallLogProjection
				.add(CldCalLgrCallLogDataStoreSchema.mStrContactCallDuration);
		mLstStrCallLogProjection
				.add(CldCalLgrCallLogDataStoreSchema.mStrContactCallType);
		mLstStrCallLogProjection
				.add(CldCalLgrCallLogDataStoreSchema.mStrContactCallCachedNumberType);
		mLstStrCallLogProjection
				.add(CldCalLgrCallLogDataStoreSchema.mStrContactCallCachedName);
		mBolCallLogProjectionDone = true;
	}

	public String[] getCallLogProjection() {
		int liLstCnt;
		if (mBolCallLogProjectionDone == false) {
			setCallLogProjection();
		}
		mStrCallLogProjection = new String[mLstStrCallLogProjection.size()];

		liLstCnt = mLstStrCallLogProjection.size();
		if (!mLstStrCallLogProjection.isEmpty()) {
			for (int cntr = 0; cntr < liLstCnt; cntr++) {
				mStrCallLogProjection[cntr] = mLstStrCallLogProjection
						.get(cntr);
			}
		}
		return mStrCallLogProjection;
	}

/**
 * This method will get the time as a long value required for the query
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
		mStrCallLogWhereClauseSelection = null;
		mStrCallLogWhereClauseSelection  = com.cloudcalllogger.utills.CldCalLgrCallLogDataStoreSchema.mStrContactCallDate
				+ " = "
				+ queryDateAndTime(year, month, day) 
				+ " OR " 
				+ com.cloudcalllogger.utills.CldCalLgrCallLogDataStoreSchema.mStrContactCallDate
				+ " > " 
				+ queryDateAndTime(year, month, day);
		return mStrCallLogWhereClauseSelection;
	}

/**
 * TODO: this function needs to be written to clean up additional data after
 * we recieve the data
 * 
 * @param aStrDateRangeStart
 * @param aStrDateRangeEnd
 * @param aObjCntx
 */
	public Cursor getCallLog(String aStrDateRangeStart, String aStrDateRangeEnd,
			Context aObjCntx) {
		Cursor lObjCursor = aObjCntx
				.getContentResolver()
				.query(com.cloudcalllogger.utills.CldCalLgrContentProviderURIS
						.getCallLogProviderURI(),
						mStrCallLogProjection, mStrCallLogWhereClauseSelection, null,
						android.provider.CallLog.Calls.DATE + " DESC");
		// Will hold the calls, available to the cursor
		if (lObjCursor != null  ) {
			return lObjCursor;
		}
		else {
			return null;
		}		
	}

}
