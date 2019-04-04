package com.helpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.helpa.adapters.WalkThroughPagerAdapter;
import com.helpa.R;
import com.helpa.utils.AppSharedPreference;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class WalkThroughActivity extends BaseActivity {
    private ViewPager pager;
    private CircleIndicator pageIndicator;
    private WalkThroughPagerAdapter adapter ;
    private int currentPage = 0;
    private Runnable Update;
    private Timer swipeTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(AppSharedPreference.getInstance().getString(WalkThroughActivity.this,AppSharedPreference.ACCESS_TOKEN)!=null)
        {
            Intent intent = new Intent(WalkThroughActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        initViews();
        loadPager();
        imageAutoChange();

        ((TextView)findViewById(R.id.tv_skip)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity();
            }
        });
    }


    /*
    * method to initialize all UI components
    * */
    public void initViews() {
        pager = (ViewPager) findViewById(R.id.pager);
        pageIndicator = (CircleIndicator) findViewById(R.id.pageIndicatorView);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /*
    * Load all fragments in adaptor for tutorials
    * */
    public void loadPager(){
        adapter = new WalkThroughPagerAdapter(getSupportFragmentManager(), 3);
        pager.setAdapter(adapter);
        pageIndicator.setViewPager(pager);
    }


    /*
    * Auto change image after 1.5 seconds
    * */
    private void imageAutoChange() {
        final int size = 3;
        final Handler handler = new Handler();
        Update = new Runnable() {
            public void run() {
                if (currentPage == size) {
                    currentPage=0;
                }
                pager.setCurrentItem(currentPage++);
            }
        };

        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            //
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0, 1500);
    }


    /*
    * Move to next activity
    * */
    private void moveToNextActivity() {
        Intent intent = new Intent(WalkThroughActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}
