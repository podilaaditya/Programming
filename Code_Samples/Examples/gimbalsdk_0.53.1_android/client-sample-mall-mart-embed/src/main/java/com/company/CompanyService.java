package com.innominds.company;

import java.util.Iterator;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.qualcommlabs.usercontext.ConnectorPermissionChangeListener;
import com.qualcommlabs.usercontext.ContentListener;
import com.qualcommlabs.usercontext.ContextCoreConnector;
import com.qualcommlabs.usercontext.ContextCoreConnectorFactory;
import com.qualcommlabs.usercontext.ContextInterestsConnector;
import com.qualcommlabs.usercontext.ContextInterestsConnectorFactory;
import com.qualcommlabs.usercontext.ContextPlaceConnector;
import com.qualcommlabs.usercontext.ContextPlaceConnectorFactory;
import com.qualcommlabs.usercontext.InterestChangeListener;
import com.qualcommlabs.usercontext.PlaceEventListener;
import com.qualcommlabs.usercontext.protocol.ContentAttributes;
import com.qualcommlabs.usercontext.protocol.ContentDescriptor;
import com.qualcommlabs.usercontext.protocol.ContentEvent;
import com.qualcommlabs.usercontext.protocol.PlaceEvent;
import com.qualcommlabs.usercontext.protocol.profile.Profile;

public class CompanyService extends Service implements PlaceEventListener, ContentListener {

    public static final String PLACE_EVENT_DESCRIPTION_KEY = "PLACE_EVENT_DESCRIPTION_KEY";

    static int notificationId = 1;

    private ContextPlaceConnector contextPlaceConnector;
    private ContextCoreConnector contextCoreConnector;
    private ContextInterestsConnector contextInterestsConnector;
    private ConnectorPermissionChangeListener subscriptionPermissionChangeListener = new ConnectorPermissionChangeListener() {

        @Override
        public void permissionChanged(Boolean enabled) {
            if (enabled == false) {
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
            }
        }
    };

    InterestChangeListener interestChangeListener = new InterestChangeListener() {

        @Override
        public void interestChanged(Profile profile) {
            Log.i("CompanyService", "New profile generated." + profile.toString());
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contextCoreConnector = ContextCoreConnectorFactory.get(this);
        contextCoreConnector.addContentListener(this);
        contextCoreConnector.addConnectorPermissionChangeListener(subscriptionPermissionChangeListener);

        contextPlaceConnector = ContextPlaceConnectorFactory.get(this);
        contextPlaceConnector.addPlaceEventListener(this);

        contextInterestsConnector = ContextInterestsConnectorFactory.get(this);
        contextInterestsConnector.addInterestChangeListener(interestChangeListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contextCoreConnector.removeContentListener(this);
        contextCoreConnector.removeConnectorPermissionChangeListener(subscriptionPermissionChangeListener);
        contextPlaceConnector.removePlaceEventListener(this);
    }

    @Override
    public void contentEvent(ContentEvent contentEvent) {
        for (ContentDescriptor contentDescriptor : contentEvent.getContent()) {
            Notification notification = new Notification(R.drawable.icon, contentDescriptor.getTitle(),
                    System.currentTimeMillis());
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.ledARGB = 0xff31a2dd;
            notification.ledOnMS = 500;
            notification.ledOffMS = 200;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;
            notification.defaults |= Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;

            PendingIntent pendingIntent = createPendingIntent(contentDescriptor);

            notification.setLatestEventInfo(this, contentDescriptor.getTitle(),
                    contentDescriptor.getContentDescription(), pendingIntent);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(notificationId, notification);

            notificationId += 1;
        }
    }

    @Override
    public void placeEvent(PlaceEvent event) {
        String placeEventDescription = String.format("%s %s", event.getEventType(), event.getPlace().getPlaceName());
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(PLACE_EVENT_DESCRIPTION_KEY, placeEventDescription);
        editor.commit();
    }

    private PendingIntent createPendingIntent(ContentDescriptor contentDescriptor) {
        Bundle extras = new Bundle();
        extras.putString(ContentActivity.CONTENT_KEY, generateContentString(contentDescriptor));

        Intent launchIntent = new Intent();
        launchIntent.setClass(this, ContentActivity.class);
        launchIntent.putExtras(extras);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                Integer.parseInt(contentDescriptor.getCampaignId()), launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private String generateContentString(ContentDescriptor contentDescriptor) {
        return "Content:\nTitle = " + contentDescriptor.getTitle() + "\nDescription = "
                + contentDescriptor.getContentDescription() + "\nIcon URL = " + contentDescriptor.getIconUrl()
                + "\nContent URL = " + contentDescriptor.getContentUrl() + "\nCampaign ID = "
                + contentDescriptor.getCampaignId() + "\nExpires = " + contentDescriptor.getExpires()
                + "\nContent Attributes:\n" + getContentAttributeString(contentDescriptor.getContentAttributes());
    }

    private String getContentAttributeString(ContentAttributes contentAttributes) {
        if (contentAttributes == null || contentAttributes.getAttributes() == null) {
            return null;
        }
        String attributesString = new String();
        Map<String, Object> attributes = contentAttributes.getAttributes();
        Iterator attributeIterator = attributes.entrySet().iterator();
        while (attributeIterator.hasNext()) {
            Map.Entry pairs = (Map.Entry) attributeIterator.next();
            attributesString += "Key = " + pairs.getKey() + ", Value = " + pairs.getValue() + "\n";
        }
        return attributesString;
    }
}
