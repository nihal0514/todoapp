package com.example.todoapp.model.Auth;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse{

	@SerializedName("success")
	private boolean success;

	@SerializedName("token")
	private String token;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}
}