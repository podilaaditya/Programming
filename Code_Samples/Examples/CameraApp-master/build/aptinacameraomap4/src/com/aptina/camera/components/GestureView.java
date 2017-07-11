/**
 * 
 */
package com.aptina.camera.components;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.aptina.R;
import com.aptina.camera.CameraInfo;
import com.aptina.camera.eventlisteners.CameraCompositeListener;
import com.aptina.camera.interfaces.GestureInterface;
import com.aptina.miscellaneous.DimensionConverter;

/**
 * @author stoyan
 *
 */
public class GestureView extends View{
	private static final String TAG = "GestureView";
	
	/**
	 * Toggle to turn on/off logging for {@link #LOGI(String)}
	 */
	private static final boolean LOG_ON = true;
	
	/**
	 * Get the current smile setting on the camera, use it to define
	 * the path direction of {@link #mInnerCircleShape} and {@link #mOuterCircleShape}
	 */
	private Path.Direction mInnerCircleDirection = Path.Direction.CCW;
	private Path.Direction mPointerCircleDirection = Path.Direction.CCW;
	private Path.Direction mOuterCircleDirection = Path.Direction.CW;
    
    /**
     * The paint of the inner circle that expands first when
     * the user first touches the view
     */
    private Paint mInnerCirclePaint;
    
    /**
     * The paint of the outer circle that expands after the animation
     * {@link #mInnerCircleAppearAnim} finishes
     */
    private Paint mOuterCirclePaint;
    
    /**
     * The paint of the circle, {@link #mOuterCircleShape} XOR {@link #mInnerCircleShape},
     * that becomes disabled when either Face or Smile detection has been turned on
     */
    private Paint mDisabledCirclePaint;
    
    /**
     * The paint of the sweeping arc that responds to the angle
     * theta from the circle gesture
     */
    private Paint mSweepArcPaint;
    
    /**
     * The inner circle shape
     */
    private AnimShapeDrawable mInnerCircleShape;
    
    /**
     * The circle shape for the pointer touch event
     */
    private AnimShapeDrawable mPointerCircleShape;
    /**
     * The outer circle shape
     */
    private AnimShapeDrawable mOuterCircleShape;
    
    /**
     * The rectangle within which the {@link #mInnerCircleShape} will be drawn
     */
    private Rect mInnerCircleFinalBounds;
    
    /**
     * The rectangle within which the {@link #mPointerCircleShape} will be drawn
     */
    private Rect mPointerCircleFinalBounds;
    
    /**
     * The rectangle within which the {@link #mOuterCircleShape} will be drawn
     */
    private Rect mOuterCircleFinalBounds;
    /**
     * The diameter of the inner circle
     */
    private float mInnerCicleDiameter;
    
    /**
     * The diameter of the ourter circle
     */
    private float mOuterCicleDiameter;
    
    /**
     * The current angle theta of the user finger, used to draw
     * the sweep arc that uses the RectF from the {@link #mOuterCircleShape}
     */
    private double mSweepCircleArcTheta;
    
    /**
     * The start angle of the sweep arc
     */
    private double mSweepStartTheta;
    /**
     * Indicate if the user is in the process of generating a circle gesture
     */
    private boolean mThetaSweeping = false;
    
    /**
     * Animation of the inner circle expanding from 0 to final diameter
     * {@link #mInnerCicleDiameter} after touch down
     */
    private AnimatorSet mInnerCircleAppearAnim = null;
    
    /**
     * Animation of the pointer circle expanding from 0 to final diameter
     * {@link #mInnerCicleDiameter} after touch down
     */
    private AnimatorSet mPointerCircleAppearAnim = null;
    /**
     * Animation of the outer circle expanding from {@link #mInnerCicleDiameter} to final 
     * diameter {@link #mOuterCicleDiameter} after {@link #mInnerCircleAppearAnim} finishes
     * as long as {@link #mTouching} is still true
     */
    private AnimatorSet mOuterCircleAppearAnim = null;
    
    /**
     * Animation of the {@link #mInnerCircleShape} shrinking from {@link #mInnerCicleDiameter}
     * to 0, once the {@link #mOuterCircleShape} has shrinked from {@link #mOuterCicleDiameter}
     * to {@link #mInnerCicleDiameter}
     */
    private AnimatorSet mInnerCircleDisappearAnim = null;
    
