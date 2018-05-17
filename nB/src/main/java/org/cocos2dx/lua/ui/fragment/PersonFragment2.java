package org.cocos2dx.lua.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.maisi.video.obj.video.ChargeInfoEntity;
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
import org.cocos2dx.lua.ui.BindZhiFuActivity;
import org.cocos2dx.lua.ui.ChargeActivity;
import org.cocos2dx.lua.ui.KeFuActivity;
import org.cocos2dx.lua.ui.MyTeamActivity;
import org.cocos2dx.lua.ui.QuestionActivity;
import org.cocos2dx.lua.ui.RankActivity;
import org.cocos2dx.lua.ui.RewardCenterActivity;
import org.cocos2dx.lua.ui.SettingActivity;
import org.cocos2dx.lua.ui.ShareActivity2;
import org.cocos2dx.lua.ui.ShouCeActivity;
import org.cocos2dx.lua.ui.widget.NoScrollGridView;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class PersonFragment2 extends LazyFragment {

    @BindView(R.id.tv_commend_no)
    TextView tvCommendNo;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_all_benefit)
    TextView tvAllBenefit;
    @BindView(R.id.tv_has_benefit)
    TextView tvHasBenefit;
    @BindView(R.id.tv_can_benefit)
    TextView tvCanBenefit;
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
    @BindView(R.id.scroll_view)
    NoScrollGridView scrollView;

    private String[] names = {
            "升级会员", "奖励中心", "我要推广",
            "我的团队", "排行榜", "魔视百科",
            "平台手册", "在线客服", "设置",
    };
    private int[] icons = {
            R.drawable.ic_icon0, R.drawable.ic_icon1, R.drawable.ic_icon2,
            R.drawable.ic_icon3, R.drawable.ic_icon4, R.drawable.ic_icon5,
            R.drawable.ic_icon6, R.drawable.ic_icon7, R.drawable.ic_icon8,
    };
    private Class[] activitys = {
            ChargeActivity.class, RewardCenterActivity.class, ShareActivity2.class,
            MyTeamActivity.class, RankActivity.class, QuestionActivity.class,
            ShouCeActivity.class, KeFuActivity.class, SettingActivity.class,
    };

    private int[] menuIndex = {
            0,
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8,
    };

    private EditText amountInput;
    private EditText transferAccount;
    private boolean isFirstLazyLoad;
    private ArrayList<ChargeInfoEntity> list = new ArrayList<>();

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(mActivity, "已分享~~", Toast.LENGTH_LONG).show();
            if (!VipHelperUtils.getInstance().isWechatLogin()) {
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
    private ListAdapter listAdapter;


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
        mTvVersion.setText("v" + AppUtils.getAppVersionName());
        listAdapter = new ListAdapter(getContext(), menuIndex);
        scrollView.setAdapter(listAdapter);
        scrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jumpActivity(activitys[position]);
            }
        });
    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_person_moshi;
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
                .apply(bitmapTransform(new CropCircleTransformation()))
                .into(mIvHead);
        mTvName.setText(entity.getWeiChatUserInfo().getNickname());
        mTvVipLeft.setText("VIP剩余时长 " + entity.getVipLeft() + "天");
        mTvPointsLeft.setText("我的积分 " + entity.getPointsLeft());
        String level = "";
        switch (entity.getCommend_level()) {
            case 0:
                level = "会员";
                break;
            case 1:
                level = "大使";
                break;
            case 2:
                level = "合伙人";
                break;
        }
        tvLevel.setText("【等级】："+level);
        tvCommendNo.setText("推荐码："+entity.getCommendNo());
        tvAllBenefit.setText("¥"+entity.getIncomeAll());
        tvHasBenefit.setText("¥"+entity.getCommend2cash());
        tvCanBenefit.setText("¥"+entity.getCommendLeft());

    }

    @org.simple.eventbus.Subscriber(tag = EventBusTag.TAG_LOGIN_EXIT, mode = ThreadMode.MAIN)
    public void exitLogin(Object o) {
        Glide.with(PersonFragment2.this)
                .load(R.drawable.ic_login_avator_default)
                .into(mIvHead);
        mTvName.setText("未登录");
        mTvVipLeft.setText("");
        mTvPointsLeft.setText(" " );
        String level = "";
        tvLevel.setText("【等级】：");
        tvCommendNo.setText("推荐码：");
        tvAllBenefit.setText("¥");
        tvHasBenefit.setText("¥");
        tvCanBenefit.setText("¥");

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
                if (!VipHelperUtils.getInstance().isWechatLogin()) {
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
                Intent intent = new Intent(getActivity(), BindZhiFuActivity.class);
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

    private void jumpActivity(Class _activity) {
        UserModel.getInstance().launchActivity(getActivity(), _activity);
    }

    private void activeVip() {
        if (!VipHelperUtils.getInstance().isWechatLogin()) {
            ToastUtil.show(getContext(), "请先登录~~", Toast.LENGTH_SHORT);
            return;
        }

        MaterialDialog dialog =
                new MaterialDialog.Builder(getContext())
                        .title("卡密激活")
                        .customView(R.layout.dialog_input_card_no_pwd, true)
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String amount = amountInput.getText().toString();
                                String account = transferAccount.getText().toString();
                                if (amount.length() == 0 || account.length() == 0) {
                                    Toast.makeText(
                                            APPAplication.instance,
                                            "请确定卡号和卡密填写完整~~",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Service.getComnonServiceForString().activeVip(amount, account, VipHelperUtils.getInstance().getVipUserInfo().getUid())
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
                        })
                        .build();
        dialog.show();
        amountInput = dialog.getCustomView().findViewById(R.id.et_amount);
        transferAccount = dialog.getCustomView().findViewById(R.id.et_recommend_num);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class ListAdapter extends BaseAdapter {

        private final int[] indexs;
        private final LayoutInflater inflater;
        private final Context context;

        public ListAdapter(Context context, int[] indexs) {
            this.context = context;
            this.indexs = indexs;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return indexs.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = convertView;
            ViewHolder holder = null;
            if (convertView == null) {
                rootView = inflater.inflate(R.layout.list_person_menu_item, parent, false);
                holder = new ViewHolder();
                holder.textView = (TextView) rootView.findViewById(R.id.iv_icon);
                holder.ivLogo = (ImageView) rootView.findViewById(R.id.iv_logo);
                rootView.setTag(holder);
            } else {
                holder = (ViewHolder) rootView.getTag();
            }
            holder.textView.setText(names[indexs[position]]);
            Drawable drawable = context.getResources().getDrawable(icons[indexs[position]]);
//                holder.textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            holder.ivLogo.setImageDrawable(drawable);
            return rootView;
        }

        class ViewHolder {
            TextView textView;
            ImageView ivLogo;
        }

    }
}
