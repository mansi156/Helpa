package com.helpa.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.helpa.interfaces.NetworkListener;
import com.helpa.network.ApiCall;
import com.helpa.network.ApiInterface;
import com.helpa.network.RestApi;
import com.helpa.utils.AppConstant;
import com.helpa.utils.AppUtils;
import com.helpa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener, NetworkListener {
    private EditText etEmail,etPhone;
    private Button btSubmit;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
        setListeners();
    }

    /*
    * Initialize all views IDs
    * */
    private void initView() {
        bar = (ProgressBar) findViewById(R.id.bar);
        etEmail =(EditText) findViewById(R.id.et_email);
        etPhone =(EditText) findViewById(R.id.et_phone);
        btSubmit =(Button) findViewById(R.id.bt_submit);
    }

    /*
   * Initialize all listeners
   * */
    private void setListeners() {
        btSubmit.setOnClickListener(this);
    }

    /*
    * validate all cases
    * */
    private Boolean validateEmail()
    {
         if(etEmail.getText().toString().trim().length()==0)
        {
            AppUtils.showToast(ForgotPasswordActivity.this,getResources().getString(R.string.h_email));
            return false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches())
        {
            AppUtils.showToast(ForgotPasswordActivity.this,getResources().getString(R.string.email_invalid_val));
            return false;
        }
        return true;
    }

    /*
    * validate all cases
    * */
    private Boolean validatePhone()
    {
        if(etPhone.getText().toString().trim().length()==0)
        {
            AppUtils.showToast(ForgotPasswordActivity.this,getResources().getString(R.string.h_number));
            return false;
        }
        else  if(etPhone.getText().toString().trim().length()!=10)
        {
            AppUtils.showToast(ForgotPasswordActivity.this,getResources().getString(R.string.h_number_length));
            return false;
        }
        else if(!Patterns.PHONE.matcher(etPhone.getText().toString().trim()).matches())
        {
            AppUtils.showToast(ForgotPasswordActivity.this,getResources().getString(R.string.phone_invalid_val));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_submit:
                if(AppUtils.isInternetAvailable(ForgotPasswordActivity.this)) {
                    if(etEmail.toString().trim().length()>0 && etPhone.toString().trim().length()==0) {
                        if (validateEmail())
                            hitForgotPasswordAPI();
                    }
                    else if(etPhone.toString().trim().length()>0 && etEmail.toString().trim().length()==0) {
                        if (validatePhone())
                            hitForgotPasswordAPI();
                    }
                    else
                    {
                        AppUtils.showToast(ForgotPasswordActivity.this,"Please enter email id or number");
                    }
                }
                else
                    AppUtils.showToast(ForgotPasswordActivity.this,getResources().getString(R.string.no_internet));
                break;
        }
    }

    /*
    * Hit Forgot password Api to reset password
    * */
    private void hitForgotPasswordAPI() {
        bar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = RestApi.createService(ForgotPasswordActivity.this,ApiInterface.class);
        final HashMap params = new HashMap<>();
        params.put("email", etEmail.getText().toString());
        Call<ResponseBody> call = apiInterface.forgotPassword(params);
        ApiCall.getInstance().hitService(ForgotPasswordActivity.this, call, this,1);
    }

    /*
   * Hit Forgot password Api to reset password
   * */
    private void hitForgotPasswordPhoneAPI() {
        bar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = RestApi.createService(ForgotPasswordActivity.this,ApiInterface.class);
        final HashMap params = new HashMap<>();
        params.put("email", etEmail.getText().toString());
        Call<ResponseBody> call = apiInterface.forgotPassword(params);
        ApiCall.getInstance().hitService(ForgotPasswordActivity.this, call, this,2);
    }

    @Override
    public void onSuccess(int responseCode, String response,int requestCode) {
        bar.setVisibility(View.GONE);
        try {
            JSONObject object = new JSONObject(response);
            AppUtils.showToast(ForgotPasswordActivity.this,object.getString(AppConstant.message));
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onError( String response, int requestCode) {
        bar.setVisibility(View.GONE);
        AppUtils.showToast(ForgotPasswordActivity.this,"Error");
    }

    @Override
    public void onFailure() {
        bar.setVisibility(View.GONE);
        AppUtils.showToast(ForgotPasswordActivity.this,"Failure");
    }


}
