package com.example.todoapp.navigationmenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.Adapters.FinishedTodoListAdapter;
import com.example.todoapp.Adapters.TodoListAdapter;
import com.example.todoapp.Interfaces.RecyclerViewClickListener;
import com.example.todoapp.R;
import com.example.todoapp.UtilsService.SharedPreferenceClass;
import com.example.todoapp.data.ApiClient;
import com.example.todoapp.model.DisplayTasks.DisplayResponse;
import com.example.todoapp.model.DisplayTasks.TodosItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinishedTaskFragment extends Fragment implements RecyclerViewClickListener {

    SharedPreferenceClass sharedPreferenceClass;
    String token;

    FinishedTodoListAdapter  finishedtodoListAdapter;
    RecyclerView recyclerView;
    List<TodosItem> todosItemList;

    TextView empty_tv;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_finished_task, container, false);
        sharedPreferenceClass = new SharedPreferenceClass(getContext());
        token = sharedPreferenceClass.getValue_string("token");
        progressBar = view.findViewById(R.id.finished_progress_bar);


        recyclerView = view.findViewById(R.id.finished_recycler_view);
        todosItemList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
         finishedtodoListAdapter = new FinishedTodoListAdapter(todosItemList, getActivity(), FinishedTaskFragment.this);
        recyclerView.setAdapter( finishedtodoListAdapter);

        getTasks();
        return view;
    }
    private void getTasks() {
        Call<DisplayResponse> getTasksCall = ApiClient.getApiService().displayFinishedTask(token);

        getTasksCall.enqueue(new Callback<DisplayResponse>() {
            @Override
            public void onResponse(Call<DisplayResponse> call, Response<DisplayResponse> response) {
                if (response.body().isSuccess()) {
                    todosItemList.clear();
                    todosItemList.addAll(response.body().getTodos());
                    finishedtodoListAdapter.notifyDataSetChanged();

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

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void onEditButtonClick(int position) {

    }

    @Override
    public void onDeleteButtonClick(int position) {

    }

    @Override
    public void onDoneButtonClick(int position) {

    }
}