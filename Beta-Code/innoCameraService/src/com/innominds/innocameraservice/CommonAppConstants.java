package com.innominds.innocameraservice;


public class CommonAppConstants {

    public static final String APPLICATION_QUIT_EVENT = "APPLICATION_QUIT_EVENT";
	public static final String ALLJOYN_ERROR_EVENT = "ALLJOYN_ERROR_EVENT";
	public static final String HOST_START_CAMERA_CHANNEL_EVENT = "HOST_START_CAMERA_CHANNEL_EVENT";
	public static final String HOST_INIT_CAMERA_CHANNEL_EVENT = "HOST_INIT_CAMERA_CHANNEL_EVENT";
	public static final String HOST_STOP_CAMERA_CHANNEL_EVENT = "HOST_STOP_CAMERA_CHANNEL_EVENT";
	public static final String HOST_CAMERA_CHANNEL_STATE_CHANGED_EVENT = "HOST_CAMERA_CHANNEL_STATE_CHANGED_EVENT";
    public static final String HOST_NEW_PREVIEW_FRAME_READY_EVENT = "HOST_NEW_PREVIEW_FRAME_READY_EVENT";

	//Service related stuff
    public static final int EXIT = 1;
    public static final int CONNECT = 2;
    public static final int DISCONNECT = 3;
    public static final int START_DISCOVERY = 4;
    public static final int CANCEL_DISCOVERY = 5;
    public static final int REQUEST_NAME = 6;
    public static final int RELEASE_NAME = 7;
    public static final int BIND_SESSION = 8;
    public static final int UNBIND_SESSION = 9;
    public static final int ADVERTISE = 10;
    public static final int CANCEL_ADVERTISE = 11;
    public static final int JOIN_SESSION = 12;
    public static final int LEAVE_SESSION = 13;
    public static final int SEND_MESSAGES = 14;

   /**
     * Value for the HANDLE_APPLICATION_QUIT_EVENT case observer notification handler. 
     */
    public static final int HANDLE_APPLICATION_QUIT_EVENT = 0;
    
    /**
     * Value for the HANDLE_USE_JOIN_CHANNEL_EVENT case observer notification handler. 
     */
    public static final int HANDLE_USE_JOIN_CHANNEL_EVENT = 1;
    
    /**
     * Value for the HANDLE_USE_LEAVE_CHANNEL_EVENT case observer notification handler. 
     */
    public static final int HANDLE_USE_LEAVE_CHANNEL_EVENT = 2;
    
    /**
     * Value for the HANDLE_HOST_INIT_CHANNEL_EVENT case observer notification handler. 
     */
    public static final int HANDLE_HOST_INIT_CHANNEL_EVENT = 3;
    
    /**
     * Value for the HANDLE_HOST_START_CHANNEL_EVENT case observer notification handler. 
     */
    public static final int HANDLE_HOST_START_CHANNEL_EVENT = 4;
    
    /**
     * Value for the HANDLE_HOST_STOP_CHANNEL_EVENT case observer notification handler. 
     */
    public static final int HANDLE_HOST_STOP_CHANNEL_EVENT = 5;
    
    /**
     * Value for the HANDLE__PREVIEW_FRAME_READY_EVENT case observer notification handler. 
     */
    public static final int HANDLE_PREVIEW_FRAME_READY_EVENT = 6;
    
    /**
     * Enumeration of the states of the AllJoyn bus attachment.  This
     * lets us make a note to ourselves regarding where we are in the process
     * of preparing and tearing down the fundamental connection to the AllJoyn
     * bus.
     * 
     * This should really be a more public think, but for the sample we want
     * to show the user the states we are running through.  Because we are
     * really making a data hiding exception, and because we trust ourselves,
     * we don't go to any effort to prevent the UI from changing our state out
     * from under us.
     * 
     * There are separate variables describing the states of the client
     * ("use") and service ("host") pieces.
     */
    public static enum BusAttachmentState {
        DISCONNECTED,   /** The bus attachment is not connected to the AllJoyn bus */ 
        CONNECTED,      /** The  bus attachment is connected to the AllJoyn bus */
        DISCOVERING     /** The bus attachment is discovering remote attachments hosting chat channels */
    }

}