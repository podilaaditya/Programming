/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.aptina.util;

import java.util.List;

import com.aptina.camera.CameraActivity;
import com.aptina.camera.GridGalleryActivity;
import com.aptina.camera.VideoActivity;

import android.annotation.TargetApi;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.StrictMode;

/**
 * Class containing some static utility methods.
 */
public class Utils {
    private Utils() {};

    @TargetApi(11)
    public static void enableStrictMode() {
        if (Utils.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            if (Utils.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                vmPolicyBuilder
                        .setClassInstanceLimit(GridGalleryActivity.class, 1)
                        .setClassInstanceLimit(CameraActivity.class, 1)
                        .setClassInstanceLimit(VideoActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

//    public static boolean hasJellyBean() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
//    }
    
    /**
     * Check if package is installed and callable
     */
    public static boolean doesPackageExist(String targetPackage, List<ApplicationInfo> packs){
            for (ApplicationInfo packageInfo : packs) {
            	if(packageInfo.packageName.equals(targetPackage)){

            		return true;
            	}
        }        
        return false;
    }
}
