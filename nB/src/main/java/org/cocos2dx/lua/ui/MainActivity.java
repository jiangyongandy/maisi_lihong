package org.cocos2dx.lua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.zuiai.nn.R;

import org.cocos2dx.lua.model.UserModel;
import org.cocos2dx.lua.ui.common.CommonPageAdapter;
import org.cocos2dx.lua.ui.fragment.FindWebFragment;
import org.cocos2dx.lua.ui.fragment.HomeFragment;
import org.cocos2dx.lua.ui.fragment.LiveFragment;
import org.cocos2dx.lua.ui.fragment.PersonFragment2;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by JIANG on 2017/9/16.
 */

public class MainActivity extends BaseActivity {

    private static final String IS_FIRST_RUN_TIPS = "IS_FIRST_RUN_TIPS";
    @BindView(R.id.rb_niuying)
    RadioButton mRbNiuying;
    @BindView(R.id.rb_person)
    RadioButton mRbPerson;
    @BindView(R.id.rb)
    RadioGroup mRb;
    @BindView(R.id.fragment_container)
    ViewPager mFragmentContainer;
    @BindView(R.id.tv_know)
    TextView mTvKnow;
    @BindView(R.id.rl_guide)
    RelativeLayout mRlGuide;
    @BindView(R.id.rb_live)
    RadioButton rbLive;
    @BindView(R.id.rb_find)
    RadioButton rbFind;
    @BindView(R.id.rl_about)
    ImageView rlAbout;
    private Unbinder mUnbinder;
    private final String mAboutUrl = "file:///android_asset/about/index.html";
    private Fragment mHomeFragment;
    private Fragment mPersonFragment;
    private FindWebFragment mVideoFragment;
    private Fragment mLiveFragment;
    private int currentTabIndex;
    private Fragment[] fragments;
    private RadioButton[] rbBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
        }
        return true;
    }

    private void init() {
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        mHomeFragment = new HomeFragment();
        mLiveFragment = new LiveFragment();
        mVideoFragment = new FindWebFragment();
        mPersonFragment = new PersonFragment2();
        fragments = new Fragment[]{mHomeFragment, mLiveFragment, mVideoFragment, mPersonFragment};
        rbBtns = new RadioButton[]{mRbNiuying, rbLive, rbFind, mRbPerson};
        mFragmentContainer.setAdapter(new CommonPageAdapter(getSupportFragmentManager(), Arrays.asList(fragments)));
        mFragmentContainer.setOffscreenPageLimit(fragments.length);
        mFragmentContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentTabState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
/*        mRb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_niuying:
                        mFragmentContainer.setCurrentItem(0);
                        break;
                    case R.id.rb_live:
                        mFragmentContainer.setCurrentItem(1);
                        break;
                    case R.id.rb_find:
                        mFragmentContainer.setCurrentItem(2);
                        mVideoFragment.refreshPage();
                        break;
                    case R.id.rb_person:
                        mFragmentContainer.setCurrentItem(3);
                        break;
                }
            }
        });*/
        mRbNiuying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCLick(view.getId());
            }
        });
        mRbPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCLick(view.getId());
            }
        });
        rbLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCLick(view.getId());
            }
        });
        rbFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCLick(view.getId());
            }
        });
        currentTabIndex = 0;
        setCurrentTabState(currentTabIndex);
//guide
/*        boolean isFirstRun = DataHelper.getBoolSp(this, IS_FIRST_RUN_TIPS, true);
        if(isFirstRun) {
            mRlGuide.setVisibility(View.VISIBLE);
            mTvKnow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRlGuide.setVisibility(View.GONE);
                }
            });
        }
        DataHelper.setBoolSF(this, IS_FIRST_RUN_TIPS, false);*/
    }

    private void setCurrentTabState(int index) {
        rbBtns[currentTabIndex].setChecked(false);
        rbBtns[index].setChecked(true);
        currentTabIndex = index;
    }

    private void handleCLick(int id) {
        switch (id) {
            case R.id.rb_niuying:
                mFragmentContainer.setCurrentItem(0);
                break;
            case R.id.rb_live:
                mFragmentContainer.setCurrentItem(1);
                break;
            case R.id.rb_find:
                mFragmentContainer.setCurrentItem(2);
                mVideoFragment.refreshPage();
                break;
            case R.id.rb_person:
                mFragmentContainer.setCurrentItem(3);
                break;
        }
    }

    @OnClick(R.id.rl_about)
    public void onViewClicked() {
        UserModel.getInstance().launchActivity(this, ShareActivity2.class);
    }
}
