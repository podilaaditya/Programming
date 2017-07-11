package com.cloudcalllogger.utills;

public class CldCalLgrCallLogDataStoreSchema {
	
	public static final String mStrTableName = "CallLogRecords";

	public static String mStrRecordId						= "RecordKey"; /*Better Create the Record with Call Date + 
																	Time from which we started the recording */
	public static String mStrContactNumber 					= android.provider.CallLog.Calls.NUMBER;
	public static String mStrContactCallDuration 			= android.provider.CallLog.Calls.DURATION;
	public static String mStrContactCallType 				= android.provider.CallLog.Calls.TYPE;
	public static String mStrContactCallDate 				= android.provider.CallLog.Calls.DATE;
	public static String mStrContactCallCachedNumberType 	= android.provider.CallLog.Calls.CACHED_NUMBER_TYPE;
	public static String mStrContactCallCachedName			= android.provider.CallLog.Calls.CACHED_NAME;
	public static String mStrContactCallNew 				= android.provider.CallLog.Calls.NEW;
	
	public static final String mStrTableCreate = 
			"CREATE TABLE " 
			+ mStrTableName /*Table Name */ 
			+ " (" 
			+ mStrRecordId
			+ "INTEGER PRIMARY KEY AUTOINCREMENT" /* Column data type and other attributes */
			+ mStrContactNumber 
			+ " TEXT,"
			+ mStrContactCallDuration /* Column Name*/
			+ " TEXT," 			/* Column Data Type*/
			+ mStrContactCallType 
			+ " TEXT,"
            + mStrContactCallDate
            + " TEXT,"
            + mStrContactCallCachedNumberType
            + " TEXT,"
            + mStrContactCallCachedName
            + " TEXT,"
            + mStrContactCallNew
            + " TEXT );"
            ;
	
	public static final String mStrTableDelete = "DROP TABLE IF EXISTS "+ mStrTableName;
	
}
