package com.example.todoapp.data;

import com.example.todoapp.model.Auth.LoginRequest;
import com.example.todoapp.model.Auth.LoginResponse;
import com.example.todoapp.model.Auth.RegisterRequest;
import com.example.todoapp.model.Auth.RegisterResponse;
import com.example.todoapp.model.DisplayTasks.DisplayResponse;
import com.example.todoapp.model.Tasks.TasksRequest;
import com.example.todoapp.model.Tasks.TasksResponse;

import java.util.HashMap;

import okhttp3.internal.concurrent.Task;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST(" ")
    Call<TasksResponse> createTask(@Header("Authorization") String authorization, @Body TasksRequest tasksRequest);

    @PUT("{id}")
    Call<TasksResponse> updateTask(@Path("id") String id, @Body TasksRequest tasksRequest);

    @PUT("{id}")
    Call<TasksResponse> finishedTask(@Path("id") String id, @Body TasksRequest tasksRequest);

    @DELETE("{id}")
    Call<TasksResponse> deleteTask(@Path("id") String id);

    @GET(" ")
    Call<DisplayResponse> displayResponse(@Header("Authorization") String authorization);


    @GET("finished")
    Call<DisplayResponse> displayFinishedTask(@Header("Authorization") String authorization);
}
