package com.shekhar.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

public class Homepage extends AppCompatActivity {

    private Button register,complaintstatus,stockverification,Bid;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_homepage);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationview);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        register = findViewById(R.id.register);
        complaintstatus = findViewById(R.id.complaintstatus);
        stockverification = findViewById(R.id.stock_verification);
        Bid = findViewById(R.id.Bid);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this,Home.class);
                startActivity(intent);
            }
        });
        complaintstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this,Complaints.class);
                startActivity(intent);
            }
        });
        stockverification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this,Stockverification.class);
                startActivity(intent);
            }
        });
        Bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this,Bid.class);
                startActivity(intent);
            }
        });






        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }


        }




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Log.i("MENU_DRAWER_TAG","Home item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);

                        Intent intent = new Intent(Homepage.this,Homepage.class);
                        startActivity(intent);
                    case R.id.nav_notifications:
                        Log.i("MENU_DRAWER_TAG","Notifications item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                    case R.id.nav_status:
                        Log.i("MENU_DRAWER_TAG","Status item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);

                    case R.id.nav_settings:
                        Log.i("MENU_DRAWER_TAG","Settings item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(Homepage.this);
                        builder.setTitle("Logout");
                        builder.setMessage("Are u sure u want to logout?");
                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Homepage.this,LoginPage.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();




                }
                return true;
            }
        });

    }


}
