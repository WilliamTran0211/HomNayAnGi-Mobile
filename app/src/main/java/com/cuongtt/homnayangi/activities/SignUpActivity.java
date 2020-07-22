package com.cuongtt.homnayangi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cuongtt.homnayangi.DateValidator;
import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.Utils;
import com.cuongtt.homnayangi.models.Users;
import com.cuongtt.homnayangi.network.APIService;
import com.cuongtt.homnayangi.network.ApiUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.moshi.Json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;


public class SignUpActivity extends AppCompatActivity {

    TextInputLayout edtEmail, edtPasswords, edtFullName, edtDisplayName, edtBirthday, edtRepeatPassword;

    TextView titleSignUp;

    Button btnSave, btnCancel;

    RadioGroup genderSelectionGroup;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private GoogleSignInClient mGoogleSignInClient;

    private APIService mAPIService = ApiUtils.getAPIService();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        addControls();

        Gson gson = new Gson();
        Intent intent = getIntent();
        final String json = intent.getStringExtra("UserRawInfo");

        if(json != null){
            System.out.println("Json from loginActivity " + json);
            if(!json.isEmpty()) {

                titleSignUp.setText("Lưu thông tin");

                Users obj = gson.fromJson(json, Users.class);

                edtEmail.getEditText().setEnabled(false);
                edtEmail.getEditText().setText(obj.getEmail().toString());

                edtFullName.getEditText().setText(obj.getFull_name());
                edtDisplayName.getEditText().setText(obj.getDisplay_name());
            }
        }else{
            System.out.println("Json không có gì ");
        }

        btnSave.setOnClickListener(clickSave);
        btnCancel.setOnClickListener(clickCancel);

        edtRepeatPassword.getEditText().addTextChangedListener(watchingEditTextRepeatPassword);
        edtBirthday.getEditText().addTextChangedListener(watchingEditTextBirthday);

