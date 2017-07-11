/**
 * 
 */
package com.aptina.camera.components;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;

/**
 * Animation Shape Oval. Custom {@link ShapeDrawable}
 * in which we can set width/height
 * 
 * @author stoyan
 */
public class AnimShapeDrawable extends ShapeDrawable{
	private static final String TAG = "AnimShapeDrawable";
	
	/**
	 * Toggle to turn on/off logging for {@link #LOGI(String)}
	 */
	private static final boolean LOG_ON = false;
	
	/**
	 * Remember the boundary rectangle of the shape
	 */
	private Rect mBoundRect;
	
	/**
	 * Remember the current boundary rectangle of the shape
	 */
	private Rect mCurrentRect;
	
	/**
	 * The starting rectangle boundary if the shape does not 
	 * start with a diameter of 0
	 */
	private Rect mOffsetRect;
	
	/**
	 * A floating point Rect in order to create path using it
	 */
	private RectF mFloatBounds;
	
	/**
	 * The path of the shape using {@link #mCurrentRect}, with added 
	 * paint effects
	 */
	private Path mShapePath;
	
	/**
	 * The phase of the paint effect: clockwise or counter clockwise
	 * depending on the setting of {@link #mEffectDirection}
	 */
	private int mEffectPhase;
	
	/**
	 * 
	 */
	private Direction mEffectDirection = Path.Direction.CW; 
	/**
	 * Constructor
	 * 
	 * @param s
	 */
	public AnimShapeDrawable(Shape s){
		super(s);
		mBoundRect = new Rect(0,0,0,0);
		mCurrentRect = new Rect(0,0,0,0);
		mOffsetRect =  new Rect(0,0,0,0);
		mFloatBounds = new RectF(0,0,0,0);
		mShapePath = new Path();
		setBounds(mCurrentRect);
	}


	/**
	 * Sets the width of the shape as a percentage of what the shape bounds
	 * were initially set up as in the constructor {@link #setDefaultBounds(Rect)}
	 * 
	 * @param width_percent The width as a percentile of the initial
	 */
	public void setWidth(float width_percent){
		final int default_width = mBoundRect.right - mBoundRect.left;
		final float delta_width = default_width * width_percent + (mOffsetRect.right - mOffsetRect.left);
		LOGI("new width : " + delta_width);
		mCurrentRect.left = (int)(mBoundRect.centerX() - delta_width/2);
		mCurrentRect.right = (int)(mBoundRect.centerX() + delta_width/2);
		//left, top, right, bottom
		mCurrentRect.set(mCurrentRect.left, mCurrentRect.top, mCurrentRect.right, mCurrentRect.bottom);
		setBounds(mCurrentRect);
	}
	
	/**
	 * Sets the height of the shape as a percentage of what the shape bounds
	 * were initially set up as in the constructor {@link #setDefaultBounds(Rect)}
	 * 
	 * @param height_percent The height as a percentile of the initial
	 */
	public void setHeight(float height_percent){
		final int default_height = mBoundRect.bottom - mBoundRect.top;
		LOGI("default_height : " + default_height);
		final float delta_height = default_height * height_percent + (mOffsetRect.bottom - mOffsetRect.top);
		LOGI("new height : " + delta_height);
		LOGI("height_percent : " + height_percent);
		mCurrentRect.top = (int)(mBoundRect.centerY() - delta_height/2);
		mCurrentRect.bottom = (int)(mBoundRect.centerY() + delta_height/2);
		LOGI("top : " + mCurrentRect.top);
		LOGI("bottom : " + mCurrentRect.bottom);
		//left, top, right, bottom
		mCurrentRect.set(mCurrentRect.left, mCurrentRect.top, mCurrentRect.right, mCurrentRect.bottom);
		setBounds(mCurrentRect);
	}
	
	/**
	 * Method that transforms the shape into a path with effects
	 * 
	 * @param canvas Canvas to draw the shape with path effects
	 */
	public void drawPath(Canvas canvas){
		//Clear the path so that it has only one oval, as the shape is animated
		mShapePath.reset();
		
		this.getPaint().setPathEffect(makeEffects(getEffectPhase()));
		mShapePath.addOval(getCurrentFloatingBounds(), mEffectDirection);
		canvas.drawPath(mShapePath, this.getPaint());
	}
	/**
	 * Set the default bounds of the shape if not set in constructor {@link #AnimShapeDrawable(Shape, Rect)}
	 * 
	 * @param bounds The default bounds of the shape
	 */
	public void setDefaultBounds(Rect bounds){
		mBoundRect = bounds;
		setBounds(bounds);
	}
	
	/**
	 * Set the initial bounds of {@link #mOffsetRect}, which act as an
	 * offset of where the oval begins its expansion. Leave empty if you want
	 * the oval with {@link #setWidth(float)} and {@link #setHeight(float)} when float = 0.0
	 * to have a diameter of 0
	 * 
	 * @param bounds The bounds to be set
	 */
	public void setInitialBounds(Rect bounds){
		mOffsetRect = bounds;
	}
	
	/**
	 * Converts the {@link #mCurrentRect} to a RectF
	 * 
	 * @return A RectF version of {@link #mCurrentRect}
	 */
	public RectF getCurrentFloatingBounds(){
		mFloatBounds.set(mCurrentRect);
		return mFloatBounds;
	}
	
	/**
	 * Returns the phase of the paint effect, and 
	 * decrements the phase by one
	 * 
	 * To get the path to rotate backwards to the direction
	 * it points to, increment the phase by 1 instead
	 * 
	 * @return The current phase of the effect
	 */
	private int getEffectPhase(){
		return mEffectPhase--;
	}
	
	/**
	 * Set the direction of the path
	 */
	public void setPathDirection(Direction dir){
		mEffectDirection = dir;
	}
    private PathEffect makeEffects(float phase) {
    	Path p = new Path();
    	p.addOval(getCurrentFloatingBounds(), mEffectDirection);
    	PathEffect effect = new PathDashPathEffect(makePathDash(), 12, phase,
                					PathDashPathEffect.Style.ROTATE);
    	
        return effect;

    }
    
    /**
     * Creates a path that is stamped onto {@link #mShapePath} to give it the look
     * of a bunch of arrows
     * 
     * @return The dashed path that is stamped onto {@link #mShapePath}
     */
    private static Path makePathDash() {
        Path p = new Path();
        p.moveTo(4, 0);
        p.lineTo(0, -4);
        p.lineTo(8, -4);
        p.lineTo(12, 0);
        p.lineTo(8, 4);
        p.lineTo(0, 4);
        return p;
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
