/**
 * 
 */
package com.aptina.camera.eventlisteners;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.aptina.R;
import com.aptina.camera.interfaces.CircleInterface;
import com.aptina.miscellaneous.DimensionConverter;

/**
 * @author stoyan
 *
 */
public class CircleGestureListener implements OnTouchListener{
	/**
	 * String to describe function to logcat
	 */
	private static final String TAG = "CircleGestureListener";
	/**
	 * Toggle to turn on/off logging
	 */
	private static final boolean LOG_ON = false;
	/**
	 * The distance from the initial ACTION_DOWN that we will not
	 * trigger an event, re-set in constructor to be size of inner cirle in 
	 * GestureView
	 */
	private float mDeadRadius = 40f;
	
	/**
	 * The absolute distance from the ACTION_DOWN event the
	 * finger is
	 */
	private float mCurrentRadius = -1f;

	/**
	 * Remember the initial (x,y) coordinates of the ACTION_DOWN event
	 */
	private PointF mInitCoord;
	
	/**
	 * Remember the last (x,y) coordinates 
	 */
	private PointF mPrevCoord;
	
	/**
	 * Remember what the initial angle is of the ACTION_DOWN so that
	 * we can base future angles off of it in {@link #angle(MotionEvent, PointF)}
	 */
	private double mInitAngle;
	
	/**
	 * Remember if the pointer has traveled outside of the {@link #mDeadRadius} 
	 * since action down
	 */
	private boolean mInitiated = false;

	/**
	 * Hold the context of the activity
	 */
	private Context mContext;
	
	/**
	 * Number of degrees in a circle
	 */
	private static final int CIRCLE = 360;
    /**
     * The angle ,0-360, in degrees between the motion event
     * and the initial pointer down {@link #mInitCoord}
     */
    private double mCurrentTheta = -1;
    private double mPreviousTheta = -1;
    private double mInitialTheta = -1;
    
    /**
     * Sum of all the angle differences for the 
     * motion events. Represents the total circularity of the gesture
     */
    private double mThetaSum;
	
	/**
     * CircleInterface selection callback target.
     */
    private CircleInterface mCallbackTarget = null;
	
    public void setCallback(CircleInterface callback){
    	mCallbackTarget= callback;
    }
    
    public CircleGestureListener(Context context){
    	mInitCoord = new PointF();
    	mPrevCoord = new PointF();
    	mContext = context;
    	float diameter = DimensionConverter.dipToPixels(
    			mContext, mContext.getResources().getDimension(R.dimen.gesture_view_inner_circle_diameter));
    	mDeadRadius = diameter/2;
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
	    switch (event.getActionMasked()) {
	    case MotionEvent.ACTION_DOWN:
	    	mInitCoord.x = (int)event.getX();
	    	mInitCoord.y = (int)event.getY();

	    	LOGI("down : " + mInitCoord.x + "," + mInitCoord.y);

	        break;
	    case MotionEvent.ACTION_UP:
	    	LOGI("down : " + mInitCoord.x + "," + mInitCoord.y);
	    	LOGI("up : " + event.getX() + "," + event.getY());
	    	mInitiated = false;
	    	mThetaSum = 0;
	    	break;
	    case MotionEvent.ACTION_MOVE:	    
	    	mCurrentRadius = spacingFromDownEvent(event, mInitCoord);
	    	if(mDeadRadius < mCurrentRadius || mInitiated){
	    		if(!mInitiated){
	    			mInitiated = true;
	    			LOGI("outside mDeadRadius");
	    			mPrevCoord.x = (int)event.getX();
	    			mPrevCoord.y = (int)event.getY();
	    			mPreviousTheta = angle(event, mInitCoord);
	    			mInitialTheta = mPreviousTheta;
	    		}
	    		mCurrentTheta = angle(event, mInitCoord);
	    		getAngleDiff(mCurrentTheta, mPreviousTheta);
	    		mPreviousTheta = mCurrentTheta;
	    		mPrevCoord.x = (int)event.getX();
    			mPrevCoord.y = (int)event.getY(); 
//	    		mCallbackTarget.onThetaEvent(mCurrentTheta, mInitialTheta);
    			mCallbackTarget.onThetaEvent(mThetaSum, mInitialTheta);
    		}
	        break;
	    }//End switch
	    //Indicate event was not handled
		return true;
	}

	private void getAngleDiff(double current_theta, double previous_theta) {
		if (current_theta > 270 && previous_theta < 90){
			previous_theta += 360;
		}
		else if (previous_theta > 270 && current_theta < 90){
			current_theta += 360;
		}
		double thetaDif = current_theta - previous_theta;
		mThetaSum += thetaDif;
		LOGI("thetaDif : " + thetaDif);
		LOGI("mAbsThetaSum : " + mThetaSum);
		if(mThetaSum > CIRCLE){
			mCallbackTarget.onCircleEvent(Path.Direction.CW);
			mThetaSum = 0;
			Log.e(TAG, "CIRCLE : clockwise ");
		}else if(Math.abs(mThetaSum) > CIRCLE){
			mCallbackTarget.onCircleEvent(Path.Direction.CCW);
			mThetaSum = 0;
			Log.e(TAG, "CIRCLE : counter clockwise ");
		}
	}

	/**
	 * Determine the space between to points
	 * @param event The current motion event
	 * @param initPoint The point (x,y) that we want to compare it to
	 * @return The absolute distance between the two points
	 */
    private float spacingFromDownEvent(MotionEvent event, PointF initPoint) {
       float x = event.getX() - initPoint.x;
       float y = event.getY() - initPoint.y;
       float ans = FloatMath.sqrt(x*x + y*y);

       return ans;
    }

	
    /**
     * Base all the angles from the initial angle position when the pointer
     * moves beyond the {@link mDeadRadius}
     */
    private double angle(MotionEvent event, PointF initP){
    	float Xe = event.getX();
    	float Ye = event.getY();
    	float xDif = Xe - initP.x; 
    	float yDif = Ye - initP.y; 

    	double angle = vectorAngle(xDif, yDif);

    	
  
    	LOGI("angle  = " + angle);
    	return angle;
    }

    /**
     * Get the angle of the coordinates, in radians
     */
    private double vectorAngle(float dx, float dy){
    	double angle = Math.atan2(dy,dx);
    	//Offset angle if the coordinates are in the bottom two quadrants, [PI->2PI]
    	angle = angle < 0 ? 2*Math.PI + angle : angle;
    	angle *= (180 / Math.PI);
    	return angle;

    }
    


	
	 /**
     * Log to logcat with a conditional toggle
     * @param msg string to be printed to logcat
     */
    private void LOGI(String msg){
    	if(LOG_ON){
    		Log.i(TAG, msg);
    	}
    }
}
