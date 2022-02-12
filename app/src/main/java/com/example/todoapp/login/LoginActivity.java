package com.example.todoapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.todoapp.MainActivity;
import com.example.todoapp.R;
import com.example.todoapp.UtilsService.SharedPreferenceClass;
import com.example.todoapp.UtilsService.UtilService;
import com.example.todoapp.data.ApiClient;
import com.example.todoapp.model.Auth.LoginRequest;
import com.example.todoapp.model.Auth.LoginResponse;
import com.example.todoapp.model.Auth.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button registerBtn;
    private Button loginBtn;
    private EditText email_ET, password_ET;
    ProgressBar progressBar;

    private String email, password;
    UtilService utilService;
    SharedPreferenceClass sharedPreferenceClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginBtn);
        email_ET = findViewById(R.id.email_ET);
        password_ET = findViewById(R.id.password_ET);
        progressBar = findViewById(R.id.progress_bar);
        utilService = new UtilService();
        sharedPreferenceClass = new SharedPreferenceClass(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilService.hideKeyboard(view, LoginActivity.this);
                if(validate(view)) {
                    LoginRequest loginRequest= new LoginRequest();
                    loginRequest.setEmail(email_ET.getText().toString());
                    loginRequest.setPassword(password_ET.getText().toString());
                    login(loginRequest);
                }
            }
        });
    }
    private void login(LoginRequest loginRequest){
        Call<LoginResponse> loginResponseCall= ApiClient.getApiService().loginUser(loginRequest);

        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.body().isSuccess()){
                    String token = response.body().getToken();
                    sharedPreferenceClass.setValue_string("token", token);

                    String message= "Successful....";
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                }else{
                    String message= "User Not Register, please Register";
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String message= t.getLocalizedMessage();
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private boolean validate(View view) {
        boolean isValid;

        if(!TextUtils.isEmpty(email)) {
            if(!TextUtils.isEmpty(password)) {
                isValid = true;
            } else {
                utilService.showSnackBar(view,"please enter password....");
                isValid = false;
            }
        } else {
            utilService.showSnackBar(view,"please enter email....");
            isValid = false;
        }

        return  isValid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences todo_pref = getSharedPreferences("user_todo", MODE_PRIVATE);
        if(todo_pref.contains("token")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}