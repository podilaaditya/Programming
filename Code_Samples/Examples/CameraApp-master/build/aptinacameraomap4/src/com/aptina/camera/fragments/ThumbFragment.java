/**
 * 
 */
package com.aptina.camera.fragments;

import java.util.List;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.aptina.R;
import com.aptina.camera.components.ThumbnailControl;
import com.aptina.logger.Logger;
import com.aptina.util.Utils;

/**
 * @author stoyan
 *
 */
public class ThumbFragment extends Fragment{
	/**
	 * Logging tag
	 */
	private static final String TAG = "ThumbFragment";
	
	/**
     * Thumbnail control.
     */
    private ThumbnailControl mGallery;
    
    /**
     * PackageManager to check what gallery packages there are to use
     */
    private PackageManager myPackMan = null;
	
	/**
	 * Instantiate the fragment with the {@link #setArguments(Bundle)} called 
	 */
	public static final ThumbFragment newInstance()
	{
		ThumbFragment frag = new ThumbFragment();
	    return frag;
	}
	/**
	 * Set all the arguments for the fragment and initialize the graph
	 */
	@Override
	public void setArguments(Bundle bundle) {
		if(bundle != null){
			
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPackMan = this.getActivity().getPackageManager();
        
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		final View fragV = inflater.inflate(R.layout.thumbnail_fragment, container, false);
		mGallery = (ThumbnailControl) fragV.findViewById(R.id.gallery_thumbnail);
        return fragV;
    }
	@Override
    public void onResume() {
        super.onResume();
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}
	
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
	/**
     * Gallery view click listener.
     */
    private OnClickListener mGalleryListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
        	try {
                Uri uri = getGalleryURI();
                Intent showImageOrVideo = null;
                List<ApplicationInfo> packs = myPackMan.getInstalledApplications(0);        
                if (uri != null) {
                	//Check to see what activities can display our images, use default android if non of them exist/are-installed
                    if(Utils.doesPackageExist("com.cooliris.media", packs)){
                  		showImageOrVideo = new Intent("com.cooliris.media.action.REVIEW", uri);
                  	}else if(Utils.doesPackageExist("com.htc.album", packs)){
                  		showImageOrVideo = new Intent("com.htc.album.action.VIEW_PHOTO_FROM_CAMERA");
                  		showImageOrVideo.setDataAndType(Uri.withAppendedPath(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "1"), "image/*");
                  	}else{
                  		Log.i(TAG,"default com.android.camera");
                  		showImageOrVideo = new Intent("com.android.camera.action.REVIEW", uri);
                  	}

                  	startActivity(showImageOrVideo);
                }
            } catch (Exception e) {
            	Logger.logApplicationException(e, "mGallery: could not open gallery");
            }
        }
    };
    private Uri getGalleryURI(){
    	return mGallery.getUri();
    }
    
    public void setGalleryListener(boolean on){
    	if(on){
    		mGallery.setOnClickListener(mGalleryListener);
    	}else{
    		mGallery.setOnClickListener(null);
    	}
    	
    }
    
    public boolean isGalleryNull(){
    	return mGallery == null;
    }
    
    /**
     * Sets new thumbnail.
     * 
     * @param newThumbnail New thumbnail.
     * @param uri Uri to the latest thumbnail.
     */
    public void setGalleryThumbnail(Bitmap newThumbnail, Uri uri){
    	mGallery.setThumbnail(newThumbnail, null);
    }
    
    public void showGallery(){
    	mGallery.show();
    }
    
    public void setGalleryUri(Uri uri){
    	mGallery.setUri(uri);
    }
    
    /**
     * Updates last thumbnail.
     * 
     * @param contentResolver Content resolver.
     * @return 
     */
    public void updateGallery(ContentResolver contentResolver, int type) {   
    	mGallery.update(contentResolver, type);
    }
    
    
}
