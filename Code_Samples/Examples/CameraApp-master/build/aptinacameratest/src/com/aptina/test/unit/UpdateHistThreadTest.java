/**
 * 
 */
package com.aptina.test.unit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import android.os.AsyncTask;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.aptina.camera.interfaces.UpdateHistThreadInterface;
import com.aptina.data.HistogramRGBData;
import com.aptina.threads.UpdateHistThread;

/**
 * @author stoyan
 *
 */
public class UpdateHistThreadTest extends InstrumentationTestCase {
	/**
	 * Logging tag
	 */
	private static final String TAG = "UpdateHistThreadTest";
	/**
	 * Thread under testing
	 */
	private UpdateHistThread mThread = null;
	/**
	 * The interface for {@link #mThread}
	 */
	private UpdateHistThreadInterface mUpdateInterface = null;
	
	//DO NOT HAVE PARAMERITIZED CONSTRUCTOR
//	public UpdateHistThreadTest(String name) {
//		super();
//	}
	public UpdateHistThreadTest() {
		super();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mUpdateInterface = new UpdateHistThreadInterface(){

			@Override
			public void onUpdateDataChannels(double[][] colorArray) {
				
			}

			@Override
			public void notifyPagerDataSetChanged() {
				
			}
			
		};
		mThread = new UpdateHistThread();
	}

	/* (non-Javadoc)
	 * @see android.test.InstrumentationTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	/**
	 * Check that on each execution the test the constructors exist for 
	 * {@link UpdateHistThread} and {@link UpdateHistThreadInterface}
	 */
	public void testPreconditions(){
		assertNotNull(mThread);
		assertNotNull(mUpdateInterface);
	}
	/**
	 * Test method for {@link com.aptina.threads.UpdateHistThread#setUpdateHistThreadInterface()}.
	 */
	public void testSetUpdateHistThreadInterface(){
		assertNull(mThread.getUpdateHistThreadInterface());
		mThread.setUpdateHistThreadInterface(mUpdateInterface);
		UpdateHistThreadInterface intr = mThread.getUpdateHistThreadInterface();
		assertNotNull(intr);
		assertEquals(intr,mUpdateInterface);
	}
	
	/**
	 * Test method for {@link com.aptina.threads.UpdateHistThread#getUpdateHistThreadInterface()}.
	 */
	public void testGetUpdateHistThreadInterface(){
		mThread = new UpdateHistThread(mUpdateInterface);
		assertNotNull(mThread);
		assertEquals(mThread.getUpdateHistThreadInterface(),mUpdateInterface);
		
		UpdateHistThreadInterface inter = new UpdateHistThreadInterface(){
			@Override
			public void onUpdateDataChannels(double[][] colorArray) {
			}

			@Override
			public void notifyPagerDataSetChanged() {
			}
		};
		
		mThread.setUpdateHistThreadInterface(inter);
		assertNotSame(mThread.getUpdateHistThreadInterface(),mUpdateInterface);
	}
	
