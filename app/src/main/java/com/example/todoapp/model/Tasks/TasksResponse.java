package com.example.todoapp.model.Tasks;

import com.google.gson.annotations.SerializedName;

public class TasksResponse{

	@SerializedName("todo")
	private Todo todo;

	@SerializedName("msg")
	private String msg;

	@SerializedName("success")
	private boolean success;

	public void setTodo(Todo todo){
		this.todo = todo;
	}

	public Todo getTodo(){
		return todo;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}
}