package com.cloudcalllogger.utills;

public class CldCalLgrLinkDataStoreSchema {
	
	
	public static final String mStrTableName = "CallLogLink"; 
	
	public static String mStrCallerID;
	public static String mStrSmsRecordId;
	public static String mStrCallLogRecordId;
	public static String mStrRecordId									= "RecordKey"; /*Better Create the Record with Call Date + 
	Time from which we started the recording */
	
	
	public static final String mStrTableCreate = 
			"CREATE TABLE " 
			+ mStrTableName /*Table Name */ 
			+ " (" 
			+ mStrRecordId
			+ "INTEGER PRIMARY KEY AUTOINCREMENT" /* Column data type and other attributes */
			+ mStrCallerID /* Column Name*/
			+ " TEXT," 			/* Column Data Type*/
            + mStrSmsRecordId
            + " TEXT,"
            + mStrCallLogRecordId
            + " TEXT );"
            ;
	
	public static final String mStrTableDelete = "DROP TABLE IF EXISTS "+ mStrTableName;
    

}