	/**
	 * Test method for {@link com.aptina.threads.UpdateHistThread#onPreExecute()}.
	 */
	public final void testOnPreExecute() {
		UpdateHistThread ut = new UpdateHistThread(new UpdateHistThreadInterface(){
			@Override
			public void onUpdateDataChannels(double[][] colorArray) {
			}

			@Override
			public void notifyPagerDataSetChanged() {
			}
		});
		assertNotNull(ut);
		assertTrue(ut.getUpdateLatch());
		
		//Get the method
		Method onPreExecute = getMethod("onPreExecute", null);
		
		//Assert that the method is private, should be protected
		assertFalse(onPreExecute.isAccessible());
		
		try {
			onPreExecute.setAccessible(true);
			onPreExecute.invoke(ut, (Object[]) null);
		} catch (IllegalArgumentException e) {
			fail("Method IllegalArgumentException");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			fail("Method IllegalAccessException");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			fail("Method InvocationTargetException");
			e.printStackTrace();
		}
		assertFalse(ut.getUpdateLatch());
	}
	/**
	 * Test method for {@link com.aptina.threads.UpdateHistThread#doInBackground(byte[][])}.
	 */
	public void testDoInBackgroundByteArrayArray() {
		UpdateHistThread ut = new UpdateHistThread(new UpdateHistThreadInterface(){
			@Override
			public void onUpdateDataChannels(double[][] colorArray) {
			}

			@Override
			public void notifyPagerDataSetChanged() {
			}
		});
		assertNotNull(ut);
		//Make sure that the update latch isn't changed during this methods
		assertTrue(ut.getUpdateLatch());
		
		//Create a class object of byte[][] to get the reflection method
		Class<?>[] cs = new Class<?>[1];
		cs[0] = byte[][].class; 
		
		//Get the method
		Method doInBackground = getMethod("doInBackground", cs);
		
		//Assert that the method is private, should be protected
		assertFalse(doInBackground.isAccessible());
		
		Object arglistPos[] = new Object[1];
		arglistPos[0] = createByteArrayObj(HistogramRGBData.RGB_COLOR_DEPTH , 
				HistogramRGBData.CURRENT_CHART_INTENSITY_DEPTH,(byte) 4);
		Object arglistNeg[] = new Object[1];
		arglistNeg[0] = createByteArrayObj(HistogramRGBData.RGB_COLOR_DEPTH , 
				HistogramRGBData.CURRENT_CHART_INTENSITY_DEPTH,(byte) -4);
		Object normPos = null;
		Object normNeg = null;
		try {
			doInBackground.setAccessible(true);
			normPos = doInBackground.invoke(ut, arglistPos);
			normNeg = doInBackground.invoke(ut, arglistNeg);
		} catch (IllegalArgumentException e) {
			fail("Method IllegalArgumentException");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			fail("Method IllegalAccessException");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			fail("Method InvocationTargetException");
			e.printStackTrace();
		}
		//Make sure that the update latch isn't changed during this methods
		assertTrue(ut.getUpdateLatch());
		
		//Check that the method returns not null
		assertNotNull(normPos);
		assertNotNull(normNeg);
		
		//Check that the return type is correct
		assertEquals(double[][].class, normPos.getClass());
		double[][] ansPos = (double[][]) normPos;
		double[][] ansNeg = (double[][]) normNeg;
		
		//Check that the return array is of the right depth and channel size
		assertEquals(HistogramRGBData.RGB_COLOR_DEPTH,ansPos.length);//channel size
		assertEquals(HistogramRGBData.RGB_COLOR_DEPTH,ansNeg.length);//channel size
		for(int i = 0; i < HistogramRGBData.RGB_COLOR_DEPTH; i++){
			assertEquals(HistogramRGBData.CURRENT_CHART_INTENSITY_DEPTH,ansPos[i].length);//depth
			assertEquals(HistogramRGBData.CURRENT_CHART_INTENSITY_DEPTH,ansNeg[i].length);//depth
		}
		
		//Check that the arrays were normalized correctly
		assertTrue(isNormalized(ansPos));
		assertTrue(isNormalized(ansNeg));
		
	}
	
