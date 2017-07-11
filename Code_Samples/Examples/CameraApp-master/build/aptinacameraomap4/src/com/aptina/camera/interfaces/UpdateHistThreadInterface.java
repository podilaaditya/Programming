package com.aptina.camera.interfaces;

public interface UpdateHistThreadInterface {
	
	/**
	 * Function updates the color array data set of the {@link HistogramRGBData}
	 * @param colorArray The array of normalized color values
	 */
	public void onUpdateDataChannels(double[][] colorArray);
	
	public void notifyPagerDataSetChanged();
}
