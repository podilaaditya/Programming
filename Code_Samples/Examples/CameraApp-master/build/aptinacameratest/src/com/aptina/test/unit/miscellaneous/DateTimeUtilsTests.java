/**
 * 
 */
package com.aptina.test.unit.miscellaneous;

import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;

import com.aptina.miscellaneous.DateTimeUtils;

/**
 * @author stoyan
 *
 */
public class DateTimeUtilsTests extends TestCase {
	private static final HashMap<Date, String> conversionTableFormatDate =
		    new HashMap<Date, String>();
	static {
	    // initialize (date, string) pairs
		conversionTableFormatDate.put(new Date(1000), "1970-01-01 00:00:01:000");
		conversionTableFormatDate.put(new Date(5000), "1970-01-01 00:00:05:000");
		conversionTableFormatDate.put(new Date(1234), "1970-01-01 00:00:01:234");
		conversionTableFormatDate.put(new Date(9876), "1970-01-01 00:00:09:876");
	}
	
	private static final HashMap<Date, String> conversionTablePhotoFormatDate =
		    new HashMap<Date, String>();
	static {
	    // initialize (date, string) pairs
		conversionTablePhotoFormatDate.put(new Date(1000), "19700101_000001");
		conversionTablePhotoFormatDate.put(new Date(5000), "19700101_000005");
		conversionTablePhotoFormatDate.put(new Date(12340), "19700101_000012");
	}
	/**
	 * @param name
	 */
	public DateTimeUtilsTests(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.aptina.miscellaneous.DateTimeUtils#formatDate(java.util.Date)}.
	 */
	public void testFormatDate() {
		for(Date s : conversionTableFormatDate.keySet()){
			final String expected = conversionTableFormatDate.get(s);
			final String actual = DateTimeUtils.formatDate(s);
			final String msg = "expected : " + expected + ", but returned : " + actual;
			assertEquals(msg, expected, actual);
		}
		
	}


	/**
	 * Test method for {@link com.aptina.miscellaneous.DateTimeUtils#getPhotoFormatDate(java.util.Date)}.
	 */
	public void testGetPhotoFormatDate() {
		for(Date s : conversionTablePhotoFormatDate.keySet()){
			final String expected = conversionTablePhotoFormatDate.get(s);
			final String actual = DateTimeUtils.getPhotoFormatDate(s);
			final String msg = "expected : " + expected + ", but returned : " + actual;
			assertEquals(msg, expected, actual);
		}
	}

}
