package com.helpa.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.helpa.R;


public class MainActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.signup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
        });

        ((Button)findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LogInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
        });

        ((Button)findViewById(R.id.forgot)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ForgotPasswordActivity.class);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                startActivity(intent);
            }
        });

        /*
        * Reset Password deep linking code
        * */
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            Uri data = null;
            if (intent.getData() != null)
                data = intent.getData();
            try {
                if (Intent.ACTION_VIEW.equals(action) && data != null) {
                    String value = String.valueOf(data);
                    if (value.contains("applaurels")) {
                        String[] abc = value.split("userId/");
                        String userId = abc[abc.length-1];
                        if(userId!=null)
                        {
                            Intent resetIntent = new Intent(MainActivity.this,ResetPasswordActivity.class);
                            resetIntent.putExtra("id",userId+"");
                            startActivity(resetIntent);
                            finish();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
