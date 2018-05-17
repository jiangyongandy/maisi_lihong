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

import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.DataHelper;
import org.cocos2dx.lua.ui.fragment.GuideFragment1;
import org.cocos2dx.lua.ui.fragment.GuideFragment2;
import org.cocos2dx.lua.ui.fragment.GuideFragment3;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 功能
 * Created by Jiang on 2017/10/16.
 */

public class GuideActivity extends BaseActivity {

    public static final String IS_FIRST_Run = "IS_FIRST_Run";
    private int delay = 1500;
    private boolean isHasGuide;

    private int count;
    private Timer timer ;
    private boolean isFirstRun;
    private Handler handler = new Handler();
    private APPAplication.QbInitListener listener = new APPAplication.QbInitListener() {
        @Override
        public void onQbInit() {
            if (APPAplication.instance.qbsdkIsInit)
                jumpActivity(SplashActivity.class);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
/*        if (listener != null) {
            APPAplication.instance.qbInitListenerArrayList.remove(listener);
        }*/
    }

    private void initData() {
//        APPAplication.instance.qbInitListenerArrayList.add(listener);
        isFirstRun = DataHelper.getBoolSp(this, IS_FIRST_Run, true);
        ViewPager vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        GuideFragment1 guideFragment = new GuideFragment1();
        GuideFragment2 guideFragment2 = new GuideFragment2();
        GuideFragment3 guideFragment3 = new GuideFragment3();
        final ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(guideFragment);
        fragments.add(guideFragment2);
        fragments.add(guideFragment3);
        vpGuide.setAdapter(new InnerPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"1", "2"}));
        final Handler handler = new Handler();
        final ViewPager finalVpGuide = vpGuide;
        final TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        if (count == fragments.size() + 1) {
                            timer.cancel();
                            isHasGuide = true;
                            jumpActivity(SplashActivity.class);
/*                            if (APPAplication.instance.qbsdkIsInit) {
//                                timer.schedule(timerTaskGoSplash, delay1);
                                return;
                            } else {
                                Toast.makeText(GuideActivity.this, "vip服务器加载中.....", Toast.LENGTH_LONG).show();
                            }*/
                            return;
                        }
                        finalVpGuide.setCurrentItem(count);
                    }
                });
            }
        };
        timer = new Timer();
        if (isFirstRun) {
            DataHelper.setBoolSF(GuideActivity.this, IS_FIRST_Run, false);
            timer.schedule(timerTask, delay, delay);
        } else {
            Intent intent = new Intent(GuideActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void jumpActivity(Class _activity) {
            Intent intent = new Intent(GuideActivity.this, _activity);
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
