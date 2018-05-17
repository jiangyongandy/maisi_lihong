package org.cocos2dx.lua.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.zuiai.nn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能
 * Created by Jiang on 2017/10/16.
 */

public class SingeImageFragment extends BaseFragment {

    @BindView(R.id.imageView)
    ImageView mImageView;

    public static SingeImageFragment newInstance(int res) {
        SingeImageFragment fragmentOne = new SingeImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("res", res);
        //fragment保存参数，传入一个Bundle对象
        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this, mRootView);
        if (getArguments() != null) {
            //取出保存的值
            int res = getArguments().getInt("res");
            Drawable drawable = getContext().getResources().getDrawable(res);
            mImageView.setImageDrawable(drawable);
        }
    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_guide;
    }

}
