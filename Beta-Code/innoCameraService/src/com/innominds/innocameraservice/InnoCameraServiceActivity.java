package com.innominds.innocameraservice;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.lang.InterruptedException;

//Custom clases 
import com.innominds.innocameraservice.camera.CameraPreview;
import com.innominds.innocameraservice.interfacestub.InnoAlljoynCamInterFace;
import com.innominds.innocameraservice.InnoCameraServiceApplication;


/*
This Activity will recieve the activity launch initent and the main intent from 
Application grid and this would be prefaced by the Application Class and there i would triger the 
Alljoyn Service and then it would launch the 
*/
public class InnoCameraServiceActivity extends Activity {

    private static final String LOG_TAG = "InnoCameraServiceActivity";
    private CameraPreview mPreview;
    private RelativeLayout mLayout;
    public  PreviewFrameReciever mPreviewFrameReciever =  new PreviewFrameReciever();
    public  InnoCameraServiceApplication mInnoServAppInstance;

    //Creating Application class instance here to get a hanle to call the 
    //notification and pushing data to the clients in the notification 
    //

    public class  PreviewFrameReciever implements CameraPreview.PreviewFrameCallBack {

        public void onPreviewFrameReady(byte[] previewFrame) {

            // Here i would call the Application class 
            // i need to send this as a copy to the Application class
            // from there i will send to service immediately ... with out delay
            if(mInnoServAppInstance != null) {
                mInnoServAppInstance.newPreviewFrameMessage(previewFrame);
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //I would create the view at runtime and then i woul the set layout to setcontentview
        //setContentView(R.layout.main);
        mInnoServAppInstance = (InnoCameraServiceApplication)getApplication();
        mInnoServAppInstance.checkin();
        try {
            Thread.sleep(100);
        }
        catch ( InterruptedException ex) {
            Log.i(LOG_TAG,"Exception ::"+ex);
        }
        mLayout = new RelativeLayout(this);
        setContentView(mLayout);



    }

    @Override 
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.stop();
            mLayout.removeView(mPreview);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Then continue with what i want to do we will create the preview here and continue with 
        //starting the preview
        // Set the second argument by your choice.
        // Usually, 0 for back-facing camera, 1 for front-facing camera.
        // If the OS is pre-gingerbreak, this does not have any effect.
        mPreview = new CameraPreview(this, 0, CameraPreview.LayoutMode.FitToParent);
        mPreview.setOnPreviewFrameCallBack(mPreviewFrameReciever);
        mPreview.setPreviewCallback(true);
        LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, 
            LayoutParams.WRAP_CONTENT); 
        mLayout.addView(mPreview, 0, previewLayoutParams);
                           
    }

    @Override
    protected void onPause() {
        //mPreview.setPreviewCallback(false);
        mPreview.stop();
        mPreview.removeOnPreviewFrameCallBack(true);
        mLayout.removeView(mPreview); // This is necessary.
        mPreview = null;
        //Do what i need then i call
        super.onPause();

    }




}
