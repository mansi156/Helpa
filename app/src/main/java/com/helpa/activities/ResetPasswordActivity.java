package com.helpa.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.helpa.R;
import com.helpa.interfaces.NetworkListener;
import com.helpa.network.ApiCall;
import com.helpa.network.ApiInterface;
import com.helpa.network.RestApi;
import com.helpa.utils.AppConstant;
import com.helpa.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener, NetworkListener {
    private EditText etNew, etConfirm;
    private Button btSubmit;
    private String userId;
    private ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
        setListeners();
        userId = getIntent().getStringExtra("id");
    }

    /*
    * Initialize all views IDs
    * */
    private void initView() {
        bar = (ProgressBar) findViewById(R.id.bar);
        etNew =(EditText) findViewById(R.id.et_new);
        etConfirm =(EditText) findViewById(R.id.et_confirm);
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
    private Boolean validate()
    {
        String newPassword =  etNew.getText().toString().trim();
        String confirmPassword = etConfirm.getText().toString().trim();
         if(newPassword.length()==0)
        {
            AppUtils.showToast(ResetPasswordActivity.this,getResources().getString(R.string.new_valid));
            return false;
        }
        else if(confirmPassword.length()==0)
        {
            AppUtils.showToast(ResetPasswordActivity.this,getResources().getString(R.string.confirm_valid));
            return false;
        }
        else if(!confirmPassword.equalsIgnoreCase(newPassword))
        {
            AppUtils.showToast(ResetPasswordActivity.this,getResources().getString(R.string.match));
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_submit:
                if(AppUtils.isInternetAvailable(ResetPasswordActivity.this)) {
                    if (validate())
                        hitResetPasswordAPI();
                }
                else
                    AppUtils.showToast(ResetPasswordActivity.this,getResources().getString(R.string.no_internet));
                break;
        }
    }

    /*
    * Hit Reset password Api
    * */
    private void hitResetPasswordAPI() {
        bar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = RestApi.createService(ResetPasswordActivity.this,ApiInterface.class);
        final HashMap params = new HashMap<>();
        params.put("userId", userId);
        params.put("password", etNew.getText().toString());
        Call<ResponseBody> call = apiInterface.reset(params);
        ApiCall.getInstance().hitService(ResetPasswordActivity.this, call, this,1);
    }

    @Override
    public void onSuccess(int responseCode, String response, int requestCode) {
        bar.setVisibility(View.GONE);
        try {
            JSONObject object = new JSONObject(response);
            AppUtils.showToast(ResetPasswordActivity.this,object.getString(AppConstant.message));
            int code = object.getInt(AppConstant.code);
            if(code==200) {
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onError( String response, int requestCode) {
        bar.setVisibility(View.GONE);
        AppUtils.showToast(ResetPasswordActivity.this,"Error");
    }

    @Override
    public void onFailure() {
        bar.setVisibility(View.GONE);
        AppUtils.showToast(ResetPasswordActivity.this,"Failure");
    }
}
