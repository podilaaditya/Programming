/**
 * 
 */
package com.aptina.camera.fragments;



import java.io.File;
import java.util.Date;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aptina.camera.CameraInfo;
import com.aptina.camera.GridGalleryActivity;
import com.aptina.camera.ImageDetailActivity;
import com.aptina.camera.components.BurstFolder;
import com.aptina.camera.components.BurstScroll;
import com.aptina.camera.components.BurstScroll.BurstScrollViewInterface;
import com.aptina.miscellaneous.DateTimeUtils;


/**
 * @author stoyan
 *
 */
public class BurstScrollFragment extends Fragment {
	private static final String TAG = "BurstScrollFragment";
	public static final String BURST_SCROLL_FRAGMENT_TAG = "Burst_Scroll_Fragment_TAG";
	/**
	 * Custom Burst ScrollView, contains horizontall scroll view
	 */
	private BurstScroll mBurstScrollView;
    /**
     * Variable to tell us when we have finished the burst
     */
    private boolean burstComplete = false;
    
    private BurstScrollFragInterface callbackListener;
    
    /**
     * Burst folder that we use to store all the bursts, for each n-burst
     */
    private BurstFolder mBurstFolder;
    
    /**
	 * If the user already set the number
     */
    private int mBurstsToTakeSetting = -1;
    public interface BurstScrollFragInterface{

    }
    
    public void setCallbackListener(BurstScrollFragInterface listener){
    	callbackListener = listener;
    }
    
    /**
     * Empty constructor as per the Fragment documentation
     */
    public BurstScrollFragment() {}

    
    /**
     * Number of bursts to take constructor. Set the {@link BurstScrollView} to this
     * number in onCreateView
     */
    public BurstScrollFragment(int burst_to_take) {
    	mBurstsToTakeSetting = burst_to_take;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBurstScrollView = new BurstScroll(getActivity());
        mBurstScrollView.setCallbackListener(new BurstScrollViewInterface(){

			@Override
			public void setComplete(boolean complete) {
				Log.e(TAG, "Burst Sequence is complete");
				burstComplete = complete;
				
			}

			@Override
			public void OnBurstViewClicked(int index) {
				final Intent intent = new Intent(getActivity(), GridGalleryActivity.class);
				intent.putExtra(ImageDetailActivity.EXTRA_IMAGE_ID, (int) index);
				intent.putExtra(ImageDetailActivity.EXTRA_IMAGE_FOLDER, getBurstFolder());
		        startActivity(intent);
				
			}
        	
        });

    }
    
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return mBurstScrollView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * Retrieves name for burst folder (without extension)
     * 
     * @return folder name
     */
    private String getBurstFolderName(){
    	Log.i(TAG, "Burst folder root path : " + CameraInfo.CAMERA_FOLDER_ROOT_PATH);
    	return CameraInfo.CAMERA_FOLDER_ROOT_PATH + CameraInfo.BURST_FOLDER_PREFIX + DateTimeUtils.getBurstFolderFormatDate(new Date());
    }
    
    private BurstFolder createBurstFolder(){
    	Log.i(TAG, "::::::: Create burst folder :::::::");
    	String path = getBurstFolderName();
    	int num = 1;
        while ((new File(path)).exists()) {
        	path = getBurstFolderName() + "-" + num;
            num++;
        }
        File f = new File(path);
    	f.mkdir();
    	Log.i(TAG, "burst folder file : " + f.getAbsolutePath());
    	mBurstFolder = new BurstFolder(f);
    	return mBurstFolder;
    }
    public BurstFolder getBurstFolder(){
    	return (mBurstFolder != null) ? mBurstFolder : createBurstFolder();
    }
    


    /**
     * Check if the burst sequence has completed
     */
    public boolean hasCompleted(){
    	return burstComplete;
    }
    
    
    public void newBurstSeed(){
    	mBurstScrollView.createSeed(); 
    }
    public boolean continueFakeBurst(){
    	return mBurstScrollView.continueFakeBurst();
    }
    /**
     * Create the burst folder if it doesn't exist
     */
	public void createFolder() {
		if(mBurstScrollView.createBurstFolder()){
			createBurstFolder();
		}
	}

	/**
	 * Create a new blank image view for the burst thumbnail
	 * @param index
	 */
	public void createNewBlank(int index) {
		mBurstScrollView.createBlank(index);
		
	}

	/**
	 * Add a bitmap to a blank view in the burst scroll
	 * @param index
	 * @param bitmap
	 */
	public void addBurst(int index, Bitmap bitmap) {
		mBurstScrollView.addThumbBitmap(index, bitmap);
		
	}

	/**
	 * Return the number that is set on the {@link BurstScrollView} by the user
	 */
	public int getBurstNumSet() {
		
		return mBurstScrollView.getSelectedBurstNum();
	}
        
}
