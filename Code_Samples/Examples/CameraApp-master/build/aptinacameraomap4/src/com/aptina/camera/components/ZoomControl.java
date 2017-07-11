package com.aptina.camera.components;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.OnZoomChangeListener;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aptina.R;
import com.aptina.logger.Logger;

/**
 * Implements zoom control.
 * Handles zoom changes and redraws zoom selector.
 * Handles press events on the control.
 */
public class ZoomControl extends RelativeLayout implements OnZoomChangeListener {
	private static final String TAG = "ZoomControl";
    /**
     * Zooming completion callback.
     */
    public interface ZoomingCompletedCallback {
        
        /**
         * Called when zooming is completed.
         */
        public void onZoomingCompleted();
    }
    
    /**
     * Number of zoom points on the control.
     */
    private final static float TOTAL_ZOOM_POINTS = 12.0f;

    /**
     * The width in pixels of the original image
     */
    private final static int ZOOM_SLIDER_IMAGE_WIDTH = 276;
    /**
     * The actual pixel width of the slider in the activity
     */
    private int mZoomSliderWidth = -1;
    /**
     * Width of point on the control, in pixels.
     */
    private final static float POINT_WIDTH_PIXEL = 19;
    /**
     * Relative width of point on the control, as a percentage of image width.
     */
    private final static double POINT_WIDTH_PERCENT = POINT_WIDTH_PIXEL/276;
    /**
     * The actual pixel width of each point in the activity
     */
    private float mPointWidth = -1;
    /**
     * The width of zoom control before first point, in pixels. 
     */
    private final static float WIDTH_BEFORE_FIRST_POINT_PIXELS = 28;
    /**
     * The relative width of zoom control before first point, as a percentage of image width.
     */
    private final static double WIDTH_BEFORE_FIRST_POINT_PERCENT = WIDTH_BEFORE_FIRST_POINT_PIXELS/276;
    /**
     * The actual pixel width before the first point in the slider, in the activity
     */
    private float mWidthBeforeFirstPoint = -1;

    /**
     * The parent view of all the other views, multiple inflations without
     * clearing previous ones can lead to a stacking bug
     */
    private View zoom_slider;
    /**
     * Slider image.
     */
    private ImageView mSlider;
    
    /**
     * Zoom selector.
     */
    private ImageView mZoomSelector;

    /**
     * Maximal supported zoom of camera.
     */
    private int mMaxZoom;

    /**
     * Camera instance.
     */
    private Camera mCamera;
    
    /**
     * Callback target for finishing zoom event.
     */
    private ZoomingCompletedCallback mCompletedCallbackListener = null;
    
