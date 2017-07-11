#!/bin/sh
#Connect the device under test to the computer from where the test is going to be executed.
#TODO all SdCards don't mount to /sdcard
runtests(){
    adb shell am instrument -w -e class com.aptina.test.functional.CameraActivityFunctionalTests com.aptina.test/com.neenbedankt.android.test.InstrumentationTestRunner
    adb shell am instrument -w -e class com.aptina.test.functional.VideoActivityFunctionalTests com.aptina.test/com.neenbedankt.android.test.InstrumentationTestRunner
	getresultfile;

}

t
getresultfile(){
	adb pull /sdcard/CameraAppTest/ .
}
cleanup(){
	echo === Cleanup previously installed applications ===
    adb uninstall com.aptina >/dev/null
    adb uninstall com.aptina.test > /dev/null
    
}

#========= Command Line ===========
echo ====== Start Aptina CameraApp Test Run =====
echo ====== Test results are stored in CameraAppTestLog.xml =========

cleanup;

echo ====== Install Aptina Camera App ======
adb install CameraActivity-release.apk

echo ====== Install Aptina Camera App Tests ======
adb install CameraActivityTest-release.apk


runtests; cleanup;

adb shell rm /sdcard/CameraAppTest/*.*

echo ====== Completed Aptina Aptina Camera App Test Run ======
