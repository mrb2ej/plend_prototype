package com.example.doublepictureprototype;

import android.hardware.Camera;
import android.util.Log;

public class CustomPictureCallback implements Camera.PictureCallback{	
	
	private CameraActivity parentActivity;
	
	public CustomPictureCallback(CameraActivity parent){
		this.parentActivity = parent;
	}
	
	@Override
    public void onPictureTaken(byte[] data, Camera camera) {				
		Log.i("info", "PICTURE CAPTURED!");
		parentActivity.SetImageData(data);			
    }
}
