/**
 * 
 */
package com.aptina.camera.fragments;



import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aptina.camera.components.GestureView;
import com.aptina.camera.interfaces.GestureInterface;

/**
 * @author stoyan
 *
 */
public class GestureFragment extends Fragment {
	private static final String TAG = "GestureFragment";
	private GestureView mGestureView;
    public interface GestureFragInterface{

    }
    
    /**
     * Empty constructor as per the Fragment documentation
     */
    public GestureFragment() {}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGestureView = new GestureView(getActivity());


    }
    
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return mGestureView;
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
     * Set the interface with the activity
     * @param listener
     */
	public void setGestureListener(GestureInterface listener) {
		mGestureView.setGestureFragInterface(listener);
	}
}
