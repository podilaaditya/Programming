#!/bin/bash

echo "Entering buildCam.sh"

echo "==============================="
echo "===========STARTING============"
echo "==============================="


#Set the Hudson workspace
HUD_WORKSPACE=$1
echo "HUD_WORKSPACE : " $HUD_WORKSPACE

#Append the workspace with our application location
CAM_WORKSPACE=$HUD_WORKSPACE/build/aptinacameraomap4
CAM_TEST_WORKSPACE=$HUD_WORKSPACE/build/aptinacameratest
echo "CAM_WORKSPACE : " $CAM_WORKSPACE
echo "CAM_TEST_WORKSPACE : " $CAM_TEST_WORKSPACE

#Set the URL to where the build workspace is, ie the src, manifest.xml, etc.
BUILD_URL=$2
echo "BUILD_URL : " $BUILD_URL

#Get the revision number and id
REV_ID=$(git rev-parse --short HEAD)
REV_NUM=$(git shortlog | grep -E '^[ ]+\w+' | wc -l)
echo "REV_ID : " $REV_ID
echo "REV_NUM : " $REV_NUM


#Set the jira sprint number by hand
SPRINT_NUM="v1.5.11"
echo "SPRINT_NUM : " $SPRINT_NUM

#Create the application version string
APP_VERSION="$SPRINT_NUM$REV_NUM:$REV_ID"
echo "APP_VERSION : " $APP_VERSION

#Update value of an element, -L inline to file so that it is permenant to file
#Note this overrides whatever the most recent changeset sets these values to
STRING_VALUES_LOC=$CAM_WORKSPACE/res/values/strings.xml
echo "Using string.xml file at : " $STRING_VALUES_LOC
xml ed -L -u "resources/string[@name='app_version']" -v "$APP_VERSION" $STRING_VALUES_LOC
XML_RETURN_CODE=$?
echo "App_Version XML_RETURN_CODE = " $XML_RETURN_CODE
if [ $XML_RETURN_CODE -ne 0 ]; then
echo "res/values/strings XML validation failed with code : " $XML_RETURN_CODE
   exit 1
else
echo "res/values/strings XML validation succeeded"
fi
#Make sure that the SDK version of android that the application is built for is ICS 4.0.3, API 15
#Note this overrides whatever the most recent changeset sets these values to
echo "Setting the android:minSdkVersion and android:targetSdkVersion to API level 15, ICS"
MANIFEST_LOC=$CAM_WORKSPACE/AndroidManifest.xml
echo "Using AndroidManifest file at : " $MANIFEST_LOC
xml ed -L -u "/manifest/uses-sdk/@android:minSdkVersion" -v 15 $MANIFEST_LOC
xml ed -L -u "/manifest/uses-sdk/@android:targetSdkVersion" -v 15 $MANIFEST_LOC
XML_RETURN_CODE=$?
echo "AndroidManifest XML_RETURN_CODE = " $XML_RETURN_CODE
#Return non-zero to tell hudson that build failed
if [ $XML_RETURN_CODE -ne 0 ]; then
echo "AndroidManifest XML validation failed with code : " $XML_RETURN_CODE
   exit 1
else
echo "AndroidManifest XML validation succeeded"
fi
#Set path for android debug bridge
PLATFORM_TOOLS=/home/sdimitrov/android-sdk-linux/platform-tools

#Set path for android build.xml file builder
ANDROID_TOOLS=/home/sdimitrov/android-sdk-linux/tools

#Add tools to PATH
export PATH=$PATH:$PLATFORM_TOOLS:$ANDROID_TOOLS

echo "script PATH : " $PATH

echo "Starting adb server"
adb start-server

#Check to see if all builds directory exists
ALL_BUILDS_DIR=$HUD_WORKSPACE"/all_builds"
if [ -d "$ALL_BUILDS_DIR" ]; then
echo $ALL_BUILDS_DIR " exists"
   else
echo "Making directory for builds : " $ALL_BUILDS_DIR
        mkdir $ALL_BUILDS_DIR
