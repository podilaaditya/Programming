package com.aptina.threads;

import android.os.AsyncTask;
import android.util.Log;

import com.aptina.adapter.HistogramPageAdapter;
import com.aptina.camera.interfaces.UpdateHistThreadInterface;
import com.aptina.data.HistogramRGBData;

/**
 * Async task to update the histogram fragment
 */
public class UpdateHistThread extends AsyncTask<byte[], Object, double[][]> {
	/**
	 * Logging tag
	 */
	private static final String TAG = "UpdateHistThread";
	
	/**
     * Control the update of the histogram between the Async and UI/surface threads
     */
    private volatile boolean mUpdateHist = true;
    
    /**
     * The callback interface for the {@link HistogramPageAdapter} to update the charts
     */
    private UpdateHistThreadInterface mCallback = null;
    /**
     * Variable to hold the max number in the dataset for normalization of the dataset
     */
	private double mMax = -1;
	
	public UpdateHistThread(UpdateHistThreadInterface callback){
		setUpdateHistThreadInterface(callback);
	}
	
	public UpdateHistThread(){
	}
	
	public void setUpdateHistThreadInterface(UpdateHistThreadInterface callback){
		mCallback = callback;
	}
	
	public UpdateHistThreadInterface getUpdateHistThreadInterface(){
		return mCallback;
	}
	
	public boolean getUpdateLatch(){
		return mUpdateHist;
	}
	
	/**
	 * A linear execute that does not create a queue of tasks to complete
	 * @param params
	 */
	public void linearExecute(byte[]... params){
		if(mUpdateHist){
			this.execute(params);
		}
	}
	@Override
    protected void onPreExecute(){
//		Log.e(TAG, "onPreExecute()");
		mUpdateHist = false;
    }
	@Override
	protected double[][] doInBackground(byte[]... params) {
//		Log.e(TAG, "doInBackground()");
		byte[][] split = new byte[HistogramRGBData.RGB_COLOR_DEPTH][HistogramRGBData.CURRENT_CHART_INTENSITY_DEPTH];
		double[][] rgb = new double[HistogramRGBData.RGB_COLOR_DEPTH][HistogramRGBData.CURRENT_CHART_INTENSITY_DEPTH];
		for(int j = 0, i = 0; i < HistogramRGBData.CURRENT_CHART_INTENSITY_DEPTH; i++, j++){
			split[0][j] = params[0][i*3];
			split[1][j] = params[0][i*3 + 1];
			split[2][j] = params[0][i*3 + 2];
		}
		
		rgb[0] = getNorm(split[0]);
		rgb[1] = getNorm(split[1]);
		rgb[2] = getNorm(split[2]);
		
		
		return rgb;
	}
	@Override
    protected void onProgressUpdate(Object... values) {

    }
    
    @Override
    protected void onPostExecute(double[][] rgbArray) {
//    	Log.e(TAG, "onPostExecute()");
    	mCallback.onUpdateDataChannels(rgbArray);
    	mCallback.notifyPagerDataSetChanged();
    	mUpdateHist = true;
    }
    
    private double[] normalize(double[] toNorm, double max){
    	if(max !=0){
    		for(int i = 0; i < toNorm.length; i++){
        		toNorm[i] /= max;
        		
        	}
    	}else{//Max is a double so /0 produces NaN and not a crash
    		Log.e(TAG,"MAX : " + max + ", returning array unchanged, cannot divide by 0");
    		return toNorm;
    	}
    	
    	
    	return toNorm;
    }
   
    /**
     * Control the latch
     * @param val
     */
    private void setUpdateLatch(boolean val){
    	mUpdateHist = val;
    }
    private double[] getNorm(byte[] ba){
    	double[] ans = new double[ba.length];
    	for(int i = 0; i < ba.length; i++){
    		ans[i] = (double) ba[i];
    		if(mMax < ans[i]){
    			mMax = ans[i];
    		}
    	}
    	return normalize(ans, mMax);
    }
    
	
}