	/**
	 * Test method for {@link com.aptina.threads.UpdateHistThread#onPostExecute(double[][])}.
	 */
	public void testOnPostExecuteDoubleArrayArray() {
		UpdateHistThread ut = new UpdateHistThread(new UpdateHistThreadInterface(){
			@Override
			public void onUpdateDataChannels(double[][] colorArray) {
			}

			@Override
			public void notifyPagerDataSetChanged() {
			}
		});
		assertNotNull(ut);
		//Check the latch is true
		assertTrue(ut.getUpdateLatch());
		
		//Create a class object of byte[][] to get the reflection method
		Class<?>[] cs = new Class<?>[1];
		cs[0] = double[][].class;
		
		//Get the method
		Method doInBackground = getMethod("onPostExecute", cs);
				
		//Assert that the method is private, should be protected
		assertFalse(doInBackground.isAccessible());
	}
	
//	/**
//	 * Test method for {@link com.aptina.threads.UpdateHistThread#UpdateHistThread(com.aptina.camera.interfaces.UpdateHistFragInterface)}.
//	 */
//	public final void testUpdateHistThreadUpdateHistFragInterface() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link com.aptina.threads.UpdateHistThread#linearExecute(byte[][])}.
//	 */
//	public void testLinearExecute() {
//		fail("Not yet implemented");
//	}
//
	/**
	 * Test method for {@link com.aptina.threads.UpdateHistThread#UpdateHistThread()}.
	 */
	public final void testUpdateHistThread() {
		UpdateHistThread ut = new UpdateHistThread();
		assertNotNull(ut);
		assertNull(ut.getUpdateHistThreadInterface());
		assertTrue(ut.getUpdateLatch());
		
		ut = new UpdateHistThread(mUpdateInterface);
		assertNotNull(ut);
		assertNotNull(ut.getUpdateHistThreadInterface());
		assertTrue(ut.getUpdateLatch());
		
		//Check inheritance class
		Class<?> ancestor = ut.getClass().getSuperclass();
		assertEquals(AsyncTask.class,ancestor);
		
		//Check interfaces implemented
		Type[] interfaces = ut.getClass().getGenericInterfaces();
		assertEquals(0,interfaces.length);
		
		//Generic types
		TypeVariable<?>[] params = ut.getClass().getTypeParameters();
		assertEquals(0, params.length);
		params = ancestor.getTypeParameters();
		assertEquals(3, params.length);
		
		//Check class modifiers
		int modifier = ut.getClass().getModifiers();
		assertEquals(Modifier.PUBLIC, modifier);
		
		
	}

	
//
//	/**
//	 * Test method for {@link com.aptina.threads.UpdateHistThread#onProgressUpdate(java.lang.Object[])}.
//	 */
//	public void testOnProgressUpdateObjectArray() {
//		fail("Not yet implemented");
//	}
//

	private Method getMethod(String name, Class<?>[] params){
		UpdateHistThread ut = new UpdateHistThread(new UpdateHistThreadInterface(){
			@Override
			public void onUpdateDataChannels(double[][] colorArray) {
			}

			@Override
			public void notifyPagerDataSetChanged() {
			}
		});
		assertNotNull(ut);
		
		try {
			return ut.getClass().getDeclaredMethod(name, params);
		} catch (NoSuchMethodException e) {
			StringBuilder sb = null;
			if(params != null){
				sb = new StringBuilder(params.length);
				for(Class<?> c : params){
					if(c != null){
						sb.append(c.getName() + ": ");
					}
				}
			}
			String pNames = sb != null ? sb.toString() : "null";
			String err = "Does not exist : protected void onPreExecute(), with " + params.length 
					+ " prarams  : " + pNames;
			Log.e(TAG, err); 
			fail(err);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Create a byte[][] for testing. The array is populated so that all even indices have valH; 
	 * all odd indices have valL
	 * 
	 * @param channels The number of color channels in the array
	 * @param debth The depth of each color channel, all channels must have the same depth
	 * @param val The high value, low value is 1/2 high value
	 * @return the populated array
	 */
	private byte[][] createByteArrayObj(int channels, int depth, byte val){
		byte[][] ba = new byte[1][channels * depth];
		if(val %2 != 0){
			Log.e(TAG, "Array val must be an even number");
			return null;
		}
		for(int i = 0; i < ba.length; i++){
			for(int j = 0; j < ba[i].length; j++){
				ba[i][j] = (byte) (j%2 == 0 ? val : val/2);
			}
		}
		return ba;
	}
	
	private boolean isNormalized(double[][] ar){
		StringBuilder sb = new StringBuilder(ar.length*ar[0].length);
		for(int i = 0; i < ar.length; i++){
			for(int j = 0; j < ar[i].length; j++){
				sb.append(ar[i][j]+",");
			}
		}
		int i = 0;//Only checks first channel, TODO update
		for(int j = 0; j < ar[i].length; j++){
			if(j%2 == 0 && Math.abs(ar[i][j]) != 1){
				Log.e(TAG,"return even : " + i + " " + j + " = " + ar[i][j]);
				return false;
			}else if(j%2 == 1 && Math.abs(ar[i][j]) != 0.5){
				Log.e(TAG,"return odd : " + i + " " + j);
				return false;
			}
			
		}	
		return true;
	}
}
