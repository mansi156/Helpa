package com.helpa.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.helpa.interfaces.ImageCallback;
import com.helpa.interfaces.NetworkListener;
import com.helpa.network.ApiCall;
import com.helpa.network.ApiInterface;
import com.helpa.network.RestApi;
import com.helpa.utils.AppConstant;
import com.helpa.utils.AppSharedPreference;
import com.helpa.utils.AppUtils;
import com.helpa.utils.TakeImage;
import com.helpa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SignUpActivity extends BaseActivity implements View.OnClickListener, NetworkListener, ImageCallback {
    private EditText etFName,  etEmail, etPassword,  etNumber;
    private Button btSignUp;
    private ProgressBar bar;
    private Uri mImageUri;
    private ImageView imageView;
    private static TakeImage takeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        setListeners();
        takeImage = TakeImage.getInstance(SignUpActivity.this,this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /*
        * Initialize all views IDs
        * */
    private void initView() {
        bar = (ProgressBar) findViewById(R.id.bar);
        imageView = (ImageView) findViewById(R.id.iv_image);
        etFName =(EditText) findViewById(R.id.et_fname);
        etEmail =(EditText) findViewById(R.id.et_email);
        etPassword =(EditText) findViewById(R.id.et_password);
        etNumber =(EditText) findViewById(R.id.et_phone);
        btSignUp =(Button) findViewById(R.id.bt_signup);
    }

    /*
    * Initialize all listeners
    * */
    private void setListeners() {
        btSignUp.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    /*
    * validate all cases
    * */
    private Boolean validate()
    {
        if(etFName.getText().toString().trim().length()==0)
        {
            AppUtils.showToast(SignUpActivity.this,getResources().getString(R.string.h_fname));
            return false;
        }
        else if(etEmail.getText().toString().trim().length()==0)
        {
            AppUtils.showToast(SignUpActivity.this,getResources().getString(R.string.h_email));
            return false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches())
        {
            AppUtils.showToast(SignUpActivity.this,getResources().getString(R.string.email_invalid_val));
            return false;
        }
        else if(etPassword.getText().toString().trim().length()==0)
        {
            AppUtils.showToast(SignUpActivity.this,getResources().getString(R.string.h_password));
            return false;
        }
        else if(etPassword.getText().toString().trim().length()<6)
        {
            AppUtils.showToast(SignUpActivity.this,getResources().getString(R.string.h_password_lenght));
            return false;
        }
        else if(etNumber.getText().toString().trim().length()>=0 && etNumber.getText().toString().trim().length()!=10)
        {
            AppUtils.showToast(SignUpActivity.this,getResources().getString(R.string.v_number));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_signup:
                if(AppUtils.isInternetAvailable(SignUpActivity.this)) {
                    if (validate()) {
                        if(mImageUri!=null)
                            hitSignUpMultipartAPI();
                        else
                            hitSignUpAPI();
                    }
                }
                else
                    AppUtils.showToast(SignUpActivity.this,getResources().getString(R.string.no_internet));
                break;
            case R.id.iv_image:
                selectImage();
                break;
        }
    }

    /*
    *  Dialog to choose image from gallery or camera
    * */
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    takeImage.fromCamera();
                } else if (items[item].equals("Choose from Library")) {
                    takeImage.fromGallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /*
    * Hit SIGN Up Api
    * */
    private void hitSignUpAPI() {
        bar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = RestApi.createService(SignUpActivity.this,ApiInterface.class);
        final HashMap params = new HashMap<>();
        params.put("first_name", etFName.getText().toString());
        params.put("email", etEmail.getText().toString());
        params.put("password", etPassword.getText().toString());
        if(etNumber.getText().toString()!=null)
            params.put("phone", etNumber.getText().toString());
        params.put("device_id", AppUtils.getDeviceId(SignUpActivity.this));
        params.put("device_token", "12345");
        params.put("platform", "1");
        Call<ResponseBody> call = apiInterface.signUp(params);
        ApiCall.getInstance().hitService(SignUpActivity.this, call, this,1);
    }

    /*
   * Hit SIGN Up Api
   * */
    private void hitSignUpMultipartAPI() {
        bar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = RestApi.createService(SignUpActivity.this,ApiInterface.class);
        Map<String, RequestBody> params = new HashMap<>();
        params.put("first_name", toRequestBody(etFName.getText().toString()));
        params.put("email", toRequestBody(etEmail.getText().toString()));
        params.put("password", toRequestBody(etPassword.getText().toString()));
        if(etNumber.getText().toString()!=null)
            params.put("phone", toRequestBody(etNumber.getText().toString()));
        params.put("device_id", toRequestBody(AppUtils.getDeviceId(SignUpActivity.this)));
        params.put("device_token", toRequestBody(FirebaseInstanceId.getInstance().getToken()));
        params.put("platform", toRequestBody("1"));
        File file = new File(mImageUri.getPath());
        MultipartBody.Part fileToUpload = RestApi.prepareFilePart("profile_image",file);
        Call<ResponseBody> call = apiInterface.signUpMultipaty(fileToUpload,params);
        ApiCall.getInstance().hitService(SignUpActivity.this, call, this,1);
    }

    // This method  converts String to RequestBody
    public RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }


    @Override
    public void onSuccess(int responseCode, String response,int requestCode) {
        bar.setVisibility(View.GONE);
        String token = null, refreshToken = null;
        try {
            JSONObject object = new JSONObject(response);
            AppUtils.showToast(SignUpActivity.this,object.getString(AppConstant.message));
            int code = object.getInt(AppConstant.code);
            if(code==200) {
                token = object.getJSONObject(AppConstant.result).getString(AppConstant.accessToken);
                refreshToken = object.getJSONObject(AppConstant.result).getString(AppConstant.refreshToken);
                AppSharedPreference.getInstance().putString(SignUpActivity.this,AppSharedPreference.ACCESS_TOKEN, token);
                AppSharedPreference.getInstance().putString(SignUpActivity.this,AppSharedPreference.REFRESH_TOKEN, refreshToken);
                AppSharedPreference.getInstance().putString(SignUpActivity.this,AppSharedPreference.PROFILE, response);
                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onError( String response, int requestCode) {
        bar.setVisibility(View.GONE);
        AppUtils.showToast(SignUpActivity.this,response+"");
    }

    @Override
    public void onFailure() {
        bar.setVisibility(View.GONE);
        AppUtils.showToast(SignUpActivity.this,"Failure");
    }




    @Override
    public void onSuccess(Uri mImageUYri) {
        if(mImageUYri!=null)
        {
            mImageUri = mImageUYri;
            String path = mImageUri.getPath();
            Glide.with(SignUpActivity.this).load(path).into(imageView);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takeImage.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takeImage.onActivityResult(requestCode, resultCode, data);
    }


}
