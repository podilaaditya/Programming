/**
 * Copyright (C) 2011 Qualcomm Retail Solutions, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information of Qualcomm
 * Retail Solutions, Inc.
 *
 * The following sample code illustrates various aspects of the ContextConnector SDK.
 *
 * The sample code herein is provided for your convenience, and has not been
 * tested or designed to work on any particular system configuration. It is
 * provided AS IS and your use of this sample code, whether as provided or with
 * any modification, is at your own risk. Neither Qualcomm Retail Solutions, Inc. nor any
 * affiliate takes any liability nor responsibility with respect to the sample
 * code, and disclaims all warranties, express and implied, including without
 * limitation warranties on merchantability, fitness for a specified purpose,
 * and against infringement.
 */

How to run the Android sample application

1/ Pre-requisites

1.1/ Java

You must have a JVM installed (1.6 and above).

1.2/ Eclipse

You must have Eclipse installed - recommended version Indigo
Additional eclipse plugins required:
 * Android development tools Version 17 - refer to Google documentation at
   http://developer.android.com/sdk/eclipse-adt.html#installing

1.3/ Required open source

The Gimbal SDK depends on the following Open Source libraries which exist under context-libs/lib:

    spring-android-rest-template-1.0.0.M2.jar
    jackson-mapper-asl-1.8.0.jar
    jackson-core-asl-1.8.0.jar

If you require Image Recognition refer to the Gimbal SDK documentation

1.4/ Android device running at least 2.1

Android device must also be in development mode
Preferably gps enabled and data connectivity

2/ Running the android sample application

2.1 Eclipse setup

Create a clean eclipse workspace
Configure the workspace to point at the installed Android SDK home
Configure the workspace to be Java SDK 1.6 compliant

2.2. Import the three required projects:

    context-libs
    client-context-services-ir-apklib
    client-sample-mall-mart-embed

ADT may not properly determine the dependencies between projects. If you get compile errors or if, when running
the sample application, you get an undefined class (ususlly LogConfig), quit and re-open eclipse

2.3 Run the application

Connect a device by usb
Run a project clean
Run the sample application with the device connected

Known Issues
- When running Eclipse with Android development tools prior to version 17
        * you must rename the libs directory to lib
        * click Project->Properties->Java Build Path->libraries tab and click "add jars" selecting the above 3 jars from the lib directory



