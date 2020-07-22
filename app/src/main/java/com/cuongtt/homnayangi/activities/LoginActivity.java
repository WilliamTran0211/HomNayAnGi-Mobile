package com.cuongtt.homnayangi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.models.Users;
import com.cuongtt.homnayangi.network.APIService;
import com.cuongtt.homnayangi.network.ApiUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.moshi.Json;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 001;

    private APIService mAPIService;

    private TextInputLayout edtEmail, edtPassword;
    private Button btnDangNhap, btnDangKy;

    private TextView txtChangePassword;

    public final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    private GoogleSignInClient mGoogleSignInClient;

    private SignInButton btnGoogleSignIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAPIService = ApiUtils.getAPIService();


        addControl();
        addEvents();


        Gson gson = new Gson();
        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        String json = mPrefs.getString("user_info", "");
        Users obj = gson.fromJson(json, Users.class);

//        if(obj.isValid()){
//            Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("UserInfo",  json);
//            intent_main.putExtras(bundle);
//            startActivity(intent_main);
//        }


        btnGoogleSignIn = findViewById(R.id.google_sign_in_button);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.google_sign_in_button:
                        signInGoogle();
                        break;
                    // ...
                }
            }
        });

    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
//            Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent_main);

            System.out.println("đăng nhặp bằng email "+account.getEmail());


            String fullName = account.getFamilyName() + " " + account.getGivenName();

            checkUserExisted(account.getEmail(), fullName, account.getDisplayName());


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//    }

    private void addEvents() {

        btnDangNhap.setEnabled(false);

        edtEmail.getEditText().addTextChangedListener(emailValidate);

        btnDangNhap.setOnClickListener(loginButtonClick);

        btnDangKy.setOnClickListener(registerButtonClick);

        txtChangePassword.setOnClickListener(changePasswordClick);

    }

    private void addControl() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtChangePassword = findViewById(R.id.changePassword);
    }

    public View.OnClickListener changePasswordClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent changedPassIntent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
            startActivity(changedPassIntent);
        }
    };

    public final View.OnClickListener loginButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {

            Log.d("DEBUGGGGGGGGGG", "onClick - LOGIN Activity");

            final String email = edtEmail.getEditText().getText().toString().trim();
            final String password = edtPassword.getEditText().getText().toString().trim();

            Map<String, String> user_login = new HashMap<>();

            user_login.put("email", email);
            user_login.put("password", password);

            Gson gson = new Gson();

            JsonObject kk = new JsonObject();

            kk.addProperty("email", email);
            kk.addProperty("password", password);


            mAPIService.login(kk).enqueue(new Callback<Users>() {
                @Override
                public void onResponse(Call<Users> call, Response<Users> response) {
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        if (response.code() == 200) {
                            Log.d("DEBUGGGGGGGGGG", "onClick - LOGIN Activity - API Call SUCCESS");
                            Snackbar.make(view, "Đăng nhập thành công!", Snackbar.LENGTH_SHORT).show();

                            Users users_login_info = new Users();

                            users_login_info = response.body();

                            SharedPreferences sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("user_info", gson.toJson(users_login_info));

                            editor.commit();

                            Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();

                            bundle.putString("UserInfo",  gson.toJson(users_login_info));

                            intent_main.putExtras(bundle);
                            startActivity(intent_main);

                        }
                    } else {
                        Log.d("DEBUGGGGGGGGGG", "onClick - LOGIN Activity - API Call - " + response.message());
                        Snackbar.make(view, "Email hoặc password không đúng.", Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Users> call, Throwable throwable) {
                    Log.d("DEBUGGGGGGGGGG", "onClick - LOGIN Activity - Failed: " + throwable.getMessage().toString());
                    Snackbar.make(view, "Đăng nhập thất bại, thử lại sau! ", Snackbar.LENGTH_SHORT).show();
                }
            });

        }
    };

    public void checkUserExisted( final String email, final String full_name, final String display_name){
        System.out.println("Check account with email: "+email.toString());

        JsonObject mBody = new JsonObject();
        mBody.addProperty("email", email);

        mAPIService.getUserInfoWithEmailIfExisted(mBody).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                Gson gson = new Gson();

                if(response.isSuccessful()){
                    Users tmp= response.body();
                    if(tmp.isValid()){
                        System.out.println("Check account with email: "+ email + " has existed with data is "+ tmp.toString());
                        Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);


                        Bundle bundle = new Bundle();
                        bundle.putString("UserInfo",  gson.toJson(tmp));

                        SharedPreferences.Editor editor = getSharedPreferences("MyUserInfo", MODE_PRIVATE).edit();
                        editor.putString("UserId", tmp.getId());
                        editor.putString("UserEmail", tmp.getEmail());
                        editor.putString("UserFullName", tmp.getFull_name());
                        editor.apply();


                        intent_main.putExtras(bundle);
                        startActivity(intent_main);
                    }

                }else{
                    System.out.println("Check account with email: "+ email + " not existed ");

                    Users tmp = new Users();
                    tmp.setEmail(email);
                    tmp.setDisplay_name(full_name);
                    tmp.setFull_name(display_name);

                    Intent intent_SignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("UserRawInfo",  gson.toJson(tmp));
                    intent_SignUp.putExtras(bundle);
                    startActivity(intent_SignUp);
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable throwable) {
                System.out.println("Check account with email failed "+throwable.getMessage());
            }
        });

    }

//    public void CreateNewUser(JsonObject userData){
//        mAPIService.signUp(userData).enqueue(new Callback<Users>() {
//            @Override
//            public void onResponse(Call<Users> call, Response<Users> response) {
//                if(response.isSuccessful()){
//                    Users tmp = response.body();
//                    if(tmp.isValid()){
//                        Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent_main);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Users> call, Throwable throwable) {
//
//            }
//        });
//    }


    public View.OnClickListener registerButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplication(), SignUpActivity.class);
            getApplication().startActivity(intent);
        }
    };

    public TextWatcher emailValidate = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {

            btnDangNhap.setEnabled(true);

            String email = edtEmail.getEditText().getText().toString().trim();

            if (email.matches(emailPattern) && editable.length() > 0)
            {
                edtEmail.setError(null);
            }else {
                edtEmail.setError("Email invalid");
            }

        }
    };

}
