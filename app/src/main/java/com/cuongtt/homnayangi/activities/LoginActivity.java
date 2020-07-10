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

import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.models.Users;
import com.cuongtt.homnayangi.network.APIService;
import com.cuongtt.homnayangi.network.ApiUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private APIService mAPIService;

    private TextInputLayout edtEmail, edtPassword;
    private Button btnDangNhap, btnDangKy;

    public final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


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
    }

    private void addEvents() {

        btnDangNhap.setEnabled(false);

        edtEmail.getEditText().addTextChangedListener(emailValidate);

        btnDangNhap.setOnClickListener(loginButtonClick);

        btnDangKy.setOnClickListener(registerButtonClick);

    }

    private void addControl() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnDangNhap = findViewById(R.id.btnDangNhap);
    }

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
