package org.cocos2dx.lua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.ui.fragment.GuideFragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 功能
 * Created by Jiang on 2017/10/16.
 */

public class SplashActivity extends BaseActivity {

    private int count;
    private Timer timer = new Timer();
    private boolean isFirstRun;
    private Handler handler = new Handler();
    private int delay = 1000;
    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            jumpActivity(MainActivity.class);
        }
    };
    private APPAplication.QbInitListener listener = new APPAplication.QbInitListener() {
        @Override
        public void onQbInit() {
            if (APPAplication.instance.qbsdkIsInit)
                timer.schedule(timerTask, delay);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //全屏效果
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
//        APPAplication.instance.qbInitListenerArrayList.add(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (listener != null) {
//            APPAplication.instance.qbInitListenerArrayList.remove(listener);
        }
    }

    private void initData() {
        ViewPager vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        GuideFragment guideFragment = new GuideFragment();
//        GuideFragment2 guideFragment2 = new GuideFragment2();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(guideFragment);
//        fragments.add(guideFragment2);
        vpGuide.setAdapter(new InnerPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"1"}));
        final Handler handler = new Handler();
        final ViewPager finalVpGuide = vpGuide;
/*        if (APPAplication.instance.qbsdkIsInit) {
            timer.schedule(timerTask, delay);
        }else {
            Toast.makeText(this, "vip服务器加载中.....", Toast.LENGTH_LONG).show();
        }*/
        timer.schedule(timerTask, delay);
    }

    public void jumpActivity(Class _activity) {
        Intent intent = new Intent(SplashActivity.this, _activity);
        startActivity(intent);
        finish();
    }

    class InnerPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private String[] titles;

        public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
            // super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
