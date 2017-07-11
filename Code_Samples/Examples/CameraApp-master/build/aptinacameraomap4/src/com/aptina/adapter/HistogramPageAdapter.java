/**
 * 
 */
package com.aptina.adapter;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.support.v13.app.FragmentPagerAdapter;

import com.aptina.camera.interfaces.UpdateHistThreadInterface;
import com.aptina.data.HistogramRGBData;
import com.aptina.threads.UpdateHistThread;


/**
 * @author stoyan
 *
 */
public class HistogramPageAdapter extends FragmentPagerAdapter {
	/**
	 * Logging tag
	 */
	private static final String TAG = "HistogramPageAdapter";
	
	private List<Fragment> mHistFrags;
	
	/**
	 * The Async Task that we use to process and post updates to the graphs
	 */
//	private UpdateHistFrag mUpdateTask = null;
	
	/**
	 * The interface for {@link #mUpdateTask} that will update the data in {@link HistogramRGBData}
	 */
	private UpdateHistThreadInterface mUpdateInterface = null;
    
    /**
     * Remember that we are in a test so as to use the {@link CountDownLatch}
     */
    private boolean runningTest = false;
    
    /**
     * The signal controlling this thread's {@link AsyncTask}
     */
    private CountDownLatch mHistSignal;
    /**
     * The signal controlling the tests thread
     */
    private CountDownLatch mTestSignal;
    
    
	public HistogramPageAdapter(FragmentManager fm) {
		super(fm);
//		Log.e(TAG,"HistogramRGBData instance null ? " + (HistogramRGBData.getInstance().getFragList() == null));
		this.mHistFrags =  HistogramRGBData.getInstance().getFragList();
		this.mUpdateInterface = new UpdateHistThreadInterface(){

			@Override
			public void onUpdateDataChannels(double[][] colorArray) {
				updateHistDataChannels(colorArray);
			}

			@Override
			public void notifyPagerDataSetChanged() {
				notifyDataSetChanged();
				
			}
			
		};
		
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {    //<>http://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
		return this.mHistFrags.get(position);
	}
	@Override
	public int getItemPosition(Object object) {
	    return POSITION_NONE;//could reset mViewPager.setAdapter(mHistPageAdapter); every click to get similar updates
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.mHistFrags.size();
	}
	

	
	private void updateHistDataChannels(double[][] vals){ //<<>http://stackoverflow.com/questions/3669325/notifydatasetchanged-example
		HistogramRGBData.getInstance().updateYAxisList(vals);
	}
	
//	/**
//	 * Set the update to true if histogram is ready to receive new info
//	 * @param update
//	 */
//	public void setUpdateLatch(boolean update){
//		mUpdateHist= update;
//	}
//	
//	/**
//	 * Set the {@link #mUpdateHist} control boolean, true means update with
//	 * new data. 
//	 */
//	public synchronized boolean getUpdateLatch(){
//		return mUpdateHist;
//	}
	/**
	 * Updates the histogram with new data if the {@link UpdateHistFrag} from the
	 * previous update has completed and posted
	 * 
	 * @param data Byte array of the information to graph
	 */
	public void update(byte[] data) {
		new UpdateHistThread(mUpdateInterface).linearExecute(data);
	}
	/**
     * Control the update of the histogram between the Async and UI/surface threads
     */
    public static volatile boolean mUpdateHist = true;
	/**
	 * Set the histogram into test mode
	 */
	public void setTestRunning(boolean running){
		runningTest = running;
	}
	
	public synchronized void setHistCountDownLatch(int n){
		mHistSignal = new CountDownLatch(n);
    }
    public CountDownLatch getHistCountDownLatch(){
    	return mHistSignal;
    }
    public synchronized void incrementHistLatch(){
    	mHistSignal.countDown();
    }
    
    public void setTestCountDownLatch(int n){
		mTestSignal = new CountDownLatch(n);
    }
    public CountDownLatch getTestCountDownLatch(){
    	return mTestSignal;
    }
    public void incrementTestLatch(){
    	mTestSignal.countDown();
    }

	public boolean latchWait(){
		try {
			boolean didNotTimedOut = mHistSignal.await(10, TimeUnit.SECONDS);
			return didNotTimedOut;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	

}