        edtDisplayName.getEditText().addTextChangedListener(editTextWatcher);
        edtFullName.getEditText().addTextChangedListener(editTextWatcher);
        edtEmail.getEditText().addTextChangedListener(editTextWatcher);
        edtPasswords.getEditText().addTextChangedListener(editTextWatcher);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        disconnectGoogleSignIn();
    }

    View.OnClickListener clickSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email = null, password = null, fullName = null, displayName = null, birthday = null, gender = null;


            email = edtEmail.getEditText().getText().toString().trim();
            password = edtPasswords.getEditText().getText().toString().trim();
            fullName = edtFullName.getEditText().getText().toString().trim();
            displayName = edtDisplayName.getEditText().getText().toString().trim();
            birthday = birthdayEditTextHandling();
            gender = genderSelectionHandling();

            if(email.isEmpty()){
                edtEmail.setError("Email không được trống");
            }else if(fullName.isEmpty()){
                edtFullName.setError("Họ tên không được trống");
            }else if(displayName.isEmpty()){
                edtDisplayName.setError("Tên hiển thị không được trống");
            }else if(password.isEmpty()){
                edtPasswords.setError("Mật khẩu không được trống");
            }else if(birthday.isEmpty()){
                edtBirthday.setError("Ngày sinh không được trống");
            }else{
                createUser(email, password, fullName, displayName, birthday, gender);
            }
        }
    };

    View.OnClickListener clickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            disconnectGoogleSignIn();
        }
    };

    public String genderSelectionHandling(){
        Boolean male = false, female = false;

        int radioButtonID = genderSelectionGroup.getCheckedRadioButtonId();
        RadioButton r = (RadioButton) genderSelectionGroup.findViewById(radioButtonID);


        String selectedtext = r.getText().toString();

        if(selectedtext.equals("Nam")) return "male";
        else return "female";

    }

    public String birthdayEditTextHandling(){

        String birthday = null;

        Calendar cal = Calendar.getInstance();
        Date dateBirthday;
        try {
            dateBirthday = dateFormat.parse(edtBirthday.getEditText().getText().toString());

            cal.setTime(dateBirthday);

            int month = cal.get(Calendar.MONTH) + 1;

            birthday = cal.get(Calendar.DATE) +"/"+ month +"/"+ cal.get(Calendar.YEAR);
            System.out.println(birthday);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return birthday;
    }

    private void addControls() {
        edtEmail = findViewById(R.id.edt_signUp_email);
        edtFullName = findViewById(R.id.edt_signUp_fullName);
        edtDisplayName = findViewById(R.id.edt_signUp_displayName);
        edtPasswords = findViewById(R.id.edt_signUp_password);
        edtRepeatPassword = findViewById(R.id.edt_signUp_repeatPassword);
        edtBirthday = findViewById(R.id.edt_signUp_birthday);

        titleSignUp = findViewById(R.id.nameTitleSignUp);

        genderSelectionGroup = findViewById(R.id.edt_signUp_gender);

        btnCancel = findViewById(R.id.btn_signUp_cancel);
        btnSave = findViewById(R.id.btn_signUp_save);
    }

    public void disconnectGoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(SignUpActivity.this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
    }

    public void createUser(String email, String password, String fullName, String displayName, String birthday, String gender){

//        JsonObject userData = new JsonObject();
//
//        userData.addProperty("email", email);
//        userData.addProperty("password", password);
//        userData.addProperty("full_name", fullName);
//        userData.addProperty("display_name", displayName);
//        userData.addProperty("gender", gender);
//        userData.addProperty("birthday", birthday);
//        userData.addProperty("images", "avatar");
//
//        System.out.println(userData.toString());

        Map<String,String> userData = new HashMap<String, String>();
        userData.put("email", email);
        userData.put("password", password);
        userData.put("full_name", fullName);
        userData.put("display_name", displayName);
        userData.put("gender", gender);
        userData.put("birthday", birthday);
        userData.put("images", "avatar");

        MultipartBody.Part emailer = MultipartBody.Part.createFormData("email", email);
        MultipartBody.Part pass = MultipartBody.Part.createFormData("password", password);
        MultipartBody.Part full_name = MultipartBody.Part.createFormData("full_name", fullName);
        MultipartBody.Part display_name = MultipartBody.Part.createFormData("display_name", displayName);
        MultipartBody.Part gendere = MultipartBody.Part.createFormData("gender", gender);
        MultipartBody.Part birthdayy  = MultipartBody.Part.createFormData("birthday", birthday);
        MultipartBody.Part imagess = MultipartBody.Part.createFormData("images", "avatar");

        mAPIService.signUp(emailer, full_name, display_name, pass, birthdayy, gendere, imagess).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    Log.d("DEBUGGGGGGGGGGG", "SignUpActivity - SignUp User Successfully!");
                    Snackbar.make(getCurrentFocus(), "Tạo tài khoản thành công, vui lòng đăng nhập lại. ", Snackbar.LENGTH_LONG ).show();
                    disconnectGoogleSignIn();
                }else{
                    Log.d("DEBUGGGGGGGGGGG", "SignUpActivity - SignUp User not Success - "+response.errorBody());
                    Snackbar.make(getCurrentFocus(), "Đăng ký tài khoản không thành công. Lỗi "+ response.message(), Snackbar.LENGTH_SHORT ).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Log.d("DEBUGGGGGGGGGGG", "SignUpActivity - Call API create user fail - "+throwable.getMessage());
            }
        });
    }

    TextWatcher editTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.toString().length() > 0){
                edtPasswords.setError(null);
                edtBirthday.setError(null);
                edtDisplayName.setError(null);
                edtEmail.setError(null);
                edtFullName.setError(null);
            }
        }
    };

    TextWatcher watchingEditTextRepeatPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String password = edtPasswords.getEditText().getText().toString().trim();

            if(!editable.toString().equals(password)){
                edtRepeatPassword.setError("Mật khẩu nhập lại phải giống mật khẩu.");
            }else{
                edtRepeatPassword.setError(null);
            }
        }
    };


    TextWatcher watchingEditTextBirthday = new TextWatcher() {

        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!charSequence.toString().equals(current)) {
                String clean = charSequence.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                int cl = clean.length();
                int sel = cl;
                for (int k = 2; k <= cl && k < 6; k += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8) {
                    clean = clean + ddmmyyyy.substring(clean.length());
                } else {
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day = Integer.parseInt(clean.substring(0, 2));
                    int mon = Integer.parseInt(clean.substring(2, 4));
                    int year = Integer.parseInt(clean.substring(4, 8));

                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                    cal.set(Calendar.MONTH, mon - 1);
                    year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                    clean = String.format("%02d%02d%02d", day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;

                edtBirthday.getEditText().setText(current);
                edtBirthday.getEditText().setSelection(sel < current.length() ? sel : current.length());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String dateStr = edtBirthday.getEditText().getText().toString();

            DateValidator dateVal = new DateValidator();

            if(!dateVal.validate(dateStr)){
                edtBirthday.setError("Không đúng định dạng.");
            }else{
                edtBirthday.setError(null);
            }

        }
    };
}
