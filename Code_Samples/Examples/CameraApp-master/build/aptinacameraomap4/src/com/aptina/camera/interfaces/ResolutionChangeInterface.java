package com.aptina.camera.interfaces;

import android.hardware.Camera.Size;

public interface ResolutionChangeInterface {
    /**
     * Invokes when resolution is selected.
     * 
     * @param resolution Selected resolution.
     * @param index Index of selected element.
     */
    public void onResolutionSelected(Size resolution, int index);

    /**
     * Retrieves current resolution.
     * 
     * @return current resolution.
     */
    public Size getCurrentResolution();
}