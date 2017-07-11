/**
 * Application : CloudCallLogger
 * 
 * Class Name  : CldCalLgrMainActivity 
 * Parent For  : None
 * Extends from: Activity 
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
 * Date : 31 December 2012
 * 
 * @author souris
 * 
 */

package com.cloudcalllogger;

import static com.cloudcalllogger.gcmlasses.CldCalLgrGCMCommonUtilities.SENDER_ID;


import java.util.List;
import com.google.android.gcm.GCMRegistrar;
import com.cloudcalllogger.R;
// Import for the SMS 

import com.cloudcalllogger.gcmlasses.CldCalLgrGCMAction;
import com.cloudcalllogger.utills.*;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceActivity.Header;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.util.Log;

public class CldCalLgrMainActivity extends PreferenceActivity {
	private static final String TAG = "CldCalLgrMainActivity";
	private static final boolean ALWAYS_SIMPLE_PREFS = false;
	private String mStrGcmRegId;
	AsyncTask<Void, Void, Void> mAsyTskRegisterTask;
	public Cursor mObjCursorSms, mObjCursorCallLogs;
	public CldCalLgrSmsUtills mObjCallLgrSmsUtil = new CldCalLgrSmsUtills();
	public CldCalLgrCallLogUtills mObjCallLgrCallUtil = new CldCalLgrCallLogUtills();

	// public CldCalLgrEmailUtills mObjEmailUtilObj = new
	// CldCalLgrEmailUtills();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// check if the device has the proper settings and properties
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then un comment it when it's ready.
		GCMRegistrar.checkManifest(this);

		mStrGcmRegId = GCMRegistrar.getRegistrationId(this);

		if (mStrGcmRegId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(this, SENDER_ID);
		}

		// printGoogleMailIDs();

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		boolean lBoolServiceState = isMyServiceRunning();
		// StrictMode.ThreadPolicy policy = new
		// StrictMode.ThreadPolicy.Builder().permitAll().build();
		// StrictMode.setThreadPolicy(policy);
		if (!lBoolServiceState) {

			Intent lObjActionServiceIntent = new Intent(
					this.getApplicationContext(),
					com.cloudcalllogger.gcmlasses.CldCalLgrGCMAction.class);
			startService(lObjActionServiceIntent);
		}
		setupSimplePreferencesScreen();
	}

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (CldCalLgrGCMAction.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Shows the simplified settings UI if the device configuration if the
	 * device configuration dictates that a simplified, single-pane UI should be
	 * shown.
	 */
	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) {
			return;
		}

		// In the simplified UI, fragments are not used at all and we instead
		// use the older PreferenceActivity APIs.

		// Add 'general' preferences.
		PreferenceCategory fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(R.string.pref_header_general);

		addPreferencesFromResource(R.xml.pref_general);

		// Add 'notifications' preferences, and a corresponding header.
		// getPreferenceScreen().addPreference(fakeHeader);
		// addPreferencesFromResource(R.xml.pref_notification);

		// Add 'data and sync' preferences, and a corresponding header.
		// fakeHeader = new PreferenceCategory(this);
		// fakeHeader.setTitle(R.string.pref_header_data_sync);
		// getPreferenceScreen().addPreference(fakeHeader);
		// addPreferencesFromResource(R.xml.pref_data_sync);

		// Bind the summaries of EditText/List/Dialog/Ringtone preferences to
		// their values. When their values change, their summaries are updated
		// to reflect the new value, per the Android Design guidelines.
		bindPreferenceSummaryToValue(findPreference("message_text"));
		bindPreferenceSummaryToValue(findPreference("email_text"));
		bindPreferenceSummaryToValue(findPreference("alternate_number_text"));
		bindPreferenceSummaryToValue(findPreference("email_frequency_list"));

	}

	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}

	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	/**
	 * Determines whether the simplified settings UI should be shown. This is
	 * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
	 * doesn't have newer APIs like {@link PreferenceFragment}, or the device
	 * doesn't have an extra-large screen. In these cases, a single-pane
	 * "simplified" settings UI should be shown.
	 */
	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS
				|| Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
				|| !isXLargeTablet(context);
	}

	/** {@inheritDoc} */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
			loadHeadersFromResource(R.xml.pref_headers, target);
		}
	}

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference
						.setSummary(index >= 0 ? listPreference.getEntries()[index]
								: null);
			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference
				.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
	}

	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GeneralPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_general);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("message_text"));
			bindPreferenceSummaryToValue(findPreference("email_text"));
			bindPreferenceSummaryToValue(findPreference("alternate_number_text"));
			bindPreferenceSummaryToValue(findPreference("email_frequency_list"));
			// pref_title_alternate_number

		}
	}

}
