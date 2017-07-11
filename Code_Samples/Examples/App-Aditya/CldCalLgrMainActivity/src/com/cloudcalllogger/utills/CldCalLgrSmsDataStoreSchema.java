package com.cloudcalllogger.utills;

/**
 * This class would give the column's of the SMS Content Provider 
 * Note: Presently we do not have the SMS Provider being exposed by Android Documentation  
 * @author aditya
 * 
 */

public class CldCalLgrSmsDataStoreSchema {

	//{ "address", "person", "date", "body","type" }
	public static final String TAG = "SmsContentProviderSchema";
	
	public static final String mStrTableName = "SmsRecords";
	
	//Declaration for the Columns in the SMS provide Table
	public static final String mSrtClmMessageId   						=  "_id";
	
	// Me 
	public static final String mSrtClmMessageType   					=  "type";
	
	//Thread id for the message type to convert to is integer
	public static final String mStrClmThreadId							=  "thread_id";

	//Address of the other party 
	//TODO Need to check what its
	public static final String mSrtClmSenderAddress 					=  "address";

	//The date on which the message was received
	public static final String mSrtClmMessageDateRecieved   			=  "date";
	
	//The date on which the message was sent
	public static final String mStrClmMessageDateSent 					=  "date_sent";
	
	//The column which gives the state of message, if the message been read or not
	public static final String mStrClmFlagRead							=  "read";
	
	//The Column gives if the message has been seen or not 
	public static final String mStrClmFlagSceen						    =  "seen";
	
    /**
     * The subject of the message, if present
     * <P>Type: TEXT</P>
     */
    public static final String mStrClmSubject 							= "subject";

    /**
     * The body of the message
     * <P>Type: TEXT</P>
     */
    public static final String mStrClmBody 								= "body";

    /**
     * The id of the sender of the conversation, if present
     * <P>Type: INTEGER (reference to item in content://contacts/people)</P>
     */
    public static final String mstrClmPerson 							= "person";

	public static String mStrRecordId									= "RecordKey"; /*Better Create the Record with Call Date + 
	Time from which we started the recording */
    
    
	public static final String mStrTableCreate = 
			"CREATE TABLE " 
			+ mStrTableName /*Table Name */ 
			+ " (" 
			+ mStrRecordId
			+ "INTEGER PRIMARY KEY AUTOINCREMENT" /* Column data type and other attributes */
			+ mSrtClmMessageType /* Column Name*/
			+ " TEXT," 			/* Column Data Type*/
            + mSrtClmMessageDateRecieved
            + " TEXT,"
            + mStrClmMessageDateSent
            + " TEXT,"
            + mStrClmFlagRead
            + " TEXT,"
            + mStrClmSubject
            + " TEXT,"
            + mStrClmBody
            + " TEXT );"
            ;
	
	public static final String mStrTableDelete = "DROP TABLE IF EXISTS "+ mStrTableName;	
    
	
}
