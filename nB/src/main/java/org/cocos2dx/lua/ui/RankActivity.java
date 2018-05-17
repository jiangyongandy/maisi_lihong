package org.cocos2dx.lua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.umeng.socialize.UMShareAPI;
import com.zuiai.nn.R;

import org.cocos2dx.lua.ui.common.CommonPageAdapter;
import org.cocos2dx.lua.ui.fragment.RankPersonFragment;
import org.cocos2dx.lua.ui.fragment.RankTeamFragment;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by JIANG on 2017/9/16.
 */

public class RankActivity extends BaseActivity {

    @BindView(R.id.rb_person)
    RadioButton rbPerson;
    @BindView(R.id.rb_team)
    RadioButton rbTeam;
    @BindView(R.id.rb)
    RadioGroup rb;
    @BindView(R.id.view_status_1)
    View viewStatus1;
    @BindView(R.id.view_status_2)
    View viewStatus2;
    @BindView(R.id.fragment_container)
    ViewPager fragmentContainer;
    private Unbinder mUnbinder;
    private final String mAboutUrl = "file:///android_asset/about/index.html";
    private Fragment mHomeFragment;
    private Fragment mPersonFragment;
    private Fragment mVideoFragment;
    private Fragment mLiveFragment;
    private int currentTabIndex;
    private Fragment[] fragments;
    private RadioButton[] rbBtns;
    private View[] selViews;

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

    private void init() {
        setContentView(R.layout.activity_rank);
        mUnbinder = ButterKnife.bind(this);

        mHomeFragment = new RankPersonFragment();
        mLiveFragment = new RankTeamFragment();
        fragments = new Fragment[]{mHomeFragment, mLiveFragment,};
        rbBtns = new RadioButton[]{rbPerson, rbTeam, };
        selViews = new View[]{viewStatus1, viewStatus2, };
        fragmentContainer.setAdapter(new CommonPageAdapter(getSupportFragmentManager(), Arrays.asList(fragments)));
        fragmentContainer.setOffscreenPageLimit(fragments.length);
        fragmentContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_person:
                        fragmentContainer.setCurrentItem(0);
                        break;
                    case R.id.rb_team:
                        fragmentContainer.setCurrentItem(1);
                        break;
                }
            }
        });
        currentTabIndex = 0;
        setCurrentTabState(currentTabIndex);
    }

    private void setCurrentTabState(int index) {
        rbBtns[currentTabIndex].setChecked(false);
        selViews[currentTabIndex].setVisibility(View.INVISIBLE);
        rbBtns[index].setChecked(true);
        selViews[index].setVisibility(View.VISIBLE);
        currentTabIndex = index;
    }

}
