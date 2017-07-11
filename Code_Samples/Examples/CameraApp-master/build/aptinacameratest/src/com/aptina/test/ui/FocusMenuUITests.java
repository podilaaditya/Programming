/**
 * 
 */
package com.aptina.test.ui;


import static android.test.ViewAsserts.assertGroupContains;
import static android.test.ViewAsserts.assertGroupIntegrity;
import static android.test.ViewAsserts.assertHorizontalCenterAligned;
import static android.test.ViewAsserts.assertOnScreen;
import static android.test.ViewAsserts.assertVerticalCenterAligned;
import static android.test.ViewAsserts.assertLeftAligned;
import static android.test.ViewAsserts.assertRightAligned;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aptina.camera.CameraActivity;



/**
 * @author stoyan
 *
 */
public class FocusMenuUITests extends ActivityInstrumentationTestCase2<CameraActivity> {
	private CameraActivity mActivity;
	
	private LinearLayout mOptionsMenu;
	private LinearLayout mFMMainLinearLayout;
	private LinearLayout mFMFocusMenuLinearListLayout;
	private LinearLayout mFMLlFocuses;
	
	private ScrollView mFMScroll;
	private TextView mFMOptionsTitle;
	private TextView mFMFocusTitle;
	private View mFMLineSeperatorView;

	
	/**
	 * @param name
	 */
	public FocusMenuUITests(String name) {
		super(CameraActivity.class);
		setName(name);
	}
	public FocusMenuUITests() {
		this("FocusMenuUITests");
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();

		mOptionsMenu = (LinearLayout) mActivity.findViewById(com.aptina.R.id.options_menu);
		mFMMainLinearLayout = (LinearLayout) mActivity.findViewById(com.aptina.R.id.focus_menu_main_linear_layout);
		mFMFocusMenuLinearListLayout = (LinearLayout) mActivity.findViewById(com.aptina.R.id.focus_menu_linear_list_layout);
		mFMLlFocuses = (LinearLayout) mActivity.findViewById(com.aptina.R.id.ll_menu_focuses);
		
		mFMScroll = (ScrollView) mActivity.findViewById(com.aptina.R.id.options_menu_scroll);
		 
		mFMOptionsTitle = (TextView) mActivity.findViewById(com.aptina.R.id.focus_menu_options_title);
		mFMFocusTitle = (TextView) mActivity.findViewById(com.aptina.R.id.menu_focus_title);
		 
		mFMLineSeperatorView = (View) mActivity.findViewById(com.aptina.R.id.focus_menu_line_seperator_view);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public final void testPreconditions() {
		assertNotNull(mActivity);
		
		assertNotNull(mOptionsMenu);
		assertNotNull(mFMMainLinearLayout);
		assertNotNull(mFMFocusMenuLinearListLayout);
		assertNotNull(mFMLlFocuses);
		
		assertNotNull(mFMScroll);
		
		assertNotNull(mFMOptionsTitle);
		assertNotNull(mFMFocusTitle);
		
		assertNotNull(mFMLineSeperatorView);
	}

	public final void testFieldsOnScreen() {
		final Window window = mActivity.getWindow();
		final View origin = window.getDecorView();
		assertOnScreen(origin, mOptionsMenu);
		assertOnScreen(origin, mFMMainLinearLayout);
		assertOnScreen(origin, mFMFocusMenuLinearListLayout);
		assertOnScreen(origin, mFMLlFocuses);
		
		assertOnScreen(origin, mFMScroll);
		
		assertOnScreen(origin, mFMOptionsTitle);
		assertOnScreen(origin, mFMFocusTitle);
		
		assertOnScreen(origin, mFMLineSeperatorView);
	}
	
	public final void testViewIntegrity(){
		assertGroupIntegrity(mOptionsMenu);
		assertGroupIntegrity(mFMMainLinearLayout);
		assertGroupIntegrity(mFMFocusMenuLinearListLayout);
		assertGroupIntegrity(mFMLlFocuses);
		
		assertGroupIntegrity(mFMScroll);
	}
	
	public final void testViewHierarchy(){
		assertGroupContains(mOptionsMenu, mFMScroll);
		
		assertGroupContains(mFMScroll, mFMMainLinearLayout);
		
		assertGroupContains(mFMMainLinearLayout, mFMOptionsTitle);
		assertGroupContains(mFMMainLinearLayout, mFMFocusMenuLinearListLayout);
		
		assertGroupContains(mFMFocusMenuLinearListLayout, mFMFocusTitle);
		assertGroupContains(mFMFocusMenuLinearListLayout, mFMLineSeperatorView);
		assertGroupContains(mFMFocusMenuLinearListLayout, mFMLlFocuses);
	}
	
	public final void testViewCenterAlignment(){
		assertVerticalCenterAligned(mOptionsMenu, mFMScroll);
		assertHorizontalCenterAligned(mOptionsMenu, mFMScroll);
		
		assertVerticalCenterAligned(mFMMainLinearLayout, mFMOptionsTitle);
		assertVerticalCenterAligned(mFMMainLinearLayout, mFMFocusMenuLinearListLayout);
		
		assertVerticalCenterAligned(mFMFocusMenuLinearListLayout, mFMFocusTitle);
		assertVerticalCenterAligned(mFMFocusMenuLinearListLayout, mFMLineSeperatorView);
		assertVerticalCenterAligned(mFMFocusMenuLinearListLayout, mFMLlFocuses);
	}
	
	public final void testViewEdgeAlignment(){
		//Left alignment
		assertLeftAligned(mOptionsMenu, mFMScroll);
		
		assertLeftAligned(mFMScroll, mFMMainLinearLayout);
		
		assertLeftAligned(mFMMainLinearLayout, mFMOptionsTitle);
		assertLeftAligned(mFMMainLinearLayout, mFMFocusMenuLinearListLayout);
		
		assertLeftAligned(mFMFocusMenuLinearListLayout, mFMFocusTitle);
		assertLeftAligned(mFMFocusMenuLinearListLayout, mFMLineSeperatorView);
		assertLeftAligned(mFMFocusMenuLinearListLayout, mFMLlFocuses);
		
		
		//Right alignment
		assertRightAligned(mOptionsMenu, mFMScroll);
		
		assertRightAligned(mFMScroll, mFMMainLinearLayout);
		
		assertRightAligned(mFMMainLinearLayout, mFMOptionsTitle);
		assertRightAligned(mFMMainLinearLayout, mFMFocusMenuLinearListLayout);
		
		assertRightAligned(mFMFocusMenuLinearListLayout, mFMFocusTitle);
		assertRightAligned(mFMFocusMenuLinearListLayout, mFMLineSeperatorView);
		assertRightAligned(mFMFocusMenuLinearListLayout, mFMLlFocuses);
	}
	
	public final void testViewDimensions(){		
		focusMenuOneDimTest();
		focusMenuScrollDimTest();
		focusMainLinearLayoutDimTest();
		focusMenuOptionsTitleDimTest();
		focusMenuLinearListLayout();
		focusMenuTitleDimTest();
		focusLineSeperatorDimTest();
		focusLLFocusesDimTest();
	}
	
	public final void testViewTextStrings(){
		String expected = mActivity.getString(com.aptina.R.string.options_title);
		String actual = mFMOptionsTitle.getText().toString();
		
		String msg = "mFMOptionsTitle text string is not : " + expected;
		assertEquals(msg, expected, actual);
		
		expected = mActivity.getString(com.aptina.R.string.focus_title);
		actual = mFMFocusTitle.getText().toString();
		
		msg = "mFMFocusTitle text string is not : " + expected;
		assertEquals(msg, expected, actual);
	}
	
	public final void testViewTextColor(){
		int expected_color = Color.BLACK;
		ColorStateList mList = mFMOptionsTitle.getTextColors();
		int actual_color = mList.getDefaultColor();
		
		String msg = "mFMOptionsTitle default text color is not : " + expected_color;
		assertEquals(msg, expected_color, actual_color);
		
		mList = mFMFocusTitle.getTextColors();
		actual_color = mList.getDefaultColor();
		expected_color = mActivity.getResources().getColor(com.aptina.R.color.focus_menu_focus_title_text_color);
		msg = "mFMFocusTitle default text color is not : " + expected_color;
		assertEquals(msg, expected_color, actual_color);
		
	}
	
	public final void testViewBackgroud(){
		ColorDrawable backGround = (ColorDrawable) mFMOptionsTitle.getBackground();
		int actual_color = backGround.getColor();
		int expected_color = mActivity.getResources().getColor(com.aptina.R.color.focus_menu_options_title_backgroud_color);
		
		String msg = "mFMOptionsTitle default background color is not : " + expected_color;
		assertEquals(msg, expected_color, actual_color);
		
		backGround = (ColorDrawable) mFMLineSeperatorView.getBackground();
		actual_color = backGround.getColor();
		expected_color = mActivity.getResources().getColor(com.aptina.R.color.focus_menu_line_seperator_view_background_color);

		msg = "mFMLineSeperatorView default background color is not : " + expected_color;
		assertEquals(msg, expected_color, actual_color);
		
		////TODO figure out how to check background drawable
		//assertEquals(msg,mContext.getResources().getDrawable(com.aptina.R.drawable.border_focus_menu),mFocusMenuOne.getBackground());
	}
	

	private final void focusMenuOneDimTest() {
		final LayoutParams lp = mOptionsMenu.getLayoutParams();
		double expected_dip = getDipFromString(mActivity.getString(com.aptina.R.dimen.focus_menu_width));
		
		String msg  = "mFocusMenuOne layout width is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.width);
		
		expected_dip = getDipFromString(mActivity.getString(com.aptina.R.dimen.focus_menu_height));
		msg  = "mFocusMenuOne layout height is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.height);
		
		expected_dip = getDipFromString(mActivity.getString(com.aptina.R.dimen.focus_menu_border_size));
		msg  = "mFocusMenuOne layout border is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double) mOptionsMenu.getPaddingBottom());
		
		msg  = "mFocusMenuOne visibility is not : " + View.GONE;
		assertEquals(msg, View.GONE, mOptionsMenu.getVisibility());

	}
	
	private final void focusMenuScrollDimTest(){
		final LayoutParams lp = mFMScroll.getLayoutParams();
		double expected_dip = LayoutParams.MATCH_PARENT;
		
		String msg  = "mFMScroll layout width is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.width);
		
		msg  = "mFMScroll layout height is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.height);
	}
	
	private final void focusMainLinearLayoutDimTest(){
		final LayoutParams lp = mFMMainLinearLayout.getLayoutParams();
		double expected_dip = LayoutParams.MATCH_PARENT;
		
		String msg  = "mFMMainLinearLayout layout width is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.width);
		
		msg  = "mFMMainLinearLayout layout height is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.height);
	}
	
	private final void focusMenuOptionsTitleDimTest(){
		
		final LayoutParams lp = mFMOptionsTitle.getLayoutParams();
		double expected_dip = LayoutParams.MATCH_PARENT;
		
		String msg  = "mFMOptionsTitle layout width is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.width);
		
		expected_dip = LayoutParams.WRAP_CONTENT;
		msg  = "mFMOptionsTitle layout height is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.height);
		
		expected_dip = getDipFromString(mActivity.getString(com.aptina.R.dimen.focus_menu_options_title_text_size));
		msg  = "mFMOptionsTitle layout text size is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)mFMOptionsTitle.getTextSize());
	}
	private final void focusMenuLinearListLayout(){
		final LayoutParams lp = mFMFocusMenuLinearListLayout.getLayoutParams();
		double expected_dip = LayoutParams.MATCH_PARENT;
		
		String msg  = "mFMFocusMenuLinearListLayout layout width is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.width);
		
		msg  = "mFMFocusMenuLinearListLayout layout height is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.height);
		
		//Padding Top
		expected_dip = getDipFromString(mActivity.getString(com.aptina.R.dimen.focus_menu_list_layout_pad_top));
		msg  = "mFMFocusMenuLinearListLayout padding top is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double) mFMFocusMenuLinearListLayout.getPaddingTop());
		
		//Padding Right
		expected_dip = getDipFromString(mActivity.getString(com.aptina.R.dimen.focus_menu_list_layout_pad_right));
		msg  = "mFMFocusMenuLinearListLayout padding right is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double) mFMFocusMenuLinearListLayout.getPaddingRight());
		
		//Padding Bottom
		expected_dip = getDipFromString(mActivity.getString(com.aptina.R.dimen.focus_menu_list_layout_pad_bottom));
		msg  = "mFMFocusMenuLinearListLayout padding bottom is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double) mFMFocusMenuLinearListLayout.getPaddingBottom());
		
		//Padding Left
		expected_dip = getDipFromString(mActivity.getString(com.aptina.R.dimen.focus_menu_list_layout_pad_left));
		msg  = "mFMFocusMenuLinearListLayout padding left is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double) mFMFocusMenuLinearListLayout.getPaddingLeft());
				
				
	}
	
	private final void focusMenuTitleDimTest(){
		final LayoutParams lp = mFMFocusTitle.getLayoutParams();
		double expected_dip = LayoutParams.WRAP_CONTENT;
		
		String msg  = "mFMFocusTitle layout width is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.width);

		msg  = "mFMFocusTitle layout height is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.height);
		
		expected_dip = getDipFromString(mActivity.getString(com.aptina.R.dimen.focus_menu_focus_title_text_size));
		msg  = "mFMFocusTitle layout text size is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)mFMFocusTitle.getTextSize());
	}
	
	private final void focusLineSeperatorDimTest(){
		final LayoutParams lp = mFMLineSeperatorView.getLayoutParams();
		double expected_dip = LayoutParams.MATCH_PARENT;
		
		String msg  = "mFMLineSeperatorView layout width is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.width);
		
		expected_dip = getDipFromString(mActivity.getString(com.aptina.R.dimen.focus_menu_line_seperator_view_height));
		msg  = "mFMLineSeperatorView layout height is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.height);
	}
	
	private final void focusLLFocusesDimTest(){
		final LayoutParams lp = mFMLlFocuses.getLayoutParams();
		double expected_dip = LayoutParams.MATCH_PARENT;
		
		String msg  = "mFMLlFocuses layout width is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.width);
		
		expected_dip = LayoutParams.WRAP_CONTENT;
		msg  = "mFMLlFocuses layout height is not : " + expected_dip;
		assertEquals(msg, (double)expected_dip, (double)lp.height);
	}
	
	private double getDipFromString(String dip_str) {
		int idx = dip_str.indexOf("dip");
		String val_str = dip_str.substring(0, idx);

		return  Double.valueOf(val_str);
	}
}
