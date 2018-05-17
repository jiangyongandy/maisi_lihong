package org.cocos2dx.lua.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.maisi.video.obj.video.AppInfo;
import com.maisi.video.obj.video.BannerEntity;
import com.maisi.video.obj.video.NotifyEntity;
import com.maisi.video.obj.video.UserInfoEntity;
import com.tmall.ultraviewpager.UltraViewPager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.AppHelperUtils;
import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.EventBusTag;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.model.UserModel;
import org.cocos2dx.lua.service.Service;
import org.cocos2dx.lua.ui.AppListActivity;
import org.cocos2dx.lua.ui.BrowserActivity;
import org.cocos2dx.lua.ui.ChargeActivity;
import org.cocos2dx.lua.ui.BindZhiFuActivity;
import org.cocos2dx.lua.ui.widget.BannerPageAdapter;
import org.cocos2dx.lua.ui.widget.SortableNinePhotoLayout;
import org.cocos2dx.lua.ui.widget.holder.CBViewHolderCreator;
import org.cocos2dx.lua.ui.widget.holder.Holder;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * Created by JIANG on 2017/9/16.
 */

public class AppUtilsFragment extends BaseFragment implements SortableNinePhotoLayout.Delegate{

    private static final int REQUEST_CODE_CHOOSE_APP = 1;
    @BindView(R.id.ll_jiaoliu)
    LinearLayout mLlJiaoliu;
    @BindView(R.id.ll_communication)
    LinearLayout mLlCommunication;
    @BindView(R.id.ll_member_center)
    LinearLayout mLlMemberCenter;
    @BindView(R.id.ll_share)
    LinearLayout mLlShare;
    @BindView(R.id.sv_container)
    ScrollView mSvContainer;
    @BindView(R.id.tv_notify)
    TextView mTvNotify;
    @BindView(R.id.ultra_viewpager)
    UltraViewPager mUltraViewpager;
    @BindView(R.id.gv_list)
    SortableNinePhotoLayout  mGvList;

    private final String mAboutUrl = "file:///android_asset/about/index.html";
    private final int BANNER_LOOP_TIME = 5000;
    private List<BannerEntity> bannerList = new ArrayList<>();
    private ArrayList<NotifyEntity> notifyList = new ArrayList<>();

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(mActivity, "已分享", Toast.LENGTH_LONG).show();
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
    private BannerPageAdapter adapter;

