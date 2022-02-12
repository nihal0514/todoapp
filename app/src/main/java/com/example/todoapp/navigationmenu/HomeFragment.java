package com.example.todoapp.navigationmenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.Adapters.TodoListAdapter;
import com.example.todoapp.Interfaces.RecyclerViewClickListener;
import com.example.todoapp.MainActivity;
import com.example.todoapp.R;
import com.example.todoapp.UtilsService.SharedPreferenceClass;
import com.example.todoapp.data.ApiClient;
import com.example.todoapp.login.RegisterActivity;
import com.example.todoapp.model.Auth.RegisterResponse;
import com.example.todoapp.model.DisplayTasks.DisplayResponse;
import com.example.todoapp.model.DisplayTasks.TodosItem;
import com.example.todoapp.model.Tasks.TasksRequest;
import com.example.todoapp.model.Tasks.TasksResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements RecyclerViewClickListener {
    FloatingActionButton floatingActionButton;
    SharedPreferenceClass sharedPreferenceClass;
    String token;

    TodoListAdapter todoListAdapter;
    RecyclerView recyclerView;
    List<TodosItem> todosItemList;

    TextView empty_tv;
    ProgressBar progressBar;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferenceClass = new SharedPreferenceClass(getContext());
        token = sharedPreferenceClass.getValue_string("token");
        progressBar = view.findViewById(R.id.progress_bar);

        floatingActionButton = view.findViewById(R.id.add_task_btn);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        todosItemList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        todoListAdapter = new TodoListAdapter(todosItemList, getActivity(), HomeFragment.this);
        recyclerView.setAdapter(todoListAdapter);

        getTasks();
        return view;
    }

    private void getTasks() {
        Call<DisplayResponse> getTasksCall = ApiClient.getApiService().displayResponse(token);

        getTasksCall.enqueue(new Callback<DisplayResponse>() {
            @Override
            public void onResponse(Call<DisplayResponse> call, Response<DisplayResponse> response) {
                if (response.body().isSuccess()) {
                    todosItemList.clear();
                    todosItemList.addAll(response.body().getTodos());
                    todoListAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DisplayResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_dialog_layout, null);

        final EditText title_field = alertLayout.findViewById(R.id.title);
        final EditText description_field = alertLayout.findViewById(R.id.description);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(alertLayout)
                .setTitle("Add Task")
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInter) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = title_field.getText().toString();
                        String description = description_field.getText().toString();
                        if (!TextUtils.isEmpty(title)) {
                            TasksRequest tasksRequest = new TasksRequest();
                            tasksRequest.setTitle(title_field.getText().toString());
                            tasksRequest.setDescription(description_field.getText().toString());
                            addTask(tasksRequest);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "Please enter title...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();

    }
    private void showFinishedTaskDialog(String id, int position) {
        TasksRequest tasksRequest= new TasksRequest();
        tasksRequest.setFinished(true);
        UpdateTaskTodo(id,position,tasksRequest);

    }

    private void UpdateTaskTodo(String id, int position, TasksRequest tasksRequest) {
        Call<TasksResponse> tasksResponseCall = ApiClient.getApiService().finishedTask(id,tasksRequest);

        tasksResponseCall.enqueue(new Callback<TasksResponse>() {
            @Override
            public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {
                if (response.body().isSuccess()) {
                 //   todosItemList.clear();
                    todosItemList.remove(position);
                //    todoListAdapter.notifyDataSetChanged();
                    String message = "Successful....";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    getTasks();


                } else {
                    String message = "Sorry";
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<TasksResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void addTask(TasksRequest tasksRequest) {
        Call<TasksResponse> tasksResponseCall = ApiClient.getApiService().createTask(token, tasksRequest);

        tasksResponseCall.enqueue(new Callback<TasksResponse>() {
            @Override
            public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {
                if (response.body().isSuccess()) {

                    String message = "Successful....";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    getTasks();


                } else {
                    String message = "Sorry";
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<TasksResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void showUpdateDialog(String id, String title, String description) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_dialog_layout, null);

        final EditText title_field = alertLayout.findViewById(R.id.title);
        final EditText description_field = alertLayout.findViewById(R.id.description);

        title_field.setText(title);
        description_field.setText(description);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(alertLayout)
                .setTitle("Update Task")
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInter) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = title_field.getText().toString();
                        String description = description_field.getText().toString();
                        if (!TextUtils.isEmpty(title)) {
                            TasksRequest tasksRequest = new TasksRequest();
                            tasksRequest.setTitle(title_field.getText().toString());
                            tasksRequest.setDescription(description_field.getText().toString());
                            UpdateTask(id, tasksRequest);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "Please enter title...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();

    }

    private void UpdateTask(String id, TasksRequest tasksRequest) {
        Call<TasksResponse> tasksResponseCall = ApiClient.getApiService().updateTask(id, tasksRequest);

        tasksResponseCall.enqueue(new Callback<TasksResponse>() {
            @Override
            public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {
                if (response.body().isSuccess()) {

                    String message = "Successful....";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    getTasks();


                } else {
                    String message = "Sorry";
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<TasksResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void deleteTodo(String id, int position) {
        Call<TasksResponse> tasksResponseCall = ApiClient.getApiService().deleteTask(id);

        tasksResponseCall.enqueue(new Callback<TasksResponse>() {
            @Override
            public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {
                if (response.body().isSuccess()) {

                    todosItemList.remove(position);
                    String message = "Successful....";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    getTasks();


                } else {
                    String message = "Sorry";
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<TasksResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(), "Position " + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLongItemClick(int position) {
        showUpdateDialog(todosItemList.get(position).getId(), todosItemList.get(position).getTitle(), todosItemList.get(position).getDescription());
        Toast.makeText(getActivity(), "Position " + position, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onEditButtonClick(int position) {
        showUpdateDialog(todosItemList.get(position).getId(), todosItemList.get(position).getTitle(), todosItemList.get(position).getDescription());

    }

    @Override
    public void onDeleteButtonClick(int position) {

        deleteTodo(todosItemList.get(position).getId(), position);


    }


    @Override
    public void onDoneButtonClick(int position) {
        showFinishedTaskDialog(todosItemList.get(position).getId(), position);

    }
}