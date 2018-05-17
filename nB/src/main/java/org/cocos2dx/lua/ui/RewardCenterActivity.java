package org.cocos2dx.lua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.maisi.video.obj.video.CashRequestEntity;
import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.BoyiRxUtils;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.model.UserModel;
import org.cocos2dx.lua.service.Service;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class RewardCenterActivity extends BaseActivity {


    @BindView(R.id.tv_cash_num)
    TextView tvCashNum;
    @BindView(R.id.rl_ti_xian)
    RelativeLayout rlTiXian;
    @BindView(R.id.rl_bind_account)
    RelativeLayout rlBindAccount;
    @BindView(R.id.rl_benefit)
    RelativeLayout rlBenefit;
    @BindView(R.id.rl_cash_record)
    RelativeLayout rlCashRecord;
    @BindView(R.id.navigation1)
    RelativeLayout navigation1;
    private EditText cashNumEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_reward_center);
        ButterKnife.bind(this);
        tvCashNum.setText("¥"+VipHelperUtils.getInstance().getVipUserInfo().getCommendLeft());
    }

    public void jumpActivity(Class _activity) {
        Intent intent = new Intent(this, _activity);
        startActivity(intent);
    }


    @OnClick({R.id.rl_ti_xian, R.id.rl_bind_account, R.id.rl_benefit, R.id.rl_cash_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_ti_xian:
                go2Cash();
                break;
            case R.id.rl_bind_account:
                jumpActivity(BindZhiFuActivity.class);
                break;
            case R.id.rl_benefit:
                jumpActivity(RewardListActivity.class);
                break;
            case R.id.rl_cash_record:
                jumpActivity(CashActivity.class);
                break;
        }
    }

    private void go2Cash() {
        if (!VipHelperUtils.getInstance().isWechatLogin()) {
            Toast.makeText(
                    APPAplication.instance,
                    "请先登录~~",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        MaterialDialog dialog =
                new MaterialDialog.Builder(this)
                        .title("请输入提现金额")
                        .customView(R.layout.dialog_input_transfer2, true)
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String payAccountS = cashNumEdit.getText().toString();
                                if ( payAccountS.length() == 0  ) {
                                    Toast.makeText(
                                            APPAplication.instance,
                                            "请确定提现金额填写完整~~",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                CashRequestEntity entity = new CashRequestEntity();
                                entity.setUid(VipHelperUtils.getInstance().getVipUserInfo().getUid());
                                entity.setAmount(Double.parseDouble(payAccountS));
                                entity.setMaisibi(VipHelperUtils.getInstance().getVipUserInfo().getCommendLeft());
                                Gson gson = new Gson();
                                String toJson = gson.toJson(entity);
                                RequestBody body = RequestBody.create(MediaType.parse("application/json"), toJson);
                                Service.getComnonServiceForString().alitransfer(body)
                                        .compose(BoyiRxUtils.<String>applySchedulers())
                                        .subscribe(new BoyiRxUtils.MySubscriber<String>() {

                                            @Override
                                            public void onNext(String result) {
                                                if (result.equals("success")) {

                                                    Toast.makeText(
                                                            APPAplication.instance,
                                                            "提现成功",
                                                            Toast.LENGTH_SHORT).show();
                                                }else if(result.equals("fail")) {
                                                    Toast.makeText(
                                                            APPAplication.instance,
                                                            "提现失败",
                                                            Toast.LENGTH_SHORT).show();

                                                } else {
                                                    Toast.makeText(
                                                            APPAplication.instance,
                                                            result,
                                                            Toast.LENGTH_SHORT).show();

                                                }
                                                UserModel.getInstance().refreshUserInfo();
                                            }
                                        });
                            }
                        })
                        .build();
        dialog.show();
        cashNumEdit = dialog.getCustomView().findViewById(R.id.et_pay_account);
    }
}
