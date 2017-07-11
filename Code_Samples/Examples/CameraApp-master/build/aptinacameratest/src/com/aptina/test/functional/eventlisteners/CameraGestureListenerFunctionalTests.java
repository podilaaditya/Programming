/**
 * 
 */
package com.aptina.test.functional.eventlisteners;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optimization.fitting.CurveFitter;
import org.apache.commons.math3.optimization.fitting.WeightedObservedPoint;
import org.apache.commons.math3.optimization.general.LevenbergMarquardtOptimizer;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;
import android.view.SurfaceView;

import com.aptina.camera.CameraActivity;
import com.aptina.camera.eventlisteners.ZoomGestureListener;

/**
 * @author stoyan
 *
 */
public class CameraGestureListenerFunctionalTests extends
		ActivityInstrumentationTestCase2<CameraActivity> {
	private CameraActivity mActivity;
	private SurfaceView mPreview;
	private ZoomGestureListener mZoomGestureListener;

	private static final String TAG = "CameraGestureTest";
	
	/**
	 * The initial distance between the two fingers on 
	 * MotionEvent.ACTION_DOWN
	 */
	float initDist = -1f;
	
	/**
	 * The new distance that the fingers are at
	 */
	float newDist = -1f;
	
	/**
	 * The previous distance that the fingers were at, equal to initDist
	 * if this is the first MotionEvent.ACTION_MOVE
	 */
	float lastDist = -1f;
	
	/**
	 * The zoom_ratio of the gesture
	 */
	float zoom_ratio = -1f;
	
	/**
	 * The current camera zoom
	 */
	private int current_zoom = -1;
	
	/**
	 * Hold the last initDist if the zoom was not returned to 0
	 */
	private float incompleteGestureDist = -1f;
	/**
	 * @param name
	 */
	public CameraGestureListenerFunctionalTests(String name) {
		super(CameraActivity.class);
		setName(name);
	}
	public CameraGestureListenerFunctionalTests() {
		this("ZoomGestureListenerFunctionalTests");
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mPreview = (SurfaceView) mActivity.findViewById(com.aptina.R.id.preview);
//		mZoomGestureListener = mActivity.getZoomGestureListener();
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testPreconditions(){
		assertNotNull(mActivity);
		assertNotNull(mPreview);
		assertNotNull(mZoomGestureListener);
	}
	/**
	 * Test method for {@link com.aptina.camera.eventlisteners.ZoomGestureListener#setCallbackTarget(com.aptina.camera.eventlisteners.ZoomGestureListener.ZoomInterface)}.
	 */
	public final void testSetCallbackTarget() {
		assertNotNull(mZoomGestureListener.getCallBackTarget());
	}
	/**
	 * Test method for {@link com.aptina.camera.eventlisteners.ZoomGestureListener#ZoomGestureListener(android.view.SurfaceView, android.content.Context)}.
	 */
	
//	public final void testZoomGestureListener() {
//		assertEquals(ZoomGestureListener.TOUCH_NONE,mZoomGestureListener.getTouchMode());
//		Point startPoint1 = new Point(50, 0);
//		Point startPoint2 = new Point(100, 0); 
//		
//		
//		Point endPoint1 = new Point(50, 0);
//		Point endPoint2 = new Point(100 * mActivity.getMaxZoomDividor(),0);
//		/** Difference of 50 between start and stop,
//		 *  (gesture duration)/(event_min_interval) = 1000/100 = 10 intervals to go 50
//		 *  50/10 = 5 points to go each interval which gives scale of 1.2 in finger gesture right now
//		 * */ 
//		int max_zoom = mActivity.getMaxZoom();
//		int max_zoom_dividor = mActivity.getMaxZoomDividor();
//		zoom_ratio = max_zoom/max_zoom_dividor;
//		
//		assertEquals(zoom_ratio, mActivity.getZoomRatio());
//		int event_min_interval = 100;
//		int gesture_duration = 1000;
//		
//		//Zoom all the way in
//		generateZoomGesture(getInstrumentation(),
//				SystemClock.uptimeMillis(), true, startPoint1,
//				startPoint2, endPoint1,
//				endPoint2, gesture_duration, event_min_interval);
//		LOGI("camera zoom at :" + mZoomGestureListener.getZoomLevel());
//		assertEquals(mActivity.getCamera().getParameters().getZoom(), mActivity.getCamera().getParameters().getMaxZoom());
//		
//		
//
//		//Zoom all the way out
//		generateZoomGesture(getInstrumentation(),
//				SystemClock.uptimeMillis(), true, endPoint1,
//				endPoint2, startPoint1, startPoint2,  
//				gesture_duration, event_min_interval);
//		LOGI("camera zoom at :" + mZoomGestureListener.getZoomLevel());
//		assertEquals(mActivity.getCamera().getParameters().getZoom(), 0);
//	}


	/**
	 * Test the U shaped smile finger gesture
	 */
	public final void testSmileGesture(){
		int event_min_interval = 50;
		int gesture_duration = 3000;
		float startX = 380f;
		float stopX = 691.10f;
	    double[] coef = {-1970.0006984931495, 9.087474981352061, -0.008413687221159538};//0+0x-x^2
		PolynomialFunction func = new PolynomialFunction(coef);
		generateSmileGesture(getInstrumentation(),
				SystemClock.uptimeMillis(), startX, stopX, func, gesture_duration, event_min_interval);
		
		solveLinearSystem();
		
		UnivariateFunction rootFindingFunction = mFunc();
		RombergIntegrator integrator = new RombergIntegrator();
		double val = integrator.integrate(10, rootFindingFunction, 0, 5);
		Log.i(TAG, "3x^2; 0->5 == " +val);

	}
	public UnivariateFunction mFunc() {
	    return new UnivariateFunction() {
	        public double value(double x) {
	            return 3*x*x;
	        }
	    };
	}
    private void solveLinearSystem() {
    	RealMatrix coefficients = new Array2DRowRealMatrix(new double[][] { { 2, 3, -2 }, 
    			{ -1, 7, 6 }, { 4, -3, -5 } },false);
    	DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
    	RealVector constants = new ArrayRealVector(new double[] { 1, -2, 1 }, false);
    	RealVector solution = solver.solve(constants);
    	int m = solution.getDimension();
    	Log.i(TAG, "solution.getDimension() : " + m);
    	for(int i = 0; i < m; i++){
    		Log.i(TAG, "getEntry " + i + " : " + solution.getEntry(i));
    	}


		
	}
	/**
     * Event sequence of a smile gesture. Creates a parabola representing a smilly face:
     * 1. send ACTION_DOWN event at startX point, calculate the startY point from the function
     * 2. calculate the points from the polynomial function
     * 3. send ACTION_MOVE of polynomial points
     * 4. send ACTION_UP of one end point
     * 
     * @param inst  Instance of the test instrumentation
     * @param startTime The current milliseconds time, SystemClock.uptimeMillis()
     * @param startX The starting position of the x variable in the function
     * @param stopX  The stopping position of the x variable in the function
     * @param function A PolynomialFunction of degree == 2
     * @param duration The time that the gesture completes the motion
     * @param event_min_interval The time interval between consecutive points, the larger duration/event_min_interval the 
     * finer the consecutive points will be
     */
	private final void generateSmileGesture(Instrumentation inst,
			long startTime, float startX, float stopX, PolynomialFunction function, int duration, int event_min_interval){
		if (inst == null || startX == stopX || function == null) {
			 return;
	     }
		 long eventTime = startTime;
		 long downTime = startTime;
		 MotionEvent event;
		 float eventX1, eventY1;
		 
		 eventX1 = startX;
		 eventY1 = (float)(function.value(startX));
		 Log.i(TAG, "Action Down : " + eventX1 + "x" + eventY1);
		 
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
	    

	    
	    //Step 1
	    event = MotionEvent.obtain(downTime, eventTime, 
               MotionEvent.ACTION_DOWN, 1, properties, 
               pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );
	  
	    inst.sendPointerSync(event);
	    
	    //step 2, 3
//	    double[] coef = {0,0,1};//0+0x+x^2
//		PolynomialFunction func = new PolynomialFunction(coef);
	    WeightedObservedPoint lastPoint = null;

	    int moveEventNumber = 1;
       moveEventNumber = duration / event_min_interval;

       CurveFitter curve = getCurvePoints(startX, stopX, moveEventNumber,function);
      
       for(WeightedObservedPoint point :  curve.getObservations()){
       	// update the move events
           eventTime += event_min_interval;
           pc1.x = (float) point.getX();
           pc1.y = (float) point.getY();
           
           pointerCoords[0] = pc1;

           Log.i(TAG, "Action Move : " + pc1.x + "x" + pc1.y);
           event = MotionEvent.obtain(downTime, eventTime,
                       MotionEvent.ACTION_MOVE, 1, properties, 
                       pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);

           inst.sendPointerSync(event);
           lastPoint = point;
       }
	
		//Set 4
       if(lastPoint != null){
    	   pc1.x = (float) lastPoint.getX();
           pc1.y = (float) lastPoint.getY();
      	    pointerCoords[0] = pc1;
      	    
      	    eventTime += event_min_interval;
      	    Log.i(TAG, "Action UP : " + pc1.x + "x" + pc1.y);
      	    event = MotionEvent.obtain(downTime, eventTime, 
                      		MotionEvent.ACTION_UP, 1, properties, 
                      		pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0 );
      	    inst.sendPointerSync(event);
       }
	}
	
	private CurveFitter getCurvePoints(float startX, float stopX, int numOfPoints, PolynomialFunction function){
		CurveFitter curve = new CurveFitter(new LevenbergMarquardtOptimizer());
		float xDelta = (stopX - startX)/(float)numOfPoints;
		Log.i(TAG, "xDelta : " + xDelta);
		Log.i(TAG, "(stopX - startX) : " + (stopX - startX));
		Log.i(TAG, "numOfPoints : " + numOfPoints);
		for(int i = 0; i < numOfPoints; i++){
			float xVal = startX + i*xDelta;
			curve.addObservedPoint(xVal, function.value(xVal));
		}
		
		return curve;
	}
	//http://commons.apache.org/math/userguide/optimization.html
		//At the bottom Section 12.6
//	    private void checkLibApacheMath3(){
//	    	final CurveFitter fitter = new CurveFitter(new LevenbergMarquardtOptimizer());//<><><><><><><><><><><><><><>
//
//	    	fitter.addObservedPoint(-1.00, 2.021170021833143);
//	    	fitter.addObservedPoint(-0.99, 2.221135431136975);
//	    	fitter.addObservedPoint(-0.98, 2.09985277659314);
//	    	fitter.addObservedPoint(-0.97, 2.0211192647627025);
//	    	// ... Lots of lines omitted ...
//	    	fitter.addObservedPoint( 0.99, -2.4345814727089854);
//
//	    	// The degree of the polynomial is deduced from the length of the array containing
//	    	// the initial guess for the coefficients of the polynomial.
//	    	final double[] init = { 12.9, -3.4, 2.1 }; // 12.9 - 3.4 x + 2.1 x^2
//
//	    	// Compute optimal coefficients.
//	    	final double[] best = fitter.fit(new PolynomialFunction.Parametric(), init);
//
//	    	// Construct the polynomial that best fits the data.
//	    	final PolynomialFunction fitted = new PolynomialFunction(best);
//	    	Log.i(TAG, "degree : " + fitted.degree());
//	    	
//	    	//////////////////////////////////////////////////////////////////////////////////////////////////
//	    	//////////////////////////////////////////////////////////////////////////////////////////////////
//	    	final CurveFitter fitter1 = new CurveFitter(new LevenbergMarquardtOptimizer());
//	    	fitter1.clearObservations();
//	    	fitter1.addObservedPoint(-3.00, -26);
//	    	fitter1.addObservedPoint(-2.00, -7);
//	    	fitter1.addObservedPoint(-1.00, 0);
//	    	fitter1.addObservedPoint(0.00, 1);
//	    	fitter1.addObservedPoint(1.00, 2);
//	    	fitter1.addObservedPoint(2.00, 9);
//	    	fitter1.addObservedPoint(3.00, 28);
//	    	final double[] init1 = { 12.9, -3.4, 2.1 ,1,1}; // 12.9 - 3.4 x + 2.1 x^2 + 1.1 x^3
//
//	    	// Compute optimal coefficients.
//	    	final double[] best1 = fitter1.fit(new PolynomialFunction.Parametric(), init1);
//
//	    	Log.i(TAG, "" + best1[0] + ", " + best1[1] + "x, " + best1[2] + "x^2, " + best1[3] + "x^3");
//
//	    	// Construct the polynomial that best fits the data.
//	    	final PolynomialFunction fitted1 = new PolynomialFunction(best1);
//	    	Log.i(TAG, "degree : " + fitted1.degree());
//
//
//	    }

	private float getSpacing(MotionEvent.PointerCoords p1, MotionEvent.PointerCoords p2){
		  float x = p1.x - p2.x;
	      float y = p1.y - p2.y;
	      return FloatMath.sqrt(x * x + y * y);
	}
//	private final void generateZoomGesture(Instrumentation inst,
//	        long startTime, boolean ifMove, Point startPoint1,
//	        Point startPoint2, Point endPoint1,
//	        Point endPoint2, int duration, int event_min_interval) {
//
//	    if (inst == null || startPoint1 == null
//	            || (ifMove && endPoint1 == null)) {
//	        return;
//	    }
//
//	    long eventTime = startTime;
//	    long downTime = startTime;
//	    MotionEvent event;
//	    float eventX1, eventY1, eventX2, eventY2;
//
//	    eventX1 = startPoint1.x;
//	    eventY1 = startPoint1.y;
//	    eventX2 = startPoint2.x;
//	    eventY2 = startPoint2.y;
//
//	    // specify the property for the two touch points
//	    PointerProperties[] properties = new PointerProperties[2];
//	    PointerProperties pp1 = new PointerProperties();
//	    pp1.id = 0;
//	    pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
//	    PointerProperties pp2 = new PointerProperties();
//	    pp2.id = 1;
//	    pp2.toolType = MotionEvent.TOOL_TYPE_FINGER;
//
//	    properties[0] = pp1;
//	    properties[1] = pp2;
//
//	    //specify the coordinations of the two touch points
//	    //NOTE: you MUST set the pressure and size value, or it doesn't work
//	    PointerCoords[] pointerCoords = new PointerCoords[2];
//	    PointerCoords pc1 = new PointerCoords();
//	    pc1.x = eventX1;
//	    pc1.y = eventY1;
//	    pc1.pressure = 1;
//	    pc1.size = 1;
//	    PointerCoords pc2 = new PointerCoords();
//	    pc2.x = eventX2;
//	    pc2.y = eventY2;
//	    pc2.pressure = 1;
//	    pc2.size = 1;
//	    pointerCoords[0] = pc1;
//	    pointerCoords[1] = pc2;
//
//	    //////////////////////////////////////////////////////////////
//	    // events sequence of zoom gesture
//	    // 1. send ACTION_DOWN event of one start point
//	    // 2. send ACTION_POINTER_2_DOWN of two start points
//	    // 3. send ACTION_MOVE of two middle points
//	    // 4. repeat step 3 with updated middle points (x,y),
//	    //      until reach the end points
//	    // 5. send ACTION_POINTER_2_UP of two end points
//	    // 6. send ACTION_UP of one end point
//	    //////////////////////////////////////////////////////////////
//	    current_zoom = mZoomGestureListener.getCameraParams().getZoom();
//	    // step 1
//	    event = MotionEvent.obtain(downTime, eventTime, 
//	                MotionEvent.ACTION_DOWN, 1, properties, 
//	                pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );
//	    assertEquals(ZoomGestureListener.TOUCH_NONE,mZoomGestureListener.getTouchMode());
//	    inst.sendPointerSync(event);
//	    
//	    //step 2
//	    event = MotionEvent.obtain(downTime, eventTime, 
//	                MotionEvent.ACTION_POINTER_2_DOWN, 2, 
//	                properties, pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
//	    assertEquals(ZoomGestureListener.TOUCH_NONE,mZoomGestureListener.getTouchMode());
//	    inst.sendPointerSync(event);
//	    //check incompleteGestrureDist
//	    initDist = incompleteGestureDist > 0 ? incompleteGestureDist : getSpacing(pointerCoords[0], pointerCoords[1]);
//	    assertEquals(initDist, mZoomGestureListener.getInitDist());
//	    lastDist = getSpacing(pointerCoords[0], pointerCoords[1]);
//	    
//	    //step 3, 4
//	    if (ifMove) {
//	        int moveEventNumber = 1;
//	        moveEventNumber = duration / event_min_interval;
//
//	        float stepX1, stepY1, stepX2, stepY2;
//
//	        stepX1 = (endPoint1.x - startPoint1.x) / moveEventNumber;
//	        stepY1 = (endPoint1.y - startPoint1.y) / moveEventNumber;
//	        stepX2 = (endPoint2.x - startPoint2.x) / moveEventNumber;
//	        stepY2 = (endPoint2.y - startPoint2.y) / moveEventNumber;
//
//	        for (int i = 0; i < moveEventNumber; i++) {
//	            // update the move events
//	            eventTime += event_min_interval;
//	            eventX1 += stepX1;
//	            eventY1 += stepY1;
//	            eventX2 += stepX2;
//	            eventY2 += stepY2;
//
//	            pc1.x = eventX1;
//	            pc1.y = eventY1;
//	            pc2.x = eventX2;
//	            pc2.y = eventY2;
//
//	            pointerCoords[0] = pc1;
//	            pointerCoords[1] = pc2;
//
//	            event = MotionEvent.obtain(downTime, eventTime,
//	                        MotionEvent.ACTION_MOVE, 2, properties, 
//	                        pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
//	            assertEquals(ZoomGestureListener.TOUCH_ZOOM_ON,mZoomGestureListener.getTouchMode());
//	            inst.sendPointerSync(event);
//	            
//	            /**
//	             * test that the scale and zoom is correct for every movement
//	             */
//	            newDist = getSpacing(pointerCoords[0], pointerCoords[1]);
//	            float zoom_scale = 1 +  (newDist - lastDist)/ initDist;
//	            assertEquals(zoom_scale, mZoomGestureListener.getZoomScale());
//	            lastDist = newDist;
//	            LOGI("zoom_scale : " + zoom_scale);
//	            LOGI("zoom_ratio : " + zoom_ratio);
//	            
//	            int scale  =  current_zoom;
//	            scale += (zoom_scale - 1) * zoom_ratio;
//	            if(scale > mZoomGestureListener.getMaxZoom()){
//	            	scale = mZoomGestureListener.getMaxZoom();
//	    		}else if(scale < 0){
//	    			scale = 0;
//	    		}
//	            assertEquals(scale,  mZoomGestureListener.getZoomLevel());
//	            current_zoom = mZoomGestureListener.getCameraParams().getZoom();
//	        }
//	    }
//
//	    //step 5
//	    pc1.x = endPoint1.x;
//	    pc1.y = endPoint1.y;
//	    pc2.x = endPoint2.x;
//	    pc2.y = endPoint2.y;
//	    pointerCoords[0] = pc1;
//	    pointerCoords[1] = pc2;
//
//	    eventTime += event_min_interval;
//	    event = MotionEvent.obtain(downTime, eventTime,
//	                MotionEvent.ACTION_POINTER_2_UP, 2, properties, 
//	                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
//	    inst.sendPointerSync(event);
//	    incompleteGestureDist = mZoomGestureListener.getCameraParams().getZoom() > 0 ? 
//    			initDist : -1f;
//	    assertEquals(ZoomGestureListener.TOUCH_ZOOM_OFF,mZoomGestureListener.getTouchMode());
//	    // step 6
//	    eventTime += event_min_interval;
//	    event = MotionEvent.obtain(downTime, eventTime, 
//	                MotionEvent.ACTION_UP, 1, properties, 
//	                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0 );
//	    inst.sendPointerSync(event);
//	}
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
