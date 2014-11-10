package com.example.doublepictureprototype;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class CameraActivity extends Activity {
	
	public static final String IMAGE_DATA_KEY = "Image Data";

	private Camera frontCamera;
	private Camera backCamera;

	private CameraPreview frontPreview;
	private CameraPreview backPreview;


	private PictureCallback frontPictureCallback = new CustomPictureCallback(this);
	private PictureCallback backPictureCallback = new CustomPictureCallback(this);

	public void captureButtonClicked(View target) {
		// get an image from the camera
		frontCamera.takePicture(null, null, frontPictureCallback);
		// backCamera.takePicture(null, null, backPictureCallback);
	}

	public void SetImageData(byte[] data) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra(IMAGE_DATA_KEY, data);
		setResult(RESULT_OK, returnIntent);
		
		this.releaseCamera();
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		// Create both camera instances
		// frontCamera = getCameraInstance(Camera.CameraInfo.CAMERA_FACING_FRONT);
		// backCamera = getCameraInstance(Camera.CameraInfo.CAMERA_FACING_BACK);
		this.InitializeCameraInstance();

		// Create two previews and set them as the content of our activity.
		frontPreview = new CameraPreview(this, frontCamera);
		// backPreview = new CameraPreview(this, backCamera);

		FrameLayout frontPreviewFrame = (FrameLayout) findViewById(R.id.front_camera_preview);
		frontPreviewFrame.addView(frontPreview);

		// FrameLayout backPreviewFrame = (FrameLayout)findViewById(R.id.back_camera_preview);
		// backPreviewFrame.addView(frontPreview);

	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera();
	}

	private void releaseCamera() {
		if (frontCamera != null) {
			frontCamera.release(); // release the camera for other applications
			frontCamera = null;
		}

		if (backCamera != null) {
			backCamera.release();
			backCamera = null;
		}
	}

	/** Check if this device has at least two cameras */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return Camera.getNumberOfCameras() >= 2;
		} else {
			// no camera on this device
			return false;
		}
	}

	// Caution: On some devices, this method may take a long time to complete.
	// It is best to call this method from a worker thread (possibly using
	// AsyncTask)
	// to avoid blocking the main application UI thread.
	public static Camera getCameraInstance(int cameraType) {
		Camera c = null;
		try {
			c = Camera.open(cameraType); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	private void InitializeCameraInstance() {

		if (!this.checkCameraHardware(this.getApplicationContext())) {
			// The device does not meet the requirements for
			// capturing images.
			return;
		}

		/*
		 * BUG: The camera information is causing a problem
		 * 
		 * Camera.CameraInfo frontCameraInformation = null; Camera.CameraInfo
		 * backCameraInformation = null;
		 * 
		 * Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT,
		 * frontCameraInformation);
		 * Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK,
		 * backCameraInformation);
		 * 
		 * if(frontCameraInformation == null || backCameraInformation = null){
		 * // Something happened and we are not getting information on both
		 * cameras. return; }
		 */

		// this.backCamera =
		// getCameraInstance(Camera.CameraInfo.CAMERA_FACING_BACK);
		this.frontCamera = getCameraInstance(Camera.CameraInfo.CAMERA_FACING_FRONT);
	}	
}