fi

#Change to builds dir
cd $ALL_BUILDS_DIR


###########################
#####Build the Project#####
###########################
BUILD_FOLDER=$ALL_BUILDS_DIR/$BUILD_NUMBER


mkdir $BUILD_FOLDER
echo "Build folder for project : " $BUILD_FOLDER


#Go to the root of the project
echo "Changing dir to : " $CAM_WORKSPACE
cd $CAM_WORKSPACE

#Create the build.xml file for ant
echo "Creating build.xml with android-15 target"
android update project --target "android-15" --path $CAM_WORKSPACE
#android update project -p $CAM_WORKSPACE


#Build the .apk debug app
echo "Building debug apk"
ant debug
ANT_RETURN_CODE=$?
echo "debug antReturnCode = " $ANT_RETURN_CODE

#Return non-zero to tell hudson that build failed
if [ $? -ne 0 ]; then
echo "Debug Build Failed : antReturnCode = " $ANT_RETURN_CODE
   exit 1
else
echo "Debug Build succeeded"
fi

#Copy build to debug folder
echo "Moving debug .apk to all builds folder"
cp $CAM_WORKSPACE/bin/CameraActivity-debug.apk $BUILD_FOLDER

#Build the .apk release app
echo "Building release apk"
ant release
ANT_RETURN_CODE=$?
echo "release antReturnCode = " $ANT_RETURN_CODE

#Return non-zero to tell hudson that build failed
if [ $? -ne 0 ]; then
echo "Release Build Failed : antReturnCode = " $ANT_RETURN_CODE
   exit 1
else
echo "Release Build succeeded"
fi


#Copy build to release folder
echo "Moving release .apk to all builds folder"
cp $CAM_WORKSPACE/bin/CameraActivity-release.apk $BUILD_FOLDER


########################
#####Build the Test#####
########################

#Go to the root of the test project
echo "Changing dir to test project : " $CAM_TEST_WORKSPACE
cd $CAM_TEST_WORKSPACE

#Create the build.xml file for ant
echo "Creating build.xml for test"
android update test-project --main $CAM_WORKSPACE --path $CAM_TEST_WORKSPACE

#Build the .apk debug app
echo "Building debug apk"
ant debug
ANT_RETURN_CODE=$?
echo "debug test antReturnCode = " $ANT_RETURN_CODE

#Return non-zero to tell hudson that build failed
if [ $? -ne 0 ]; then
echo "Debug Test Build Failed : antReturnCode = " $ANT_RETURN_CODE
   exit 1
else
echo "Debug Test Build succeeded"
fi

#Copy build to debug folder
echo "Moving debug .apk to all builds folder"
cp $CAM_TEST_WORKSPACE/bin/CameraActivityTest-debug.apk $BUILD_FOLDER


#Build the .apk release app
echo "Building release apk"
ant release

ANT_RETURN_CODE=$?
echo "release test antReturnCode = " $ANT_RETURN_CODE

#Return non-zero to tell hudson that build failed
if [ $? -ne 0 ]; then
echo "Release Test Build Failed : antReturnCode = " $ANT_RETURN_CODE
   exit 1
else
echo "Release Test Build succeeded"
fi

#Copy build to release folder
echo "Moving release .apk to all builds folder"
cp $CAM_TEST_WORKSPACE/bin/CameraActivityTest-release.apk $BUILD_FOLDER

echo "==============================="
echo "===========FINISHIED==========="
echo "==============================="
runtests(){
    adb shell am instrument -w -e class com.aptina.test.functional.CameraActivityFunctionalTests com.aptina.test/com.neenbedankt.android.test.InstrumentationTestRunner
    getresultfile CameraFunctionalTest;
    adb shell am instrument -w -e class com.aptina.test.functional.VideoActivityFunctionalTests com.aptina.test/com.neenbedankt.android.test.InstrumentationTestRunner
    getresultfile VideoFunctionalTest;

}

TESTRESULT_DIR=$BUILD_FOLDER/"TestResult"
FAIL_CODE=0

