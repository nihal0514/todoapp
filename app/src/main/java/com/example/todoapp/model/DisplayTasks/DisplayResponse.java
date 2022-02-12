package com.example.todoapp.model.DisplayTasks;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DisplayResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("success")
	private boolean success;

	@SerializedName("count")
	private int count;

	@SerializedName("todos")
	private List<TodosItem> todos;

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

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setTodos(List<TodosItem> todos){
		this.todos = todos;
	}

	public List<TodosItem> getTodos(){
		return todos;
	}
}