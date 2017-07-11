
package com.innominds.innocameraservice;



public interface Observer {
	public void update(Observable o, Object arg, byte[] inPreviewFrame);
}
