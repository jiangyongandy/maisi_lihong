package org.cocos2dx.lua.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maisi.video.obj.video.ChargeInfoEntity;
import com.zuiai.nn.R;

import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.EventBusTag;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.model.LiveModel;
import org.cocos2dx.lua.model.UserModel;
import org.cocos2dx.lua.ui.BrowserActivity;
import org.cocos2dx.lua.ui.ShareActivity2;
import org.cocos2dx.lua.ui.widget.NoScrollGridView;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class LiveFragment extends LazyFragment {

    @BindView(R.id.navigation1)
    RelativeLayout navigation1;
    @BindView(R.id.ultra_viewpager)
    ImageView ultraViewpager;
    @BindView(R.id.tv_notify)
    TextView tvNotify;
    @BindView(R.id.ll_notify)
    LinearLayout llNotify;
    @BindView(R.id.rl_more_catalog)
    RelativeLayout rlMoreCatalog;
    @BindView(R.id.scrollView)
    NoScrollGridView scrollView;

    private EditText amountInput;
    private EditText transferAccount;
    private boolean isFirstLazyLoad;
    private ArrayList<ChargeInfoEntity> list = new ArrayList<>();

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
        listAdapter = new ListAdapter(getContext(), LiveModel.getInstance().getMenuIndex());
        scrollView.setAdapter(listAdapter);
        scrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LiveModel.getInstance().setCurrentPosition(position);
                Intent intent = new Intent(getContext(), BrowserActivity.class);
                intent.putExtra("SiteType", 2);
                intent.putExtra("SpecialSiteType", 2);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_live;
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
        Intent intent = new Intent(getContext(), _activity);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.ultra_viewpager)
    public void onViewClicked() {
        UserModel.getInstance().launchActivity(getActivity(), ShareActivity2.class);
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
            holder.textView.setText(LiveModel.getInstance().getNames()[indexs[position]]);
            Drawable drawable = context.getResources().getDrawable(LiveModel.getInstance().getIcons()[indexs[position]]);
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
