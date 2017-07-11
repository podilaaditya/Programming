/**
 * 
 */
package com.aptina.test.functional.eventlisteners;


import java.util.LinkedHashMap;

import org.apache.commons.math3.util.Precision;

import android.app.Instrumentation;
import android.graphics.PointF;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;
import android.view.SurfaceView;

import com.aptina.camera.VideoActivity;
import com.aptina.camera.eventlisteners.VideoCompositeListener;


/**
 * @author stoyan
 *
 */
public class VideoGestureListenerTests extends ActivityInstrumentationTestCase2<VideoActivity> {
	private VideoActivity mActivity;
	private SurfaceView mPreview;
	private VideoCompositeListener mVideoGestureListener;

	/**
	 * String for logcat
	 */
	private static final String TAG = "VideoGestureListenerTests";
	
	/**
	 * A hash map that holds slide points, 2 points start and end, and the corresponding angle that
	 * they create from ACTION_DOWN to ACTION_UP
	 */
	private static final LinkedHashMap<PointF[], Double> slideGesturePointsTable =
		    new LinkedHashMap<PointF[], Double>();
	// initialize (Point[], Double) pairs
	static {
	    //Begin with the 2 axis, 0, pi/2, pi, 2pi
		slideGesturePointsTable.put(new PointF[]{new PointF(300f, 300f), new PointF(400f, 300f)}, (double) 0);//along +x
		slideGesturePointsTable.put(new PointF[]{new PointF(300f, 300f), new PointF(300f, 400f)}, Math.PI/2);//along +y
		slideGesturePointsTable.put(new PointF[]{new PointF(300f, 300f), new PointF(200f, 300f)}, Math.PI);//along -x
		slideGesturePointsTable.put(new PointF[]{new PointF(300f, 300f), new PointF(300f, 200f)}, 3*Math.PI/2);//along -y
		
		//Test PI/6 +PI/2*n, where n is from 0 to 3, inclusive
		double y = Math.sqrt(Math.pow(100,2)/Math.pow(Math.cos(Math.PI/6),2) - Math.pow(100, 2));
		slideGesturePointsTable.put(new PointF[]{new PointF(300f, 300f), new PointF(400f, (float) (300f + y))}, Math.PI/6);//along PI/6
		
		y = Math.sqrt(Math.pow(100,2)/Math.pow(Math.cos(4*Math.PI/6),2) - Math.pow(100, 2));
		slideGesturePointsTable.put(new PointF[]{new PointF(300f, 300f), new PointF(200f, (float) (300f + y))}, 4*Math.PI/6);//along 4PI/6
		
		y = Math.sqrt(Math.pow(100,2)/Math.pow(Math.cos(7*Math.PI/6),2) - Math.pow(100, 2));
		slideGesturePointsTable.put(new PointF[]{new PointF(300f, 300f), new PointF(200f, (float) (300f - y))}, 7*Math.PI/6);//along 7PI/6
//		
		y = Math.sqrt(Math.pow(100,2)/Math.pow(Math.cos(10*Math.PI/6),2) - Math.pow(100, 2));
		slideGesturePointsTable.put(new PointF[]{new PointF(300f, 300f), new PointF(400f, (float) (300f - y))}, 10*Math.PI/6);//along 10PI/6
	}
	/**
	 * @param name
	 */
	public VideoGestureListenerTests(String name) {
		super(VideoActivity.class);
		setName(name);
	}
	public VideoGestureListenerTests() {
		this("VideoGestureListenerTests");
	}
	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mPreview = (SurfaceView) mActivity.findViewById(com.aptina.R.id.preview);
		mVideoGestureListener = mActivity.getVideoGestureListener();
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * Test that our preconditions are valid
	 */
	public final void testPreconditions(){
		assertNotNull(mActivity);
		assertNotNull(mPreview);
		assertNotNull(mVideoGestureListener);
	}
	/**
	 * Test to see if the motion event calculates the correct angle of the
	 * finger movement
	 */
	public final void testMotionEventAngle(){
		PointF startPoint;
		PointF endPoint;

		int event_min_interval = 50;
		int gesture_duration = 300;


		for(PointF[] pArr : slideGesturePointsTable.keySet()){
			double expected = slideGesturePointsTable.get(pArr);
			startPoint = pArr[0];
			endPoint = pArr[1];
			generateSlideMotion(getInstrumentation(),
					SystemClock.uptimeMillis(), true, startPoint, endPoint, gesture_duration, event_min_interval);
			double actual = mVideoGestureListener.getSlideListener(0).getEventTheta();
//			LOGI( "actual before : " + actual);
			actual = Precision.round(actual, 1);//rounds to the tenth's place
//			LOGI( "actual after : " + actual);
//			LOGI( "expected before : " + expected);
			expected = Precision.round(expected, 1);//rounds to the tenth's place
//			LOGI( "expected after : " + expected);
			assertEquals(expected, actual);
		}

	}
	
	
	/**
	 * Generate a slide motion event
	 */
	private final void generateSlideMotion(Instrumentation inst,
	        	long startTime, boolean ifMove, PointF startPoint,PointF endPoint, int duration, int event_min_interval){
		 if (inst == null || startPoint == null || (ifMove && endPoint == null)) {
		        return;
		 }
		 
		 long eventTime = startTime;
		 long downTime = startTime;
		 MotionEvent event;
		 float eventX1, eventY1;
		 
		 eventX1 = startPoint.x;
		 eventY1 = startPoint.y;
		 
		// specify the property for the touch point
		PointerProperties[] properties = new PointerProperties[1];
		PointerProperties pp1 = new PointerProperties();
		pp1.id = 0;
		pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
		properties[0] = pp1;
		
		 //specify the coordinations of the touch point
	    //NOTE: you MUST set the pressure and size value, or it doesn't work
	    PointerCoords[] pointerCoords = new PointerCoords[1];
	    PointerCoords pc1 = new PointerCoords();
	    pc1.x = eventX1;
	    pc1.y = eventY1;
	    pc1.pressure = 1;
	    pc1.size = 1;
	    pointerCoords[0] = pc1;
	    
	    //////////////////////////////////////////////////////////////
	    // events sequence of zoom gesture
	    // 1. send ACTION_DOWN event of one start point
	    // 2. send ACTION_MOVE of point
	    // 3. repeat step 3 with updated middle points (x,y),
	    //      until reach the end points
	    // 4. send ACTION_UP of one end point
	    //////////////////////////////////////////////////////////////
	    
	    //Step 1
	    event = MotionEvent.obtain(downTime, eventTime, 
                MotionEvent.ACTION_DOWN, 1, properties, 
                pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );
	  
	    inst.sendPointerSync(event);
	    
	    //step 2, 3
	    if (ifMove) {
	        int moveEventNumber = 1;
	        moveEventNumber = duration / event_min_interval;

	        float stepX1, stepY1;

	        stepX1 = (endPoint.x - startPoint.x) / moveEventNumber;
	        stepY1 = (endPoint.y - startPoint.y) / moveEventNumber;


	        for (int i = 0; i < moveEventNumber; i++) {
	            // update the move events
	            eventTime += event_min_interval;
	            eventX1 += stepX1;
	            eventY1 += stepY1;


	            pc1.x = eventX1;
	            pc1.y = eventY1;


	            pointerCoords[0] = pc1;


	            event = MotionEvent.obtain(downTime, eventTime,
	                        MotionEvent.ACTION_MOVE, 1, properties, 
	                        pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);

	            inst.sendPointerSync(event);
	        }
	    }
	    
	    //Set 4
	    pc1.x = endPoint.x;
	    pc1.y = endPoint.y;
	    pointerCoords[0] = pc1;
	    
	    eventTime += event_min_interval;
	    event = MotionEvent.obtain(downTime, eventTime, 
                		MotionEvent.ACTION_UP, 1, properties, 
                		pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0 );
	    inst.sendPointerSync(event);

	}
	/**
	 * Toggle to turn on/off logging
	 */
	private static final boolean LOG_ON = false;
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
