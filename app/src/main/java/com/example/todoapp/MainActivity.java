package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.UtilsService.SharedPreferenceClass;
import com.example.todoapp.data.ApiClient;
import com.example.todoapp.login.LoginActivity;
import com.example.todoapp.model.Auth.LoginResponse;
import com.example.todoapp.navigationmenu.FinishedTaskFragment;
import com.example.todoapp.navigationmenu.HomeFragment;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    SharedPreferenceClass sharedPreferenceClass;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private TextView user_name, user_email;
    private CircleImageView userImage;

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferenceClass = new SharedPreferenceClass(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View hdView = navigationView.getHeaderView(0);
        user_name = (TextView) hdView.findViewById(R.id.username);
        user_email = (TextView) hdView.findViewById(R.id.user_email);
        userImage = (CircleImageView) hdView.findViewById(R.id.avatar);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                setDrawerClick(item.getItemId());
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

        initDrawer();

    }

    private void initDrawer() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content, new HomeFragment());
        ft.commit();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setDrawerClick(int itemId) {
        switch (itemId) {
            case R.id.action_finished_task:
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new FinishedTaskFragment()).commit();
                break;
            case R.id.action_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new HomeFragment()).commit();
                break;
            case R.id.action_logout:
                sharedPreferenceClass.clear();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                String shareBody = "Hey try this to do app, it uses permanent saving of your task.";

                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Via"));

                return true;

            case R.id.refresh_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new HomeFragment()).commit();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}