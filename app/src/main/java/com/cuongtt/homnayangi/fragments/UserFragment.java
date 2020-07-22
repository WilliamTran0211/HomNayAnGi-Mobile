package com.cuongtt.homnayangi.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.activities.LoginActivity;
import com.cuongtt.homnayangi.models.Users;
import com.cuongtt.homnayangi.network.APIService;
import com.cuongtt.homnayangi.network.ApiUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment extends Fragment {
    APIService mAPIService = ApiUtils.getAPIService();
    ImageView imgView;
    TextView displayName, fullName, birthday, gender, email;
    Button logoutButton;
    ProgressBar loading;

    private GoogleSignInClient mGoogleSignInClient;

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

        loading = view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);


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
                signOut();
            }
        });



        return view;
    }


    private void signOut() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

//    private void revokeAccess() {
//        mGoogleSignInClient.revokeAccess()
//                .addOnCompleteListener((Executor) this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // ...
//                    }
//                });
//    }

    public void fetchUserInfo(String id){
        mAPIService.getUserInfoWithID(id).enqueue(new Callback<Users>() {
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

                    loading.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable throwable) {
                Log.d("DEBUGGGGG", " User Fragment - fetchUserInfo - API Call Failure - "+throwable.getMessage());
            }
        });
    }
}