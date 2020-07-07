package com.cuongtt.homnayangi.fragments;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.activities.LoginActivity;
import com.cuongtt.homnayangi.activities.MainActivity;
import com.cuongtt.homnayangi.activities.RecipeDetailActivity;
import com.cuongtt.homnayangi.activities.RecipeFormActivity;
import com.cuongtt.homnayangi.models.Users;
import com.cuongtt.homnayangi.network.APIService;
import com.cuongtt.homnayangi.network.ApiUtils;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class UserFragment extends Fragment {
    APIService mAPIService = ApiUtils.getAPIService();
    ImageView imgView;
    TextView displayName, fullName, birthday, gender, email;
    Button logoutButton;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        Log.d("DEBUGGGGGGGGGG", "User Fragment - onCreateView");


        Gson gson = new Gson();

        Bundle bundle = getArguments();
        String json = bundle.getString("UserInfo");
        Users obj = gson.fromJson(json, Users.class);

        imgView = view.findViewById(R.id.profile_image);
        displayName = view.findViewById(R.id.txtDisplayName);
        fullName= view.findViewById(R.id.txtFullName);
        birthday = view.findViewById(R.id.txtBirthday);
        gender = view.findViewById(R.id.txtGender);
        email = view.findViewById(R.id.txtEmail);



        fetchUserInfo(obj.getId());



        logoutButton = view.findViewById(R.id.btnDangXuat);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });



        return view;
    }

    public void fetchUserInfo(String id){
        mAPIService.getUserInfo(id).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if(response.isSuccessful()){
                    Log.d("DEBUGGGGG", " User Fragment - fetchUserInfo - API Call Success");

                    Glide.with(UserFragment.this)
                            .load(response.body().getAvatar())
                            .fitCenter()
                            .into(imgView);

                    displayName.setText(response.body().getDisplay_name());
                    fullName.setText(response.body().getFull_name());
                    email.setText(response.body().getEmail());

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    birthday.setText(simpleDateFormat.format(response.body().getBirthday()).toString());

                    if(response.body().getGender().equals("male")){
                        gender.setText("Nam");
                    }else{
                        gender.setText("Nữ");
                    }

                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable throwable) {
                Log.d("DEBUGGGGG", " User Fragment - fetchUserInfo - API Call Failure - "+throwable.getMessage());
            }
        });
    }
}