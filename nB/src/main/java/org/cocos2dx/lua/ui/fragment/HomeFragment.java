package org.cocos2dx.lua.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maisi.video.obj.video.BannerEntity;
import com.maisi.video.obj.video.NotifyEntity;
import com.tmall.ultraviewpager.UltraViewPager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.model.UserModel;
import org.cocos2dx.lua.service.Service;
import org.cocos2dx.lua.service.UrlConnect;
import org.cocos2dx.lua.ui.BindZhiFuActivity;
import org.cocos2dx.lua.ui.BrowserActivity;
import org.cocos2dx.lua.ui.MoreCatalogActivity;
import org.cocos2dx.lua.ui.PlayActivity;
import org.cocos2dx.lua.ui.ShareActivity2;
import org.cocos2dx.lua.ui.widget.BannerPageAdapter;
import org.cocos2dx.lua.ui.widget.NoScrollGridView;
import org.cocos2dx.lua.ui.widget.holder.CBViewHolderCreator;
import org.cocos2dx.lua.ui.widget.holder.Holder;

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

public class HomeFragment extends BaseFragment {

    @BindView(R.id.ll_jiaoliu)
    LinearLayout mLlJiaoliu;
    @BindView(R.id.ll_share)
    LinearLayout mLlShare;
    @BindView(R.id.gv_list)
    NoScrollGridView mGvList;
    @BindView(R.id.sv_container)
    ScrollView mSvContainer;
    @BindView(R.id.tv_notify)
    TextView mTvNotify;
    @BindView(R.id.ultra_viewpager)
    UltraViewPager mUltraViewpager;

    private final String mAboutUrl = "file:///android_asset/about/index.html";
    private final int BANNER_LOOP_TIME = 5000;
    @BindView(R.id.ll_charge)
    LinearLayout mLlCharge;
    @BindView(R.id.ll_recommend)
    LinearLayout mLlRecommend;
    @BindView(R.id.rl_more_catalog)
    RelativeLayout mRlMoreCatalog;
    private List<BannerEntity> bannerList = new ArrayList<>();
    private ArrayList<NotifyEntity> notifyList = new ArrayList<>();

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(mActivity, "已分享", Toast.LENGTH_LONG).show();
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
    private BannerPageAdapter adapter;

    @OnClick({R.id.ll_jiaoliu, R.id.ll_share, R.id.ll_charge, R.id.ll_recommend, R.id.rl_more_catalog})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_jiaoliu:
                boolean isSuccess = joinQQGroup("HyMW0-l607nnminoIIEroDmeHspsPlZG");
                if (isSuccess) {

                } else {
                    Toast.makeText(
                            APPAplication.instance,
                            "打开QQ失败，请确认是否安装了QQ哦~~", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_share:
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

//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_TEXT, "这里是分享内容");
//                intent.setType("text/plain");
//                //设置分享列表的标题，并且每次都显示分享列表
//                startActivity(Intent.createChooser(intent, "分享到"));
                break;
            case R.id.ll_charge:
//                UserModel.getInstance().launchActivity(mActivity, ChargeActivity.class);
                Intent testIntent = new Intent(mActivity, BrowserActivity.class);
                testIntent.setData(Uri.parse("http://t.cn/RQ6mk9f"));
                HomeFragment.this.startActivity(testIntent);
                break;
            case R.id.ll_recommend:
                Intent intent3 = new Intent(getActivity(), BindZhiFuActivity.class);
                startActivity(intent3);
                break;
            case R.id.rl_more_catalog:
                Intent intent4 = new Intent(getActivity(), MoreCatalogActivity.class);
                startActivity(intent4);
                break;
        }
    }

