package org.cocos2dx.lua.ui;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maisi.video.obj.video.CalcuPayEntity;
import com.maisi.video.obj.video.ChargeInfoEntity;
import com.maisi.video.obj.video.ChargeRequestEntity;
import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.BoyiRxUtils;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.model.UserModel;
import org.cocos2dx.lua.service.Service;
import org.cocos2dx.lua.ui.widget.NoScrollGridView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class ChargeActivity extends BaseActivity {

    @BindView(R.id.iv_charge)
    TextView mIvCharge;
    @BindView(R.id.gv_list)
    NoScrollGridView mGvList;
    @BindView(R.id.et_recommend_num)
    EditText mEtRecommendNum;
    @BindView(R.id.iv_real_charge)
    TextView mIvRealCharge;

    private int currentIndex;
    private double currentPay;
    private LinearLayout[] linearLayouts;
    private ArrayList<ChargeInfoEntity> list = new ArrayList<>();
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_charge);
        ButterKnife.bind(this);

        listAdapter = new ListAdapter(this, list);
        mGvList.setAdapter(listAdapter);

        if (VipHelperUtils.getInstance().isWechatLogin() && VipHelperUtils.getInstance().getVipUserInfo().getIfFirst() == 1) {
            mEtRecommendNum.setEnabled(true);
        }

        //获取充值金额
        Service.getComnonService().getChargeCatalog()
                .compose(BoyiRxUtils.<ArrayList<ChargeInfoEntity>>applySchedulers())
                .subscribe(new BoyiRxUtils.MySubscriber<ArrayList<ChargeInfoEntity>>() {

                    @Override
                    public void onNext(ArrayList<ChargeInfoEntity> result) {
                        list.clear();
                        list.addAll(result);
                        list.get(0).setSelected(true);
                        listAdapter.notifyDataSetChanged();

                        CalcuPayEntity entity = new CalcuPayEntity();
                        entity.setAmount(Double.parseDouble(list.get(0).getValue1()));
                        entity.setPoints(VipHelperUtils.getInstance().getVipUserInfo().getPointsLeft());
                        Gson gson = new Gson();
                        String toJson = gson.toJson(entity);
                        RequestBody body = RequestBody.create(MediaType.parse("application/json"), toJson);
                        Service.getComnonService().calcuPay(body)
                                .compose(BoyiRxUtils.<String>applySchedulers())
                                .subscribe(new BoyiRxUtils.MySubscriber<String>() {

                                    @Override
                                    public void onNext(String result) {
                                        mIvRealCharge.setText("积分抵现后仅需支付：" + result + "元");
                                        currentPay = Double.parseDouble(result);
                                    }
                                });
                    }
                });


    }

    @OnClick(R.id.iv_charge)
    public void onClick() {

        if(currentPay != 0 ) {

            UserModel.getInstance().requestCharge(this, currentPay, VipHelperUtils.getInstance().getVipUserInfo().getPointsLeft(), mEtRecommendNum.getText().toString());
        }else {
            Toast.makeText(
                    APPAplication.instance,
                   "请稍候再试。。。。。。。",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public class ListAdapter extends BaseAdapter {

        private final ArrayList<ChargeInfoEntity> prices;
        private final LayoutInflater inflater;
        private final Context context;
        private int currentIndex;

        public ListAdapter(Context context, ArrayList<ChargeInfoEntity> prices) {
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
                rootView = inflater.inflate(R.layout.list_charge_item, parent, false);
                holder = new ViewHolder();
                holder.sourcePrice = (TextView) rootView.findViewById(R.id.tv_source_price);
                holder.currentPrice = (TextView) rootView.findViewById(R.id.tv_current_price);
                holder.btnBg = (LinearLayout) rootView.findViewById(R.id.ll_charge_catalog);
                rootView.setTag(holder);
            } else {
                holder = (ViewHolder) rootView.getTag();
            }
            String temp = "";
            switch (position) {
                case 0:
                    temp = "月卡";
                    break;
                case 1:
                    temp = "季卡";
                    break;
                case 2:
                    temp = "半年卡";
                    break;
                case 3:
                    temp = "年卡";
                    break;
            }
            holder.currentPrice.setText(temp + prices.get(position).getValue1() + "元");
            holder.sourcePrice.setText("原价" + prices.get(position).getValue2() + "元");
            holder.sourcePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.btnBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prices.get(currentIndex).setSelected(false);
                    prices.get(position).setSelected(true);
                    notifyDataSetChanged();
                    currentIndex = position;

                    CalcuPayEntity entity = new CalcuPayEntity();
                    entity.setAmount(Double.parseDouble(list.get(position).getValue1()));
                    entity.setPoints(VipHelperUtils.getInstance().getVipUserInfo().getPointsLeft());
                    Gson gson = new Gson();
                    String toJson = gson.toJson(entity);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), toJson);
                    Service.getComnonService().calcuPay(body)
                            .compose(BoyiRxUtils.<String>applySchedulers())
                            .subscribe(new BoyiRxUtils.MySubscriber<String>() {

                                @Override
                                public void onNext(String result) {
                                    currentPay = Double.parseDouble(result);
                                    mIvRealCharge.setText("积分抵现后仅需支付：" + result + "元");
                                }
                            });
                }
            });
            if (prices.get(position).isSelected()) {
                holder.btnBg.setSelected(true);

            } else {
                holder.btnBg.setSelected(false);
            }
            return rootView;
        }

        public double getCurrentAmount() {
            return Double.parseDouble(prices.get(currentIndex).getValue1());
        }

        class ViewHolder {
            TextView sourcePrice;
            TextView currentPrice;
            LinearLayout btnBg;
        }

    }
}
