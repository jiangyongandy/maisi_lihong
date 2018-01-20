package org.cocos2dx.lua.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.maisi.video.obj.video.UserInfoEntity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.BoyiRxUtils;
import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.EventBusTag;
import org.cocos2dx.lua.ToastUtil;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.model.UserModel;
import org.cocos2dx.lua.service.Service;
import org.cocos2dx.lua.ui.DaiLiActivity;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PersonFragment2 extends LazyFragment {

    @BindView(R.id.iv_head)
    ImageView mIvHead;
    @BindView(R.id.rl_login)
    RelativeLayout mRlLogin;
    @BindView(R.id.rl_charge)
    RelativeLayout mRlCharge;
    @BindView(R.id.rl_share)
    RelativeLayout mRlShare;
    @BindView(R.id.rl_question)
    RelativeLayout mRlQuestion;
    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.rl_about)
    RelativeLayout mRlAbout;
    @BindView(R.id.rl_daili)
    RelativeLayout mRlDaili;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_vip_left)
    TextView mTvVipLeft;
    @BindView(R.id.tv_points_left)
    TextView mTvPointsLeft;
    private boolean isFirstLazyLoad;

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(mActivity, "已分享~~", Toast.LENGTH_LONG).show();
            if(!VipHelperUtils.getInstance().isWechatLogin()) {
                return;
            }
            Service.getComnonService().updatePoints(VipHelperUtils.getInstance().getUserInfo().getUnionid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(
                                    APPAplication.instance,
                                    "错误:" + throwable.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(
                                    APPAplication.instance,
                                    e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(String result) {
                            UserModel.getInstance().refreshUserInfo();
                            if (!result.equals("0.0")) {

                                Toast.makeText(
                                        APPAplication.instance,
                                        "分享获得积分-----" + result,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(
                                        APPAplication.instance,
                                        "分享间隔要大于一天哦~~",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(mActivity, "分享失败", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(mActivity, "分享取消", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void lazyLoad() {
        if (!CommonConstant.isRelease) {
            return;
        }
        if (VipHelperUtils.getInstance().isWechatLogin() && !isFirstLazyLoad) {
            EventBus.getDefault().post(VipHelperUtils.getInstance().getVipUserInfo(), EventBusTag.TAG_LOGIN_SUCCESS);

            isFirstLazyLoad = true;

        }
    }

    @Override
    protected void initData() {
        if (!CommonConstant.isRelease) {
            mRlCharge.setVisibility(View.GONE);
            mRlDaili.setVisibility(View.GONE);
            mRlQuestion.setVisibility(View.GONE);
            mRlAbout.setVisibility(View.GONE);
        }
        //版本信息
        mTvVersion.setText("v"+AppUtils.getAppVersionName());
    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_person2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (VipHelperUtils.getInstance().isWechatLogin())
            UserModel.getInstance().refreshUserInfo();
    }

    @org.simple.eventbus.Subscriber(tag = EventBusTag.TAG_LOGIN_SUCCESS, mode = ThreadMode.MAIN)
    public void userInfoChange(UserInfoEntity entity) {
        Glide.with(PersonFragment2.this)
                .load(entity.getWeiChatUserInfo().getHeadimgurl())
                .fitCenter()
                .into(mIvHead);
        mTvName.setText(entity.getWeiChatUserInfo().getNickname());
        mTvVipLeft.setText("VIP剩余时长 " + entity.getVipLeft() + "天");
        mTvPointsLeft.setText("我的积分 " + entity.getPointsLeft());
    }

    @OnClick({R.id.rl_login, R.id.rl_charge, R.id.rl_share, R.id.rl_question, R.id.tv_version, R.id.rl_about, R.id.rl_daili})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_login:
                wechatLogin();
                break;
            case R.id.rl_charge:
//                UserModel.getInstance().launchActivity(mActivity, ChargeActivity.class);
                activeVip();
                break;
            case R.id.rl_share:
                if(!VipHelperUtils.getInstance().isWechatLogin()) {
                    Toast.makeText(
                            APPAplication.instance,
                            "先登录再分享可以获得积分哦~~", Toast.LENGTH_SHORT).show();
                }
                String temp = "";
                if (!CommonConstant.isRelease) {
                    temp = "我在这里发现一个可以看收藏全网视频网站的APP，你要不要来看一下0.0";
                } else {
                    temp = VipHelperUtils.shareDescription;
                }

                UMImage image = new UMImage(getActivity(), R.drawable.ic_launcher1);//资源文件
                UMWeb web = new UMWeb(VipHelperUtils.shareLink);
                web.setTitle("VIP免费看啦");//标题
                web.setThumb(image);  //缩略图
                web.setDescription(temp);//描述

                new ShareAction(getActivity())
                        .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA)
                        .setCallback(umShareListener)
                        .open();
                break;
            case R.id.rl_question:
                break;
            case R.id.tv_version:
                break;
            case R.id.rl_about:
                break;
            case R.id.rl_daili:
                Intent intent = new Intent(getActivity(), DaiLiActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void wechatLogin() {
        // send oauth request
//        final SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "none";
//        boolean sendReq = api.sendReq(req);
//        if (sendReq) {
//            Log.v("weChat_login", "sendReq  sendReq ---------------true");
//        }
        UserModel.getInstance().login(mActivity);
    }

    private void activeVip() {
        if(!VipHelperUtils.getInstance().isWechatLogin()) {
            ToastUtil.show(getContext(), "请先登录~~", Toast.LENGTH_SHORT);
            return;
        }
        new MaterialDialog.Builder(getContext())
                .title("卡密激活")
                .content("请输入卡密")
                .inputType(InputType.TYPE_CLASS_TEXT )
                .input("推荐粘贴输入", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        if(input.length() <= 0) {
                            ToastUtil.show(getContext(), "请先输入卡密~~", Toast.LENGTH_SHORT);
                            return;
                        }
                        Service.getComnonServiceForString().activeVip(input.toString().trim(), VipHelperUtils.getInstance().getVipUserInfo().getUid())
                                .compose(BoyiRxUtils.<String>applySchedulers())
                                .subscribe(new BoyiRxUtils.MySubscriber<String>() {

                                    @Override
                                    public void onNext(String result) {
                                        if (result.equals("faild")) {

                                            Toast.makeText(
                                                    APPAplication.instance,
                                                    "激活失败，请检查是否输入正确或联系客服",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(
                                                    APPAplication.instance,
                                                    "激活成功",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                        UserModel.getInstance().refreshUserInfo();
                                    }
                                });
                    }
                }).show();
    }
}
