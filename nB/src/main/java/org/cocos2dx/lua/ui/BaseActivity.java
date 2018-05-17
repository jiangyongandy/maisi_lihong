package org.cocos2dx.lua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import org.simple.eventbus.EventBus;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class BaseActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (useEventBus())
            EventBus.getDefault().register(this);
        LogUtils.i("-----------"+this.getClass().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus())
            EventBus.getDefault().unregister(this);
    }

    /**
     * 是否使用eventbus
     *
     * @return true
     */
    protected boolean useEventBus() {
        return true;
    }

    public void onBackView(View view){
        finish();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void jumpActivity(Class _activity) {
        Intent intent = new Intent(this, _activity);
        startActivity(intent);
    }


}
