package com.helpa.network;

import android.content.Context;


import com.helpa.models.RefreshTokenResponse;
import com.helpa.utils.AppSharedPreference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * This class is used to handling operation for Service
 */
public class RestApi {
    static final String API_BASE_URL = "http://rccapi.appinventive.com/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(final Context context, Class<S> aClass) {
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                String accessToken = AppSharedPreference.getInstance().getString(context,AppSharedPreference.ACCESS_TOKEN);
                final String basic = "Bearer " + accessToken;

                Request original = chain.request();
                Request.Builder requestBuilder = null;
                if(accessToken!=null)
                {
                    requestBuilder = original.newBuilder()
                            .header("Authorization",basic)
                            .method(original.method(), original.body());
                }
                else {
                    requestBuilder = original.newBuilder()
                            .method(original.method(), original.body());
                }
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        httpClient.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                if (responseCount(response) >= 2) {
                    // If both the original call and the call with refreshed token failed,
                    // it will probably keep failing, so don't try again.
                    return null;
                }
                // We need a new client, since we don't want to make another call using our client with access token
                ApiInterface tokenClient = RestApi.createService(context, ApiInterface.class);
                HashMap params = new HashMap();
                params.put("refresh_token", AppSharedPreference.getInstance().getString(context, "refresh_token"));
                params.put("name",AppSharedPreference.getInstance().getString(context,"name"));
                Call<RefreshTokenResponse> call = tokenClient.refreshToken(params);
                retrofit2.Response<RefreshTokenResponse> execute = call.execute();
                if(execute.code()==200) {
                    String newAccessToken = execute.body().getRefreshTokenResult().getToken();
                    String newRefreshToken = execute.body().getRefreshTokenResult().getRefreshToken();
                    AppSharedPreference.getInstance().putString(context,AppSharedPreference.ACCESS_TOKEN,newAccessToken);
                    AppSharedPreference.getInstance().putString(context,AppSharedPreference.REFRESH_TOKEN,newRefreshToken);
                    return response.request().newBuilder()
                            .header("Authorization", "Bearer " + newAccessToken)
                            .build();
                }
                else
                    return null;

            }
        });

        Retrofit retrofit = retrofitBuilder.client(httpClient.build()).build();
        return retrofit.create(aClass);
    }


    public static RequestBody getRequestBody(String params) {
        return RequestBody.create(MediaType.parse("text/plain"), params.getBytes());
    }

    //This method is used to create the multipart file data
    public static MultipartBody.Part prepareFilePart(String partName, File file) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*; boundary=----"+System.currentTimeMillis()), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}