    /**
     * Animation of the {@link #mPointerCircleShape} shrinking from {@link #mInnerCicleDiameter}
     * to 0, once the {@link #mOuterCircleShape} has shrinked from {@link #mOuterCicleDiameter}
     * to {@link #mInnerCicleDiameter}
     */
    private AnimatorSet mPointerCircleDisappearAnim = null;
    /**
     * Animation of the {@link #mOuterCircleShape} shrinking from {@link #mOuterCicleDiameter}
     * to {@link #mInnerCicleDiameter}
     */
    private AnimatorSet mOuterCircleDisappearAnim = null;
    
    /**
     * Listener that registers touch events of the view
     */
    private CameraCompositeListener mGestureViewListener; 
    
    /**
     * Listener that passes unused touch events up to the fragment, which then passes them 
     * to the camera activity
     */
    private GestureInterface mGestureFragListener;
    
    /**
     * Remember the width and height of the view
     */
    private int mViewWidth;
    private int mViewHeight;
    
    /**
     * Remember whether the finger is touching the screen
     */
    private boolean mTouching = false;
    
    /**
     * Remember whether there is more than one finger touching the screen
     */
    private boolean mPointerDown = false;
    
    /**
     * Remember the id of the pointer down so that we can get its position later
     */
    private int mPointer1Id = -1;
    
    /**
     * Draw the outer circle only when the {@link #mInnerCircleAppearAnim} finishes
     */
    private boolean mDrawOuterCircle = false;
    
    /**
     * Draw the pointer circle when there is a second finger on the screen
     */
    private boolean mDrawPointerCircle = false;
    
	/**
     * Constructor with context.
     * 
     * @param context Application context.
     */
    public GestureView(Context context) {
        super(context);
        init();
    }

