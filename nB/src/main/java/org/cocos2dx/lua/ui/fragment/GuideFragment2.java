package org.cocos2dx.lua.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.CommonConstant;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 功能
 * Created by Jiang on 2017/10/16.
 */

public class GuideFragment2 extends BaseFragment implements APPAplication.QbInitListener {

    @BindView(R.id.iv_guide)
    ImageView mIvGuide;

    @Override
    protected void initData() {
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!APPAplication.instance.qbsdkIsInit) {
                    Toast.makeText(getActivity(), "稍候....,内核初始化中.....", Toast.LENGTH_LONG).show();
                    return;
                }
                // send oauth request
/*                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "none";
                boolean sendReq = api.sendReq(req);
                if(sendReq) {
                    getActivity().finish();
                    Log.v("GuideFragment2","sendReq  sendReq ---------------true");
                }*/
            }
        });
        APPAplication.instance.qbInitListener = this;
        if (!CommonConstant.isRelease) {
            mIvGuide.setImageResource(R.drawable.iv_app_utils_guide2);
        }
    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_guide2;
    }

    @Override
    public void onQbInit() {
        // send oauth request
//        Toast.makeText(getActivity(), "内核初始化完成", Toast.LENGTH_LONG).show();
/*        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "none";
        boolean sendReq = api.sendReq(req);
        if(sendReq) {
            getActivity().finish();
            Log.v("GuideFragment2","sendReq  sendReq ---------------true");
        }*/
    }

}