    @OnClick({R.id.ll_jiaoliu, R.id.ll_communication, R.id.ll_member_center, R.id.ll_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_jiaoliu:
                break;
            case R.id.ll_communication:
                Intent intent2 = new Intent(getActivity(), ChargeActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll_member_center:
                Intent intent3 = new Intent(getActivity(), BindZhiFuActivity.class);
                startActivity(intent3);
                break;
            case R.id.ll_share:
                String temp = "";
                if (!CommonConstant.isRelease) {
                    temp = "我在这里发现一个可以看收藏APP的APP，你要不要来看一下0.0";
                } else {
                    temp = "我在这里发现一个可以看全网VIP视频的APP，你要不要来看一下0.0";
                }
                new ShareAction(mActivity)
                        .withText(temp)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA)
                        .setCallback(umShareListener)
                        .open();
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_TEXT, "这里是分享内容");
//                intent.setType("text/plain");
//                //设置分享列表的标题，并且每次都显示分享列表
//                startActivity(Intent.createChooser(intent, "分享到"));
                break;
        }
    }

    @Override
    protected void initData() {

        mLlMemberCenter.setVisibility(View.GONE);
        mTvNotify.setText("最新版已经发布啦~~");
        mTvNotify.requestFocus();
        //test
        BannerEntity entity1 = new BannerEntity();
        entity1.setValue1("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512699756&di=11400746eead7717a64925344430f37b&imgtype=jpg&er=1&src=http%3A%2F%2Fimage13.m1905.cn%2Fuploadfile%2F2014%2F0827%2F20140827123253146060.jpg");
        BannerEntity entity2 = new BannerEntity();
        entity2.setValue1("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512105037165&di=65d3e9d10fa61d1e199b33fe4ae3683e&imgtype=0&src=http%3A%2F%2Fwww.cinema.com.cn%2Fupload%2F2011-04%2F11042710516220.jpg");
        bannerList.add(entity1);
        bannerList.add(entity2);
        CBViewHolderCreator<NetworkImageHolderView> cbViewHolderCreator = new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        };
        mUltraViewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        adapter = new BannerPageAdapter<>(cbViewHolderCreator, bannerList);
        mUltraViewpager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mUltraViewpager.setMultiScreen(0.8f);
        mUltraViewpager.setAutoMeasureHeight(true);
        mUltraViewpager.setInfiniteLoop(true);
        mUltraViewpager.setAutoScroll(BANNER_LOOP_TIME);

        mGvList.setDelegate(this);
        mGvList.init(getActivity());
        mGvList.setData(AppHelperUtils.getCollectAPPdata(getContext()));


    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_utils;
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //成功选择收藏的APP
        if (resultCode == AppListActivity.RESULT_CODE_CHOOSE_APP_SUCCESS && requestCode == REQUEST_CODE_CHOOSE_APP) {
            mGvList.setData(AppHelperUtils.getCollectAPPdata(getContext()));
        }
    }

    private void wechatLogin(int position) {
        VipHelperUtils.getInstance().changeCurrentSite(position);
        //test
        if (CommonConstant.isDebug) {

            Intent testIntent = new Intent(mActivity, BrowserActivity.class);
            AppUtilsFragment.this.startActivity(testIntent);
            return;
        }

        if (VipHelperUtils.getInstance().isWechatLogin()) {

            Service.getComnonService().requestLogin(VipHelperUtils.getInstance().getVipUserInfo().getUid())
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
                    .subscribe(new Subscriber<UserInfoEntity>() {
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
                        public void onNext(UserInfoEntity result) {
                            result.setWeiChatUserInfo(VipHelperUtils.getInstance().getVipUserInfo().getWeiChatUserInfo());
                            VipHelperUtils.getInstance().setVipUserInfo(result);
                            EventBus.getDefault().post(result, EventBusTag.TAG_LOGIN_SUCCESS);
                            if (result.getVipLeft() > 0) {
                                VipHelperUtils.getInstance().setValidVip(true);
                                Intent testIntent = new Intent(mActivity, BrowserActivity.class);
                                AppUtilsFragment.this.startActivity(testIntent);

                            } else {
                                VipHelperUtils.getInstance().setValidVip(false);
                                new AlertDialog.Builder(mActivity)
                                        .setTitle("VIP已过期不能愉快的使用啦，是否前往充值？")
                                        .setPositiveButton("立即充值",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        Intent testIntent = new Intent(mActivity, ChargeActivity.class);
                                                        AppUtilsFragment.this.startActivity(testIntent);
                                                    }
                                                })
                                        .setNegativeButton("稍等",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        Toast.makeText(
                                                                mActivity,
                                                                "取消...",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                        .setOnCancelListener(
                                                new DialogInterface.OnCancelListener() {

                                                    @Override
                                                    public void onCancel(DialogInterface dialog) {
                                                        Toast.makeText(
                                                                mActivity,
                                                                "取消...",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }).show();
                            }
                        }
                    });
        } else {

            if (!CommonConstant.isRelease) {
                VipHelperUtils.getInstance().setWechatLogin(true);
                VipHelperUtils.getInstance().setValidVip(true);
                Intent intent = new Intent(mActivity, BrowserActivity.class);
                AppUtilsFragment.this.startActivity(intent);
                return;
            }
            new AlertDialog.Builder(mActivity)
                    .setTitle("需要登陆")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    UserModel.getInstance().login(mActivity);
                                }
                            })
                    .setNegativeButton("否",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Toast.makeText(
                                            mActivity,
                                            "拒绝登录无法观看...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .setOnCancelListener(
                            new DialogInterface.OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    Toast.makeText(
                                            mActivity,
                                            "取消...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).show();

        }
    }

    @Override
    public void onClickAddNinePhotoItem(SortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<AppInfo> models) {
        Intent intent = new Intent(getActivity(), AppListActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_APP);
    }

    @Override
    public void onClickDeleteNinePhotoItem(View view, int position, AppInfo model, ArrayList<AppInfo> models) {
        AppHelperUtils.deleteAppinfo(getActivity(), models.get(position));
        mGvList.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(SortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, AppInfo model, ArrayList<AppInfo> models) {
        AppUtils.launchApp(model.getPackageName());
    }

    public class ListAdapter extends BaseAdapter {

        private final String[] names;
        private final LayoutInflater inflater;
        private final Context context;

        public ListAdapter(Context context, String[] names) {
            this.context = context;
            this.names = names;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return names.length;
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
                rootView = inflater.inflate(R.layout.list_menu_item, parent, false);
                holder = new ViewHolder();
                holder.textView = (TextView) rootView.findViewById(R.id.iv_icon);
                holder.ivLogo = (ImageView) rootView.findViewById(R.id.iv_logo);
                rootView.setTag(holder);
            } else {
                holder = (ViewHolder) rootView.getTag();
            }
            holder.textView.setText(VipHelperUtils.getInstance().getNames()[position]);
            Drawable drawable = context.getResources().getDrawable(VipHelperUtils.getInstance().getIcons()[position]);
//                holder.textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            holder.ivLogo.setImageDrawable(drawable);
            return rootView;
        }

        class ViewHolder {
            TextView textView;
            ImageView ivLogo;
        }

    }

    /**
     * 广告的事件
     */
    class NetworkImageHolderView implements Holder<BannerEntity> {
        private ImageView imageView;
        private View contentView;

        @Override
        public View createView(Context context) {
            contentView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_home_convenient, null);
            imageView = (ImageView) contentView.findViewById(R.id.iv_home_convenient);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return contentView;
        }

        @Override
        public void UpdateUI(final Context context, final int position, final BannerEntity entity) {
            Drawable drawable = context.getResources().getDrawable(AppHelperUtils.icons2[position]);
//                holder.textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            imageView.setImageDrawable(drawable);
            //广告栏点击事件
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(), InformationDetailsActivity.class);
//                    intent.putExtra("ID", entity.getInformationTextID());
//                    startActivity(intent);
                }
            });
        }

    }
}
