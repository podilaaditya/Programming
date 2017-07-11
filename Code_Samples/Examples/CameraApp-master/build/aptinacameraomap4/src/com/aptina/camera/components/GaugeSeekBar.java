/**
 * 
 */
package com.aptina.camera.components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.AbsSeekBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.aptina.R;

/**
 * @author stoyan
 *
 */
public class GaugeSeekBar extends AbsSeekBar {
	private final static String TAG = "GaugeSeekBar";
	/**
     * A callback that notifies clients when the progress level has been
     * changed. This includes changes that were initiated by the user through a
     * touch gesture or arrow key/trackball as well as changes that were initiated
     * programmatically.
     */
    public interface OnGaugeSeekBarChangeListener {
        
        /**
         * Notification that the progress level has changed. Clients can use the fromUser parameter
         * to distinguish user-initiated changes from those that occurred programmatically.
         * 
         * @param seekBar The SeekBar whose progress has changed
         * @param progress The current progress level. This will be in the range 0..max where max
         *        was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
         * @param fromUser True if the progress change was initiated by the user.
         */
        void onProgressChanged(GaugeSeekBar seekBar, int progress, boolean fromUser);
    
        /**
         * Notification that the user has started a touch gesture. Clients may want to use this
         * to disable advancing the seekbar. 
         * @param seekBar The SeekBar in which the touch gesture began
         */
        void onStartTrackingTouch(GaugeSeekBar seekBar);
        
        /**
         * Notification that the user has finished a touch gesture. Clients may want to use this
         * to re-enable advancing the seekbar. 
         * @param seekBar The SeekBar in which the touch gesture began
         */
        void onStopTrackingTouch(GaugeSeekBar seekBar);
    }
	private OnGaugeSeekBarChangeListener mOnSeekBarChangeListener;
	
	public GaugeSeekBar(Context context) {
		super(context);
		init();
	}
	public GaugeSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public GaugeSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		Resources res = getResources();
		Drawable notch = res.getDrawable(R.drawable.gauged_seekbar_notch);
		this.setBackgroundDrawable(notch);
	}
	
	/**
     * Sets a listener to receive notifications of changes to the SeekBar's progress level. Also
     * provides notifications of when the user starts and stops a touch gesture within the SeekBar.
     * 
     * @param l The seek bar notification listener
     * 
     * @see SeekBar.OnSeekBarChangeListener
     */
    public void setOnSeekBarChangeListener(OnGaugeSeekBarChangeListener l) {
        mOnSeekBarChangeListener = l;
    }
    
//    @Override
//    void onStartTrackingTouch() {
//        super.onStartTrackingTouch();
//        if (mOnSeekBarChangeListener != null) {
//            mOnSeekBarChangeListener.onStartTrackingTouch(this);
//        }
//    }
    
//    @Override
//    void onStopTrackingTouch() {
//        super.onStopTrackingTouch();
//        if (mOnSeekBarChangeListener != null) {
//            mOnSeekBarChangeListener.onStopTrackingTouch(this);
//        }
//    }

}
