/*
This is the basic interface for the camera by which i would send the 
preview and preview format and bits per pixel

*/

package com.innominds.innocameraservice.interfacestub;


import org.alljoyn.bus.BusException;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusMethod;
import org.alljoyn.bus.annotation.AccessPermission;
import org.alljoyn.bus.annotation.BusSignal;


// interface for the Raw data transfer
// interface for the Data format and the pixel sizes 

@BusInterface (name = "com.innominds.bus.camera")
public interface InnoAlljoynCamInterFace {
	//Define each of the interface properly need to check on
	// 1. Signal for the data 
	// 2. Methods
	// 3. Properties [Get properties]
	
	// @BusMethod(replySignature = "ar")
	// public byte[] getPreviewFrame();
	/*
     * The BusSignal annotation signifies that this function should be used as
     * part of the AllJoyn interface.  The runtime is smart enough to figure
     * out that this is a used as a signal emitter and is only called to send
     * signals and not to receive signals.
     */
    @BusSignal	
	public void CameraPreviewSignal(byte[] aPreviewFrame) throws BusException;

    @BusSignal	
	public void CameraPreviewString(String aStr) throws BusException;
	
	@BusMethod(replySignature = "ar")	
	public int getPreviewFormat();
	
	@BusMethod(replySignature = "ar")	
	public int getPreviewFormatBitsPerPixel();

}