    /**
     * Constructor with context and attribute set.
     * 
     * @param context Application context.
     * @param attrs Attribute set.
     */
    public GestureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructor with context. attribute set and default style.
     * 
     * @param context Application context.
     * @param attrs Attribute set.
     * @param defStyle Style resource.
     */
    public GestureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){

    	mGestureViewListener = new CameraCompositeListener(getContext());
    	mGestureViewListener.setCallback(mGestureViewInterface);
    	this.setOnTouchListener(mGestureViewListener);
    	
    	initPaints();
    	
    	//Initiate the shapes in the view center initially, so that animations can set targets
    	initBounds(mViewWidth, mViewHeight);
    	
    	initAnim();
    }
    public void setGestureFragInterface(GestureInterface callback){
    	mGestureFragListener = callback;
    }
    
    private GestureInterface mGestureViewInterface = new GestureInterface(){
    	/**
    	 * Empty object that is used to prevent synchronization problems
    	 * between gesture events, {@link #OnDownTouch(MotionEvent)} {@link #OnThetaEvent(double, double)}
    	 * {@link #OnCircleEvent(android.graphics.Path.Direction)}
    	 */
//    	private Object syncObj = new Object();
		@Override
		public void OnSlideGesture(int slide_dir) {
			mGestureFragListener.OnSlideGesture(slide_dir);
		}

		@Override
		public void OnDoubleTapGesture(MotionEvent event) {
			LOGI("OnDoubleTapGesture");
			mGestureFragListener.OnDoubleTapGesture(event);
		}

		@Override
		public void OnSmileGesture() {
			LOGI("Smile Gesture Detected, but Gesture Disabled");
//            mGestureFragListener.OnSmileGesture();
		}

		@Override
		public void OnFrownDetected() {
			LOGI("Frown Gesture Detected, but Gesture Disabled");
//            mGestureFragListener.OnFrownDetected();
		}

		@Override
		public void OnZoomGesture(float zoom_scale) {
			mGestureFragListener.OnZoomGesture(zoom_scale);
		}

		@Override
		public Camera getCamera() {
			return mGestureFragListener.getCamera();
		}

		@Override
		public void OnSingleTapGesture(MotionEvent event) {
			LOGI("OnSingleTapGesture");
		}

		@Override
		public void OnDownTouch(MotionEvent event) {
			LOGI("OnDownTouch");
			//adb shell monkey -p com.aptina -s 99 -v 15000
			Camera cam = mGestureFragListener.getCamera();
			if(cam != null){
				Camera.Parameters params = cam.getParameters();
				initBounds((int)event.getX(),(int)event.getY());
				initShapes(params);

				initAnim();
				mTouching = true;
				mInnerCircleAppearAnim.start(); 
				setAnimationsCreated(true);
			}else{
				Log.w(TAG, "Camera returned is null, skipping view animation");
				setAnimationsCreated(false);
			}	
		}


		@Override
		public void OnPointerDown(MotionEvent event) {
			LOGI("OnPointerDown");
			
			final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
	                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	        LOGI("OnPointerDown index : " + pointerIndex);
	        if(pointerIndex > 1){
	        	Log.i(TAG, "only using pointers 0 and 1");
	        	return;
	        }else{
	        	mPointer1Id = event.getPointerId(pointerIndex);
	 			mPointerCircleFinalBounds = getBoundsRect(mInnerCicleDiameter, 
	 					(int)event.getX(pointerIndex), (int)event.getY(pointerIndex));
	 	    	mPointerCircleShape.setDefaultBounds(mPointerCircleFinalBounds);
	 	    	
	 	    	getOuterCircleDisappearAnim().start();
	 	    	
	 	    	mPointerCircleAppearAnim.start();
	 			mDrawPointerCircle = true;
	 			mPointerDown = true;
	        }
			
		}
		@Override
		public void OnUpTouch(MotionEvent event) {
			LOGI("OnUpTouch");
			if(getAnimationsCreated()){
				getInnerCircleDisappearAnim().start();
				getOuterCircleDisappearAnim().start();
				
				mThetaSweeping = false;
			}	
		}
		
		@Override
		public void OnPointerUp(MotionEvent event) {
			LOGI("OnPointerUp");
			getPointerCircleDisappearAnim().start();
			mPointerDown = false;
		}
		@Override
		public void OnMoveTouch(MotionEvent event) {
			LOGI("OnMoveTouch");
			int x;
			int y;
			//update the centers of the two fingers
			if(mPointerDown && mTouching){
				final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
		                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		        int c = event.getPointerCount();
		        for(int i = 0; i < c; i++){
		        	x = (int) event.getX(i);
		    		y = (int) event.getY(i);    		
		    		int id = event.getPointerId(i);
		    		int action = event.getActionMasked();
		    		int actionIndex = event.getActionIndex();
		    		Log.w(TAG,"x,y :  " + x + ", " + y + ", actionIndex : " + actionIndex);
		    		final Rect bounds = getBoundsRect(mInnerCicleDiameter, 
							x, y);
					if(i == 0){
						mInnerCircleShape.setDefaultBounds(bounds);
					}else if(i == 1){
						mPointerCircleShape.setDefaultBounds(bounds);
					}
					postInvalidate();//<<<<<<<<? whys it not updating smoothly, sync lagging?>
		        }
//		        LOGI("OnMoveTouch index : " + pointerIndex);
//		        if(pointerIndex > 1){
//		        	Log.i(TAG, "only using pointers 0 and 1");
//		        	return;
//		        }
//				final Rect bounds = getBoundsRect(mInnerCicleDiameter, 
//						(int)event.getX(pointerIndex), (int)event.getY(pointerIndex));
//				if(pointerIndex == 0){
//					mInnerCircleShape.setDefaultBounds(bounds);
//				}else if(pointerIndex == 1){
//					mPointerCircleShape.setDefaultBounds(bounds);
//				}
		    	
			}
		}
		@Override
		public void OnThetaEvent(double current_theta, double start_theta) {
			synchronized(this){
				if(!mThetaSweeping){
					mThetaSweeping = true;
				}
				mSweepCircleArcTheta = current_theta;
				mSweepStartTheta = start_theta;
			}

		}

		@Override
		public void OnCircleEvent(Path.Direction rotation) {
			synchronized(this){
				mGestureFragListener.OnCircleEvent(rotation);
			}

		}





	};
    private void initPaints(){
    	 mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    	 mInnerCirclePaint.setAntiAlias(true);
    	 mInnerCirclePaint.setDither(true);
    	 mInnerCirclePaint.setColor(this.getResources().getColor(R.color.gesture_oval_bright_green));
    	 mInnerCirclePaint.setAlpha(180);
    	 mInnerCirclePaint.setStyle(Paint.Style.STROKE);
    	 mInnerCirclePaint.setStrokeJoin(Paint.Join.ROUND);
    	 mInnerCirclePaint.setStrokeCap(Paint.Cap.ROUND);
    	 mInnerCirclePaint.setStrokeWidth(3);
    	 
    	 mOuterCirclePaint = mInnerCirclePaint;
    	 
    	 
    	 mDisabledCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    	 mDisabledCirclePaint.setAntiAlias(true);
    	 mDisabledCirclePaint.setDither(true);
    	 mDisabledCirclePaint.setColor(this.getResources().getColor(R.color.gesture_oval_disabled_gray));
    	 mDisabledCirclePaint.setStyle(Paint.Style.STROKE);
    	 mDisabledCirclePaint.setStrokeJoin(Paint.Join.ROUND);
    	 mDisabledCirclePaint.setStrokeCap(Paint.Cap.ROUND);
    	 mDisabledCirclePaint.setStrokeWidth(3);   	 
    	 
    	 
    	 mSweepArcPaint = new Paint();
    	 mSweepArcPaint.setAntiAlias(true);
    	 mSweepArcPaint.setStyle(Paint.Style.FILL);
    	 mSweepArcPaint.setStrokeWidth(4);
    	 mSweepArcPaint.setColor(0x880000FF);

    	 mInnerCicleDiameter = DimensionConverter.dipToPixels(
    			 getContext(), getContext().getResources().getDimension(R.dimen.gesture_view_inner_circle_diameter));
    	 mOuterCicleDiameter = DimensionConverter.dipToPixels(
    			 getContext(), getContext().getResources().getDimension(R.dimen.gesture_view_outer_circle_diameter));

    }
    /**
     * Instantiate all of the global animation references
     */
    private void initAnim(){
    	mInnerCircleAppearAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.anim.gesture_circle_inflate);
    	mInnerCircleAppearAnim.setTarget(mInnerCircleShape);
    	mInnerCircleAppearAnim.addListener(new AnimatorListener(){

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				Log.i(TAG ,"inner animation ended");
				mDrawOuterCircle = true;
				mOuterCircleAppearAnim.start();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
				LOGI("inner animation started");
				mDrawOuterCircle = false;
				postInvalidate();
			}

        	
        });
    	mPointerCircleAppearAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.anim.gesture_circle_inflate);
    	mPointerCircleAppearAnim.setTarget(mPointerCircleShape);
    	mPointerCircleAppearAnim.addListener(new AnimatorListener(){

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				Log.i(TAG ,"pointer animation ended");
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
				LOGI("pointer animation started");
				mDrawOuterCircle = false;
				postInvalidate();
			}

        	
        });
    	mOuterCircleAppearAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.anim.gesture_circle_inflate);
    	mOuterCircleAppearAnim.setTarget(mOuterCircleShape);
    	mOuterCircleAppearAnim.addListener(new AnimatorListener(){

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				LOGI("outer animation ended");
				
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
				LOGI("outer animation started");
				postInvalidate();
			}

        	
        });
    	
    	setInnerCircleDisappearAnim((AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.anim.gesture_circle_collapse));
    	getInnerCircleDisappearAnim().setTarget(mInnerCircleShape);
    	getInnerCircleDisappearAnim().addListener(new AnimatorListener(){

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mTouching = false;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
			}
    		
    	});
    	
    	setPointerCircleDisappearAnim((AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.anim.gesture_circle_collapse));
    	getPointerCircleDisappearAnim().setTarget(mPointerCircleShape);
    	getPointerCircleDisappearAnim().addListener(new AnimatorListener(){

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mDrawPointerCircle = false;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
			}
    		
    	});
    	
    	setOuterCircleDisappearAnim((AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.anim.gesture_circle_collapse));
    	getOuterCircleDisappearAnim().setTarget(mOuterCircleShape);
    	getOuterCircleDisappearAnim().addListener(new AnimatorListener(){

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mDrawOuterCircle = false;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
			}
    		
    	});
    }
    
    private synchronized AnimatorSet getInnerCircleDisappearAnim(){
    	return mInnerCircleDisappearAnim;
    }
    private synchronized void setInnerCircleDisappearAnim(AnimatorSet set){
    	mInnerCircleDisappearAnim = set;
    }
    
    private synchronized AnimatorSet getPointerCircleDisappearAnim(){
    	return mPointerCircleDisappearAnim;
    }
    private synchronized void setPointerCircleDisappearAnim(AnimatorSet set){
    	mPointerCircleDisappearAnim = set;
    }
    
    private synchronized AnimatorSet getOuterCircleDisappearAnim(){
    	return mOuterCircleDisappearAnim;
    }
    private synchronized void setOuterCircleDisappearAnim(AnimatorSet set){
    	mOuterCircleDisappearAnim = set;
    }
    
    private boolean mAnimationsCreated = false;
    private synchronized void setAnimationsCreated(boolean val){
    	mAnimationsCreated = val;
    }
    /**
     * @return Returns whether animations have been created
     */
    private boolean getAnimationsCreated(){
    	return mAnimationsCreated;
    }
    
    private void initBounds(int w, int h){
    	//int left, int top, int right, int bottom
    	mInnerCircleFinalBounds = getBoundsRect(mInnerCicleDiameter, w, h);//new Rect(w/3, h/4,2*w/3, 3*h/4);
    	
    	mOuterCircleFinalBounds = getBoundsRect(mOuterCicleDiameter, w, h);
    }

    private void initShapes(Camera.Parameters params){
    	mInnerCircleShape = new AnimShapeDrawable(new  OvalShape());
    	mInnerCircleShape.setDefaultBounds(mInnerCircleFinalBounds);
    	mInnerCircleShape.getPaint().set(mInnerCirclePaint);

    	mPointerCircleShape = new AnimShapeDrawable(new  OvalShape());

    	mPointerCircleShape.getPaint().set(mInnerCirclePaint);
    	
    	mOuterCircleShape = new AnimShapeDrawable(new  OvalShape());
    	mOuterCircleShape.setDefaultBounds(mOuterCircleFinalBounds);
    	mOuterCircleShape.setInitialBounds(mInnerCircleFinalBounds);
		mOuterCircleShape.getPaint().set(mOuterCirclePaint);

		//Check to see if we aren't already in this setting
    	String currentFaceSet = params.get(CameraInfo.FACE_SHUTTER);
    	String currentSmileSet = params.get(CameraInfo.SMILE_SHUTTER);
    	if(currentFaceSet == null){
    		Log.e(TAG, "Face Shutter not supported");
    		return;
    	}
    	if(currentSmileSet == null){
    		Log.e(TAG, "Smile Shutter not supported");
    		return;
    	}
    	
    	/**
    	 * Determine the spinning direction of each circle, and change their color
    	 * if they have been disabled
    	 */
    	if(currentFaceSet.equalsIgnoreCase(CameraInfo.FACE_SHUTTER_ON) &&
    			currentSmileSet.equalsIgnoreCase(CameraInfo.SMILE_SHUTTER_OFF)){
    		mInnerCircleDirection = Path.Direction.CW;
    		mOuterCircleDirection = Path.Direction.CCW;
    		mInnerCircleShape.getPaint().set(mDisabledCirclePaint);
    		mOuterCircleShape.getPaint().set(mInnerCirclePaint);
    	}else if(currentFaceSet.equalsIgnoreCase(CameraInfo.FACE_SHUTTER_OFF) &&
    			currentSmileSet.equalsIgnoreCase(CameraInfo.SMILE_SHUTTER_ON)){
    		mInnerCircleDirection = Path.Direction.CW;
    		mOuterCircleDirection = Path.Direction.CCW;
    		mInnerCircleShape.getPaint().set(mInnerCirclePaint);
    		mOuterCircleShape.getPaint().set(mDisabledCirclePaint);
    	}else if(currentFaceSet.equalsIgnoreCase(CameraInfo.FACE_SHUTTER_OFF) &&
    			currentSmileSet.equalsIgnoreCase(CameraInfo.SMILE_SHUTTER_OFF)){
    		mInnerCircleDirection = Path.Direction.CCW;
    		mOuterCircleDirection = Path.Direction.CW;
    		mInnerCircleShape.getPaint().set(mInnerCirclePaint);
    		mOuterCircleShape.getPaint().set(mInnerCirclePaint);
    	}
    	mPointerCircleDirection = mInnerCircleDirection;
    	mPointerCircleShape.getPaint().set(mInnerCircleShape.getPaint());
    	
    	mPointerCircleShape.setPathDirection(mPointerCircleDirection);
    	mInnerCircleShape.setPathDirection(mInnerCircleDirection);
    	mOuterCircleShape.setPathDirection(mOuterCircleDirection);

    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	if(mTouching){
//    		LOGI("onDraw");
    		mInnerCircleShape.drawPath(canvas);
    		

    		if(mDrawOuterCircle){
    			mOuterCircleShape.drawPath(canvas);
    		}
    		if(mDrawPointerCircle){
    			mPointerCircleShape.drawPath(canvas);
    		}
    		if(mThetaSweeping && !mDrawPointerCircle){
    			canvas.drawArc(mOuterCircleShape.getCurrentFloatingBounds(), 
    					(float)mSweepStartTheta, (float)mSweepCircleArcTheta, true, mSweepArcPaint);
    		}
    		postInvalidate();
    	}
    }

    /**
     * Function that returns a bounds rectangle for a circle depending
     * on the diameter of the said circle. Circles are centered at x,y = w,h
     * 
     * @param diam The diameter of the circle
     * @param center_w X center of the circle
     * @param center_h Y center of the circle
     * @return Bounds rectangle for {@link ShapeDrawable}
     */
    private Rect getBoundsRect(float diam, int center_w, int center_h){
    	int rad = (int) diam/2;
    	//int left, int top, int right, int bottom
    	return new Rect(center_w - rad, center_h - rad, center_w + rad, center_h + rad);
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
