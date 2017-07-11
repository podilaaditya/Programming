/**
 * 
 */
package com.aptina.data;

import java.util.ArrayList;
import java.util.List;

import com.aptina.camera.fragments.HistogramFrag;

import android.app.Fragment;
import android.util.Log;

/**
 * @author stoyan
 *
 */
public class HistogramRGBData {
	private static final String TAG = "HistogramRGBData";
	
	private static HistogramRGBData mInstance = null;
	/**
	 * The intensity depth of the colors, 8 byte color would be 256
	 */
	public static final int RGB_INTENSITY_DEPTH = 255;
	public static int CURRENT_CHART_INTENSITY_DEPTH = RGB_INTENSITY_DEPTH;
	
	/**
	 * The color depth of the channel, RGB would have a depth of 3
	 */
	public static final int RGB_COLOR_DEPTH = 3;
	
	/**
	 * The current color depth of the histogram, default {@link #RGB_COLOR_DEPTH}
	 */
	private int mCurrentColorDepth = RGB_COLOR_DEPTH;
	
	/**
	 * Final int representations of the 3 RGB channels
	 */
	public static final int RED_CHANNEL = 0;
	public static final int GREEN_CHANNEL = 1;
	public static final int BLUE_CHANNEL = 2;
	public static final int RGB_CHANNELS = 3;
	
	/**
	 * A list holding the arrays of the x axis, for 8 bit each array should be 0-255({@link #RGB_INTENSITY_DEPTH}).
	 * For RBG there should be 3 x axis arrays, all equal
	 */
	private static List<double[]> xAxisList;
	
	/**
	 * A list holding the arrays of the y-axis.
	 * For RBG there should be 3 y-axis arrays.
	 * 
	 * All values will be normalized when added to their array
	 */
	private static List<double[]> yAxisList;
	
	/**
	 * 
	 */
	private static List<Fragment> mRGBFrags = null;
	/**
	 * Instantiate the singleton class
	 * 
	 * @param colorDepth
	 */
	protected HistogramRGBData(int colorDepth) {
		mCurrentColorDepth = colorDepth;
		xAxisList = new ArrayList<double[]>(mCurrentColorDepth);
		yAxisList = new ArrayList<double[]>(mCurrentColorDepth);
		for(int i = 0; i < mCurrentColorDepth; i++){
			xAxisList.add(getXAxisArray(CURRENT_CHART_INTENSITY_DEPTH));
			yAxisList.add(getYZeroAxisArray(CURRENT_CHART_INTENSITY_DEPTH));
		}
	}
	
	/**
	 * Create the x-axis arrays with all values initiated from
	 * 0->intensityDepth in ascending order
	 * 
	 * @param intensityDepth The color channel depth
	 * @return The x-axis array
	 */
	private double[] getXAxisArray(int intensityDepth) {
		double[] ar = new double[intensityDepth];
		for(int i = 0; i < intensityDepth; i++){
			ar[i] = i;
		}
		return ar;
	}
	
	/**
	 * Create the y-axis arrays with all values initially at 0
	 * 
	 * @param intensityDepth The color channel depth
	 * @return An empty array of size intensityDepth
	 */
	private double[] getYZeroAxisArray(int intensityDepth){
		double[] ar = new double[intensityDepth];
		return ar;
	}
	
	/**
	 * Ensure that we have a singleton, only one instance of this class 
	 * @return The singleton object
	 */
	public static HistogramRGBData getInstance() {
		if(mInstance == null) {
			mInstance = new HistogramRGBData(RGB_COLOR_DEPTH);
	    }
		return mInstance;
	 }

	/**
	 * Reset the instance so that we can use it across contexts.
	 * Fragments need different view hierarchies for different contexts
	 */
	public static void reset(){
		mInstance = null;
		mRGBFrags = null;
	}
	/**
	 * Return the double array of the color channel selected
	 * 
	 * @param channel One of {@link #RED_CHANNEL}, {@link #GREEN_CHANNEL}, or {@link #BLUE_CHANNEL}
	 * @return The double array of that channel, intensity from 0 to {@link #INTENTSITY_DEPTH}
	 */
	public synchronized double[] getYAxisArray(int channel){
		switch(channel){
		case RED_CHANNEL:
			return yAxisList.get(0);
		case GREEN_CHANNEL:
			return yAxisList.get(1);
		case BLUE_CHANNEL:
			return yAxisList.get(2);
			default:
				return yAxisList.get(0);
		}
	}
	
	/**
	 * Get the y-axis list of arrays that represent the y points to be graphed
	 * @param channel 
	 * @return
	 */
	public synchronized List<double[]> getYAxisList(int channel){
		switch(channel){
		case RED_CHANNEL:
			return yAxisList.subList(0, 1);
		case GREEN_CHANNEL:
			return yAxisList.subList(1, 2);
		case BLUE_CHANNEL:
			return yAxisList.subList(2, 3);
		case RGB_CHANNELS:
			return yAxisList;
			default:
				return yAxisList.subList(0, 1);
		}
	}
	
	/**
	 * Get a list of all the histogram fragments for RGBA
	 * @return
	 */
	public List<Fragment> getFragList() {
		if(mRGBFrags != null){
			return mRGBFrags;
		}
		mRGBFrags = new ArrayList<Fragment>();
		mRGBFrags.add(HistogramFrag.newInstance(HistogramFrag.EXTRA_VALUE_ALL, 3, HistogramRGBData.RGB_CHANNELS));
		mRGBFrags.add(HistogramFrag.newInstance(HistogramFrag.EXTRA_VALUE_RED, 1, HistogramRGBData.RED_CHANNEL));
		mRGBFrags.add(HistogramFrag.newInstance(HistogramFrag.EXTRA_VALUE_GREEN, 1, HistogramRGBData.GREEN_CHANNEL));
		mRGBFrags.add(HistogramFrag.newInstance(HistogramFrag.EXTRA_VALUE_BLUE, 1, HistogramRGBData.BLUE_CHANNEL));
		return mRGBFrags;
	}
	/**
	 * Get the x axis list of arrays that represent the x points to be graphed
	 * 
	 * @param size The number of independent lines on the graph
	 * @return The list of x axis points for each graph set
	 */
	public synchronized List<double[]> getXAxisList(int size){
		switch(size){
		case 1:
			return xAxisList.subList(0, 1);
		case 2:
			return xAxisList.subList(0, 2);
		case 3:
			return xAxisList.subList(0, 3);
			default:
				return xAxisList.subList(0, 1);
		}
	}
	
	public synchronized void updateYAxisList(double[][] vals){
		for(int i = 0; i < mCurrentColorDepth; i++){
			yAxisList.set(i, vals[i]);
//			logArr(yAxisList.get(i));
		}
	}
	
	public static void logArr(double[] ar){
		StringBuilder sb = new StringBuilder();
		Log.w(TAG, "length : " + ar.length);
		for(int i = 0; i < ar.length; i++){
			sb.append(ar[i] + ",");
		}
		Log.w(TAG, sb.toString());
	}
	
}
