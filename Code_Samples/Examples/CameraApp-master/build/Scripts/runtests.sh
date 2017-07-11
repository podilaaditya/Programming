#!/bin/sh
#Connect the device under test to the computer from where the test is going to be executed.

runtests(){
	adb shell am instrument -w -e class com.aptina.test.functional.CameraActivityFunctionalTests com.aptina.test/com.neenbedankt.android.test.InstrumentationTestRunner
    adb shell am instrument -w -e class com.aptina.test.functional.VideoActivityFunctionalTests com.aptina.test/com.neenbedankt.android.test.InstrumentationTestRunner
    getresultfile;

}

#Results are in XML file pull from this directory
getresultfile(){
    adb pull /sdcard/CameraAppTest/ .
}


runtests; 
echo ====== Completed Aptina CameraApp Test Run ======
