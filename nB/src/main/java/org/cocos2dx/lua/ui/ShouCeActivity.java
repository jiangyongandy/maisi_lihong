package org.cocos2dx.lua.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;

import com.flyco.tablayout.SlidingTabLayout;
import com.zuiai.nn.R;

import org.cocos2dx.lua.ui.fragment.SingeImageFragment;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by JIANG on 2017/9/16.
 */

public class ShouCeActivity extends BaseActivity {

    @BindView(R.id.tl_shouCe)
    SlidingTabLayout tlShouCe;
    @BindView(R.id.fragment_container)
    ViewPager fragmentContainer;
    private Unbinder mUnbinder;
    private Fragment mHomeFragment;
    private Fragment mPersonFragment;
    private Fragment mVideoFragment;
    private Fragment mLiveFragment;
    private Fragment mPersonFragment2;
    private int currentTabIndex;
    private Fragment[] fragments;
    private RadioButton[] rbBtns;
    private String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    private void init() {
        setContentView(R.layout.activity_shouce);
        mUnbinder = ButterKnife.bind(this);

        mHomeFragment = SingeImageFragment.newInstance(R.drawable.iv_singe_img_1);
        mLiveFragment = SingeImageFragment.newInstance(R.drawable.iv_singe_img_2);
        mVideoFragment = SingeImageFragment.newInstance(R.drawable.iv_singe_img_3);
        mPersonFragment = SingeImageFragment.newInstance(R.drawable.iv_singe_img_4);
        mPersonFragment2 = SingeImageFragment.newInstance(R.drawable.iv_singe_img_5);
        fragments = new Fragment[]{mHomeFragment, mLiveFragment, mVideoFragment, mPersonFragment, mPersonFragment2};
        titles = new String[]{"魔视","直播","视频","我的","播放页"};
        ArrayList<Fragment> arrayList = new ArrayList<Fragment>(Arrays.asList(fragments));
        tlShouCe.setViewPager(fragmentContainer, titles, this, arrayList);
    }


}
