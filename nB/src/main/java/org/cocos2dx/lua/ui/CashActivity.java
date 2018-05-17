package org.cocos2dx.lua.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zuiai.nn.R;

import org.cocos2dx.lua.BoyiRxUtils;
import org.cocos2dx.lua.ToastUtil;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.service.Service;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class CashActivity extends BaseActivity {


    @BindView(R.id.lv_list)
    ListView lvList;
    private int currentIndex;
    private double currentPay;
    private LinearLayout[] linearLayouts;
    private ArrayList<String[]> list = new ArrayList<>();
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_cash_list);
        ButterKnife.bind(this);

        listAdapter = new ListAdapter(this, list);
        lvList.setAdapter(listAdapter);

        Service.getComnonService().queryCommend2CashRecord(VipHelperUtils.getInstance().getVipUserInfo().getUid())
                .compose(BoyiRxUtils.<ArrayList<String[]>>applySchedulers())
                .subscribe(new BoyiRxUtils.MySubscriber<ArrayList<String[]>>() {

                    @Override
                    public void onNext(ArrayList<String[]> result) {
                        if(result == null || result.size() == 0) {
                            ToastUtil.show(CashActivity.this, "暂无提现记录哦~~", Toast.LENGTH_SHORT);
                            return;
                        }
                        listAdapter = new ListAdapter(CashActivity.this, result);
                        lvList.setAdapter(listAdapter);
                    }
                });
    }


    public class ListAdapter extends BaseAdapter {

        private final ArrayList<String[]> prices;
        private final LayoutInflater inflater;
        private final Context context;
        private int currentIndex;

        public ListAdapter(Context context, ArrayList<String[]> prices) {
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
                rootView = inflater.inflate(R.layout.list_cash_item, parent, false);
                holder = new ViewHolder();
                holder.tv_date = (TextView) rootView.findViewById(R.id.tv_date);
                holder.tv_cash_num = (TextView) rootView.findViewById(R.id.tv_cash_num);
                holder.tv_num = (TextView) rootView.findViewById(R.id.tv_num);
                rootView.setTag(holder);
            } else {
                holder = (ViewHolder) rootView.getTag();
            }
            setData(holder, prices.get(position));
            return rootView;
        }

        public void setData(CashActivity.ListAdapter.ViewHolder holder, String[] entity) {
            holder.tv_date.setText(entity[0]);
            holder.tv_cash_num.setText(entity[1]);
            holder.tv_num.setText(entity[2]);
        }

        class ViewHolder {
            TextView tv_date;
            TextView tv_cash_num;
            TextView tv_num;
        }

    }
}
