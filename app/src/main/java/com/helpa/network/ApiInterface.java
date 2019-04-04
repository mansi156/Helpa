package com.helpa.network;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;


/**
 * ApiInterface.java
 * This class act as an interface between Retrofit and Classes used using Retrofit in Application
 *
 * @author Appinvetiv
 * @version 1.0
 * @since 1.0
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(@FieldMap HashMap<String,String> map);

    @FormUrlEncoded
    @POST("signup")
    Call<ResponseBody> signUp(@FieldMap HashMap<String,String> map);

    @Multipart
    @POST("signup")
    Call<ResponseBody> signUpMultipaty(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST("change-password")
    Call<ResponseBody> changePassword(@FieldMap HashMap<String,String> map);

    @FormUrlEncoded
    @POST("forget-password")
    Call<ResponseBody> forgotPassword(@FieldMap HashMap<String,String> map);

    @FormUrlEncoded
    @POST("reset-password")
    Call<ResponseBody> reset(@FieldMap HashMap<String,String> map);

    @FormUrlEncoded
    @POST("refresh")
    Call<ResponseBody> refreshToken(@QueryMap HashMap<String, String> map);


}
