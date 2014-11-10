package com.example.doublepictureprototype;


import java.lang.String;
import android.graphics.Bitmap;

public class Message {
	
	private String usernameTo;
	private String usernameFrom;	
	private byte[] imageData;
	private MessageMetaData metadata;
	
	public Message(){
		
	}
	
	public Message(
			String usernameTo, 
			String usernameFrom, 
			byte[] imageData, 
			MessageMetaData metadata){
		
		this.usernameTo = usernameTo;
		this.usernameFrom = usernameFrom;
		this.imageData = imageData;
		this.metadata = metadata;
	}
	
	public String getReceiverUsername() {
		return usernameTo;
	}
	
	public String getSenderUsername() {
		return usernameFrom;
	}
	
	public void setImageData(byte[] rawImageData){
		this.imageData = rawImageData;
	}
	
	public byte[] getImageData(){
		return this.imageData;
	}
	
	
}
