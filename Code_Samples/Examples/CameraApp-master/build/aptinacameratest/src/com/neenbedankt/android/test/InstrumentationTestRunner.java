/*
 * Copyright (C) 2007 Hugo Visser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neenbedankt.android.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;



import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

/*Referencing the code in KPITest2, for output of test results to a XML file */
public class InstrumentationTestRunner extends android.test.InstrumentationTestRunner {
	private Writer mWriter;
    private XmlSerializer mTestSuiteSerializer;
    private long mTestStarted;
    
    //Define directory and file names here temporarily, will need to move for static use when need.
    
    public static final String JUNIT_XML_FILE = "CameraAppTestLog.xml";
    
    public static final String SUBDIR ="/CameraAppTest/";
    
    @Override
    public void onStart() {
         File outputdir = new File(Environment.getExternalStorageDirectory() + SUBDIR);
         if (!outputdir.exists()) {
        	 if (!(outputdir.mkdirs())) {
        		 throw new RuntimeException("Error creating directory " + Environment.getExternalStorageDirectory() + SUBDIR);
        	 }
        	 
         }
    	 File outputfile = new File(Environment.getExternalStorageDirectory()+ SUBDIR +JUNIT_XML_FILE);
			if (!outputfile.exists()) {
				try {
					outputfile.createNewFile();
					
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			
			try {
				startJUnitOutput(new FileWriter(new File(Environment.getExternalStorageDirectory()+SUBDIR, JUNIT_XML_FILE), true));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
        super.onStart();
    }

    void startJUnitOutput(Writer writer) {
        try {
            mWriter = writer;
            mTestSuiteSerializer = newSerializer(mWriter);
            mTestSuiteSerializer.startDocument(null, null);
            mTestSuiteSerializer.startTag(null, "testsuites");
            mTestSuiteSerializer.startTag(null, "testsuite");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private XmlSerializer newSerializer(Writer writer) {
        try {
            XmlPullParserFactory pf = XmlPullParserFactory.newInstance();
            XmlSerializer serializer = pf.newSerializer();
            serializer.setOutput(writer);
            return serializer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }        
    }
    
    @Override
    public void sendStatus(int resultCode, Bundle results) {
        super.sendStatus(resultCode, results);
        switch (resultCode) {
            case REPORT_VALUE_RESULT_ERROR:
            case REPORT_VALUE_RESULT_FAILURE:
            case REPORT_VALUE_RESULT_OK:
            try {
                recordTestResult(resultCode, results);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case REPORT_VALUE_RESULT_START:
                recordTestStart(results);
            default:
                break;
        }
    }
    
    void recordTestStart(Bundle results) {
        mTestStarted = System.currentTimeMillis();
    }

    void recordTestResult(int resultCode, Bundle results) throws IOException {
    	
        float time = (System.currentTimeMillis() - mTestStarted) / 1000.0f;
        String className = results.getString(REPORT_KEY_NAME_CLASS);
        String testMethod = results.getString(REPORT_KEY_NAME_TEST);
        String stack = results.getString(REPORT_KEY_STACK);
        int current = results.getInt(REPORT_KEY_NUM_CURRENT);
        int total = results.getInt(REPORT_KEY_NUM_TOTAL);
        
        mTestSuiteSerializer.startTag(null, "testcase");
        mTestSuiteSerializer.attribute(null, "classname", className);
        mTestSuiteSerializer.attribute(null, "name", testMethod);
       
        
        if (resultCode != REPORT_VALUE_RESULT_OK) {
            mTestSuiteSerializer.startTag(null, "failure");
            if (stack != null) {
                String reason = stack.substring(0, stack.indexOf('\n'));
                String message = "";
                int index = reason.indexOf(':');
                if (index > -1) {
                    message = reason.substring(index+1);
                    reason = reason.substring(0, index);
                }
                mTestSuiteSerializer.attribute(null, "message", message);
                mTestSuiteSerializer.attribute(null, "type", reason);
                mTestSuiteSerializer.text(stack);
            }
            mTestSuiteSerializer.endTag(null, "failure");
        } else {
            mTestSuiteSerializer.attribute(null, "time", String.format("%.3f", time));
        }
        mTestSuiteSerializer.endTag(null, "testcase");        
        if (current == total) {
            mTestSuiteSerializer.startTag(null, "system-out");
            mTestSuiteSerializer.endTag(null, "system-out");
            mTestSuiteSerializer.startTag(null, "system-err");
            mTestSuiteSerializer.endTag(null, "system-err");
            mTestSuiteSerializer.endTag(null, "testsuite");
            mTestSuiteSerializer.flush();
        }
       
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        endTestSuites();
        super.finish(resultCode, results);
    }

    void endTestSuites() {
        try {
            mTestSuiteSerializer.endTag(null, "testsuites");
            mTestSuiteSerializer.endDocument();
            mTestSuiteSerializer.flush();
            mWriter.flush();
            mWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