    @Override
    protected void initData() {

        if (!CommonConstant.isRelease) {
/*            mLlMemberCenter.setVisibility(View.GONE);
            mTvNotify.setText("最新版已经发布啦~~");
            mTvNotify.requestFocus();
            //test
            BannerEntity entity1 = new BannerEntity();
            entity1.setValue1("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512699756&di=11400746eead7717a64925344430f37b&imgtype=jpg&er=1&src=http%3A%2F%2Fimage13.m1905.cn%2Fuploadfile%2F2014%2F0827%2F20140827123253146060.jpg");
            BannerEntity entity2 = new BannerEntity();
            entity2.setValue1("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512105037165&di=65d3e9d10fa61d1e199b33fe4ae3683e&imgtype=0&src=http%3A%2F%2Fwww.cinema.com.cn%2Fupload%2F2011-04%2F11042710516220.jpg");
            bannerList.add(entity1);
            bannerList.add(entity2);*/
        } else {
            mTvNotify.setText(VipHelperUtils.getInstance().getTips());
            mTvNotify.requestFocus();

            //获取轮播图
            Service.getComnonService().getBanner()
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
                    .subscribe(new Subscriber<ArrayList<BannerEntity>>() {
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
                        public void onNext(ArrayList<BannerEntity> result) {
                            bannerList.clear();
                            bannerList.addAll(result);
                            BannerEntity bannerEntity = new BannerEntity();
                            bannerEntity.setAbout(true);
                            bannerList.add(bannerEntity);
                            CBViewHolderCreator<NetworkImageHolderView> cbViewHolderCreator = new CBViewHolderCreator<NetworkImageHolderView>() {
                                @Override
                                public NetworkImageHolderView createHolder() {
                                    return new NetworkImageHolderView();
                                }
                            };
                            mUltraViewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
                            adapter = new BannerPageAdapter<>(cbViewHolderCreator, bannerList);
                            mUltraViewpager.setAdapter(adapter);

                            //内置indicator初始化
                            mUltraViewpager.initIndicator();
                            //设置indicator样式
                            float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
                            mUltraViewpager.getIndicator()
                                    .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                                    .setFocusColor(Color.WHITE)
                                    .setNormalColor(Color.GRAY)
                                    .setMargin((int) dimension, (int) dimension, (int) dimension, (int) dimension)
                                    .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
                            //设置indicator对齐方式
                            mUltraViewpager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                            //构造indicator,绑定到UltraViewPager
                            mUltraViewpager.getIndicator().build();

                            mUltraViewpager.setMultiScreen(1f);
                            mUltraViewpager.setAutoMeasureHeight(true);
                            mUltraViewpager.setInfiniteLoop(true);
                            mUltraViewpager.setAutoScroll(BANNER_LOOP_TIME);
                            adapter.notifyDataSetChanged();

                        }
                    });

            //获取公告
            Service.getComnonService().getNotify()
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
                    .subscribe(new Subscriber<ArrayList<NotifyEntity>>() {
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
                        public void onNext(ArrayList<NotifyEntity> result) {
                            notifyList.clear();
                            notifyList.addAll(result);
                            mTvNotify.setText(notifyList.get(0).getValue1());
                            mTvNotify.requestFocus();
                        }
                    });

        }

        final int[] homeIndexs = VipHelperUtils.getInstance().getHomeIndexs();
        ListAdapter listAdapter = new ListAdapter(mActivity, homeIndexs);
        mGvList.setAdapter(listAdapter);
        mGvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wechatLogin(homeIndexs[position]);
            }
        });


    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_home;
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
        mTvNotify.requestFocus();
    }

    private void wechatLogin(int position) {
        VipHelperUtils.getInstance().changeCurrentSite(position);
        //test
/*        if (CommonConstant.isDebug) {

            Intent testIntent = new Intent(mActivity, BrowserActivity.class);
            HomeFragment.this.startActivity(testIntent);
            return;
        }*/

        UserModel.getInstance().launchActivity(getActivity(), BrowserActivity.class, true);

    }

    /****************
     *
     * 发起添加群流程。群号：迈思影视交流(595447985) 的 key 为： pCo-XXeSfTMjeGd1yW2EEwohf0hx0HsA
     * 调用 joinQQGroup(pCo-XXeSfTMjeGd1yW2EEwohf0hx0HsA) 即可发起手Q客户端申请加群 迈思影视交流(595447985)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public class ListAdapter extends BaseAdapter {

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
                rootView = inflater.inflate(R.layout.list_home_video_item, parent, false);
                holder = new ViewHolder(rootView);
                holder.ivIcon = (TextView) rootView.findViewById(R.id.iv_icon);
                holder.ivLogo = (ImageView) rootView.findViewById(R.id.iv_logo);
                rootView.setTag(holder);
            } else {
                holder = (ViewHolder) rootView.getTag();
            }
            holder.ivIcon.setText(VipHelperUtils.getInstance().getNames()[indexs[position]]);
            Drawable drawable = context.getResources().getDrawable(VipHelperUtils.getInstance().getIcons()[indexs[position]]);
//                holder.textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            holder.ivLogo.setImageDrawable(drawable);
            return rootView;
        }

        class ViewHolder {
            @BindView(R.id.iv_logo)
            ImageView ivLogo;
            @BindView(R.id.iv_icon)
            TextView ivIcon;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
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
            if (entity.isAbout()) {
                Glide.with(HomeFragment.this)
                        .load(R.drawable.ic_deco_2)
                        .into(imageView);
                //广告栏点击事件
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserModel.getInstance().launchActivity(getActivity(), ShareActivity2.class);
                    }
                });
            } else {

                Glide.with(HomeFragment.this)
                        .load((CommonConstant.isRelease ? UrlConnect.ImageServer : "") + entity.getValue1())
                        .into(imageView);
                //广告栏点击事件
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (entity.getDescription().equals("2")) {
                            Intent intent = new Intent(getActivity(), PlayActivity.class);
                            intent.putExtra("URL", (String) entity.getValue2());
                            VipHelperUtils.getInstance().setCurrentPlayUrl((String) entity.getValue2());
                            getActivity().startActivity(intent);
                        } else if (entity.getDescription().equals("1")) {
                            Intent intent = new Intent(getActivity(), BrowserActivity.class);
                            intent.setData(Uri.parse((String) entity.getValue2()));
                            getActivity().startActivity(intent);
                        }
                    }
                });
            }

        }

    }
}
