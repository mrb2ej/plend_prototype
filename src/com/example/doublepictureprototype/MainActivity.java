package com.example.doublepictureprototype;

import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private CameraActivity cameraActivity;	
	private CommunicationManager communicator;
	private static final int CAMERA_INTENT_REQUEST = 0;
	
	public void StartCameraClick(View target){		
		Intent cameraIntent = new Intent(this, CameraActivity.class);
		//startActivity(cameraIntent);
		startActivityForResult(cameraIntent, CAMERA_INTENT_REQUEST);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
		Log.i("info", "Made it back to MainActivity");
		
		if (requestCode == CAMERA_INTENT_REQUEST) {
			
			Log.i("info", "Camera intent request successfull");
			
            if (resultCode == RESULT_OK) {
            	
            	Log.i("info", "Attempting to parse image");
            	
            	byte[] imageData = data.getByteArrayExtra(CameraActivity.IMAGE_DATA_KEY);            	
                
                if (imageData != null && imageData.length > 0){
                	
                	Log.i("info", "Successfully retrieved camera data. Creating message");
                	communicator.SendMessage(
                			new Message(
                					"bongiovimatthew", 
                					"tommysteimel", 
                					imageData, 
                					new MessageMetaData()));
                	
                	Log.i("info", "Message sent!");
                	
                }
            }
        }
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);	
	    
	    communicator = new CommunicationManager();
	}	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }   
}
