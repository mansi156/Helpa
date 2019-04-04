package com.helpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.helpa.utils.AppConstant;
import com.helpa.utils.AppSharedPreference;
import com.helpa.R;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private ImageView ivMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        String response = AppSharedPreference.getInstance().getString(HomeActivity.this,AppSharedPreference.PROFILE);
        if(response!=null) {
            try {
                JSONObject object = new JSONObject(response);
                int code = object.getInt(AppConstant.code);
                if (code == 200) {
                    String fname = object.getJSONObject(AppConstant.result).getString("first_name");
                    //String email = object.getJSONObject("result").getString("email");
                    ((TextView) findViewById(R.id.name)).setText(fname);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * Initialize all views Ids
    * */
    private void initView() {
        ivMenu = (ImageView) findViewById(R.id.iv_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu();
            }
        });
        ((Button)findViewById(R.id.log_out)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSharedPreference.getInstance().clearAllPrefs(HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ((Button)findViewById(R.id.change_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void openMenu() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else if (!mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
        super.onBackPressed();
    }

}
