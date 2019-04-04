package com.helpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.iid.FirebaseInstanceId;
import com.helpa.interfaces.NetworkListener;
import com.helpa.network.ApiCall;
import com.helpa.network.ApiInterface;
import com.helpa.network.RestApi;
import com.helpa.utils.AppConstant;
import com.helpa.utils.AppSharedPreference;
import com.helpa.utils.AppUtils;
import com.helpa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class LogInActivity extends BaseActivity implements View.OnClickListener, NetworkListener {
    private EditText etEmail, etPassword;
    private Button btLogIn;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setListeners();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /*
    * Initialize all views IDs
    * */
    private void initView() {
        bar = (ProgressBar) findViewById(R.id.bar);
        etEmail =(EditText) findViewById(R.id.et_email);
        etPassword =(EditText) findViewById(R.id.et_password);
       // btLogIn =(Button) findViewById(R.id.bt_login);
    }

    /*
    * Initialize all listeners
    * */
    private void setListeners() {
        btLogIn.setOnClickListener(this);
    }

    /*
    * validate all cases
    * */
    private Boolean validate()
    {
         if(etEmail.getText().toString().trim().length()==0)
        {
            AppUtils.showToast(LogInActivity.this,getResources().getString(R.string.h_email));
            return false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches())
        {
            AppUtils.showToast(LogInActivity.this,getResources().getString(R.string.email_invalid_val));
            return false;
        }
        else if(etPassword.getText().toString().trim().length()==0)
        {
            AppUtils.showToast(LogInActivity.this,getResources().getString(R.string.h_password));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
//            case R.id.bt_login:
//                if(AppUtils.isInternetAvailable(LogInActivity.this)) {
//                    if (validate())
//                        hitLogInAPI();
//                }
               // else
                  //  AppUtils.showToast(LogInActivity.this,getResources().getString(R.string.no_internet));
              //  break;
        }
    }

    /*
    * Hit login Api through email and password
    * */
    private void hitLogInAPI() {
        bar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = RestApi.createService(LogInActivity.this,ApiInterface.class);
        final HashMap params = new HashMap<>();
        params.put("email", etEmail.getText().toString());
        params.put("password", etPassword.getText().toString());
        params.put("device_id", AppUtils.getDeviceId(LogInActivity.this));
        params.put("device_token", FirebaseInstanceId.getInstance().getToken());
        params.put("platform", "1");
        Call<ResponseBody> call = apiInterface.login(params);
        ApiCall.getInstance().hitService(LogInActivity.this, call, this,1);
    }

    @Override
    public void onSuccess(int responseCode, String response, int requestCode) {
        bar.setVisibility(View.GONE);
        String token = null, refreshToken = null;
        try {
            JSONObject object = new JSONObject(response);
            AppUtils.showToast(LogInActivity.this,object.getString(AppConstant.message));
            int code = object.getInt(AppConstant.code);
            if(code==200) {
                token = object.getJSONObject(AppConstant.result).getString(AppConstant.accessToken);
                refreshToken = object.getJSONObject(AppConstant.result).getString(AppConstant.refreshToken);
                AppSharedPreference.getInstance().putString(LogInActivity.this,AppSharedPreference.ACCESS_TOKEN, token);
                AppSharedPreference.getInstance().putString(LogInActivity.this,AppSharedPreference.REFRESH_TOKEN, refreshToken);
                AppSharedPreference.getInstance().putString(LogInActivity.this,AppSharedPreference.PROFILE, response);
                Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onError( String response, int requestCode) {
        bar.setVisibility(View.GONE);
        AppUtils.showToast(LogInActivity.this,response+"");
    }

    @Override
    public void onFailure() {
        bar.setVisibility(View.GONE);
        AppUtils.showToast(LogInActivity.this,"Failure");
    }
}
