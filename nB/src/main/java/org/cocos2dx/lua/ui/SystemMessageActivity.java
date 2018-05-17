package org.cocos2dx.lua.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maisi.video.obj.video.SystemMessageEntity;
import com.zuiai.nn.R;

import org.cocos2dx.lua.BoyiRxUtils;
import org.cocos2dx.lua.ToastUtil;
import org.cocos2dx.lua.service.Service;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class SystemMessageActivity extends BaseActivity {


    @BindView(R.id.lv_list)
    ListView lvList;
    private ArrayList<SystemMessageEntity > list = new ArrayList<>();
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_system_message);
        ButterKnife.bind(this);

        listAdapter = new ListAdapter(this, list);
        lvList.setAdapter(listAdapter);

        Service.getComnonService().querySystemMessage()
                .compose(BoyiRxUtils.<ArrayList<SystemMessageEntity>>applySchedulers())
                .subscribe(new BoyiRxUtils.MySubscriber<ArrayList<SystemMessageEntity>>() {

                    @Override
                    public void onNext(ArrayList<SystemMessageEntity> result) {
                        if (result == null || result.size() == 0) {
                            ToastUtil.show(SystemMessageActivity.this, "暂无系统消息哦~~", Toast.LENGTH_SHORT);
                            return;
                        }
                        listAdapter = new ListAdapter(SystemMessageActivity.this, result);
                        lvList.setAdapter(listAdapter);
                    }
                });
    }


    public class ListAdapter extends BaseAdapter {

        private final ArrayList<SystemMessageEntity > prices;
        private final LayoutInflater inflater;
        private final Context context;
        private int currentIndex;

        public ListAdapter(Context context, ArrayList<SystemMessageEntity > prices) {
            this.context = context;
            this.prices = prices;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return prices.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rootView = convertView;
            ViewHolder holder = null;
            if (convertView == null) {
                rootView = inflater.inflate(R.layout.list_system_message_item, parent, false);
                holder = new ViewHolder(rootView);
                rootView.setTag(holder);
            } else {
                holder = (ViewHolder) rootView.getTag();
            }
            setData(holder, prices.get(position));
            return rootView;
        }

        public void setData(ViewHolder holder, SystemMessageEntity entity) {
            holder.tvContent.setText(entity.getValue1());
            Glide.with(SystemMessageActivity.this)
                    .load(R.drawable.ic_kefu_1)
                    .apply(bitmapTransform(new CropCircleTransformation()))
                    .into(holder.ivHead);
        }

        class ViewHolder {
            @BindView(R.id.tv_date)
            TextView tvDate;
            @BindView(R.id.iv_head)
            ImageView ivHead;
            @BindView(R.id.tv_content)
            TextView tvContent;
            @BindView(R.id.rl_message)
            RelativeLayout rlMessage;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
