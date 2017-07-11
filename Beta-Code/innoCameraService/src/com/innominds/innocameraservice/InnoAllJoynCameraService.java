/*
*
*
*/
package com.innominds.innocameraservice;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusListener;
import org.alljoyn.bus.MessageContext;
import org.alljoyn.bus.SessionListener;
import org.alljoyn.bus.SessionPortListener;
import org.alljoyn.bus.Mutable;
import org.alljoyn.bus.SessionOpts;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusObject;
import org.alljoyn.bus.SignalEmitter;
import org.alljoyn.bus.Status;
import org.alljoyn.bus.annotation.BusSignalHandler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.graphics.ImageFormat;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.util.Log;

import com.innominds.innocameraservice.interfacestub.InnoAlljoynCamInterFace;
import com.innominds.innocameraservice.InnoCameraServiceApplication;


/*
The main functionality is that we have a camera service which is attached to the AllJoyn DBUS 
*/
public class InnoAllJoynCameraService extends Service implements Observer {
	private static final String LOG_TAG = "innocameraservice.InnoAllJoynCameraService";
    //1. Alljoyn related stuff to be done here 
    static {
        Log.i(LOG_TAG, "System.loadLibrary(\"alljoyn_java\")");
        System.loadLibrary("alljoyn_java");
    }

	private InnoCameraServiceApplication           mInoCamServApplicaiton =  null;
    private AllJoynBackGroundHandler               mAllJoynBackgroundHandler = null;
    private CommonAppConstants.BusAttachmentState  mBusAttachmentState = 
                                                    CommonAppConstants.BusAttachmentState.DISCONNECTED;
    /**
     * The bus attachment is the object that provides AllJoyn services to Java
     * clients.  Pretty much all communication with AllJoyn is going to go through
     * this object.
     */
    private BusAttachment                           mBus  = 
                                                    new BusAttachment(InnoCameraServiceApplication.PACKAGE_NAME, 
                                                        BusAttachment.RemoteMessage.Receive);
    
    private CamServiceBusListener                   mCamBusListener = 
                                                    new CamServiceBusListener();
    
    private AlljoynCameraInterface                  mAlljoynCameraInterface =  
                                                    new AlljoynCameraInterface();

    private InnoAlljoynCamInterFace                 mSignalInterfaceObj;                                                
 
    /*Signal Emitter requisites*/
    private Mutable.ShortValue                      mContactPort = 
                                                    new Mutable.ShortValue(CONTACT_PORT);
    private SessionOpts                             mSessionOpts = new SessionOpts();

    private int                                     mSessionId;
    private String                                  mJoinerName;
    private boolean                                 mSessionEstablished = false;
    private AllJoynCamSerSessionPortListner         mAllJoynCamSerSessPrtObj = null;
    private SignalEmitter                           mPreviewSignalEmitter ;
    private Status                                  mStatus;    

 /**
     * The well-known name prefix which all bus attachments hosting a channel
     * will use.  The NAME_PREFIX and the channel name are composed to give
     * the well-known name a hosting bus attachment will request and 
     * advertise.
     */
    private static final String                     NAME_PREFIX = "com.innominds.bus.camera";
    
    /**
     * The well-known session port used as the contact port for the chat service.
     */
    private static final short                      CONTACT_PORT = 27;
    
    /**
     * The object path used to identify the service "location" in the bus
     * attachment.
     */
    private static final String                     OBJECT_PATH = "/AlljoynCameraInterface"; 
    //Debug for the DiscoveryManager::Connect(): 
    //ER_UNABLE_TO_CONNECT_TO_RENDEZVOUS_SERVER: ER_UNABLE_TO_CONNECT_TO_RENDEZVOUS_SERVER
    // Earlier Value = CamService
    
    /**
    * presently i am having the variable here but its better to host it in the Application class
    * We need to take care we do not end up using lot of Memory for this
    */
    private byte[]                                  mPreviewFrame = null;

