/**
 * 
 */
package com.aptina.test.functional;

import android.hardware.Camera;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aptina.R;
import com.aptina.camera.CameraActivity;
import com.aptina.camera.CameraInfo;
import com.aptina.camera.components.OptionsMenu;
import com.aptina.camera.interfaces.MenuLayoutInterface;


/**
 * @author stoyan
 *
 */
public class FocusMenuFunctionalTests extends ActivityInstrumentationTestCase2<CameraActivity> {
	private CameraActivity mActivity;
	private LinearLayout mFocusMenuList;
	private LinearLayout mFocusMenuView;
	private MenuLayoutInterface mMenuLayoutInterface;
	private View mOptionsButton;
	private String[] focus_options;
	private Camera mCamera;
	private OptionsMenu mOptionsMenu;
	/**
	 * @param name
	 */
	public FocusMenuFunctionalTests(String name) {
		super(CameraActivity.class);
		setName(name);
	}
	public FocusMenuFunctionalTests() {
		this("FocusMenuFunctionalTests");
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mFocusMenuList = mActivity.getFocusMenuList();
		mFocusMenuView = mActivity.getFocusMenuView();
		mMenuLayoutInterface  = mActivity.getOptionsMenuInterface();
		mOptionsButton = mActivity.getOptionsButton();
		focus_options = mActivity.getFocusOptionsArray();
		mCamera = mActivity.getCamera();
		mOptionsMenu = mActivity.getOptionsMenu();
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public final void testPreconditions() {
		assertNotNull(mActivity);
		assertNotNull(mFocusMenuList);
		assertNotNull(mFocusMenuView);
		assertNotNull(mMenuLayoutInterface);
		assertNotNull(mOptionsButton);
		assertNotNull(mCamera);
		assertNotNull(mOptionsMenu);

	}
	
	public final void testFocusMenuBeforeClick(){
		assertTrue(mFocusMenuView.getVisibility() == View.GONE);
		assertEquals(0,mFocusMenuList.getChildCount());
		assertNull(focus_options);
		assertTrue(mOptionsButton.isClickable());
		assertEquals(CameraInfo.FOCUS_CONTINUOUS_PICTURE,mCamera.getParameters().getFocusMode());
	}

	public final void testFocusMenuClick(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);

		focus_options = clickFocusOptionsButton();
		assertNotNull(focus_options);
		assertTrue(mActivity.getFocusMenuView().getVisibility() == View.VISIBLE);
		int child_count = mActivity.getFocusMenuList().getChildCount();
		assertTrue(child_count > 0);//There should be at least 1 focus mode
		assertEquals(CameraInfo.FOCUS_CONTINUOUS_PICTURE,mCamera.getParameters().getFocusMode());
		assertEquals(focus_options.length, child_count);//There should be a view for every focus option
		
		int i = 0;
		for(String mode : focus_options){//Check to see that the text and the focus strings are equal for all modes/views
			View v  = mActivity.getFocusMenuList().getChildAt(i);
			TextView text = (TextView) v.findViewById(R.id.text);
			assertEquals(mode , text.getText().toString());
			/**
			 * Test to see that "continuous-focus" and "roifocus" are not part of 
			 * the focus options available
			 */
			assertTrue(!(text.getText().toString().equalsIgnoreCase(CameraInfo.FOCUS_CONTINUOUS_VIDEO)));
			assertTrue(!(text.getText().toString().equalsIgnoreCase(CameraInfo.FOCUS_MODE_ROIFOCUS)));
			//Check to see that the correct radio button is clicked for the current focus
			RadioButton rb = (RadioButton) v.findViewById(R.id.radioButton);
			String currentFocus = mCamera.getParameters().getFocusMode();
			if(text.getText().toString().equalsIgnoreCase(currentFocus)){
				assertTrue(rb.isChecked());
			}else{
				assertFalse(rb.isChecked());
			}
			
			
			i++;
		}
		
		
	
	}
	
	public final void testFocusSwitching(){
		mActivity.runningTest = true;
		assertTrue(mActivity.runningTest);
		//Get the menu to open and initialize with focus options
		focus_options = clickFocusOptionsButton();
		assertNotNull(focus_options);

		
		int i = 0;
		for(String mode : focus_options){
			View v  = mActivity.getFocusMenuList().getChildAt(i);
			TextView text = (TextView) v.findViewById(R.id.text);
			//Make sure that the focus agrees with the text of the view
			assertEquals(mode , text.getText().toString());
			
			final RadioButton rb = (RadioButton) v.findViewById(R.id.radioButton);
			String currentFocus = mCamera.getParameters().getFocusMode();
			//Make sure the correct radio button is checked
			if(text.getText().toString().equalsIgnoreCase(currentFocus)){
				assertTrue(rb.isChecked());
			}else{
				assertFalse(rb.isChecked());
			}
			
			final int ii = i;
			try {
				runTestOnUiThread(new Runnable() {
					public void run() {
//						mMenuLayoutInterface.OnFocusClick(ii); click options button to hided options and set settings
					}
				});
				
				
			} catch (Throwable e) {
				fail("Running click on UI failed");
				e.printStackTrace();
			}finally{
				
			}
			getInstrumentation().waitForIdleSync();
			//Make sure the correct focus was set
			assertEquals(text.getText().toString(), mActivity.getCamera().getParameters().getFocusMode());
			
			//Focus Menu View closes when radio button clicked, so open it again
			focus_options = clickFocusOptionsButton();
			i++;
		}
	}

	/**
	 * Click the option button a number of times
	 * @return
	 */
	private final String[] clickFocusOptionsButton(){
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					mOptionsButton.requestFocus();
					mOptionsButton.performClick();

					
				}
			});
			
			
		} catch (Throwable e) {
			fail("Running click on UI failed");
			e.printStackTrace();
		}finally{
			
		}
		getInstrumentation().waitForIdleSync();
		String[] modes = mActivity.getFocusOptionsArray();
		assertNotNull(modes);
		return modes;
	}

}
