package com.cloudcalllogger.utills;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.cloudcalllogger.utills.CldCalLgrSmsDataStoreSchema;
import com.cloudcalllogger.utills.CldCalLgrCallLogDataStoreSchema;
import com.cloudcalllogger.utills.CldCalLgrLinkDataStoreSchema;


public class CldCalLgrDBHelper  extends SQLiteOpenHelper {
	
	public static final String CLOUD_DEVICE_DATASCOURCE = "CldCalLgr.db";
	
	//
	public CldCalLgrDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, CLOUD_DEVICE_DATASCOURCE, factory, version);
		// TODO Auto-generated constructor stub
	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CldCalLgrSmsDataStoreSchema.mStrTableCreate);
		db.execSQL(CldCalLgrCallLogDataStoreSchema.mStrTableCreate);
		db.execSQL(CldCalLgrLinkDataStoreSchema.mStrTableCreate);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(CldCalLgrSmsDataStoreSchema.mStrTableDelete);
		db.execSQL(CldCalLgrCallLogDataStoreSchema.mStrTableDelete);
		db.execSQL(CldCalLgrLinkDataStoreSchema.mStrTableDelete);
	}	
	
	
	
}