	/**
	 * We don't use the bindery to communicate between any client and this
	 * service so we return null.
	 */
	public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "onBind()");
        return null;
	}
	
	public void onCreate() {
		//Create the Bus thread //first 
        startBusThread();  
        Log.i(LOG_TAG, "Creating the Service --->>>>> ");
		mInoCamServApplicaiton = (InnoCameraServiceApplication)getApplication();
        Log.i(LOG_TAG, "Got Application instances called Add Observer--->>>>>  ");
        mInoCamServApplicaiton.addObserver(this);        
        /*
         * We have an AllJoyn handler thread running at this time, so take
         * advantage of the fact to get connected to the bus and start finding
         * remote channel instances in the background while the rest of the App
         * is starting up. 
         */      
        //TODO 1: All this would have to end up as part of Handler 
        mAllJoynBackgroundHandler.connect();
        mAllJoynBackgroundHandler.requestName();
        mAllJoynBackgroundHandler.advertise();
        mAllJoynBackgroundHandler.bindSession();
	}	

	public void onStartCommand() {

	}

	public void onDestroy() {

	}

   /**
     * This is the event handler for the Observable/Observed design pattern.
     * Whenever an interesting event happens in our application, the Model (the
     * source of the event) notifies registered observers, resulting in this
     * method being called since we registered as an Observer in onCreate().
     * 
     * This method will be called in the context of the Model, which is, in 
     * turn the context of an event source.  This will either be the single
     * Android application framework thread if the source is one of the
     * Activities of the application or the Service.  It could also be in the
     * context of the Service background thread.  Since the Android Application
     * framework is a fundamentally single threaded thing, we avoid multi-thread
     * issues and deadlocks by immediately getting this event into a separate
     * execution in the context of the Service message pump.
     * 
     * We do this by taking the event from the calling component and queuing
     * it onto a "handler" in our Service and returning to the caller.  When
     * the calling component finishes what ever caused the event notification,
     * we expect the Android application framework to notice our pending
     * message and run our handler in the context of the single application
     * thread.
     * 
     * In reality, both events are executed in the context of the single 
     * Android thread.
     */
    public synchronized void update(Observable o, Object arg,byte[] inPreviewFrame) {
        Log.i(LOG_TAG, "update(" + arg + ")");
        String qualifier = (String)arg;
        /**
        * Here We would need to remove things which we might not require and refactor the 
        * Code for more neat , clean and as per the design
        */
        if (qualifier.equals(CommonAppConstants.APPLICATION_QUIT_EVENT)) {
            Message message = mHandler.obtainMessage(CommonAppConstants.HANDLE_APPLICATION_QUIT_EVENT);
            mHandler.sendMessage(message);
        }
        
        if (qualifier.equals(CommonAppConstants.HOST_INIT_CAMERA_CHANNEL_EVENT)) {
            Message message = mHandler.obtainMessage(CommonAppConstants.HANDLE_HOST_INIT_CHANNEL_EVENT);
            mHandler.sendMessage(message);
        }
        
        if (qualifier.equals(CommonAppConstants.HOST_START_CAMERA_CHANNEL_EVENT)) {
            Message message = mHandler.obtainMessage(CommonAppConstants.HANDLE_HOST_START_CHANNEL_EVENT);
            mHandler.sendMessage(message);
        }
        
        if (qualifier.equals(CommonAppConstants.HOST_STOP_CAMERA_CHANNEL_EVENT)) {
            Message message = mHandler.obtainMessage(CommonAppConstants.HANDLE_HOST_STOP_CHANNEL_EVENT);
            mHandler.sendMessage(message);
        }
        
        if (qualifier.equals(CommonAppConstants.HOST_NEW_PREVIEW_FRAME_READY_EVENT)) {
            Message message = mHandler.obtainMessage(CommonAppConstants.HANDLE_PREVIEW_FRAME_READY_EVENT);
            /* to clean up preview values in the array*/
            mPreviewFrame = null; 
            mPreviewFrame = inPreviewFrame;
            mHandler.sendMessage(message);
        }
    }


    /**
     * This is the Android Service message handler.  It runs in the context of the
     * main Android Service thread, which is also shared with Activities since 
     * Android is a fundamentally single-threaded system.
     * 
     * The important thing for us is to note that this thread cannot be blocked for
     * a significant amount of time or we risk the dreaded "force close" message.
     * We can run relatively short-lived operations here, but we need to run our
     * distributed system calls in a background thread.
     * 
     * This handler serves translates from UI-related events into AllJoyn events
     * and decides whether functions can be handled in the context of the
     * Android main thread or if they must be dispatched to a background thread
     * which can take as much time as needed to accomplish a task.
     */
    /**
    *TODO:: I do not want a annoymous Class here please change later on
    */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonAppConstants.HANDLE_APPLICATION_QUIT_EVENT:
                {
                    Log.i(LOG_TAG, "mHandler.handleMessage(): APPLICATION_QUIT_EVENT");
                    mAllJoynBackgroundHandler.cancelAdvertise();
                    mAllJoynBackgroundHandler.releaseName();
                    mAllJoynBackgroundHandler.exit();
                    stopSelf();
                }
                break;            
            case CommonAppConstants.HANDLE_HOST_INIT_CHANNEL_EVENT:
                {
                    Log.i(LOG_TAG, "mHandler.handleMessage(): HOST_INIT_CHANNEL_EVENT");
                }
                break;              
            case CommonAppConstants.HANDLE_HOST_START_CHANNEL_EVENT:
                {
                    Log.i(LOG_TAG, "mHandler.handleMessage(): HOST_START_CHANNEL_EVENT");
                    mAllJoynBackgroundHandler.requestName();
                    mAllJoynBackgroundHandler.advertise();
                }
                break;
            case CommonAppConstants.HANDLE_HOST_STOP_CHANNEL_EVENT:
                {
                    Log.i(LOG_TAG, "mHandler.handleMessage(): HOST_STOP_CHANNEL_EVENT");
                    mAllJoynBackgroundHandler.cancelAdvertise();
                    mAllJoynBackgroundHandler.releaseName();
                }
                break;
            case CommonAppConstants.HANDLE_PREVIEW_FRAME_READY_EVENT:
                {
                    Log.i(LOG_TAG, "mHandler.handleMessage(): OUTBOUND_CHANGED_EVENT");
                    mAllJoynBackgroundHandler.sendMessages();
                }
                break;
            default:
                break;
            }
        }
    };
    


    /**
     * The instance of the AllJoyn background thread handler.  It is created
     * when Android decides the Service is needed and is called from the
     * onCreate() method.  When Android decides our Service is no longer 
     * needed, it will call onDestroy(), which spins down the thread.
     * this is a inner class which extends the Android Handler Thread Implementation
     */
    private final class AllJoynBackGroundHandler extends Handler {
    	//Need to handle all the Alloyn Bus setup and discovery and advetrising and even 
    	// Signal setup
    	// AND Otheer things which 
        public AllJoynBackGroundHandler(Looper looper) {
            super(looper);
        }
        
                /**
         * Exit the background handler thread.  This will be the last message
         * executed by an instance of the handler.
         */
        public void exit() {
            Log.i(LOG_TAG, "mAllJoynBackgroundHandler.exit()");
        	Message msg = mAllJoynBackgroundHandler.obtainMessage(CommonAppConstants.EXIT);
            mAllJoynBackgroundHandler.sendMessage(msg);
        }
        
        /**
         * Connect the application to the Alljoyn bus attachment.  We expect
         * this method to be called in the context of the main Service thread.
         * All this method does is to dispatch a corresponding method in the
         * context of the service worker thread.
         */
        public void connect() {
            Log.i(LOG_TAG, "mAllJoynBackgroundHandler.connect()");
        	Message msg = mAllJoynBackgroundHandler.obtainMessage(CommonAppConstants.CONNECT);
            mAllJoynBackgroundHandler.sendMessage(msg);
        }
        
        /**
         * Disonnect the application from the Alljoyn bus attachment.  We
         * expect this method to be called in the context of the main Service
         * thread.  All this method does is to dispatch a corresponding method
         * in the context of the service worker thread.
         */
        public void disconnect() {
            Log.i(LOG_TAG, "mAllJoynBackgroundHandler.disconnect()");
        	Message msg = mAllJoynBackgroundHandler.obtainMessage(CommonAppConstants.DISCONNECT);
            mAllJoynBackgroundHandler.sendMessage(msg);
        }

        /**
         * Start discovering remote instances of the application.  We expect
         * this method to be called in the context of the main Service thread.
         * All this method does is to dispatch a corresponding method in the
         * context of the service worker thread.
         */
        public void startDiscovery() {
            Log.i(LOG_TAG, "mAllJoynBackgroundHandler.startDiscovery()");
        	Message msg = mAllJoynBackgroundHandler.obtainMessage(CommonAppConstants.START_DISCOVERY);
        	mAllJoynBackgroundHandler.sendMessage(msg);
        }
        
        /**
         * Stop discovering remote instances of the application.  We expect
         * this method to be called in the context of the main Service thread.
         * All this method does is to dispatch a corresponding method in the
         * context of the service worker thread.
         */
        public void cancelDiscovery() {
            Log.i(LOG_TAG, "mAllJoynBackgroundHandler.stopDiscovery()");
        	Message msg = mAllJoynBackgroundHandler.obtainMessage(CommonAppConstants.CANCEL_DISCOVERY);
        	mAllJoynBackgroundHandler.sendMessage(msg);
        }

        public void requestName() {
            Log.i(LOG_TAG, "mAllJoynBackgroundHandler.requestName()");
        	Message msg = mAllJoynBackgroundHandler.obtainMessage(CommonAppConstants.REQUEST_NAME);
        	mAllJoynBackgroundHandler.sendMessage(msg);
        }
        
        public void releaseName() {
            Log.i(LOG_TAG, "mAllJoynBackgroundHandler.releaseName()");
        	Message msg = mAllJoynBackgroundHandler.obtainMessage(CommonAppConstants.RELEASE_NAME);
        	mAllJoynBackgroundHandler.sendMessage(msg);
        }
        
        public void bindSession() {
            Log.i(LOG_TAG, "mAllJoynBackgroundHandler.bindSession()");
        	Message msg = mAllJoynBackgroundHandler.obtainMessage(CommonAppConstants.BIND_SESSION);
        	mAllJoynBackgroundHandler.sendMessage(msg);
        }
        
        public void unbindSession() {
            Log.i(LOG_TAG, "mAllJoynBackgroundHandler.unbindSession()");
        	Message msg = mAllJoynBackgroundHandler.obtainMessage(CommonAppConstants.UNBIND_SESSION);
        	mAllJoynBackgroundHandler.sendMessage(msg);
        }
        
        public void advertise() {
            Log.i(LOG_TAG, "mAllJoynBackgroundHandler.advertise()");
        	Message msg = mAllJoynBackgroundHandler.obtainMessage(CommonAppConstants.ADVERTISE);
        	mAllJoynBackgroundHandler.sendMessage(msg);
        }
        
        public void cancelAdvertise() {
        	Message msg = mAllJoynBackgroundHandler.obtainMessage(CommonAppConstants.CANCEL_ADVERTISE);
        	mAllJoynBackgroundHandler.sendMessage(msg);
        }

        public void sendMessages() {
            Log.i(LOG_TAG, "mAllJoynBackgroundHandler.sendMessages()");
        	Message msg = mAllJoynBackgroundHandler.obtainMessage(CommonAppConstants.SEND_MESSAGES);
        	mAllJoynBackgroundHandler.sendMessage(msg);
        }

        /**
         * The message handler for the worker thread that handles background
         * tasks for the AllJoyn bus.
         */
        public void handleMessage(Message msg) {
            Log.i(LOG_TAG,"Msg Enum Value::" + msg.what);
            switch (msg.what) {

            case CommonAppConstants.CONNECT:
	            doConnect();
            	break;
	        case CommonAppConstants.DISCONNECT:
		        doDisconnect();
		    	break;
            case CommonAppConstants.START_DISCOVERY:
	            //doStartDiscovery();
                //I am not client i am a service please do this in a client
            	break;
	        case CommonAppConstants.CANCEL_DISCOVERY:
		        //doStopDiscovery();
                //I am not client i am a service please do this in a client            
		    	break;
	        case CommonAppConstants.REQUEST_NAME:
		        doRequestName();
		    	break;
	        case CommonAppConstants.RELEASE_NAME:
		        doReleaseName();
		    	break;		
	        case CommonAppConstants.BIND_SESSION:
		        //doBindSession();
                doBindAndRegiterSignalEmiter();            
		    	break;
	        case CommonAppConstants.UNBIND_SESSION:
		        //doUnbindSession();
		        break;
	        case CommonAppConstants.ADVERTISE:
		        doAdvertise();
		    	break;
	        case CommonAppConstants.CANCEL_ADVERTISE:
		        doCancelAdvertise();		        
		    	break;	      
	        case CommonAppConstants.SEND_MESSAGES:
		        doSendMessages();
		        break;
	        case CommonAppConstants.EXIT:
                getLooper().quit();
                break;
		    default:
		    	break;
            }
        }

    }


    /**
     * Since basically our whole reason for being is to spin up a thread to
     * handle long-lived remote operations, we provide thsi method to do so.
     */
    private void startBusThread() {
        HandlerThread busThread = new HandlerThread("AllJoynBackGroundHandler");
        busThread.start();
        mAllJoynBackgroundHandler = new AllJoynBackGroundHandler(busThread.getLooper());
    }

    /**
     * When Android decides that our Service is no longer needed, we need to
     * tear down the thread that is servicing our long-lived remote operations.
     * This method does so. 
     */
    private void stopBusThread() {
        mAllJoynBackgroundHandler.exit();
    }

    /*
    */
    /**
     * The CamServiceBusListener is a class that listens to the AllJoyn bus for
     * notifications corresponding to the existence of events happening out on
     * the bus.  We provide one implementation of our listener to the bus
     * attachment during the connect(). 
     */
    private class CamServiceBusListener extends BusListener {
        /**
         * This method is called when AllJoyn discovers a remote attachment
         * that is hosting an chat channel.  We expect that since we only
         * do a findAdvertisedName looking for instances of the chat
         * well-known name prefix we will only find names that we know to
         * be interesting.  When we find a remote application that is
         * hosting a channel, we add its channel name it to the list of
         * available channels selectable by the user.
         *
         * In the class documentation for the BusListener note that it is a
         * requirement for this method to be multithread safe.  This is
         * accomplished by the use of a monitor on the ChatApplication as
         * exemplified by the synchronized attribute of the addFoundChannel
         * method there.
         */
        public void foundAdvertisedName(String name, short transport, String namePrefix) {
            Log.i(LOG_TAG, "CamServiceBusListener.foundAdvertisedName(" + name + ")");
            //InnoCameraServiceApplication lInoCamServApplicaiton = (InnoCameraServiceApplication)getApplication();
        }
        
        /**
         * This method is called when AllJoyn decides that a remote bus
         * attachment that is hosting an chat channel is no longer available.
         * When we lose a remote application that is hosting a channel, we
         * remote its name from the list of available channels selectable
         * by the user.  
         *
         * In the class documentation for the BusListener note that it is a
         * requirement for this method to be multithread safe.  This is
         * accomplished by the use of a monitor on the ChatApplication as
         * exemplified by the synchronized attribute of the removeFoundChannel
         * method there.
         */
        public void lostAdvertisedName(String name, short transport, String namePrefix) {
            Log.i(LOG_TAG, "CamServiceBusListener.lostAdvertisedName(" + name + ")");
            //InnoCameraServiceApplication lInoCamServApplicaiton = (InnoCameraServiceApplication)getApplication();            
        }
    }

    //Here we have Camera Interface implemented by a class which will be used by 
    //the bus object to communicate to all others .. and we are only doing multi casting
    private class AlljoynCameraInterface implements InnoAlljoynCamInterFace , BusObject {

        //@BusSignal  
        public void CameraPreviewSignal(byte[] aPreviewFrame) {

        }

        public void CameraPreviewString(String astr) {

        }

        //I have hard coded it at present since i am expecting the Preview format to be NV21
        // I have decided to support only this at present
        //@BusMethod(replySignature = "ar")   
        public int getPreviewFormat() {

            return ImageFormat.NV21 ;

        }
        
        //@BusMethod(replySignature = "ar")   
        public int getPreviewFormatBitsPerPixel() {

            return ImageFormat.getBitsPerPixel(ImageFormat.NV21);

        }
    }


    private class AllJoynCamSerSessionPortListner extends SessionPortListener {

        public boolean acceptSessionJoiner(short sessionPort, String joiner,
            SessionOpts sessionOpts) {
            if (sessionPort == CONTACT_PORT) {
                return true;
            } 
            else {
                return false;
            } 
        }

        public void sessionJoined(short sessionPort, int id, String joiner) {
            mSessionId = id;
            mJoinerName = joiner;
            mSessionEstablished = true;
        }
    }

    private void doConnect() {
        Log.i(LOG_TAG, "doConnect()");
        org.alljoyn.bus.alljoyn.DaemonInit.PrepareDaemon(getApplicationContext());
        assert(mBusAttachmentState == CommonAppConstants.BusAttachmentState.DISCONNECTED);
        mBus.useOSLogging(true);
        mBus.setDebugLevel("ALLJOYN_JAVA", 7);
        //mBus.registerBusListener(mCamBusListener);
        
        /* 
         * To make a service available to other AllJoyn peers, first
         * register a BusObject with the BusAttachment at a specific
         * object path.  Our service is implemented by the ChatService
         * BusObject found at the "/chatService" object path.
         */
        Status status = mBus.registerBusObject(mAlljoynCameraInterface, OBJECT_PATH);
        if (Status.OK != status) {
            mInoCamServApplicaiton.alljoynError(InnoCameraServiceApplication.Module.HOST, 
                "Unable to register the chat bus object: (" + status + ")");
            return;
        }
        
        status = mBus.connect();
        if (status != Status.OK) {
            mInoCamServApplicaiton.alljoynError(InnoCameraServiceApplication.Module.GENERAL, 
                "Unable to connect to the bus: (" + status + ")");
            return;
        }
        
        mBusAttachmentState = CommonAppConstants.BusAttachmentState.CONNECTED;

        //Register the Signal here or check where i would do it ?
        //Would do it when we are connecting presently i would define some thing at later stage
    } 

    /**
     * Implementation of the functionality related to disconnecting our app
     * from the AllJoyn bus.  We expect that this method will only be called
     * in the context of the AllJoyn bus handler thread.  We expect that this
     * method will only be called in the context of the AllJoyn bus handler
     * thread; and while we are in the CONNECTED state.
     */
    private boolean doDisconnect() {
        Log.i(LOG_TAG, "doDisonnect()");
        assert(mBusAttachmentState == CommonAppConstants.BusAttachmentState.CONNECTED);
        mBus.unregisterBusListener(mCamBusListener);
        mBus.disconnect();
        mBusAttachmentState = CommonAppConstants.BusAttachmentState.DISCONNECTED;
        return true;
    }
    

 
       
    /**
     * Implementation of the functionality related to requesting a well-known
     * name from an AllJoyn bus attachment.
     */
    private void doRequestName() {
        Log.i(LOG_TAG, "doRequestName()");
        
        /*
         * In order to request a name, the bus attachment must at least be
         * connected.
         */
        int stateRelation = mBusAttachmentState.compareTo(CommonAppConstants.BusAttachmentState.DISCONNECTED);
        assert (stateRelation >= 0);
        
        /*
         * We depend on the user interface and model to work together to not
         * get this process started until a valid name is set in the channel name.
         */
        String wellKnownName = NAME_PREFIX ;
        Status status = mBus.requestName(wellKnownName, 
            BusAttachment.ALLJOYN_REQUESTNAME_FLAG_DO_NOT_QUEUE);
        if (status == Status.OK) {
            //mHostChannelState = HostChannelState.NAMED;
            //mInoCamServApplicaiton.hostSetChannelState(mHostChannelState);
        } else {
            mInoCamServApplicaiton.alljoynError(InnoCameraServiceApplication.Module.USE, 
                "Unable to acquire well-known name: (" + status + ")");
        }
    }
    
    /**
     * Implementation of the functionality related to releasing a well-known
     * name from an AllJoyn bus attachment.
     */
    private void doReleaseName() {
        Log.i(LOG_TAG, "doReleaseName()");
        
        /*
         * In order to release a name, the bus attachment must at least be
         * connected.
         */
        int stateRelation = mBusAttachmentState.compareTo(CommonAppConstants.BusAttachmentState.DISCONNECTED);
        assert (stateRelation >= 0);
        assert(mBusAttachmentState == CommonAppConstants.BusAttachmentState.CONNECTED || 
            mBusAttachmentState == CommonAppConstants.BusAttachmentState.DISCOVERING);
        
        /*
         * We need to progress monotonically down the hosted channel states
         * for sanity.
         */
        //assert(mHostChannelState == HostChannelState.NAMED);
        
        /*
         * We depend on the user interface and model to work together to not
         * change the name out from under us while we are running.
         */
        String wellKnownName = NAME_PREFIX ;

        /*
         * There's not a lot we can do if the bus attachment refuses to release
         * the name.  It is not a fatal error, though, if it doesn't.  This is
         * because bus attachments can have multiple names.
         */
        mBus.releaseName(wellKnownName);
        //mHostChannelState = HostChannelState.IDLE;
        //mInoCamServApplicaiton.hostSetChannelState(mHostChannelState);
    }


    /**
     * Implementation of the functionality related to advertising a service on
     * an AllJoyn bus attachment.
     */
    private void doAdvertise() {
        Log.i(LOG_TAG, "doAdvertise()");
        
        /*
         * We depend on the user interface and model to work together to not
         * change the name out from under us while we are running.
         */
        String wellKnownName = NAME_PREFIX ;        
        Status status = mBus.advertiseName(wellKnownName, SessionOpts.TRANSPORT_ANY);
        
        if (status == Status.OK) {

        } 
        else {
            mInoCamServApplicaiton.alljoynError(InnoCameraServiceApplication.Module.HOST, 
                "Unable to advertise well-known name: (" + status + ")");
            return;
        }
    }
    
    /**
     * Implementation of the functionality related to canceling an advertisement
     * on an AllJoyn bus attachment.
     */
    private void doCancelAdvertise() {
        Log.i(LOG_TAG, "doCancelAdvertise()");
        
        /*
         * We depend on the user interface and model to work together to not
         * change the name out from under us while we are running.
         */
        String wellKnownName = NAME_PREFIX ;        
        Status status = mBus.cancelAdvertiseName(wellKnownName, SessionOpts.TRANSPORT_ANY);
        
        if (status != Status.OK) {
            mInoCamServApplicaiton.alljoynError(InnoCameraServiceApplication.Module.HOST, 
                "Unable to cancel advertisement of well-known name: (" + status + ")");
            return;
        }        
    }

   /**
     * Implementation of the functionality related to sending messages out over
     * an existing remote session.  Note that we always send all of the
     * messages on the outbound queue, so there may be instances where this
     * method is called and we find nothing to send depending on the races.
     */
    private void doSendMessages() {
        Log.i(LOG_TAG, "doSendMessages()");
        /**
        * mPreviewFrame Needs to put to other listeners in Bus
        * here we would send the signal to all
        * Above we should have registered the signal emitter or else we will cross check here and 
        * then recreate the emitter and then send out the signal;
        */ 
        try {
            //mSignalInterfaceObj.CameraPreviewSignal(mPreviewFrame);
             mSignalInterfaceObj.CameraPreviewString("byte[] aPreviewFrame"); 
        }
        catch (BusException ex) {
            mInoCamServApplicaiton.alljoynError(InnoCameraServiceApplication.Module.USE, 
                "Bus exception while sending message: (" + ex + ")");
        }
    }

    private void doBindAndRegiterSignalEmiter() {
        Log.i(LOG_TAG, "doBindAndRegiterSignalEmiter() -----> Start");

        mSessionOpts.traffic = SessionOpts.TRAFFIC_MESSAGES;
        mSessionOpts.isMultipoint = false;
        mSessionOpts.proximity = SessionOpts.PROXIMITY_ANY;
        mSessionOpts.transports = SessionOpts.TRANSPORT_ANY;  
        //mAllJoynCamSerSessPrtObj
        mAllJoynCamSerSessPrtObj = new AllJoynCamSerSessionPortListner();
        //Would like wait for the session to be established 
        // try {
        //     while (!mSessionEstablished) {

        //        Log.i(LOG_TAG,"-------> "+mSessionEstablished);
        //        Thread.sleep(10);
        //     }
        // } 
        // catch (InterruptedException ex) {
        //   Log.i(LOG_TAG,"Interrupted");
        // } 


        Log.i(LOG_TAG," " + mSessionEstablished);

        if(mSessionEstablished) {
            Log.i(LOG_TAG,"mSessionEstablished ="+mSessionEstablished);
            mStatus = mBus.bindSessionPort(mContactPort, mSessionOpts,mAllJoynCamSerSessPrtObj);
            mPreviewSignalEmitter = new SignalEmitter(mAlljoynCameraInterface, mJoinerName,mSessionId,
                               SignalEmitter.GlobalBroadcast.Off);
            //Creating a session less emitter
            // mPreviewSignalEmitter = new SignalEmitter(mAlljoynCameraInterface, 0,
            //                         SignalEmitter.GlobalBroadcast.On);

            //mPreviewSignalEmitter.setSessionlessFlag(false); 
            mSignalInterfaceObj = mPreviewSignalEmitter.getInterface(InnoAlljoynCamInterFace.class);
            if(mSignalInterfaceObj != null) {
                Log.i(LOG_TAG,"mSignalInterfaceObj obj created");
            }            
        } else {
        }


        Log.i(LOG_TAG, "doBindAndRegiterSignalEmiter() ------>  end");
    }

    private void doUnBindSession() {
        Log.i(LOG_TAG, "doUnBindSession()");
    }


}
