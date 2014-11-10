package com.example.doublepictureprototype;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

public class CommunicationManager {       
    
	private static final String URL_RECEIVE_MESSAGE = 
			"http://app.tommysteimel.com/receive.php";
	
	private static final String URL_SEND_MESSAGE = 
			"http://app.tommysteimel.com/send.php";
	
	
    public String GetImageEncodedString(byte[] rawImageData){        
        return Base64.encodeToString(rawImageData, Base64.DEFAULT);
    }
    
    private String GetImageEncodedString(Bitmap image){
    	// Attempt to encode the image
        int[] imageData = null;
        image.getPixels(
        		imageData, 
        		0, 
        		image.getWidth(), 
        		0, 
        		0, 
        		image.getWidth(), 
        		image.getHeight());
        
        if (imageData == null){
        	// Failed to gather pixel data
        	return null;
        }
        
        byte[] rawImageData = new byte[imageData.length];
        
        for (int i = 0; i < imageData.length; i++){
        	rawImageData[i] = (byte)imageData[i];
        }
        
        return Base64.encodeToString(rawImageData, Base64.DEFAULT);
    }
 
    public void TryGetMessage(){   	   	 	    	
    	
    	//Create the HTTP request
    	HttpParams httpParameters = new BasicHttpParams();

    	//Setup timeouts
    	HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
    	HttpConnectionParams.setSoTimeout(httpParameters, 15000);			

    	// Create the HTTP Client
    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
    	HttpPost httppost = new HttpPost(URL_RECEIVE_MESSAGE);
    	
    	JSONObject messageObject = this.generateJSON("testID");
    	
    	// Try to add the message body to the POST request 
    	try {
			httppost.setEntity(new StringEntity(messageObject.toString()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}    	
    	
    	// Try executing the POST request
    	// TODO: Update to run asynchronously
    	HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		// Try to parse the POST request response
    	HttpEntity entity = response.getEntity();
    	String result = null;
		try {
			result = EntityUtils.toString(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}       	
    	
    	// Create a JSON object from the request response
		JSONObject jsonObject = null;
    	try {
			jsonObject = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

    	// Retrieve the data from the JSON object    
    	try {
			String sendingErrors = jsonObject.getString("SentStatus");
			String messageId = jsonObject.getString("MessageID");
	    	String messageStatus = jsonObject.getString("MessageStatus");
	    	String senderUsername = jsonObject.getString("SenderUsername");
	    	String sentTime = jsonObject.getString("SentTime");	
	    	String encodedImageString = jsonObject.getString("image");
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }    
    
    public void SendMessage(Message message){    	
    	//Create the HTTP request
    	HttpParams httpParameters = new BasicHttpParams();

    	//Setup timeouts
    	HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
    	HttpConnectionParams.setSoTimeout(httpParameters, 15000);			

    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
    	HttpPost httppost = new HttpPost(URL_SEND_MESSAGE);
    	
    	JSONObject messageObject = this.generateJSON(message);
    	
    	// Try to add the message body to the POST request 
    	try {		
			httppost.setEntity(new StringEntity(messageObject.toString()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}    	
    	
    	// Try executing the POST request
    	// TODO: Update to run asynchronously
    	HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		if (response == null){
			Log.i("error", "There was a problem in the Http Repsonse. " +
					"Response was null");
			return;
		}
		
		JSONObject jsonObject = this.generateJSON(response);
		
		if (jsonObject == null){
			Log.i("error", "There was a problem generating the JSON object. " +
					"JSON object is null");
			return;
		}

    	// Retrieve the data from the JSON object    
    	try {
			String sendingErrors = jsonObject.getString("SentStatus");
			String messageId = jsonObject.getString("MessageID");
	    	String messageStatus = jsonObject.getString("MessageStatus");
	    	String senderUsername = jsonObject.getString("SenderUsername");
	    	String sentTime = jsonObject.getString("SentTime");	
		} catch (JSONException e) {
			e.printStackTrace();
		}          
    } 
    
    private JSONObject generateJSON(Message message){

    	HashMap<String, String> nameValuePairs = new HashMap<String, String>();
    	nameValuePairs.put("usernameTo", message.getReceiverUsername());
    	nameValuePairs.put("usernameFrom", message.getSenderUsername());
    	
    	String encodedImageString = this.GetImageEncodedString(message.getImageData());
    	
    	if(encodedImageString != null){
    		nameValuePairs.put("image", encodedImageString);
    	}else{
    		nameValuePairs.put("image", "");
    	}     
    	
    	return new JSONObject(nameValuePairs);
    }
    
    private JSONObject generateJSON(HttpResponse response){
    	// Try to parse the POST request response
    	HttpEntity entity = response.getEntity();
    	String result = null;
		try {
			result = EntityUtils.toString(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}       	
    	
    	// Create a JSON object from the request response
		JSONObject jsonObject = null;
    	try {
			jsonObject = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	return jsonObject;
    }
    
    private JSONObject generateJSON(String sessionToken){
    	HashMap<String, String> nameValuePairs = new HashMap<String, String>();
    	nameValuePairs.put("messageID", sessionToken);     
    	
    	return new JSONObject(nameValuePairs);
    }
}