    /**
     * Creates new instance of the class.
     * 
     * @param context application context.
     */
    public ZoomControl(Context context) {
        super(context);
        inflateResource();
    }
    public float getTotalZoomPoints(){
    	return TOTAL_ZOOM_POINTS;
    }
    public double getWidthBeforeFirstPointPercent(){
    	return WIDTH_BEFORE_FIRST_POINT_PERCENT;
    }
    public double getZoomSliderPointWidthPercent(){
    	return POINT_WIDTH_PERCENT;
    }
    /**
     * Creates new instance of the class.
     * 
     * @param context application context.
     * @param attrs attributes.
     */
    public ZoomControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateResource();
    }

    

    /**
     * Creates new instance of the class.
     * 
     * @param context application context.
     * @param attrs attributes.
     * @param defStyle default style.
     */
    public ZoomControl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflateResource();
    }
    
    /**
     * Function to inflate xml resource at object creation {@link #ZoomControl(Context)}
     */
    private void inflateResource(){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        zoom_slider = inflater.inflate(R.layout.zoom_slider, this);
    }
    /**
     * Sets zooming completed callback listener.
     * 
     * @param listener Listener for zooming completed event.
     */
    public void setZoomingCompletedCallbackListener(ZoomingCompletedCallback listener) {
        mCompletedCallbackListener = listener;
    }
    
    /**
     * Initializes the control.
     * This method must be called before the control will be attached as zoom listener to camera.
     *  
     * @param camera Camera instance.
     * @param device_slider_width The width in pixels of the zoom slider in the application
     */
    public void init(Camera camera) {
        if (camera == null) {
            throw new IllegalArgumentException("ZoomControl.init(Camera camera): camera object is null");
        }
        
        this.mSlider = (ImageView) findViewById(R.id.img_zoom_slider);
        this.mSlider.setOnTouchListener(mSliderOnTouchListener);
        this.mZoomSelector = (ImageView) findViewById(R.id.img_zoom_selection);
        this.mCamera = camera;
        this.mMaxZoom = camera.getParameters().getMaxZoom();
        
        //TODO remove line when access doesn't crash
        mMaxZoom /= 2;

        
        this.onZoomChange(camera.getParameters().getZoom(), true, camera);
        setSliderWidths();
    }

    @Override
    public void onZoomChange(int newZoom, boolean completed, Camera camera) {
    	
        MarginLayoutParams marginParams = new MarginLayoutParams(mZoomSelector.getLayoutParams());

        int pointToDisplay = Math.round((TOTAL_ZOOM_POINTS - 1) * ((float)newZoom)/mMaxZoom);

        marginParams.setMargins((int)(mWidthBeforeFirstPoint + mPointWidth * pointToDisplay), 0, 0, 0);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
        mZoomSelector.setLayoutParams(layoutParams);
        
        if (completed && (mCompletedCallbackListener != null)) {            
            mCompletedCallbackListener.onZoomingCompleted();
        }
    }
    
    private OnTouchListener mSliderOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
        	Log.d(TAG,"zoomslider onTouch");
            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                float x = event.getX();
                x -= mWidthBeforeFirstPoint + mPointWidth/2;
                if(x < 0){// person has clicked the - sign
                	Log.d(TAG, "zoom - 1");
                	int to_zoom  = mCamera.getParameters().getZoom() - 1;
                	return setZoom(to_zoom >= 0 ? to_zoom : 0);
                }
                x /= mPointWidth;
                if(x > TOTAL_ZOOM_POINTS){ // person has clicked the + sign
                	Log.d(TAG, "zoom + 1");
                	int to_zoom  = mCamera.getParameters().getZoom() + 1;
                	return setZoom(to_zoom <= mMaxZoom ? to_zoom : mMaxZoom);
                }
                if (x < 0) x = 0;
                if (x >= TOTAL_ZOOM_POINTS) x = TOTAL_ZOOM_POINTS;
                int newZoom = Math.round(x * mMaxZoom / TOTAL_ZOOM_POINTS);
                return setZoom(newZoom);
            }

            return true;
        }
    };
    
    /**
     * Sets new zoom value for camera.
     * 
     * @param zoomValue Zoom value to apply.
     */
    public boolean setZoom(int zoomValue) {
    	Log.i(TAG, "zoomValue : " + zoomValue);
 
       try {
           Parameters parameters = mCamera.getParameters();

           // If zoom is already set, invoke completed callback manually.
           if (zoomValue == parameters.getZoom()) {
               if (mCompletedCallbackListener != null) {
                   mCompletedCallbackListener.onZoomingCompleted();
               }
           }
           
           if(zoomValue <= mMaxZoom){
        	   parameters.setZoom(zoomValue);
               mCamera.setParameters(parameters);
               onZoomChange(zoomValue, true, mCamera);  
           }else{
        	   Log.i(TAG, "zoomValue is artificially capped and set to : " + mMaxZoom);
           }
               
           return true;
       } catch (Exception ex) {
           Logger.logApplicationException(ex, "mSliderOnTouchListener.onTouch: failed to set zoom value " + zoomValue);
           return false;
       }
    }

	public void setSliderWidths() {
		Log.e(TAG, "mSlider.getWidth() : " + mSlider.getWidth());
        this.mZoomSliderWidth = mSlider.getWidth();
        this.mPointWidth = (float)POINT_WIDTH_PERCENT * (float)mZoomSliderWidth;
        this.mWidthBeforeFirstPoint = (float)WIDTH_BEFORE_FIRST_POINT_PERCENT * (float)mZoomSliderWidth;
	}
    
}
