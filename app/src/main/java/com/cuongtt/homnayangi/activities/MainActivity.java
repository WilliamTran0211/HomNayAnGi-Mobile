package com.cuongtt.homnayangi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.fragments.ContainerFragment;
import com.cuongtt.homnayangi.fragments.HomeFragment;
import com.cuongtt.homnayangi.fragments.SearchFragment;
import com.cuongtt.homnayangi.fragments.UserFragment;
import com.cuongtt.homnayangi.models.RecipesIngredients;
import com.cuongtt.homnayangi.models.Users;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActionBar toolbar;
    private ExtendedFloatingActionButton fab;
    private ChipNavigationBar cnb;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initPermission();

        cnb = findViewById(R.id.navigation);

        if(savedInstanceState == null){
            cnb.setItemSelected(R.id.mnu_home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.frame_container, homeFragment).commit();

        }

        Gson gson = new Gson();
        Intent intent = getIntent();
        final String json = intent.getStringExtra("UserInfo");
        Users obj = gson.fromJson(json, Users.class);

        fab = findViewById(R.id.floating_button);

        fab.setOnClickListener(fabAddRecipe);
        fab.setText("Thêm công thức");


        cnb.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;

                Bundle UserInfoBundle = new Bundle();
                UserInfoBundle.putString("UserInfo", json);
                fab.setVisibility(View.VISIBLE);

                switch (i) {
                    case R.id.mnu_home:
                        Log.d("DEBUGGGGG","Bottom navigation HOME click");
                        fragment = new HomeFragment();
                        fab.setOnClickListener(fabAddRecipe);
                        fab.setText("Thêm công thức");
                        break;
                    case R.id.mnu_search:
                        Log.d("DEBUGGGGG","Bottom navigation SEARCH click");
                        fragment = new SearchFragment();
                        fab.setVisibility(View.GONE);
                        break;
                    case R.id.mnu_user:
                        Log.d("DEBUGGGG", "Bottom navigation USER click");
                        fragment = new UserFragment();
                        fragment.setArguments(UserInfoBundle);
                        fab.setVisibility(View.GONE);
                        break;
                    case R.id.mnu_container:
                        Log.d("DEBUGGGG", "Bottom navigation UserContainer click");
                        fragment = new ContainerFragment();
                        fab.setText("Thêm thực phẩm");
//                        fab.setOnClickListener(fabAddFood);
                        break;
                }
                if(fragment != null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                }else {
                    Log.e(TAG, "LOAD Fragment Error");
                }
            }
        });


        loadFragment(new HomeFragment());


    }

    View.OnClickListener fabAddRecipe = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("DEBUGGGGGGGG", "ONCLICK - FAB");
            DialogFragment newRecipeDialog = AddRecipeDialog.newInstance();
            newRecipeDialog.show(getSupportFragmentManager(), "newRecipeDialog");
        }
    };

    View.OnClickListener fabAddFood = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("DEBUGGGGGGGG", "ONCLICK - AddFood - FAB");
            AddFoodDialog dialog = new AddFoodDialog();
            dialog.show(getSupportFragmentManager(), "Food  dialog");

        }
    };




    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permision Write File is Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permision Write File is Denied", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(MainActivity.this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(MainActivity.this, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }else{

            }
        }
    }

}