mkdir $TESTRESULT_DIR

getresultfile(){
cd $TESTRESULT_DIR
adb pull /sdcard/CameraAppTest/ .
adb pull /sdcard/Robotium-Screenshots/ .

cp CameraAppTestLog.xml $1.xml
#Clean out the file dir, or next test won't save override
adb shell rm /sdcard/CameraAppTest/*.*
adb shell rm /sdcard/Robotium-Screenshots/*.*
rm CameraAppTestLog.xml

#check fail test case from xml file
fail_count=$(grep -c "failure" $1.xml)
XML_RETURN_CODE=$?
if [ $XML_RETURN_CODE -ne 0 ]; then
echo "==========" $1 "Passed ============="
else
echo $1 "Failed XML return code = " $XML_RETURN_CODE
echo $1 "Failed Test Case = " $fail_count
FAIL_CODE=1
fi

cd $BUILD_FOLDER
}
cleanup(){
echo "=== Cleanup previously installed applications ==="
    adb uninstall com.aptina >/dev/null
    adb uninstall com.aptina.test > /dev/null
  
    clearCamDir;

}

clearCamDir(){
echo "=== Removing all images in DCIM/Camera ==="
    adb shell rm -r /sdcard/DCIM/Camera/*
}

report(){
if [ $FAIL_CODE -ne 0 ]; then
echo "========= There are failed test cases, exit build with error =========="
    exit 1
fi
}
#========= Command Line ===========
echo "=============== Start Aptina CameraApp Test Run ==============="
echo "======= Test results are stored in CameraAppTestLog.xml ======="

DEVICE_FOUND=false
#Nexus S
DEVICE_ID=016B756D09012003
#BlazeTablet 
#DEVICE_ID=01499EF91200B008

adb "wait-for-device"

#Make sure that adb start-server has already finished before this call
DEVICE=`adb devices | tail -n+2 | awk '{print $1}'`

echo "DEVICE ID : " $DEVICE  

#Go into the directory with the .apk files
cd $BUILD_FOLDER


cleanup;

echo "====== Install Aptina Camera App ======"
adb install CameraActivity-release.apk

ADB_RETURN_CODE=$?
#Return non-zero to tell hudson that install failed
if [ $? -ne 0 ]; then
echo "Camera Activity install failed : ADB_RETURN_CODE = " $ADB_RETURN_CODE
   exit 1
else
echo "Camera Activity install succeeded"
fi


echo "====== Install Aptina Camera App Tests ======"
adb install CameraActivityTest-release.apk

ADB_RETURN_CODE=$?
#Return non-zero to tell hudson that install failed
if [ $? -ne 0 ]; then
echo "Camera Activity install failed : ADB_RETURN_CODE = " $ADB_RETURN_CODE
   exit 1
else
echo "Camera Activity Test install succeeded"
fi

runtests; clearCamDir;



echo "====== Completed Aptina Camera App Test Run ======"

echo "================= Starting Monkey Tests ================="
echo "Changing directory to TESTRESULT_DIR : " $TESTRESULT_DIR
cd $TESTRESULT_DIR
echo "adb shell monkey -p com.aptina --pct-touch 25 --pct-motion 25 --pct-syskeys 10 --pct-appswitch 10 --pct-anyevent 30 -s 99  -v 10000"
adb shell monkey -p com.aptina --pct-touch 25 --pct-motion 25 --pct-syskeys 10 --pct-appswitch 3 --pct-anyevent 37 -s 99  -v 10000 > monkey.txt

ADB_RETURN_CODE=$?
#Return non-zero to tell hudson that install failed
if [ $? -ne 0 ]; then
echo "Monkey Test failed : ADB_RETURN_CODE = " $ADB_RETURN_CODE
FAIL_CODE=1
else
echo "Monkey Test succeeded"
fi
echo "================= Finished Monkey Tests ================="
cleanup;

#Report if any test failed
report;

#Reboot the device
echo "=================== Re-booting device ==================="
adb reboot

echo "================= Finished Build Script ================="
