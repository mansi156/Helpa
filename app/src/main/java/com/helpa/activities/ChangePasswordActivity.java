package com.helpa.activities;

import android.os.Bundle;
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

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, NetworkListener {
    private EditText etOld, etNew, etConfirm;
    private Button btSubmit;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
        setListeners();
    }

    /*
    * Initialize all views IDs
    * */
    private void initView() {
        bar = (ProgressBar) findViewById(R.id.bar);
        etOld =(EditText) findViewById(R.id.et_old);
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
        String oldPassword = etOld.getText().toString().trim();
        String newPassword =  etNew.getText().toString().trim();
        String confirmPassword = etConfirm.getText().toString().trim();
        if(oldPassword.length()==0)
        {
            AppUtils.showToast(ChangePasswordActivity.this,getResources().getString(R.string.please_enter_old_pass));
            return false;
        }
        else if(newPassword.length()==0)
        {
            AppUtils.showToast(ChangePasswordActivity.this,getResources().getString(R.string.new_valid));
            return false;
        }
        else if(confirmPassword.length()==0)
        {
            AppUtils.showToast(ChangePasswordActivity.this,getResources().getString(R.string.confirm_valid));
            return false;
        }
        else if(!confirmPassword.equalsIgnoreCase(newPassword))
        {
            AppUtils.showToast(ChangePasswordActivity.this,getResources().getString(R.string.match));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_submit:
                if(AppUtils.isInternetAvailable(ChangePasswordActivity.this)) {
                    if (validate())
                        hitChangePasswordAPI();
                }
                else
                    AppUtils.showToast(ChangePasswordActivity.this,getResources().getString(R.string.no_internet));
                break;
        }
    }

    /*
    * Hit Change password Api
    * */
    private void hitChangePasswordAPI() {
        bar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = RestApi.createService(ChangePasswordActivity.this,ApiInterface.class);
        final HashMap params = new HashMap<>();
        params.put("oldpassword", etOld.getText().toString());
        params.put("password", etNew.getText().toString());
        Call<ResponseBody> call = apiInterface.changePassword(params);
        ApiCall.getInstance().hitService(ChangePasswordActivity.this, call, this,1);
    }

    @Override
    public void onSuccess(int responseCode, String response, int requestCode) {
        bar.setVisibility(View.GONE);
        try {
            JSONObject object = new JSONObject(response);
            AppUtils.showToast(ChangePasswordActivity.this, object.getString(AppConstant.message));
            int code = object.getInt(AppConstant.code);
            if(code==200)
                finish();
        }
        catch (Exception e)
        {e.printStackTrace();}
    }

    public void onError( String response, int requestCode) {
        bar.setVisibility(View.GONE);
        AppUtils.showToast(ChangePasswordActivity.this,response.toString()+"");
    }
    @Override
    public void onFailure() {
        AppUtils.showToast(ChangePasswordActivity.this,"Failure");
    }
}
